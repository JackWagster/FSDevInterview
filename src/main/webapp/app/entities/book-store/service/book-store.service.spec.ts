import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBookStore } from '../book-store.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../book-store.test-samples';

import { BookStoreService } from './book-store.service';

const requireRestSample: IBookStore = {
  ...sampleWithRequiredData,
};

describe('BookStore Service', () => {
  let service: BookStoreService;
  let httpMock: HttpTestingController;
  let expectedResult: IBookStore | IBookStore[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BookStoreService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a BookStore', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const bookStore = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bookStore).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BookStore', () => {
      const bookStore = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bookStore).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BookStore', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BookStore', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BookStore', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBookStoreToCollectionIfMissing', () => {
      it('should add a BookStore to an empty array', () => {
        const bookStore: IBookStore = sampleWithRequiredData;
        expectedResult = service.addBookStoreToCollectionIfMissing([], bookStore);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookStore);
      });

      it('should not add a BookStore to an array that contains it', () => {
        const bookStore: IBookStore = sampleWithRequiredData;
        const bookStoreCollection: IBookStore[] = [
          {
            ...bookStore,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBookStoreToCollectionIfMissing(bookStoreCollection, bookStore);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BookStore to an array that doesn't contain it", () => {
        const bookStore: IBookStore = sampleWithRequiredData;
        const bookStoreCollection: IBookStore[] = [sampleWithPartialData];
        expectedResult = service.addBookStoreToCollectionIfMissing(bookStoreCollection, bookStore);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookStore);
      });

      it('should add only unique BookStore to an array', () => {
        const bookStoreArray: IBookStore[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bookStoreCollection: IBookStore[] = [sampleWithRequiredData];
        expectedResult = service.addBookStoreToCollectionIfMissing(bookStoreCollection, ...bookStoreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bookStore: IBookStore = sampleWithRequiredData;
        const bookStore2: IBookStore = sampleWithPartialData;
        expectedResult = service.addBookStoreToCollectionIfMissing([], bookStore, bookStore2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookStore);
        expect(expectedResult).toContain(bookStore2);
      });

      it('should accept null and undefined values', () => {
        const bookStore: IBookStore = sampleWithRequiredData;
        expectedResult = service.addBookStoreToCollectionIfMissing([], null, bookStore, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookStore);
      });

      it('should return initial array if no BookStore is added', () => {
        const bookStoreCollection: IBookStore[] = [sampleWithRequiredData];
        expectedResult = service.addBookStoreToCollectionIfMissing(bookStoreCollection, undefined, null);
        expect(expectedResult).toEqual(bookStoreCollection);
      });
    });

    describe('compareBookStore', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBookStore(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBookStore(entity1, entity2);
        const compareResult2 = service.compareBookStore(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBookStore(entity1, entity2);
        const compareResult2 = service.compareBookStore(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBookStore(entity1, entity2);
        const compareResult2 = service.compareBookStore(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
