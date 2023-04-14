import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../book-store.test-samples';

import { BookStoreFormService } from './book-store-form.service';

describe('BookStore Form Service', () => {
  let service: BookStoreFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookStoreFormService);
  });

  describe('Service methods', () => {
    describe('createBookStoreFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBookStoreFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bookStoreName: expect.any(Object),
            postalCode: expect.any(Object),
            city: expect.any(Object),
            stateProvince: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });

      it('passing IBookStore should create a new form with FormGroup', () => {
        const formGroup = service.createBookStoreFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bookStoreName: expect.any(Object),
            postalCode: expect.any(Object),
            city: expect.any(Object),
            stateProvince: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });
    });

    describe('getBookStore', () => {
      it('should return NewBookStore for default BookStore initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBookStoreFormGroup(sampleWithNewData);

        const bookStore = service.getBookStore(formGroup) as any;

        expect(bookStore).toMatchObject(sampleWithNewData);
      });

      it('should return NewBookStore for empty BookStore initial value', () => {
        const formGroup = service.createBookStoreFormGroup();

        const bookStore = service.getBookStore(formGroup) as any;

        expect(bookStore).toMatchObject({});
      });

      it('should return IBookStore', () => {
        const formGroup = service.createBookStoreFormGroup(sampleWithRequiredData);

        const bookStore = service.getBookStore(formGroup) as any;

        expect(bookStore).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBookStore should not enable id FormControl', () => {
        const formGroup = service.createBookStoreFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBookStore should disable id FormControl', () => {
        const formGroup = service.createBookStoreFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
