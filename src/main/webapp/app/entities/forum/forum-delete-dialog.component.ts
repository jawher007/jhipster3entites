import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IForum } from 'app/shared/model/forum.model';
import { ForumService } from './forum.service';

@Component({
  templateUrl: './forum-delete-dialog.component.html',
})
export class ForumDeleteDialogComponent {
  forum?: IForum;

  constructor(protected forumService: ForumService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.forumService.delete(id).subscribe(() => {
      this.eventManager.broadcast('forumListModification');
      this.activeModal.close();
    });
  }
}
