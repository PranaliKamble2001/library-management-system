package com.library.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.library.dao.BookDAO;
import com.library.dao.BookDAOImpl;
import com.library.dao.LoanDAO;
import com.library.dao.LoanDAOImpl;
import com.library.dao.MemberDAO;
import com.library.dao.MemberDAOImpl;
import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Member;

public class LibraryService 
{
     private final BookDAO bookDAO;
     private final MemberDAO memberDAO;
     private final LoanDAO loanDAO;
     
     public LibraryService()
     {
    	 this.bookDAO=new BookDAOImpl();
    	 this.memberDAO=new MemberDAOImpl();
    	 this.loanDAO=new LoanDAOImpl();
     }
     
     public LibraryService(BookDAO bookDAO, MemberDAO memberDAO, LoanDAO loanDAO)
     {
    	 this.bookDAO=bookDAO;
    	 this.memberDAO=memberDAO;
    	 this.loanDAO=loanDAO;
     }
     
     
     public Book addBook(String isbn, String title, String author, String publisher,
    		 String category, int copies) throws SQLException
     {
    	 Book book=new Book(isbn, title, author, publisher, category, copies, copies);
    	 return bookDAO.addBook(book);
     }
     
     public boolean updateBook(Book book) throws SQLException
     {
    	 return bookDAO.updateBook(book);
     }
     
     public boolean deleteBook(int bookId) throws SQLException
     {
    	 return bookDAO.deleteBook(bookId);
     }
     
     public List<Book> listAllBooks() throws SQLException
     {
         return bookDAO.getAllBooks();
     }

     public List<Book> searchBooks(String keyword) throws SQLException
     {
         return bookDAO.searchBooks(keyword);
     }

     public Book getBook(int bookId) throws SQLException
     {
         return bookDAO.getBookById(bookId);
     }
     
     public Member registerMember(String name, String email, String phone, String address) throws SQLException, LibraryException
     {
    	 if(memberDAO.getMemberByEmail(email) !=null)
    	 {
    		 throw new LibraryException("A member with email '" + email + "' is already registered.");
    		 
    	 }
    	 Member member=new Member(name,email,phone,address);
    	 member.setMembershipDate(LocalDate.now());
    	 
    	 return memberDAO.addMember(member);
     }
     
     public boolean updateMember(Member member ) throws SQLException
     {
    	 return memberDAO.updateMember(member);
     }
     
     public List<Member> listAllMembers()throws SQLException
     {
    	 return memberDAO.getAllMembers();
     }
     
     public List<Member> searchMembers(String keyword) throws SQLException
     {
    	 return memberDAO.searchMembers(keyword);
     }
     
     
     public Member getMember(int memberId) throws SQLException
     {
    	 return memberDAO.getMemberById(memberId);
     }
     
     public boolean deleteMember(int memberId) throws SQLException
     {
    	 return memberDAO.deleteMember(memberId);
     }
     
     public Loan issueBook(int bookId, int memberId) throws SQLException, LibraryException
     {
    	 Book book=bookDAO.getBookById(bookId);
    	 
    	 if(book==null)
    	 {
    		 throw new LibraryException("No book found with ID " + bookId);
    		 
    	 }
    	  
    	if(!book.isAvailable())
    	{
    		throw new LibraryException("No available copies of '" + book.getTitle() + "' to issue.");
    	}
    	
    	
    	Member member=memberDAO.getMemberById(memberId);
    	
    	if(member == null)
    	{
    		throw new LibraryException("No member found with ID " + memberId);
    	}
    	
    	if(member.getStatus() == Member.Status.INACTIVE)
    	{
    		throw new LibraryException ("Member '" + member.getName() + "' is inactive and cannot borrow books.");
    		
    	}
    	
    	LocalDate issueDate=LocalDate.now();
    	LocalDate dueDate=issueDate.plusDays(Loan.LOAN_PERIOD_DAYS);
    	
    	
    	
    	Loan loan=new Loan(bookId, memberId, issueDate, dueDate);
    	loan = loanDAO.issueBook(loan);
    	
    	bookDAO.updateAvailableCopies(bookId, -1);
    	
    	return loan;
    	
     }
     
     
     
     public Loan returnBook(int loanId) throws SQLException, LibraryException
     {
    	 Loan loan = loanDAO.getLoanById(loanId);
    	 
    	 if(loan==null)
    	 {
    		 throw new LibraryException("No loan found with ID " + loanId);
    	 }
    	 
    	 if(loan.getStatus()== Loan.Status.RETURNED)
    	 {
    		 throw new LibraryException("This loan has already been returned.");
    	 }
    	 
    	 LocalDate returnDate=LocalDate.now();
    	 double fine=loan.calculateFine(returnDate);
    	 
    	 loanDAO.returnBook(loanId, returnDate, fine);
    	 bookDAO.updateAvailableCopies(loan.getBookId(), +1);
    	 
    	  loan.setReturnDate(returnDate);
    	  loan.setFineAmount(fine);
    	  loan.setStatus(Loan.Status.RETURNED);
    	  
    	  return loan;
    	 
     }
     
     public List<Loan> listActiveLoansForMember(int memberId) throws SQLException
     {
    	 return loanDAO.getActiveLoansByMember(memberId);
     }
     
     public List<Loan> listAllLoans() throws SQLException
     {
    	 return loanDAO.getAllLoans();
     }
     
     public List<Loan> listOverdueLoans() throws SQLException
     {
    	  return loanDAO.getOverdueLoans();
     }
     

}



