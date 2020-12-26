import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEntry } from 'app/shared/model/entry.model';
import { EntryService } from './entry.service';
import { EntryDeleteDialogComponent } from './entry-delete-dialog.component';

@Component({
  selector: 'jhi-entry',
  templateUrl: './entry.component.html',
})
export class EntryComponent implements OnInit, OnDestroy {
  entries?: IEntry[];
  eventSubscriber?: Subscription;

  constructor(
    protected entryService: EntryService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.entryService.query().subscribe((res: HttpResponse<IEntry[]>) => (this.entries = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEntries();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEntry): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInEntries(): void {
    this.eventSubscriber = this.eventManager.subscribe('entryListModification', () => this.loadAll());
  }

  delete(entry: IEntry): void {
    const modalRef = this.modalService.open(EntryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.entry = entry;
  }
}
