package com.tuankiet.entities;

import com.tuankiet.enums.BookCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;

/**
* Represents a Book in the Library Management System.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@Entity
@Table(name = "Books")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Book extends BaseEntity {

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "isbn", unique = true, nullable = false, length = 13)
  private String isbn;

  @Column(name = "publication_year")
  private Integer publicationYear;

  @Enumerated(EnumType.STRING)
  @Column(name = "category")
  private BookCategory category;

  @Column(name = "available_copies", nullable = false)
  private Integer availableCopies;

  @Column(name = "total_copies", nullable = false)
  private Integer totalCopies;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private Author author;

  public Book() {
  }

  public Book(String title, String isbn, Integer publicationYear, BookCategory category, Integer totalCopies, Author author) {
      this.title = title;
      this.isbn = isbn;
      this.publicationYear = publicationYear;
      this.category = category;
      this.totalCopies = totalCopies;
      this.availableCopies = totalCopies; // Initially, all copies are available
      this.author = author;
  }

  public String getTitle() {
      return title;
  }

  public void setTitle(String title) {
      this.title = title;
  }

  public String getIsbn() {
      return isbn;
  }

  public void setIsbn(String isbn) {
      this.isbn = isbn;
  }

  public Integer getPublicationYear() {
      return publicationYear;
  }

  public void setPublicationYear(Integer publicationYear) {
      this.publicationYear = publicationYear;
  }

  public BookCategory getCategory() {
      return category;
  }

  public void setCategory(BookCategory category) {
      this.category = category;
  }

  public Integer getAvailableCopies() {
      return availableCopies;
  }

  public void setAvailableCopies(Integer availableCopies) {
      this.availableCopies = availableCopies;
  }

  public Integer getTotalCopies() {
      return totalCopies;
  }

  public void setTotalCopies(Integer totalCopies) {
      this.totalCopies = totalCopies;
  }

  public Author getAuthor() {
      return author;
  }

  public void setAuthor(Author author) {
      this.author = author;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;
      Book book = (Book) o;
      return Objects.equals(isbn, book.isbn);
  }

  @Override
  public int hashCode() {
      return Objects.hash(super.hashCode(), isbn);
  }

  @Override
  public String toString() {
      return "Book{" +
             "id=" + getId() +
             ", title='" + title + '\'' +
             ", isbn='" + isbn + '\'' +
             ", publicationYear=" + publicationYear +
             ", category=" + category +
             ", availableCopies=" + availableCopies +
             ", totalCopies=" + totalCopies +
             ", author=" + (author != null ? author.getFirstName() + " " + author.getLastName() : "N/A") +
             '}';
  }
}
