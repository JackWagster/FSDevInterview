package com.nortal.example.repository;

import com.nortal.example.domain.BookStore;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BookStoreRepositoryWithBagRelationships {
    Optional<BookStore> fetchBagRelationships(Optional<BookStore> bookStore);

    List<BookStore> fetchBagRelationships(List<BookStore> bookStores);

    Page<BookStore> fetchBagRelationships(Page<BookStore> bookStores);
}
