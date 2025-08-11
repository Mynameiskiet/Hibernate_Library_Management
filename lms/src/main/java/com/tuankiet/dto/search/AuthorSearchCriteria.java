package com.tuankiet.dto.search;

import java.util.Objects;

/**
* DTO for searching Authors.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class AuthorSearchCriteria {
  private String firstName;
  private String lastName;

  public AuthorSearchCriteria() {
  }

  public AuthorSearchCriteria(String firstName, String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
  }

  public String getFirstName() {
      return firstName;
  }

  public void setFirstName(String firstName) {
      this.firstName = firstName;
  }

  public String getLastName() {
      return lastName;
  }

  public void setLastName(String lastName) {
      this.lastName = lastName;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      AuthorSearchCriteria that = (AuthorSearchCriteria) o;
      return Objects.equals(firstName, that.firstName) &&
             Objects.equals(lastName, that.lastName);
  }

  @Override
  public int hashCode() {
      return Objects.hash(firstName, lastName);
  }

  @Override
  public String toString() {
      return "AuthorSearchCriteria{" +
             "firstName='" + firstName + '\'' +
             ", lastName='" + lastName + '\'' +
             '}';
  }
}
