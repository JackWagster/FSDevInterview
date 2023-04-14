import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBookStore, NewBookStore } from '../book-store.model';

export type PartialUpdateBookStore = Partial<IBookStore> & Pick<IBookStore, 'id'>;

export type EntityResponseType = HttpResponse<IBookStore>;
export type EntityArrayResponseType = HttpResponse<IBookStore[]>;

@Injectable({ providedIn: 'root' })
export class BookStoreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/book-stores');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bookStore: NewBookStore): Observable<EntityResponseType> {
    return this.http.post<IBookStore>(this.resourceUrl, bookStore, { observe: 'response' });
  }

  update(bookStore: IBookStore): Observable<EntityResponseType> {
    return this.http.put<IBookStore>(`${this.resourceUrl}/${this.getBookStoreIdentifier(bookStore)}`, bookStore, { observe: 'response' });
  }

  partialUpdate(bookStore: PartialUpdateBookStore): Observable<EntityResponseType> {
    return this.http.patch<IBookStore>(`${this.resourceUrl}/${this.getBookStoreIdentifier(bookStore)}`, bookStore, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBookStore>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBookStore[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBookStoreIdentifier(bookStore: Pick<IBookStore, 'id'>): number {
    return bookStore.id;
  }

  compareBookStore(o1: Pick<IBookStore, 'id'> | null, o2: Pick<IBookStore, 'id'> | null): boolean {
    return o1 && o2 ? this.getBookStoreIdentifier(o1) === this.getBookStoreIdentifier(o2) : o1 === o2;
  }

  addBookStoreToCollectionIfMissing<Type extends Pick<IBookStore, 'id'>>(
    bookStoreCollection: Type[],
    ...bookStoresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bookStores: Type[] = bookStoresToCheck.filter(isPresent);
    if (bookStores.length > 0) {
      const bookStoreCollectionIdentifiers = bookStoreCollection.map(bookStoreItem => this.getBookStoreIdentifier(bookStoreItem)!);
      const bookStoresToAdd = bookStores.filter(bookStoreItem => {
        const bookStoreIdentifier = this.getBookStoreIdentifier(bookStoreItem);
        if (bookStoreCollectionIdentifiers.includes(bookStoreIdentifier)) {
          return false;
        }
        bookStoreCollectionIdentifiers.push(bookStoreIdentifier);
        return true;
      });
      return [...bookStoresToAdd, ...bookStoreCollection];
    }
    return bookStoreCollection;
  }
}
