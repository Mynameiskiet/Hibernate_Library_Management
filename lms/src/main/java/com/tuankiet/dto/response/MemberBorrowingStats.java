package com.tuankiet.dto.response;

import java.util.Objects;
import java.util.UUID;

/**
 * DTO for member borrowing statistics.
 * 
 * @author congdinh2008
 * @version 1.0.0
 * @since 1.0.0
 */
public class MemberBorrowingStats {
    private UUID memberId;
    private String memberName;
    private String email;
    private long totalBorrows;
    private long currentBorrows;
    private long overdueBorrows;

    public MemberBorrowingStats() {
    }

    public MemberBorrowingStats(UUID memberId, String memberName, String email, long totalBorrows, long currentBorrows, long overdueBorrows) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.email = email;
        this.totalBorrows = totalBorrows;
        this.currentBorrows = currentBorrows;
        this.overdueBorrows = overdueBorrows;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTotalBorrows() {
        return totalBorrows;
    }

    public void setTotalBorrows(long totalBorrows) {
        this.totalBorrows = totalBorrows;
    }

    public long getCurrentBorrows() {
        return currentBorrows;
    }

    public void setCurrentBorrows(long currentBorrows) {
        this.currentBorrows = currentBorrows;
    }

    public long getOverdueBorrows() {
        return overdueBorrows;
    }

    public void setOverdueBorrows(long overdueBorrows) {
        this.overdueBorrows = overdueBorrows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberBorrowingStats that = (MemberBorrowingStats) o;
        return totalBorrows == that.totalBorrows &&
               currentBorrows == that.currentBorrows &&
               overdueBorrows == that.overdueBorrows &&
               Objects.equals(memberId, that.memberId) &&
               Objects.equals(memberName, that.memberName) &&
               Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, memberName, email, totalBorrows, currentBorrows, overdueBorrows);
    }

    @Override
    public String toString() {
        return "MemberBorrowingStats{" +
               "memberId=" + memberId +
               ", memberName='" + memberName + '\'' +
               ", email='" + email + '\'' +
               ", totalBorrows=" + totalBorrows +
               ", currentBorrows=" + currentBorrows +
               ", overdueBorrows=" + overdueBorrows +
               '}';
    }
}
