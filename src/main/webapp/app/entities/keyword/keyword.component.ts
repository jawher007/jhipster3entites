import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IKeyword } from 'app/shared/model/keyword.model';
import { KeywordService } from './keyword.service';
import { KeywordDeleteDialogComponent } from './keyword-delete-dialog.component';

@Component({
  selector: 'jhi-keyword',
  templateUrl: './keyword.component.html',
})
export class KeywordComponent implements OnInit, OnDestroy {
  keywords?: IKeyword[];
  eventSubscriber?: Subscription;

  constructor(protected keywordService: KeywordService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.keywordService.query().subscribe((res: HttpResponse<IKeyword[]>) => (this.keywords = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInKeywords();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IKeyword): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInKeywords(): void {
    this.eventSubscriber = this.eventManager.subscribe('keywordListModification', () => this.loadAll());
  }

  delete(keyword: IKeyword): void {
    const modalRef = this.modalService.open(KeywordDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.keyword = keyword;
  }
}
