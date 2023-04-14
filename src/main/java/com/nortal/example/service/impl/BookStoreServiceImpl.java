package com.nortal.example.service.impl;

import com.nortal.example.domain.BookStore;
import com.nortal.example.repository.BookStoreRepository;
import com.nortal.example.service.BookStoreService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BookStore}.
 */
@Service
@Transactional
public class BookStoreServiceImpl implements BookStoreService {

    private final Logger log = LoggerFactory.getLogger(BookStoreServiceImpl.class);

    private final BookStoreRepository bookStoreRepository;

    public BookStoreServiceImpl(BookStoreRepository bookStoreRepository) {
        this.bookStoreRepository = bookStoreRepository;
    }

    @Override
    public BookStore save(BookStore bookStore) {
        log.debug("Request to save BookStore : {}", bookStore);
        return bookStoreRepository.save(bookStore);
    }

    @Override
    public BookStore update(BookStore bookStore) {
        log.debug("Request to update BookStore : {}", bookStore);
        return bookStoreRepository.save(bookStore);
    }

    @Override
    public Optional<BookStore> partialUpdate(BookStore bookStore) {
        log.debug("Request to partially update BookStore : {}", bookStore);

        return bookStoreRepository
            .findById(bookStore.getId())
            .map(existingBookStore -> {
                if (bookStore.getBookStoreName() != null) {
                    existingBookStore.setBookStoreName(bookStore.getBookStoreName());
                }
                if (bookStore.getPostalCode() != null) {
                    existingBookStore.setPostalCode(bookStore.getPostalCode());
                }
                if (bookStore.getCity() != null) {
                    existingBookStore.setCity(bookStore.getCity());
                }
                if (bookStore.getStateProvince() != null) {
                    existingBookStore.setStateProvince(bookStore.getStateProvince());
                }

                return existingBookStore;
            })
            .map(bookStoreRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookStore> findAll(Pageable pageable) {
        log.debug("Request to get all BookStores");
        return bookStoreRepository.findAll(pageable);
    }

    public Page<BookStore> findAllWithEagerRelationships(Pageable pageable) {
        return bookStoreRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookStore> findOne(Long id) {
        log.debug("Request to get BookStore : {}", id);
        return bookStoreRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BookStore : {}", id);
        bookStoreRepository.deleteById(id);
    }
}
