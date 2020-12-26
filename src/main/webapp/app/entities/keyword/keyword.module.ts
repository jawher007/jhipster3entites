import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppSharedModule } from 'app/shared/shared.module';
import { KeywordComponent } from './keyword.component';
import { KeywordDetailComponent } from './keyword-detail.component';
import { KeywordUpdateComponent } from './keyword-update.component';
import { KeywordDeleteDialogComponent } from './keyword-delete-dialog.component';
import { keywordRoute } from './keyword.route';

@NgModule({
  imports: [AppSharedModule, RouterModule.forChild(keywordRoute)],
  declarations: [KeywordComponent, KeywordDetailComponent, KeywordUpdateComponent, KeywordDeleteDialogComponent],
  entryComponents: [KeywordDeleteDialogComponent],
})
export class AppKeywordModule {}
