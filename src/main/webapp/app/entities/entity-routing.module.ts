import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'book-store',
        data: { pageTitle: 'fsDevInterviewApp.bookStore.home.title' },
        loadChildren: () => import('./book-store/book-store.module').then(m => m.BookStoreModule),
      },
      {
        path: 'book',
        data: { pageTitle: 'fsDevInterviewApp.book.home.title' },
        loadChildren: () => import('./book/book.module').then(m => m.BookModule),
      },
      {
        path: 'author',
        data: { pageTitle: 'fsDevInterviewApp.author.home.title' },
        loadChildren: () => import('./author/author.module').then(m => m.AuthorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
