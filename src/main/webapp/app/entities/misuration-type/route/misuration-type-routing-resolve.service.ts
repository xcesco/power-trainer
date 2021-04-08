import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMisurationType, MisurationType } from '../misuration-type.model';
import { MisurationTypeService } from '../service/misuration-type.service';

@Injectable({ providedIn: 'root' })
export class MisurationTypeRoutingResolveService implements Resolve<IMisurationType> {
  constructor(protected service: MisurationTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMisurationType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((misurationType: HttpResponse<MisurationType>) => {
          if (misurationType.body) {
            return of(misurationType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MisurationType());
  }
}
