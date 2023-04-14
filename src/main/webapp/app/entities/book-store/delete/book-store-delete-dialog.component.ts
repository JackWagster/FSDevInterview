import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBookStore } from '../book-store.model';
import { BookStoreService } from '../service/book-store.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './book-store-delete-dialog.component.html',
})
export class BookStoreDeleteDialogComponent {
  bookStore?: IBookStore;

  constructor(protected bookStoreService: BookStoreService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bookStoreService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
