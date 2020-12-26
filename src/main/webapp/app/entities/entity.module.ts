import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'forum',
        loadChildren: () => import('./forum/forum.module').then(m => m.AppForumModule),
      },
      {
        path: 'entry',
        loadChildren: () => import('./entry/entry.module').then(m => m.AppEntryModule),
      },
      {
        path: 'keyword',
        loadChildren: () => import('./keyword/keyword.module').then(m => m.AppKeywordModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class AppEntityModule {}
