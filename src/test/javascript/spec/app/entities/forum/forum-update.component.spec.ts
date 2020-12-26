import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AppTestModule } from '../../../test.module';
import { ForumUpdateComponent } from 'app/entities/forum/forum-update.component';
import { ForumService } from 'app/entities/forum/forum.service';
import { Forum } from 'app/shared/model/forum.model';

describe('Component Tests', () => {
  describe('Forum Management Update Component', () => {
    let comp: ForumUpdateComponent;
    let fixture: ComponentFixture<ForumUpdateComponent>;
    let service: ForumService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppTestModule],
        declarations: [ForumUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ForumUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ForumUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ForumService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Forum(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Forum();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
