import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMeasureValue, MeasureValue } from '../measure-value.model';
import { MeasureValueService } from '../service/measure-value.service';

@Injectable({ providedIn: 'root' })
export class MeasureValueRoutingResolveService implements Resolve<IMeasureValue> {
  constructor(protected service: MeasureValueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMeasureValue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((measureValue: HttpResponse<MeasureValue>) => {
          if (measureValue.body) {
            return of(measureValue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MeasureValue());
  }
}
