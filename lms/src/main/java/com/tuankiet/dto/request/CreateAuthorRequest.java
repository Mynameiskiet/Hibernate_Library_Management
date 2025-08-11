package com.tuankiet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
* DTO for creating a new Author.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class CreateAuthorRequest {

  @NotBlank(message = "First name cannot be blank")
  @Size(max = 255, message = "First name cannot exceed 255 characters")
  private String firstName;

  @NotBlank(message = "Last name cannot be blank")
  @Size(max = 255, message = "Last name cannot exceed 255 characters")
  private String lastName;

  @Size(max = 4000, message = "Biography cannot exceed 4000 characters")
  private String biography;

  public CreateAuthorRequest() {
  }

  public CreateAuthorRequest(String firstName, String lastName, String biography) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.biography = biography;
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

  public String getBiography() {
      return biography;
  }

  public void setBiography(String biography) {
      this.biography = biography;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CreateAuthorRequest that = (CreateAuthorRequest) o;
      return Objects.equals(firstName, that.firstName) &&
             Objects.equals(lastName, that.lastName) &&
             Objects.equals(biography, that.biography);
  }

  @Override
  public int hashCode() {
      return Objects.hash(firstName, lastName, biography);
  }

  @Override
  public String toString() {
      return "CreateAuthorRequest{" +
             "firstName='" + firstName + '\'' +
             ", lastName='" + lastName + '\'' +
             ", biography='" + biography + '\'' +
             '}';
  }
}
