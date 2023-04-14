package com.nortal.example.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.nortal.example.web.controller.TestUtil;
import org.junit.jupiter.api.Test;

class BookStoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookStore.class);
        BookStore bookStore1 = new BookStore();
        bookStore1.setId(1L);
        BookStore bookStore2 = new BookStore();
        bookStore2.setId(bookStore1.getId());
        assertThat(bookStore1).isEqualTo(bookStore2);
        bookStore2.setId(2L);
        assertThat(bookStore1).isNotEqualTo(bookStore2);
        bookStore1.setId(null);
        assertThat(bookStore1).isNotEqualTo(bookStore2);
    }
}
