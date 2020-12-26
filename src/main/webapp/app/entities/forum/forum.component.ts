import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IForum } from 'app/shared/model/forum.model';
import { ForumService } from './forum.service';
import { ForumDeleteDialogComponent } from './forum-delete-dialog.component';

@Component({
  selector: 'jhi-forum',
  templateUrl: './forum.component.html',
})
export class ForumComponent implements OnInit, OnDestroy {
  forums?: IForum[];
  eventSubscriber?: Subscription;

  constructor(protected forumService: ForumService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.forumService.query().subscribe((res: HttpResponse<IForum[]>) => (this.forums = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInForums();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IForum): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInForums(): void {
    this.eventSubscriber = this.eventManager.subscribe('forumListModification', () => this.loadAll());
  }

  delete(forum: IForum): void {
    const modalRef = this.modalService.open(ForumDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.forum = forum;
  }
}
