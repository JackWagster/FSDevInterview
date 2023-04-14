import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBookStore } from '../book-store.model';
import { BookStoreService } from '../service/book-store.service';

@Injectable({ providedIn: 'root' })
export class BookStoreRoutingResolveService implements Resolve<IBookStore | null> {
  constructor(protected service: BookStoreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBookStore | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bookStore: HttpResponse<IBookStore>) => {
          if (bookStore.body) {
            return of(bookStore.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
