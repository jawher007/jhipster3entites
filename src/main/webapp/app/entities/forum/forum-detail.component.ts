import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IForum } from 'app/shared/model/forum.model';

@Component({
  selector: 'jhi-forum-detail',
  templateUrl: './forum-detail.component.html',
})
export class ForumDetailComponent implements OnInit {
  forum: IForum | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ forum }) => (this.forum = forum));
  }

  previousState(): void {
    window.history.back();
  }
}
