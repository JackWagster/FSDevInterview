package com.nortal.example.web.rest;

import com.nortal.example.domain.BookStore;
import com.nortal.example.repository.BookStoreRepository;
import com.nortal.example.service.BookStoreService;
import com.nortal.example.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nortal.example.domain.BookStore}.
 */
@RestController
@RequestMapping("/api")
public class BookStoreResource {

    private final Logger log = LoggerFactory.getLogger(BookStoreResource.class);

    private static final String ENTITY_NAME = "bookStore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookStoreService bookStoreService;

    private final BookStoreRepository bookStoreRepository;

    public BookStoreResource(BookStoreService bookStoreService, BookStoreRepository bookStoreRepository) {
        this.bookStoreService = bookStoreService;
        this.bookStoreRepository = bookStoreRepository;
    }

    /**
     * {@code POST  /book-stores} : Create a new bookStore.
     *
     * @param bookStore the bookStore to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookStore, or with status {@code 400 (Bad Request)} if the bookStore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/book-stores")
    public ResponseEntity<BookStore> createBookStore(@RequestBody BookStore bookStore) throws URISyntaxException {
        log.debug("REST request to save BookStore : {}", bookStore);
        if (bookStore.getId() != null) {
            throw new BadRequestAlertException("A new bookStore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookStore result = bookStoreService.save(bookStore);
        return ResponseEntity
            .created(new URI("/api/book-stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /book-stores/:id} : Updates an existing bookStore.
     *
     * @param id the id of the bookStore to save.
     * @param bookStore the bookStore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookStore,
     * or with status {@code 400 (Bad Request)} if the bookStore is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookStore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/book-stores/{id}")
    public ResponseEntity<BookStore> updateBookStore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BookStore bookStore
    ) throws URISyntaxException {
        log.debug("REST request to update BookStore : {}, {}", id, bookStore);
        if (bookStore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookStore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookStore result = bookStoreService.update(bookStore);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookStore.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /book-stores/:id} : Partial updates given fields of an existing bookStore, field will ignore if it is null
     *
     * @param id the id of the bookStore to save.
     * @param bookStore the bookStore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookStore,
     * or with status {@code 400 (Bad Request)} if the bookStore is not valid,
     * or with status {@code 404 (Not Found)} if the bookStore is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookStore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/book-stores/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookStore> partialUpdateBookStore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BookStore bookStore
    ) throws URISyntaxException {
        log.debug("REST request to partial update BookStore partially : {}, {}", id, bookStore);
        if (bookStore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookStore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookStore> result = bookStoreService.partialUpdate(bookStore);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookStore.getId().toString())
        );
    }

    /**
     * {@code GET  /book-stores} : get all the bookStores.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookStores in body.
     */
    @GetMapping("/book-stores")
    public ResponseEntity<List<BookStore>> getAllBookStores(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of BookStores");
        Page<BookStore> page;
        if (eagerload) {
            page = bookStoreService.findAllWithEagerRelationships(pageable);
        } else {
            page = bookStoreService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-stores/:id} : get the "id" bookStore.
     *
     * @param id the id of the bookStore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookStore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book-stores/{id}")
    public ResponseEntity<BookStore> getBookStore(@PathVariable Long id) {
        log.debug("REST request to get BookStore : {}", id);
        Optional<BookStore> bookStore = bookStoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookStore);
    }

    /**
     * {@code DELETE  /book-stores/:id} : delete the "id" bookStore.
     *
     * @param id the id of the bookStore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/book-stores/{id}")
    public ResponseEntity<Void> deleteBookStore(@PathVariable Long id) {
        log.debug("REST request to delete BookStore : {}", id);
        bookStoreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
