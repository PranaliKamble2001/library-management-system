package com.library.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection 
{
   private static Connection connection;
   
   private DBConnection()
   {
	   
   }
   
   public static Connection getConnection() throws SQLException
   {
	   if(connection == null || connection.isClosed())
	   {
		   Properties props = loadProperties();
		   String url = props.getProperty("db.url","jdbc:mysql://localhost:3306/library_management");
		   String user = props.getProperty("db.user","root");
		   String password = props.getProperty("db.password", "");
		   
		   try {
			   Class.forName("com.mysql.cj.jdbc.Driver");
		   } catch(ClassNotFoundException e) {
			   throw new SQLException("MySQL JDBC Driver not found on classpath.",e);
		   }
		   connection = DriverManager.getConnection(url, user, password);
	   }
	   return connection;
   }
   
   private static Properties loadProperties()
   {
	   Properties props =new Properties();
	   try(InputStream input=DBConnection.class.getClassLoader()
			   .getResourceAsStream("db.properties")){
		       if(input!=null)
		       {
		    	   props.load(input);
		       }
		       
	   }catch(IOException e) {
		   System.err.println("Warning: could not load db.properties, using default." + e.getMessage());
		   
	   }
	   return props;
   }
   
   public static void closeConnection()
   {
	   try {
		   if(connection !=null && !connection.isClosed())
		   {
			   connection.close();
		   }
			   
	   }catch (SQLException e) {
		   System.err.println("Error closing connection." + e.getMessage());
	   }
   }
}
