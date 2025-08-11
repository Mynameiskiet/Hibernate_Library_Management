package com.tuankiet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;

/**
* Represents an Author in the Library Management System.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@Entity
@Table(name = "Authors")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Author extends BaseEntity {

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "biography", columnDefinition = "NVARCHAR(MAX)")
  private String biography;

  public Author() {
  }

  public Author(String firstName, String lastName, String biography) {
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
      if (!super.equals(o)) return false;
      Author author = (Author) o;
      return Objects.equals(firstName, author.firstName) &&
             Objects.equals(lastName, author.lastName);
  }

  @Override
  public int hashCode() {
      return Objects.hash(super.hashCode(), firstName, lastName);
  }

  @Override
  public String toString() {
      return "Author{" +
             "id=" + getId() +
             ", firstName='" + firstName + '\'' +
             ", lastName='" + lastName + '\'' +
             ", biography='" + biography + '\'' +
             '}';
  }
}
