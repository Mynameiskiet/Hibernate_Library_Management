package com.tuankiet.dto.common;

import java.util.List;
import java.util.Objects;

/**
* Represents a request for a paginated and sorted list of results.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class PageRequest {
  private int page;
  private int size;
  private Sort sort;

  public PageRequest(int page, int size) {
      this(page, size, Sort.unsorted());
  }

  public PageRequest(int page, int size, Sort sort) {
      if (page < 0) {
          throw new IllegalArgumentException("Page index must not be less than zero!");
      }
      if (size < 1) {
          throw new IllegalArgumentException("Page size must not be less than one!");
      }
      this.page = page;
      this.size = size;
      this.sort = sort;
  }

  public int getPage() {
      return page;
  }

  public int getSize() {
      return size;
  }

  public Sort getSort() {
      return sort;
  }

  public long getOffset() {
      return (long) page * size;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PageRequest that = (PageRequest) o;
      return page == that.page &&
             size == that.size &&
             Objects.equals(sort, that.sort);
  }

  @Override
  public int hashCode() {
      return Objects.hash(page, size, sort);
  }

  @Override
  public String toString() {
      return "PageRequest{" +
             "page=" + page +
             ", size=" + size +
             ", sort=" + sort +
             '}';
  }
}
