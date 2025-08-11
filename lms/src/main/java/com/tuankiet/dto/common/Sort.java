package com.tuankiet.dto.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
* Represents sorting information for queries.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class Sort {
  private final List<SortCriteria> criteria;

  private Sort(List<SortCriteria> criteria) {
      this.criteria = Collections.unmodifiableList(criteria);
  }

  public static Sort by(SortCriteria... criteria) {
      return new Sort(Arrays.asList(criteria));
  }

  public static Sort by(List<SortCriteria> criteria) {
      return new Sort(new ArrayList<>(criteria));
  }

  public static Sort unsorted() {
      return new Sort(Collections.emptyList());
  }

  public List<SortCriteria> getCriteria() {
      return criteria;
  }

  public boolean isSorted() {
      return !criteria.isEmpty();
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Sort sort = (Sort) o;
      return Objects.equals(criteria, sort.criteria);
  }

  @Override
  public int hashCode() {
      return Objects.hash(criteria);
  }

  @Override
  public String toString() {
      return "Sort{" +
             "criteria=" + criteria +
             '}';
  }
}
