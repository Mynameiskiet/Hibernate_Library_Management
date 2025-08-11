package com.tuankiet.entities;

import com.tuankiet.enums.BorrowingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalDate;
import java.util.Objects;

/**
* Represents a Borrowing record in the Library Management System.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@Entity
@Table(name = "Borrowings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Borrowing extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @Column(name = "borrow_date", nullable = false)
  private LocalDate borrowDate;

  @Column(name = "return_date")
  private LocalDate returnDate;

  @Column(name = "due_date", nullable = false)
  private LocalDate dueDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private BorrowingStatus status;

  public Borrowing() {
  }

  public Borrowing(Book book, Member member, LocalDate borrowDate, LocalDate dueDate, BorrowingStatus status) {
      this.book = book;
      this.member = member;
      this.borrowDate = borrowDate;
      this.dueDate = dueDate;
      this.status = status;
  }

  public Book getBook() {
      return book;
  }

  public void setBook(Book book) {
      this.book = book;
  }

  public Member getMember() {
      return member;
  }

  public void setMember(Member member) {
      this.member = member;
  }

  public LocalDate getBorrowDate() {
      return borrowDate;
  }

  public void setBorrowDate(LocalDate borrowDate) {
      this.borrowDate = borrowDate;
  }

  public LocalDate getReturnDate() {
      return returnDate;
  }

  public void setReturnDate(LocalDate returnDate) {
      this.returnDate = returnDate;
  }

  public LocalDate getDueDate() {
      return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
      this.dueDate = dueDate;
  }

  public BorrowingStatus getStatus() {
      return status;
  }

  public void setStatus(BorrowingStatus status) {
      this.status = status;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;
      Borrowing borrowing = (Borrowing) o;
      return Objects.equals(book, borrowing.book) &&
             Objects.equals(member, borrowing.member) &&
             Objects.equals(borrowDate, borrowing.borrowDate);
  }

  @Override
  public int hashCode() {
      return Objects.hash(super.hashCode(), book, member, borrowDate);
  }

  @Override
  public String toString() {
      return "Borrowing{" +
             "id=" + getId() +
             ", book=" + (book != null ? book.getTitle() : "N/A") +
             ", member=" + (member != null ? member.getFirstName() + " " + member.getLastName() : "N/A") +
             ", borrowDate=" + borrowDate +
             ", returnDate=" + returnDate +
             ", dueDate=" + dueDate +
             ", status=" + status +
             '}';
  }
}
