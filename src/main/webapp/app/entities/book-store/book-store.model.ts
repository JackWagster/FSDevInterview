import { IBook } from 'app/entities/book/book.model';

export interface IBookStore {
  id: number;
  bookStoreName?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  books?: Pick<IBook, 'id' | 'title'>[] | null;
}

export type NewBookStore = Omit<IBookStore, 'id'> & { id: null };
