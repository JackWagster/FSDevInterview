import { IAuthor, NewAuthor } from './author.model';

export const sampleWithRequiredData: IAuthor = {
  id: 82416,
  fistName: 'generating Home networks',
  lastName: 'Herzog',
};

export const sampleWithPartialData: IAuthor = {
  id: 64791,
  fistName: 'parse software',
  lastName: 'Walsh',
};

export const sampleWithFullData: IAuthor = {
  id: 44614,
  fistName: 'Forward transmit Generic',
  lastName: 'Ritchie',
};

export const sampleWithNewData: NewAuthor = {
  fistName: 'optimize Developer Customer',
  lastName: 'Hills',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
