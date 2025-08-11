package com.tuankiet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

/**
* DTO for updating an existing Author.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class UpdateAuthorRequest {

  @NotNull(message = "Author ID cannot be null")
  private UUID id;

  @NotBlank(message = "First name cannot be blank")
  @Size(max = 255, message = "First name cannot exceed 255 characters")
  private String firstName;

  @NotBlank(message = "Last name cannot be blank")
  @Size(max = 255, message = "Last name cannot exceed 255 characters")
  private String lastName;

  @Size(max = 4000, message = "Biography cannot exceed 4000 characters")
  private String biography;

  public UpdateAuthorRequest() {
  }

  public UpdateAuthorRequest(UUID id, String firstName, String lastName, String biography) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.biography = biography;
  }

  public UUID getId() {
      return id;
  }

  public void setId(UUID id) {
      this.id = id;
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
      UpdateAuthorRequest that = (UpdateAuthorRequest) o;
      return Objects.equals(id, that.id) &&
             Objects.equals(firstName, that.firstName) &&
             Objects.equals(lastName, that.lastName) &&
             Objects.equals(biography, that.biography);
  }

  @Override
  public int hashCode() {
      return Objects.hash(id, firstName, lastName, biography);
  }

  @Override
  public String toString() {
      return "UpdateAuthorRequest{" +
             "id=" + id +
             ", firstName='" + firstName + '\'' +
             ", lastName='" + lastName + '\'' +
             ", biography='" + biography + '\'' +
             '}';
  }
}
