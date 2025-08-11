package com.tuankiet.dto.search;

import com.tuankiet.enums.BookCategory;

import java.util.UUID;

/**
 * Search criteria for Book entities.
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
public class BookSearchCriteria {
    
    private String title;
    private String isbn;
    private Integer publicationYear;
    private BookCategory category;
    private UUID authorId;
    private String authorName;
    private Boolean availableOnly;

    /**
     * Default constructor.
     */
    public BookSearchCriteria() {
    }

    /**
     * Constructor with main search parameters.
     * 
     * @param title the title to search for
     * @param isbn the ISBN to search for
     * @param publicationYear the publication year
     * @param category the book category
     * @param authorId the author ID
     */
    public BookSearchCriteria(String title, String isbn, Integer publicationYear, BookCategory category, UUID authorId) {
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.category = category;
        this.authorId = authorId;
    }

    /**
     * Full constructor with all parameters.
     * 
     * @param title the title to search for
     * @param isbn the ISBN to search for
     * @param publicationYear the publication year
     * @param category the book category
     * @param authorId the author ID
     * @param authorName the author name
     * @param availableOnly whether to search only available books
     */
    public BookSearchCriteria(String title, String isbn, Integer publicationYear, BookCategory category, 
                             UUID authorId, String authorName, Boolean availableOnly) {
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.category = category;
        this.authorId = authorId;
        this.authorName = authorName;
        this.availableOnly = availableOnly;
    }

    // Getters and Setters
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

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Boolean getAvailableOnly() {
        return availableOnly;
    }

    public void setAvailableOnly(Boolean availableOnly) {
        this.availableOnly = availableOnly;
    }

    @Override
    public String toString() {
        return "BookSearchCriteria{" +
                "title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publicationYear=" + publicationYear +
                ", category=" + category +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", availableOnly=" + availableOnly +
                '}';
    }
}
