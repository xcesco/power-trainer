import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMeasureType, MeasureType } from '../measure-type.model';
import { MeasureTypeService } from '../service/measure-type.service';

@Injectable({ providedIn: 'root' })
export class MeasureTypeRoutingResolveService implements Resolve<IMeasureType> {
  constructor(protected service: MeasureTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMeasureType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((measureType: HttpResponse<MeasureType>) => {
          if (measureType.body) {
            return of(measureType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MeasureType());
  }
}
