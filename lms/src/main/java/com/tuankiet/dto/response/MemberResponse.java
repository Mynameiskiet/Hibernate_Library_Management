package com.tuankiet.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
* DTO for Member response.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class MemberResponse {
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String address;
  private LocalDate registrationDate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public MemberResponse() {
  }

  public MemberResponse(UUID id, String firstName, String lastName, String email, String phoneNumber, String address, LocalDate registrationDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.phoneNumber = phoneNumber;
      this.address = address;
      this.registrationDate = registrationDate;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
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

  public LocalDateTime getCreatedAt() {
      return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
      return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      MemberResponse that = (MemberResponse) o;
      return Objects.equals(id, that.id) &&
             Objects.equals(firstName, that.firstName) &&
             Objects.equals(lastName, that.lastName) &&
             Objects.equals(email, that.email) &&
             Objects.equals(phoneNumber, that.phoneNumber) &&
             Objects.equals(address, that.address) &&
             Objects.equals(registrationDate, that.registrationDate) &&
             Objects.equals(createdAt, that.createdAt) &&
             Objects.equals(updatedAt, that.updatedAt);
  }

  @Override
  public int hashCode() {
      return Objects.hash(id, firstName, lastName, email, phoneNumber, address, registrationDate, createdAt, updatedAt);
  }

  @Override
  public String toString() {
      return "MemberResponse{" +
             "id=" + id +
             ", firstName='" + firstName + '\'' +
             ", lastName='" + lastName + '\'' +
             ", email='" + email + '\'' +
             ", phoneNumber='" + phoneNumber + '\'' +
             ", address='" + address + '\'' +
             ", registrationDate=" + registrationDate +
             ", createdAt=" + createdAt +
             ", updatedAt=" + updatedAt +
             '}';
  }
}
