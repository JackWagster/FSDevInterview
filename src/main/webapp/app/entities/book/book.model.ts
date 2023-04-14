import { IAuthor } from 'app/entities/author/author.model';
import { IBookStore } from 'app/entities/book-store/book-store.model';

export interface IBook {
  id: number;
  title?: string | null;
  description?: string | null;
  authors?: Pick<IAuthor, 'id' | 'lastName'>[] | null;
  bookstores?: Pick<IBookStore, 'id'>[] | null;
}

export type NewBook = Omit<IBook, 'id'> & { id: null };
