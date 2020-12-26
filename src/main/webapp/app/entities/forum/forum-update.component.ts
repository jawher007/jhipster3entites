import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IForum, Forum } from 'app/shared/model/forum.model';
import { ForumService } from './forum.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-forum-update',
  templateUrl: './forum-update.component.html',
})
export class ForumUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(3)]],
    handle: [null, [Validators.required, Validators.minLength(2)]],
    user: [],
  });

  constructor(
    protected forumService: ForumService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ forum }) => {
      this.updateForm(forum);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(forum: IForum): void {
    this.editForm.patchValue({
      id: forum.id,
      name: forum.name,
      handle: forum.handle,
      user: forum.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const forum = this.createFromForm();
    if (forum.id !== undefined) {
      this.subscribeToSaveResponse(this.forumService.update(forum));
    } else {
      this.subscribeToSaveResponse(this.forumService.create(forum));
    }
  }

  private createFromForm(): IForum {
    return {
      ...new Forum(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      handle: this.editForm.get(['handle'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IForum>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
