package com.tuankiet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
* Base entity class providing common fields like ID, creation and update timestamps.
* Uses UUID for primary keys.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public BaseEntity() {
  }

  public BaseEntity(UUID id) {
      this.id = id;
  }

  @PrePersist
  protected void onCreate() {
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
      this.updatedAt = LocalDateTime.now();
  }

  public UUID getId() {
      return id;
  }

  public void setId(UUID id) {
      this.id = id;
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
      BaseEntity that = (BaseEntity) o;
      return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
      return Objects.hash(id);
  }

  @Override
  public String toString() {
      return "BaseEntity{" +
             "id=" + id +
             ", createdAt=" + createdAt +
             ", updatedAt=" + updatedAt +
             '}';
  }
}
