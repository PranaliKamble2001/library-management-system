package com.library.model;

public class Book 
{
    private int bookId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private int totalCopies;
    private int availableCopies;
    
    public Book()
    {
    	
    }
    
    public Book(String isbn, String title, String author,String publisher, String category, int totalCopies, int availableCopies)
    {
    	this.isbn=isbn;
    	this.title=title;
    	this.author=author;
    	this.publisher=publisher;
    	this.category=category;
    	this.totalCopies=totalCopies;
    	this.availableCopies=availableCopies;	
    	
    }
    
    public Book(int bookId, String isbn, String title, String author,String publisher, String category, int totalCopies, int availableCopies)
    {
    	this(isbn,title,author,publisher,category,totalCopies,availableCopies);
    	this.bookId=bookId;
    }
    
    public void setIsbn(String isbn)
    {
    	this.isbn=isbn;
    }
    
    public String getIsbn()
    {
    	return isbn;
    }
    
    
    
    public void setBookId(int bookId)
    {
    	this.bookId=bookId;
    }
    
    public int getBookId()
    {
    	return bookId;
    }
    
    
    public void setTitle(String title)
    {
    	this.title=title;
    }
    
    public String getTitle()
    {
    	return title;
    }
    
    public void setAuthor(String author)
    {
    	this.author=author;
    }
    public String getAuthor()
    {
    	return author;
    }
    
    
    public void setPublisher(String publisher)
    {
    	this.publisher=publisher;
    }
    public String getPublisher()
    {
    	return publisher;
    }
    
    
    public void setCategory(String category)
    {
    	this.category=category;
    }
    public String getCategory()
    {
    	return category;
    }
    
    
    public void setTotalCopies(int totalCopies)
    {
    	this.totalCopies=totalCopies;
    	
    }
    public int getTotalCopies()
    {
    	return totalCopies;
    }
    
    public void setAvailableCopies(int availableCopies)
    {
    	this.availableCopies=availableCopies;
    }
    
    public int getAvailableCopies()
    {
    	return availableCopies;
    }
    
    public boolean isAvailable()
    {
    	return availableCopies >0;
    }
    
    
    
    public String toString()
    {
    	return String.format("[%d] %-30s | %-20s | ISBN:%-15s | Available: %d/%d",
                bookId, title, author, isbn, availableCopies, totalCopies);
    }
    
    
    
     
    
}
