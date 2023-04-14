package com.nortal.example.service;

import com.nortal.example.domain.BookStore;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link BookStore}.
 */
public interface BookStoreService {
    /**
     * Save a bookStore.
     *
     * @param bookStore the entity to save.
     * @return the persisted entity.
     */
    BookStore save(BookStore bookStore);

    /**
     * Updates a bookStore.
     *
     * @param bookStore the entity to update.
     * @return the persisted entity.
     */
    BookStore update(BookStore bookStore);

    /**
     * Partially updates a bookStore.
     *
     * @param bookStore the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BookStore> partialUpdate(BookStore bookStore);

    /**
     * Get all the bookStores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookStore> findAll(Pageable pageable);

    /**
     * Get all the bookStores with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookStore> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" bookStore.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookStore> findOne(Long id);

    /**
     * Delete the "id" bookStore.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
