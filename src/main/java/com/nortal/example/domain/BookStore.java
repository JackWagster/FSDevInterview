package com.nortal.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@Entity
@Table(name = "book_store")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "book_store_name")
    private String bookStoreName;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "state_province")
    private String stateProvince;

    @ManyToMany
    @JoinTable(
        name = "rel_book_store__book",
        joinColumns = @JoinColumn(name = "book_store_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "authors", "bookstores" }, allowSetters = true)
    private Set<Book> books = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BookStore id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookStoreName() {
        return this.bookStoreName;
    }

    public BookStore bookStoreName(String bookStoreName) {
        this.setBookStoreName(bookStoreName);
        return this;
    }

    public void setBookStoreName(String bookStoreName) {
        this.bookStoreName = bookStoreName;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public BookStore postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public BookStore city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return this.stateProvince;
    }

    public BookStore stateProvince(String stateProvince) {
        this.setStateProvince(stateProvince);
        return this;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public Set<Book> getBooks() {
        return this.books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public BookStore books(Set<Book> books) {
        this.setBooks(books);
        return this;
    }

    public BookStore addBook(Book book) {
        this.books.add(book);
        book.getBookstores().add(this);
        return this;
    }

    public BookStore removeBook(Book book) {
        this.books.remove(book);
        book.getBookstores().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookStore)) {
            return false;
        }
        return id != null && id.equals(((BookStore) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookStore{" +
            "id=" + getId() +
            ", bookStoreName='" + getBookStoreName() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            "}";
    }
}
