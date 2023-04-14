package com.nortal.example.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nortal.example.IntegrationTest;
import com.nortal.example.domain.BookStore;
import com.nortal.example.repository.BookStoreRepository;
import com.nortal.example.service.BookStoreService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BookStoreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookStoreResourceIT {

    private static final String DEFAULT_BOOK_STORE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BOOK_STORE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/book-stores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookStoreRepository bookStoreRepository;

    @Mock
    private BookStoreRepository bookStoreRepositoryMock;

    @Mock
    private BookStoreService bookStoreServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookStoreMockMvc;

    private BookStore bookStore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookStore createEntity(EntityManager em) {
        BookStore bookStore = new BookStore()
            .bookStoreName(DEFAULT_BOOK_STORE_NAME)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .stateProvince(DEFAULT_STATE_PROVINCE);
        return bookStore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookStore createUpdatedEntity(EntityManager em) {
        BookStore bookStore = new BookStore()
            .bookStoreName(UPDATED_BOOK_STORE_NAME)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE);
        return bookStore;
    }

    @BeforeEach
    public void initTest() {
        bookStore = createEntity(em);
    }

    @Test
    @Transactional
    void createBookStore() throws Exception {
        int databaseSizeBeforeCreate = bookStoreRepository.findAll().size();
        // Create the BookStore
        restBookStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookStore)))
            .andExpect(status().isCreated());

        // Validate the BookStore in the database
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeCreate + 1);
        BookStore testBookStore = bookStoreList.get(bookStoreList.size() - 1);
        assertThat(testBookStore.getBookStoreName()).isEqualTo(DEFAULT_BOOK_STORE_NAME);
        assertThat(testBookStore.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testBookStore.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testBookStore.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void createBookStoreWithExistingId() throws Exception {
        // Create the BookStore with an existing ID
        bookStore.setId(1L);

        int databaseSizeBeforeCreate = bookStoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookStore)))
            .andExpect(status().isBadRequest());

        // Validate the BookStore in the database
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBookStores() throws Exception {
        // Initialize the database
        bookStoreRepository.saveAndFlush(bookStore);

        // Get all the bookStoreList
        restBookStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookStoreName").value(hasItem(DEFAULT_BOOK_STORE_NAME)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookStoresWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookStoreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookStoreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookStoreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookStoresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookStoreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookStoreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookStoreRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBookStore() throws Exception {
        // Initialize the database
        bookStoreRepository.saveAndFlush(bookStore);

        // Get the bookStore
        restBookStoreMockMvc
            .perform(get(ENTITY_API_URL_ID, bookStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookStore.getId().intValue()))
            .andExpect(jsonPath("$.bookStoreName").value(DEFAULT_BOOK_STORE_NAME))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE));
    }

    @Test
    @Transactional
    void getNonExistingBookStore() throws Exception {
        // Get the bookStore
        restBookStoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookStore() throws Exception {
        // Initialize the database
        bookStoreRepository.saveAndFlush(bookStore);

        int databaseSizeBeforeUpdate = bookStoreRepository.findAll().size();

        // Update the bookStore
        BookStore updatedBookStore = bookStoreRepository.findById(bookStore.getId()).get();
        // Disconnect from session so that the updates on updatedBookStore are not directly saved in db
        em.detach(updatedBookStore);
        updatedBookStore
            .bookStoreName(UPDATED_BOOK_STORE_NAME)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE);

        restBookStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookStore.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBookStore))
            )
            .andExpect(status().isOk());

        // Validate the BookStore in the database
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeUpdate);
        BookStore testBookStore = bookStoreList.get(bookStoreList.size() - 1);
        assertThat(testBookStore.getBookStoreName()).isEqualTo(UPDATED_BOOK_STORE_NAME);
        assertThat(testBookStore.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testBookStore.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBookStore.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void putNonExistingBookStore() throws Exception {
        int databaseSizeBeforeUpdate = bookStoreRepository.findAll().size();
        bookStore.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookStore.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookStore in the database
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookStore() throws Exception {
        int databaseSizeBeforeUpdate = bookStoreRepository.findAll().size();
        bookStore.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookStore in the database
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookStore() throws Exception {
        int databaseSizeBeforeUpdate = bookStoreRepository.findAll().size();
        bookStore.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookStoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookStore)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookStore in the database
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookStoreWithPatch() throws Exception {
        // Initialize the database
        bookStoreRepository.saveAndFlush(bookStore);

        int databaseSizeBeforeUpdate = bookStoreRepository.findAll().size();

        // Update the bookStore using partial update
        BookStore partialUpdatedBookStore = new BookStore();
        partialUpdatedBookStore.setId(bookStore.getId());

        partialUpdatedBookStore
            .bookStoreName(UPDATED_BOOK_STORE_NAME)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE);

        restBookStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookStore))
            )
            .andExpect(status().isOk());

        // Validate the BookStore in the database
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeUpdate);
        BookStore testBookStore = bookStoreList.get(bookStoreList.size() - 1);
        assertThat(testBookStore.getBookStoreName()).isEqualTo(UPDATED_BOOK_STORE_NAME);
        assertThat(testBookStore.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testBookStore.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBookStore.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void fullUpdateBookStoreWithPatch() throws Exception {
        // Initialize the database
        bookStoreRepository.saveAndFlush(bookStore);

        int databaseSizeBeforeUpdate = bookStoreRepository.findAll().size();

        // Update the bookStore using partial update
        BookStore partialUpdatedBookStore = new BookStore();
        partialUpdatedBookStore.setId(bookStore.getId());

        partialUpdatedBookStore
            .bookStoreName(UPDATED_BOOK_STORE_NAME)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE);

        restBookStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookStore))
            )
            .andExpect(status().isOk());

        // Validate the BookStore in the database
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeUpdate);
        BookStore testBookStore = bookStoreList.get(bookStoreList.size() - 1);
        assertThat(testBookStore.getBookStoreName()).isEqualTo(UPDATED_BOOK_STORE_NAME);
        assertThat(testBookStore.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testBookStore.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBookStore.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void patchNonExistingBookStore() throws Exception {
        int databaseSizeBeforeUpdate = bookStoreRepository.findAll().size();
        bookStore.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookStore in the database
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookStore() throws Exception {
        int databaseSizeBeforeUpdate = bookStoreRepository.findAll().size();
        bookStore.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookStore in the database
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookStore() throws Exception {
        int databaseSizeBeforeUpdate = bookStoreRepository.findAll().size();
        bookStore.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookStoreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bookStore))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookStore in the database
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookStore() throws Exception {
        // Initialize the database
        bookStoreRepository.saveAndFlush(bookStore);

        int databaseSizeBeforeDelete = bookStoreRepository.findAll().size();

        // Delete the bookStore
        restBookStoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookStore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookStore> bookStoreList = bookStoreRepository.findAll();
        assertThat(bookStoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
