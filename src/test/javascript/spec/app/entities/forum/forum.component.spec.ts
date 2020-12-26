import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { AppTestModule } from '../../../test.module';
import { ForumComponent } from 'app/entities/forum/forum.component';
import { ForumService } from 'app/entities/forum/forum.service';
import { Forum } from 'app/shared/model/forum.model';

describe('Component Tests', () => {
  describe('Forum Management Component', () => {
    let comp: ForumComponent;
    let fixture: ComponentFixture<ForumComponent>;
    let service: ForumService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppTestModule],
        declarations: [ForumComponent],
      })
        .overrideTemplate(ForumComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ForumComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ForumService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Forum(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.forums && comp.forums[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
