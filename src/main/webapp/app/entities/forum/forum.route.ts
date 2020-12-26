import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IForum, Forum } from 'app/shared/model/forum.model';
import { ForumService } from './forum.service';
import { ForumComponent } from './forum.component';
import { ForumDetailComponent } from './forum-detail.component';
import { ForumUpdateComponent } from './forum-update.component';

@Injectable({ providedIn: 'root' })
export class ForumResolve implements Resolve<IForum> {
  constructor(private service: ForumService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IForum> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((forum: HttpResponse<Forum>) => {
          if (forum.body) {
            return of(forum.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Forum());
  }
}

export const forumRoute: Routes = [
  {
    path: '',
    component: ForumComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'App.forum.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ForumDetailComponent,
    resolve: {
      forum: ForumResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'App.forum.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ForumUpdateComponent,
    resolve: {
      forum: ForumResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'App.forum.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ForumUpdateComponent,
    resolve: {
      forum: ForumResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'App.forum.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
