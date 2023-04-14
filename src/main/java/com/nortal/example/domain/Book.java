package com.nortal.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Task entity.\n@author The JHipster team.
 */
@Schema(description = "Task entity.\n@author The JHipster team.")
@Entity
@Table(name = "book")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(name = "rel_book__author", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "books" }, allowSetters = true)
    private Set<Author> authors = new HashSet<>();

    @ManyToMany(mappedBy = "books")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "books" }, allowSetters = true)
    private Set<BookStore> bookstores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Book id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Book title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Book description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Author> getAuthors() {
        return this.authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Book authors(Set<Author> authors) {
        this.setAuthors(authors);
        return this;
    }

    public Book addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
        return this;
    }

    public Book removeAuthor(Author author) {
        this.authors.remove(author);
        author.getBooks().remove(this);
        return this;
    }

    public Set<BookStore> getBookstores() {
        return this.bookstores;
    }

    public void setBookstores(Set<BookStore> bookStores) {
        if (this.bookstores != null) {
            this.bookstores.forEach(i -> i.removeBook(this));
        }
        if (bookStores != null) {
            bookStores.forEach(i -> i.addBook(this));
        }
        this.bookstores = bookStores;
    }

    public Book bookstores(Set<BookStore> bookStores) {
        this.setBookstores(bookStores);
        return this;
    }

    public Book addBookstore(BookStore bookStore) {
        this.bookstores.add(bookStore);
        bookStore.getBooks().add(this);
        return this;
    }

    public Book removeBookstore(BookStore bookStore) {
        this.bookstores.remove(bookStore);
        bookStore.getBooks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        return id != null && id.equals(((Book) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
