import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBookStore, NewBookStore } from '../book-store.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBookStore for edit and NewBookStoreFormGroupInput for create.
 */
type BookStoreFormGroupInput = IBookStore | PartialWithRequiredKeyOf<NewBookStore>;

type BookStoreFormDefaults = Pick<NewBookStore, 'id' | 'books'>;

type BookStoreFormGroupContent = {
  id: FormControl<IBookStore['id'] | NewBookStore['id']>;
  bookStoreName: FormControl<IBookStore['bookStoreName']>;
  postalCode: FormControl<IBookStore['postalCode']>;
  city: FormControl<IBookStore['city']>;
  stateProvince: FormControl<IBookStore['stateProvince']>;
  books: FormControl<IBookStore['books']>;
};

export type BookStoreFormGroup = FormGroup<BookStoreFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookStoreFormService {
  createBookStoreFormGroup(bookStore: BookStoreFormGroupInput = { id: null }): BookStoreFormGroup {
    const bookStoreRawValue = {
      ...this.getFormDefaults(),
      ...bookStore,
    };
    return new FormGroup<BookStoreFormGroupContent>({
      id: new FormControl(
        { value: bookStoreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      bookStoreName: new FormControl(bookStoreRawValue.bookStoreName),
      postalCode: new FormControl(bookStoreRawValue.postalCode),
      city: new FormControl(bookStoreRawValue.city),
      stateProvince: new FormControl(bookStoreRawValue.stateProvince),
      books: new FormControl(bookStoreRawValue.books ?? []),
    });
  }

  getBookStore(form: BookStoreFormGroup): IBookStore | NewBookStore {
    return form.getRawValue() as IBookStore | NewBookStore;
  }

  resetForm(form: BookStoreFormGroup, bookStore: BookStoreFormGroupInput): void {
    const bookStoreRawValue = { ...this.getFormDefaults(), ...bookStore };
    form.reset(
      {
        ...bookStoreRawValue,
        id: { value: bookStoreRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BookStoreFormDefaults {
    return {
      id: null,
      books: [],
    };
  }
}
