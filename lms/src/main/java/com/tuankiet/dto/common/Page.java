package com.tuankiet.dto.common;

import java.util.List;
import java.util.Objects;

/**
* Represents a page of results from a paginated query.
* 
* @param <T> The type of elements in the page.
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class Page<T> {
  private List<T> content;
  private long totalElements;
  private int totalPages;
  private int currentPage;
  private int pageSize;

  public Page(List<T> content, long totalElements, int currentPage, int pageSize) {
      this.content = content;
      this.totalElements = totalElements;
      this.pageSize = pageSize;
      this.currentPage = currentPage;
      this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
  }

  public List<T> getContent() {
      return content;
  }

  public void setContent(List<T> content) {
      this.content = content;
  }

  public long getTotalElements() {
      return totalElements;
  }

  public void setTotalElements(long totalElements) {
      this.totalElements = totalElements;
  }

  public int getTotalPages() {
      return totalPages;
  }

  public void setTotalPages(int totalPages) {
      this.totalPages = totalPages;
  }

  public int getCurrentPage() {
      return currentPage;
  }

  public void setCurrentPage(int currentPage) {
      this.currentPage = currentPage;
  }

  public int getPageSize() {
      return pageSize;
  }

  public void setPageSize(int pageSize) {
      this.pageSize = pageSize;
  }

  public boolean hasContent() {
      return content != null && !content.isEmpty();
  }

  public boolean hasNext() {
      return currentPage < totalPages - 1;
  }

  public boolean hasPrevious() {
      return currentPage > 0;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Page<?> page = (Page<?>) o;
      return totalElements == page.totalElements &&
             totalPages == page.totalPages &&
             currentPage == page.currentPage &&
             pageSize == page.pageSize &&
             Objects.equals(content, page.content);
  }

  @Override
  public int hashCode() {
      return Objects.hash(content, totalElements, totalPages, currentPage, pageSize);
  }

  @Override
  public String toString() {
      return "Page{" +
             "content=" + content.size() + " items" +
             ", totalElements=" + totalElements +
             ", totalPages=" + totalPages +
             ", currentPage=" + currentPage +
             ", pageSize=" + pageSize +
             '}';
  }
}
