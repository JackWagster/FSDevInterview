import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BookStoreFormService, BookStoreFormGroup } from './book-store-form.service';
import { IBookStore } from '../book-store.model';
import { BookStoreService } from '../service/book-store.service';
import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';

@Component({
  selector: 'jhi-book-store-update',
  templateUrl: './book-store-update.component.html',
})
export class BookStoreUpdateComponent implements OnInit {
  isSaving = false;
  bookStore: IBookStore | null = null;

  booksSharedCollection: IBook[] = [];

  editForm: BookStoreFormGroup = this.bookStoreFormService.createBookStoreFormGroup();

  constructor(
    protected bookStoreService: BookStoreService,
    protected bookStoreFormService: BookStoreFormService,
    protected bookService: BookService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBook = (o1: IBook | null, o2: IBook | null): boolean => this.bookService.compareBook(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookStore }) => {
      this.bookStore = bookStore;
      if (bookStore) {
        this.updateForm(bookStore);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bookStore = this.bookStoreFormService.getBookStore(this.editForm);
    if (bookStore.id !== null) {
      this.subscribeToSaveResponse(this.bookStoreService.update(bookStore));
    } else {
      this.subscribeToSaveResponse(this.bookStoreService.create(bookStore));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBookStore>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(bookStore: IBookStore): void {
    this.bookStore = bookStore;
    this.bookStoreFormService.resetForm(this.editForm, bookStore);

    this.booksSharedCollection = this.bookService.addBookToCollectionIfMissing<IBook>(
      this.booksSharedCollection,
      ...(bookStore.books ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bookService
      .query()
      .pipe(map((res: HttpResponse<IBook[]>) => res.body ?? []))
      .pipe(map((books: IBook[]) => this.bookService.addBookToCollectionIfMissing<IBook>(books, ...(this.bookStore?.books ?? []))))
      .subscribe((books: IBook[]) => (this.booksSharedCollection = books));
  }
}
