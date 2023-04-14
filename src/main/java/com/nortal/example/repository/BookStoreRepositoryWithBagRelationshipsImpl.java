package com.nortal.example.repository;

import com.nortal.example.domain.BookStore;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class BookStoreRepositoryWithBagRelationshipsImpl implements BookStoreRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BookStore> fetchBagRelationships(Optional<BookStore> bookStore) {
        return bookStore.map(this::fetchBooks);
    }

    @Override
    public Page<BookStore> fetchBagRelationships(Page<BookStore> bookStores) {
        return new PageImpl<>(fetchBagRelationships(bookStores.getContent()), bookStores.getPageable(), bookStores.getTotalElements());
    }

    @Override
    public List<BookStore> fetchBagRelationships(List<BookStore> bookStores) {
        return Optional.of(bookStores).map(this::fetchBooks).orElse(Collections.emptyList());
    }

    BookStore fetchBooks(BookStore result) {
        return entityManager
            .createQuery(
                "select bookStore from BookStore bookStore left join fetch bookStore.books where bookStore is :bookStore",
                BookStore.class
            )
            .setParameter("bookStore", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<BookStore> fetchBooks(List<BookStore> bookStores) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, bookStores.size()).forEach(index -> order.put(bookStores.get(index).getId(), index));
        List<BookStore> result = entityManager
            .createQuery(
                "select distinct bookStore from BookStore bookStore left join fetch bookStore.books where bookStore in :bookStores",
                BookStore.class
            )
            .setParameter("bookStores", bookStores)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
