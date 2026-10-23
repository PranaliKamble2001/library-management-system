package com.library.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import com.library.db.DBConnection;
import com.library.model.Loan;

public class LoanDAOImpl implements LoanDAO
{

	@Override
	public Loan issueBook(Loan loan) throws SQLException {
		String sql= "INSERT INTO loans(book_id, member_id, issue_date, due_date, status)"
				+ "VALUES(?,?,?,?,'ISSUED')";
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setInt(1, loan.getBookId());
			ps.setInt(2, loan.getMemberId());
			ps.setDate(3, Date.valueOf(loan.getIssueDate()));
			ps.setDate(4, Date.valueOf(loan.getDueDate()));
			
			ps.executeUpdate();
			
			try(ResultSet keys=ps.getGeneratedKeys())
			{
				if(keys.next())
				{
					loan.setLoanId(keys.getInt(1));
				}
			}
		}
		return loan;
	}

	@Override
	public boolean returnBook(int loanId, LocalDate returnDate, double fineAmount) throws SQLException {
		String sql="UPDATE loans SET return_date=?, fine_amount=?, status='RETURNED' WHERE loan_id=?";
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql))
		{
		
        ps.setDate(1, Date.valueOf(returnDate));
		ps.setDouble(2, fineAmount);
		ps.setInt(3, loanId);
	    return ps.executeUpdate()> 0;
	
	}
	}
	
	
	private String baseSelect()
	{
		return "SELECT l.*, b.title AS book_title, m.name AS member_name "
				+ "FROM loans l "
			    + "JOIN books b ON l.book_id=b.book_id "
			    + "JOIN members m ON l.member_id=m.member_id";
	}
	
	private Loan mapRow(ResultSet rs) throws SQLException
	{
		Loan loan=new Loan();
		loan.setLoanId(rs.getInt("loan_id"));
		loan.setBookId(rs.getInt("book_id"));
		loan.setMemberId(rs.getInt("member_id"));
		loan.setIssueDate(rs.getDate("issue_date").toLocalDate());
		loan.setDueDate(rs.getDate("due_date").toLocalDate());
		
		Date returned=rs.getDate("return_date");
		loan.setReturnDate(returned !=null ? returned.toLocalDate() : null);
		
		loan.setFineAmount(rs.getDouble("fine_amount"));
		loan.setStatus(Loan.Status.valueOf(rs.getString("status")));
		loan.setBookTitle(rs.getString("book_title"));
		loan.setMemberName(rs.getString("member_name"));
		
		return loan;
	}
	


	@Override
	public Loan getLoanById(int loanId) throws SQLException {
		String sql=baseSelect() + " WHERE l.loan_id=?";
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql))
		{
			ps.setInt(1, loanId);
			try(ResultSet rs=ps.executeQuery())
			{
				if(rs.next())
				{
					return mapRow(rs);
				}
			}
		}
				
		return null;		
	}

	@Override
	public List<Loan> getActiveLoansByMember(int memberId) throws SQLException {
		String sql= baseSelect() + " WHERE l.member_id=? AND l.status IN ('ISSUED','OVERDUE') ORDER BY l.due_date";
		List<Loan>loans=new ArrayList<>();
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql))
		{
			ps.setInt(1, memberId);
			try(ResultSet rs=ps.executeQuery())
			{
				while(rs.next())
				{
					loans.add(mapRow(rs));
				}
			}
		}
		return loans;
		
	}

	@Override
	public List<Loan> getAllLoans() throws SQLException {
		String sql = baseSelect() + " ORDER BY l.issue_date DESC";
		List<Loan>loans=new ArrayList<>();
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql);
				ResultSet rs=ps.executeQuery())
		{
			while(rs.next())
			{
				loans.add(mapRow(rs));
			}
		}
		
		return loans;
	}

	@Override
	public List<Loan> getOverdueLoans() throws SQLException {
		String sql = baseSelect() + " WHERE l.status='ISSUED' AND l.due_date < CURDATE() ORDER BY l.due_date";
		List<Loan>loans=new ArrayList<>();
		try(Connection conn=DBConnection.getConnection();
			PreparedStatement ps=conn.prepareStatement(sql);
				ResultSet rs=ps.executeQuery())
		{
			while(rs.next())
			{
				loans.add(mapRow(rs));
			}
		}
		return loans;
	}
    
}