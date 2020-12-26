import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IKeyword } from 'app/shared/model/keyword.model';
import { KeywordService } from './keyword.service';

@Component({
  templateUrl: './keyword-delete-dialog.component.html',
})
export class KeywordDeleteDialogComponent {
  keyword?: IKeyword;

  constructor(protected keywordService: KeywordService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.keywordService.delete(id).subscribe(() => {
      this.eventManager.broadcast('keywordListModification');
      this.activeModal.close();
    });
  }
}
