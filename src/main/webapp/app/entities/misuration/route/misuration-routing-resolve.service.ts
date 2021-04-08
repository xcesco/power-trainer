import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMisuration, Misuration } from '../misuration.model';
import { MisurationService } from '../service/misuration.service';

@Injectable({ providedIn: 'root' })
export class MisurationRoutingResolveService implements Resolve<IMisuration> {
  constructor(protected service: MisurationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMisuration> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((misuration: HttpResponse<Misuration>) => {
          if (misuration.body) {
            return of(misuration.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Misuration());
  }
}
