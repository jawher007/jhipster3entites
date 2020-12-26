import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IKeyword, Keyword } from 'app/shared/model/keyword.model';
import { KeywordService } from './keyword.service';
import { KeywordComponent } from './keyword.component';
import { KeywordDetailComponent } from './keyword-detail.component';
import { KeywordUpdateComponent } from './keyword-update.component';

@Injectable({ providedIn: 'root' })
export class KeywordResolve implements Resolve<IKeyword> {
  constructor(private service: KeywordService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKeyword> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((keyword: HttpResponse<Keyword>) => {
          if (keyword.body) {
            return of(keyword.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Keyword());
  }
}

export const keywordRoute: Routes = [
  {
    path: '',
    component: KeywordComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'App.keyword.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KeywordDetailComponent,
    resolve: {
      keyword: KeywordResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'App.keyword.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KeywordUpdateComponent,
    resolve: {
      keyword: KeywordResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'App.keyword.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KeywordUpdateComponent,
    resolve: {
      keyword: KeywordResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'App.keyword.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
