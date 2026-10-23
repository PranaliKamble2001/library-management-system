package com.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan 
{
	public enum Status
	{
		ISSUED, RETURNED, OVERDUE
	}
	
	public static final int LOAN_PERIOD_DAYS=14;
	public static final double FINE_PER_DAY=5.00;
	
   private int loanId;
   private int bookId;
   private int memberId;
   private LocalDate issueDate;
   private LocalDate dueDate;
   private LocalDate returnDate;
   private double fineAmount;
   private Status status;
   
   private String bookTitle;
   private String memberName;
   
   public Loan()
   {
	   
   }
   
   public Loan(int bookId, int memberId, LocalDate issueDate, LocalDate dueDate)
   {
	   this.bookId=bookId;
	   this.memberId=memberId;
	   this.issueDate=issueDate;
	   this.dueDate=dueDate;
	   this.status=Status.ISSUED;
	   this.fineAmount=0.0;
   }
   
   public void setLoanId(int loanId) {this.loanId=loanId;}
   public int getLoanId() {return loanId;}
   
   public void setBookId(int bookId) {this.bookId=bookId;}
   public int getBookId() {return bookId;}
   
   public void setMemberId(int memberId) {this.memberId=memberId;}
   public int getMemberId() {return memberId;}
   
   public void setIssueDate(LocalDate issueDate) {this.issueDate=issueDate;}
   public LocalDate getIssueDate() {return issueDate;}
   
   public void setDueDate(LocalDate dueDate) {this.dueDate=dueDate;}
   public LocalDate getDueDate() {return dueDate;}
   
   public void setReturnDate(LocalDate returnDate) {this.returnDate=returnDate;}
   public LocalDate getReturnDate() {return returnDate;}
   
   public void setFineAmount(double fineAmount) {this.fineAmount=fineAmount;}
   public double getFineAmount() {return fineAmount;}
   
   public void setStatus(Status status) {this.status=status;}
   public Status getStatus() {return status;}
   
   public void setBookTitle(String bookTitle) {this.bookTitle=bookTitle;}
   public String getBookTitle() {return bookTitle;}
   
   public void setMemberName(String memberName) {this.memberName=memberName;}
   public String getMemberName() {return memberName;}
   
   public double calculateFine(LocalDate onDate)
   {
	   if(onDate.isAfter(dueDate))
	   {
		   long daysLate=ChronoUnit.DAYS.between(dueDate, onDate);
		   return daysLate*FINE_PER_DAY;
	   }
	   return 0.0;
   }
   
   
   @Override
   public String toString() {
       String title = bookTitle != null ? bookTitle : ("BookID:" + bookId);
       String member = memberName != null ? memberName : ("MemberID:" + memberId);
       return String.format("[Loan #%d] %-25s -> %-20s | Issued: %s | Due: %s | Returned: %s | Fine: %.2f | %s",
               loanId, title, member, issueDate, dueDate,
               returnDate != null ? returnDate : "-", fineAmount, status);
   }
}
