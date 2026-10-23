package com.library.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.library.model.Loan;

public interface LoanDAO 
{
    
	Loan issueBook(Loan loan) throws SQLException;
	
	boolean returnBook(int loanId, LocalDate returnDate, double fineAmount) throws SQLException;
	
	Loan getLoanById(int loanId) throws SQLException;
	
	List<Loan> getActiveLoansByMember(int memberId) throws SQLException;
	
	List<Loan> getAllLoans() throws SQLException;
	
	List<Loan> getOverdueLoans() throws SQLException;
	
	
	
}
