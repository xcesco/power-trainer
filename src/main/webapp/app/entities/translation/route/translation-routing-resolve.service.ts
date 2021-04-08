import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITranslation, Translation } from '../translation.model';
import { TranslationService } from '../service/translation.service';

@Injectable({ providedIn: 'root' })
export class TranslationRoutingResolveService implements Resolve<ITranslation> {
  constructor(protected service: TranslationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITranslation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((translation: HttpResponse<Translation>) => {
          if (translation.body) {
            return of(translation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Translation());
  }
}
