import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IKeyword, Keyword } from 'app/shared/model/keyword.model';
import { KeywordService } from './keyword.service';

@Component({
  selector: 'jhi-keyword-update',
  templateUrl: './keyword-update.component.html',
})
export class KeywordUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(2)]],
  });

  constructor(protected keywordService: KeywordService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ keyword }) => {
      this.updateForm(keyword);
    });
  }

  updateForm(keyword: IKeyword): void {
    this.editForm.patchValue({
      id: keyword.id,
      name: keyword.name,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const keyword = this.createFromForm();
    if (keyword.id !== undefined) {
      this.subscribeToSaveResponse(this.keywordService.update(keyword));
    } else {
      this.subscribeToSaveResponse(this.keywordService.create(keyword));
    }
  }

  private createFromForm(): IKeyword {
    return {
      ...new Keyword(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKeyword>>): void {
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
}
