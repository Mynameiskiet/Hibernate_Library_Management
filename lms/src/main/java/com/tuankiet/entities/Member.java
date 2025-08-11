package com.tuankiet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalDate;
import java.util.Objects;

/**
* Represents a Member in the Library Management System.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@Entity
@Table(name = "Members")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Member extends BaseEntity {

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "phone_number", length = 20)
  private String phoneNumber;

  @Column(name = "address", columnDefinition = "NVARCHAR(MAX)")
  private String address;

  @Column(name = "registration_date", nullable = false)
  private LocalDate registrationDate;

  public Member() {
      this.registrationDate = LocalDate.now();
  }

  public Member(String firstName, String lastName, String email, String phoneNumber, String address) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.phoneNumber = phoneNumber;
      this.address = address;
      this.registrationDate = LocalDate.now();
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

  public String getAddress() {
      return address;
  }

  public void setAddress(String address) {
      this.address = address;
  }

  public LocalDate getRegistrationDate() {
      return registrationDate;
  }

  public void setRegistrationDate(LocalDate registrationDate) {
      this.registrationDate = registrationDate;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;
      Member member = (Member) o;
      return Objects.equals(email, member.email);
  }

  @Override
  public int hashCode() {
      return Objects.hash(super.hashCode(), email);
  }

  @Override
  public String toString() {
      return "Member{" +
             "id=" + getId() +
             ", firstName='" + firstName + '\'' +
             ", lastName='" + lastName + '\'' +
             ", email='" + email + '\'' +
             ", phoneNumber='" + phoneNumber + '\'' +
             ", address='" + address + '\'' +
             ", registrationDate=" + registrationDate +
             '}';
  }
}
