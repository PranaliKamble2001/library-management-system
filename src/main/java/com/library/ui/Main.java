package com.library.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.library.db.DBConnection;
import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.service.LibraryService;

public class Main 
{
     private static final Scanner scanner = new Scanner(System.in);
     private static final LibraryService service = new LibraryService();
     
     public static void main(String[] args)
     {
    	 System.out.println("=============================================");
    	 System.out.println("               LIBRARY MANAGEMENT SYSTEM              ");
    	 System.out.println("=============================================");
    	 
    	 boolean running=true;
    	 
    	 while(running) 
    	 {
    		 printMainMenu();
    		 int choice = readInt("Enter choice: ");
    		 
    		 try
    		 {
    			 switch(choice)
    			 {
    			 case 1 -> bookMenu();
    			 case 2 -> memberMenu();
    			 case 3 -> loanMenu();
    			 case 0 ->
    			 {
    				 running = false;
    				 System.out.println("Goodbye");
    				 
    			 }
    			 default -> System.out.println("Invalid choice, try again.");
    			 }
    		 }
    			 catch (SQLException e)
    			 {
    				 System.out.println("Database error: " + e.getMessage());
    			 }
    		     catch (LibraryException e)
    		     {
    		    	System.out.println("Error: " + e.getMessage()); 
    		     }
    	 }
    		 DBConnection.closeConnection();
    		 scanner.close();
     }
    	 
    	 private static void printMainMenu()
    	 {
    		 System.out.println("\n--- MAIN MENU ---");
    		 System.out.println("1. Manage Books");
    		 System.out.println("2. Manage Members");
    		 System.out.println("3. Issue/ Return Books");
    		 System.out.println("0. Exit");
    	 }
    	 
    	 private static int readInt(String prompt)
    	 {
    		 while(true)
    		 {
    			 System.out.print(prompt);
    			 String line = scanner.nextLine().trim();
    			  try
    			  {
    				  return Integer.parseInt(line);
    			  }
    			  catch (NumberFormatException e)
    			  {
    				  System.out.println("Please enter a valid number. ");
    			  }
    		 }
    	 }
    	 private static String readString(String prompt)
    	 {
    		 System.out.print(prompt);
    		 return scanner.nextLine().trim();
    	 }
    	 
    	 
    	 private static void bookMenu() throws SQLException
    	 {
    		 System.out.println("\n--- BOOK MENU ---");
    		 System.out.println("1. Add Book");
    		 System.out.println("2. Update Book");
    		 System.out.println("3. Delete Book");
    		 System.out.println("4. List All Books");
    		 System.out.println("5. Search Books");
    		 System.out.println("0. Back");
    		 int choice = readInt("Enter choice : ");
    		 
    		 
    		 switch (choice)
    		 {
    		       case 1 ->
    		      {
    			     String isbn = readString("ISBN: ");
    			     String title = readString("Title: ");
    			     String author = readString("Author: ");
    			     String publisher = readString("Publisher: ");
    			     String category = readString("Category: ");
    			     int copies = readInt("Total copies: ");
    			     Book book = service.addBook(isbn, title, author, publisher, category, copies);
    			     System.out.println("Added: " + book);
    		      }
    		       case 2 ->
    		       {
    		    	   int id = readInt("Book ID to update: ");
    		    	   Book book = service.getBook(id);
    		    	   if(book == null)
    		    	   {
    		    		   System.out.println("Book not found.");
    		    		   return;
    		    	   }
    		    	   
    		    	   String newTitle = readString("New title (" + book.getTitle() + "): ");
    		    	   if(!newTitle.isEmpty()) book.setTitle(newTitle);
    		    	   String newAuthor = readString("New author (" + book.getAuthor() + "): ");
    		    	   if(!newAuthor.isEmpty()) book.setAuthor(newAuthor);
    		    	   boolean ok = service.updateBook(book);
    		    	   System.out.println(ok ? "Updated." : "Update Failed");
    		       }
    		       case 3 ->
    		       {
    		    	   int id = readInt("Book ID to delete:");
    		    	   boolean ok = service.deleteBook(id);
    		    	   System.out.println(ok ? "Deleted." : "Delete failed(book may not exist).");
    		       }
    		       case 4 ->
    		       {
    		    	   List<Book> books = service.listAllBooks();
    		    	   if(books.isEmpty()) System.out.println("No books in inventory.");
    		    	   books.forEach(System.out ::println);
    		       }
    		       case 5 ->
    		       {
    		    	   String keyword = readString("Search keyword(title/author/ISBN): ");
    		    	   List<Book> results = service.searchBooks(keyword);
    		    	   if(results.isEmpty()) System.out.println("No matches found.");
    		    	   results.forEach(System.out::println);
    		       }
    		       case 0 -> {}
    		       
    		    	   default -> System.out.println("Invalid choice");
    		       
    		 }
    		 
    		 
    	 }
    	 
    	 private static void memberMenu() throws SQLException, LibraryException
    	 {
    		 System.out.println("\n--- MEMBER MENU ---");
    		 System.out.println("1. Register Member");
    		 System.out.println("2. Update Member");
    		 System.out.println("3. Delete Member");
    		 System.out.println("4. List All Members");
    		 System.out.println("5. Search Members");
    		 System.out.println("0. Back");
    		 int choice = readInt("Enter the choice: ");
    		 
    		 switch (choice)
    		 {
    		     case 1 ->
    		     {
    		    	 String name = readString("Name: ");
    		    	 String email = readString("Email: ");
    		    	 String phone = readString("Phone: ");
    		    	 String address = readString("Address: ");
    		    	  Member member = service.registerMember(name, email, phone, address);
    		    	  System.out.println("Registered: " + member);
    		     }
    		     case 2 ->
    		     {
    		    	 int id =readInt("Member ID to update:");
    		    	 Member member = service.getMember(id);
    		    	 if(member==null)
    		    	 {
    		    		 System.out.println("Member not found");
    		    		 return;
    		    	 }
    		    	 
    		    	 String newPhone = readString("New phone (" + member.getPhone() + "):");
    		    	 if(!newPhone.isEmpty()) member.setPhone(newPhone);
    		    	 String newAddress = readString("New address (" + member.getAddress() + "): ");
    		    	 if(!newAddress.isEmpty()) member.setAddress(newAddress);
    		    	 boolean ok = service.updateMember(member);
    		    	 System.out.println(ok ? "Updated." : "Update Failed.");
    		     }
    		     
    		     case 3 ->
    		     {
    		    	 int id = readInt("Member ID to delete: ");
    		    	 boolean ok = service.deleteMember(id);
    		    	 System.out.println(ok ? "Deleted." : "Delete Failed (member may not exist)");
    		     }
    		     
    		     case 4 ->
    		     {
    		    	 List<Member> members = service.listAllMembers();
    		    	 if(members.isEmpty()) System.out.println("No members registered.");
    		    	 members.forEach(System.out::println);
    		     }
    		     
    		     case 5 ->
    		     {
    		    	 String keyword = readString("Search keyword(name/email):");
    		    	 List<Member>results = service.searchMembers(keyword);
    		    	 if(results.isEmpty()) System.out.println("No matches found.");
    		    	 results.forEach(System.out::println);
    		     }
    		     
    		     case 0 -> {}
    		     default -> System.out.println("Invalid choice.");
    		     
    		 }
    		 
    	 }
    	 
    	 private static void loanMenu() throws SQLException, LibraryException
    	 {
    		 System.out.println("\n--- ISSUE / RETURN MENU ---");
    		 System.out.println("1. Issue Book to Member");
    		 System.out.println("2. Return Book");
    		 System.out.println("3. View Active Loans for a Member");
    		 System.out.println("4. View All Loans");
    		 System.out.println("5. View overdue Loans");
    		 System.out.println("0. Back");
    		 int choice = readInt("Enter choice: ");
    		 
    		 switch (choice)
    		 {
    		      case 1 ->
    		      {
    		    	  int bookId = readInt("Book ID: ");
    		    	  int memberId = readInt("Member ID: ");
    		    	  Loan loan = service.issueBook(bookId, memberId);
    		    	  System.out.println("Issued: " + loan);
    		      }
    		      
    		      case 2 ->
    		      {
    		    	  int loanId = readInt("Loan ID to return: ");
    		    	  Loan loan = service.returnBook(loanId);
    		    	  System.out.printf("Returned. Fine due: %.2f%n" , loan.getFineAmount());;
    		      }
    		      
    		      case 3 ->
    		      {
    		    	  int memberId = readInt("Member ID: ");
    		    	  List<Loan> loans = service.listActiveLoansForMember(memberId);
    		    	  if(loans.isEmpty()) System.out.println("No active loans.");
    		    	  loans.forEach(System.out::println);
    		      }
    		      
    		      case 4 ->
    		      {
    		    	  List<Loan> loans = service.listAllLoans();
    		    	  if(loans.isEmpty()) System.out.println("No loan records.");
    		    	  loans.forEach(System.out::println);
    		      }
    		       
    		      case 5 ->
    		      {
    		    	  List<Loan> loans = service.listOverdueLoans();
    		    	  if(loans.isEmpty()) System.out.println("No overdue loans.");
    		    	  loans.forEach(System.out::println);
    		      }
    		      
    		      case 0 -> {}
    		      default -> System.out.println("Invalid choice.");
    		      
    		 }
    	 }
    	 
}  
     


