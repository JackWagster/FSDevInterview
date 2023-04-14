import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BookStoreFormService } from './book-store-form.service';
import { BookStoreService } from '../service/book-store.service';
import { IBookStore } from '../book-store.model';
import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';

import { BookStoreUpdateComponent } from './book-store-update.component';

describe('BookStore Management Update Component', () => {
  let comp: BookStoreUpdateComponent;
  let fixture: ComponentFixture<BookStoreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bookStoreFormService: BookStoreFormService;
  let bookStoreService: BookStoreService;
  let bookService: BookService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BookStoreUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BookStoreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BookStoreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bookStoreFormService = TestBed.inject(BookStoreFormService);
    bookStoreService = TestBed.inject(BookStoreService);
    bookService = TestBed.inject(BookService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Book query and add missing value', () => {
      const bookStore: IBookStore = { id: 456 };
      const books: IBook[] = [{ id: 80364 }];
      bookStore.books = books;

      const bookCollection: IBook[] = [{ id: 86147 }];
      jest.spyOn(bookService, 'query').mockReturnValue(of(new HttpResponse({ body: bookCollection })));
      const additionalBooks = [...books];
      const expectedCollection: IBook[] = [...additionalBooks, ...bookCollection];
      jest.spyOn(bookService, 'addBookToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bookStore });
      comp.ngOnInit();

      expect(bookService.query).toHaveBeenCalled();
      expect(bookService.addBookToCollectionIfMissing).toHaveBeenCalledWith(
        bookCollection,
        ...additionalBooks.map(expect.objectContaining)
      );
      expect(comp.booksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bookStore: IBookStore = { id: 456 };
      const book: IBook = { id: 54946 };
      bookStore.books = [book];

      activatedRoute.data = of({ bookStore });
      comp.ngOnInit();

      expect(comp.booksSharedCollection).toContain(book);
      expect(comp.bookStore).toEqual(bookStore);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookStore>>();
      const bookStore = { id: 123 };
      jest.spyOn(bookStoreFormService, 'getBookStore').mockReturnValue(bookStore);
      jest.spyOn(bookStoreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookStore });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookStore }));
      saveSubject.complete();

      // THEN
      expect(bookStoreFormService.getBookStore).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bookStoreService.update).toHaveBeenCalledWith(expect.objectContaining(bookStore));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookStore>>();
      const bookStore = { id: 123 };
      jest.spyOn(bookStoreFormService, 'getBookStore').mockReturnValue({ id: null });
      jest.spyOn(bookStoreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookStore: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookStore }));
      saveSubject.complete();

      // THEN
      expect(bookStoreFormService.getBookStore).toHaveBeenCalled();
      expect(bookStoreService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookStore>>();
      const bookStore = { id: 123 };
      jest.spyOn(bookStoreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookStore });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bookStoreService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBook', () => {
      it('Should forward to bookService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(bookService, 'compareBook');
        comp.compareBook(entity, entity2);
        expect(bookService.compareBook).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
