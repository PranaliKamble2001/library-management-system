package com.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import com.library.db.DBConnection;
import com.library.model.Book;

public class BookDAOImpl implements BookDAO 
{

	@Override
	public Book addBook(Book book) throws SQLException {
		String sql= "INSERT INTO books(isbn, title, author, publisher, category, total_copies, available_copies)"
				+ "VALUES(?,?,?,?,?,?,?)";
		try(Connection conn= DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, book.getIsbn());
			ps.setString(2, book.getTitle());
			ps.setString(3, book.getAuthor());
			ps.setString(4, book.getPublisher());
			ps.setString(5, book.getCategory());
			ps.setInt(6,book.getTotalCopies());
			ps.setInt(7, book.getAvailableCopies());
			
			ps.executeUpdate();
			
			try(ResultSet keys = ps.getGeneratedKeys())
			{
				if(keys.next())
				{
					book.setBookId(keys.getInt(1));
				}
			}
			
		}
		return book;
	}

	@Override
	public boolean updateBook(Book book) throws SQLException {
		
		String sql = "UPDATE books SET isbn=?, title=?, author=?, publisher=?, category=?, "
				+ "total_copies=?, available_copies=? WHERE book_id=?";
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql))
		{
			ps.setString(1, book.getIsbn());
			ps.setString(2, book.getTitle());
			ps.setString(3, book.getAuthor());
			ps.setString(4, book.getPublisher());
			ps.setString(5, book.getCategory());
			ps.setInt(6, book.getTotalCopies());
			ps.setInt(7, book.getAvailableCopies());
			ps.setInt(8,book.getBookId());
			
			return ps.executeUpdate() > 0;
		}
	}

	@Override
	public boolean deleteBook(int bookId) throws SQLException {
		String sql = "DELETE FROM books WHERE book_id=?";
		try(Connection conn =DBConnection.getConnection();
		
			PreparedStatement ps=conn.prepareStatement(sql))
                {
                	ps.setInt(1, bookId);
                	return ps.executeUpdate() > 0;
	
                 }

		}
	

	@Override
	public Book getBookById(int bookId) throws SQLException 
	{
		String sql = "SELECT * FROM books WHERE book_id=?";
		try(Connection conn = DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql))
                {
	               ps.setInt(1, bookId);
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
	public List<Book> searchBooks(String keyword) throws SQLException 
	{
	   String sql ="SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ? ORDER BY title";
	   List<Book> books=new ArrayList<>();
	   String like= "%" + keyword + "%";
	   try(Connection conn=DBConnection.getConnection();
			   PreparedStatement ps=conn.prepareStatement(sql))
	   {
		   ps.setString(1, like);
		   ps.setString(2, like);
		   ps.setString(3, like);
		   try(ResultSet rs=ps.executeQuery())
		   {
			   while(rs.next())
			   {
				   books.add(mapRow(rs));
			   }
		   }
	   }
	   return books;
	}

	@Override
	public boolean updateAvailableCopies(int bookId, int delta) throws SQLException {
		String sql="UPDATE books SET available_copies=available_copies + ? WHERE book_id=?";
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql))
		{
			ps.setInt(1, delta);
			ps.setInt(2, bookId);
			return ps.executeUpdate()>0;
		}
	}
    	
    private Book mapRow(ResultSet rs) throws SQLException
    {
    	return new Book(
    			rs.getInt("book_id"),
    			rs.getString("isbn"),
    			rs.getString("title"),
    			rs.getString("author"),
    			rs.getString("publisher"),
    			rs.getString("category"),
    			rs.getInt("total_copies"),
    			rs.getInt("available_copies")
    			);
    			
    }

	@Override
	public List<Book> getAllBooks() throws SQLException {
		String sql= "SELECT * FROM books ORDER BY title";
		List<Book> books=new ArrayList<>();
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql);
				ResultSet rs=ps.executeQuery())
						{
					while(rs.next())
					{
						books.add(mapRow(rs));
					}
						}
		return books;
	}
	
	
	
}
