import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppSharedModule } from 'app/shared/shared.module';
import { ForumComponent } from './forum.component';
import { ForumDetailComponent } from './forum-detail.component';
import { ForumUpdateComponent } from './forum-update.component';
import { ForumDeleteDialogComponent } from './forum-delete-dialog.component';
import { forumRoute } from './forum.route';

@NgModule({
  imports: [AppSharedModule, RouterModule.forChild(forumRoute)],
  declarations: [ForumComponent, ForumDetailComponent, ForumUpdateComponent, ForumDeleteDialogComponent],
  entryComponents: [ForumDeleteDialogComponent],
})
export class AppForumModule {}
