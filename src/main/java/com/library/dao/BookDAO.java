package com.library.dao;

import java.sql.SQLException;
import java.util.List;

import com.library.model.Book;

public interface BookDAO 
{
   Book addBook(Book book) throws SQLException;
   
   boolean updateBook(Book book) throws SQLException;
   
   boolean deleteBook(int bookId) throws SQLException;
   
   Book getBookById(int bookId) throws SQLException;
   
   List<Book> getAllBooks() throws SQLException;
   
   List<Book> searchBooks(String keyword) throws SQLException;
   
   boolean updateAvailableCopies(int bookId, int delta) throws SQLException;
}
