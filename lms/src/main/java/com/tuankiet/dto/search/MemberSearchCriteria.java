package com.tuankiet.dto.search;

import java.util.Objects;

/**
* DTO for searching Members.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class MemberSearchCriteria {
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;

  public MemberSearchCriteria() {
  }

  public MemberSearchCriteria(String firstName, String lastName, String email, String phoneNumber) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.phoneNumber = phoneNumber;
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

  public String getEmail() {
      return email;
  }

  public void setEmail(String email) {
      this.email = email;
  }

  public String getPhoneNumber() {
      return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      MemberSearchCriteria that = (MemberSearchCriteria) o;
      return Objects.equals(firstName, that.firstName) &&
             Objects.equals(lastName, that.lastName) &&
             Objects.equals(email, that.email) &&
             Objects.equals(phoneNumber, that.phoneNumber);
  }

  @Override
  public int hashCode() {
      return Objects.hash(firstName, lastName, email, phoneNumber);
  }

  @Override
  public String toString() {
      return "MemberSearchCriteria{" +
             "firstName='" + firstName + '\'' +
             ", lastName='" + lastName + '\'' +
             ", email='" + email + '\'' +
             ", phoneNumber='" + phoneNumber + '\'' +
             '}';
  }
}
