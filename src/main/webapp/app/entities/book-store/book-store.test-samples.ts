import { IBookStore, NewBookStore } from './book-store.model';

export const sampleWithRequiredData: IBookStore = {
  id: 87526,
};

export const sampleWithPartialData: IBookStore = {
  id: 60131,
  city: 'Pomona',
};

export const sampleWithFullData: IBookStore = {
  id: 30634,
  bookStoreName: 'Solutions Avon Centralized',
  postalCode: 'Central Pound Mississippi',
  city: 'Padbergside',
  stateProvince: 'Fantastic',
};

export const sampleWithNewData: NewBookStore = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
