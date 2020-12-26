import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IEntry, Entry } from 'app/shared/model/entry.model';
import { EntryService } from './entry.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IForum } from 'app/shared/model/forum.model';
import { ForumService } from 'app/entities/forum/forum.service';
import { IKeyword } from 'app/shared/model/keyword.model';
import { KeywordService } from 'app/entities/keyword/keyword.service';

type SelectableEntity = IForum | IKeyword;

@Component({
  selector: 'jhi-entry-update',
  templateUrl: './entry-update.component.html',
})
export class EntryUpdateComponent implements OnInit {
  isSaving = false;
  forums: IForum[] = [];
  keywords: IKeyword[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    content: [null, [Validators.required]],
    date: [null, [Validators.required]],
    forum: [],
    keywords: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected entryService: EntryService,
    protected forumService: ForumService,
    protected keywordService: KeywordService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entry }) => {
      if (!entry.id) {
        const today = moment().startOf('day');
        entry.date = today;
      }

      this.updateForm(entry);

      this.forumService.query().subscribe((res: HttpResponse<IForum[]>) => (this.forums = res.body || []));

      this.keywordService.query().subscribe((res: HttpResponse<IKeyword[]>) => (this.keywords = res.body || []));
    });
  }

  updateForm(entry: IEntry): void {
    this.editForm.patchValue({
      id: entry.id,
      title: entry.title,
      content: entry.content,
      date: entry.date ? entry.date.format(DATE_TIME_FORMAT) : null,
      forum: entry.forum,
      keywords: entry.keywords,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('App.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const entry = this.createFromForm();
    if (entry.id !== undefined) {
      this.subscribeToSaveResponse(this.entryService.update(entry));
    } else {
      this.subscribeToSaveResponse(this.entryService.create(entry));
    }
  }

  private createFromForm(): IEntry {
    return {
      ...new Entry(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      forum: this.editForm.get(['forum'])!.value,
      keywords: this.editForm.get(['keywords'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntry>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IKeyword[], option: IKeyword): IKeyword {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
