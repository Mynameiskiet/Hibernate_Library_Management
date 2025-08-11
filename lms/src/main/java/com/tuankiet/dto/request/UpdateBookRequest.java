package com.tuankiet.dto.request;

import com.tuankiet.enums.BookCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

/**
* DTO for updating an existing Book.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class UpdateBookRequest {

  @NotNull(message = "Book ID cannot be null")
  private UUID id;

  @NotBlank(message = "Title cannot be blank")
  @Size(max = 255, message = "Title cannot exceed 255 characters")
  private String title;

  @NotBlank(message = "ISBN cannot be blank")
  @Pattern(regexp = "^(?:ISBN(?:-13)?:?)(?=[0-9]{13}$)([0-9]{3}-){2}[0-9]{3}[0-9X]$", message = "Invalid ISBN-13 format")
  private String isbn;

  @Min(value = 1000, message = "Publication year must be a valid year (e.g., 1900)")
  private Integer publicationYear;

  @NotNull(message = "Category cannot be null")
  private BookCategory category;

  @Min(value = 0, message = "Available copies cannot be negative")
  @NotNull(message = "Available copies cannot be null")
  private Integer availableCopies;

  @Min(value = 1, message = "Total copies must be at least 1")
  @NotNull(message = "Total copies cannot be null")
  private Integer totalCopies;

  @NotNull(message = "Author ID cannot be null")
  private UUID authorId;

  public UpdateBookRequest() {
  }

  public UpdateBookRequest(UUID id, String title, String isbn, Integer publicationYear, BookCategory category, Integer availableCopies, Integer totalCopies, UUID authorId) {
      this.id = id;
      this.title = title;
      this.isbn = isbn;
      this.publicationYear = publicationYear;
      this.category = category;
      this.availableCopies = availableCopies;
      this.totalCopies = totalCopies;
      this.authorId = authorId;
  }

  public UUID getId() {
      return id;
  }

  public void setId(UUID id) {
      this.id = id;
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

  public UUID getAuthorId() {
      return authorId;
  }

  public void setAuthorId(UUID authorId) {
      this.authorId = authorId;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      UpdateBookRequest that = (UpdateBookRequest) o;
      return Objects.equals(id, that.id) &&
             Objects.equals(title, that.title) &&
             Objects.equals(isbn, that.isbn) &&
             Objects.equals(publicationYear, that.publicationYear) &&
             category == that.category &&
             Objects.equals(availableCopies, that.availableCopies) &&
             Objects.equals(totalCopies, that.totalCopies) &&
             Objects.equals(authorId, that.authorId);
  }

  @Override
  public int hashCode() {
      return Objects.hash(id, title, isbn, publicationYear, category, availableCopies, totalCopies, authorId);
  }

  @Override
  public String toString() {
      return "UpdateBookRequest{" +
             "id=" + id +
             ", title='" + title + '\'' +
             ", isbn='" + isbn + '\'' +
             ", publicationYear=" + publicationYear +
             ", category=" + category +
             ", availableCopies=" + availableCopies +
             ", totalCopies=" + totalCopies +
             ", authorId=" + authorId +
             '}';
  }
}
