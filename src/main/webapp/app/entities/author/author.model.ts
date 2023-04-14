import { IBook } from 'app/entities/book/book.model';

export interface IAuthor {
  id: number;
  fistName?: string | null;
  lastName?: string | null;
  books?: Pick<IBook, 'id'>[] | null;
}

export type NewAuthor = Omit<IAuthor, 'id'> & { id: null };
