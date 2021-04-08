import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILanguage, Language } from '../language.model';
import { LanguageService } from '../service/language.service';

@Injectable({ providedIn: 'root' })
export class LanguageRoutingResolveService implements Resolve<ILanguage> {
  constructor(protected service: LanguageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILanguage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((language: HttpResponse<Language>) => {
          if (language.body) {
            return of(language.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Language());
  }
}
