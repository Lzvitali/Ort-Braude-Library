package Server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import Common.Book;
import Common.IEntity;
import Common.ObjectMessage;
import Common.ReaderAccount;


public abstract class  ABookDBController 
{
	/**
	 * This function sorts the request in the 'msg' to the relevant function and returns the answer
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	public static ObjectMessage selection(ObjectMessage msg, Connection connToSQL)
	{
//		
//		if (((msg.getMessage()).equals("addBook")))
//		{
//			return tryToAddBook(msg, connToSQL);
//		}
		
		if (((msg.getMessage()).equals("SearchBook")))
		{
			return searchBook(msg, connToSQL);
		}
		
	return null; // TODO: delete it. did it only to escape the error 
	}
	
	//Natasha (lo lagaat)!!!!!!!!
	private static ObjectMessage tryToAddBook(ObjectMessage msg, Connection connToSQL)
	{
		ObjectMessage massegeRes = new ObjectMessage();
		PreparedStatement checkBook = null; 
		PreparedStatement addCopy=null ;
		PreparedStatement addBook=null ;
		ResultSet rs1 = null; 
		
		
		Book tempBook=(Book)msg.getObjectList().get(0);
		
		
		try 
		{
			//
			checkBook = (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM book WHERE bookName = ? AND authorName=? AND year = ?");
			checkBook.setString(1,tempBook.getBookName()); 
			checkBook.setString(2, tempBook.getAuthorName());
			checkBook.setInt(3, tempBook.getDateOfBook());
			rs1 =checkBook.executeQuery();
			
			//
			
			//add like copy
			if(rs1.next())
			{
				Random rand = new Random();
	    		int randId= rand.nextInt(50) + 1;
				addCopy  = (PreparedStatement) connToSQL.prepareStatement("INSERT INTO copy ('copyId','bookId') VALUES \r\n" +" (?,?); "); 
				addCopy.setInt(1,randId);
				addCopy.setInt(2,tempBook.getBookID());
				addCopy.executeQuery();
				massegeRes.setMessage("This Book is already exist in the system,so successfully add it like copy.");
			}
			
			//add like book
			else 
			{    
				//TODO:add book id
										
				addBook=(PreparedStatement) connToSQL.prepareStatement("INSERT INTO book ('bookId','bookName','authorName','year','topic',''isDesired) VALUES \r\n" +" (?,?,?,?,?,?); ");
				addBook.setInt(1, Book.getNextRow());
				addBook.setString(2, tempBook.getBookName());
				addBook.setString(3, tempBook.getAuthorName());
				addBook.setInt(4, tempBook.getDateOfBook());
				addBook.setString(5, tempBook.getTopic());
				addBook.setBoolean(6, tempBook.isDesired());
				//massegeRes.setMessage("This Book was successfully added.");
				//
				Random rand = new Random();
	    		int randId= rand.nextInt(50) + 1;
				addCopy  = (PreparedStatement) connToSQL.prepareStatement("INSERT INTO copy ('copyId','bookId') VALUES \r\n" +" (?,?); "); 
				addCopy.setInt(1,randId);
				addCopy.setInt(2,tempBook.getBookID());
				addCopy.executeQuery();
				massegeRes.setMessage("This Book was successfully added.");
			}
				
		}
			
			
		catch (SQLException e)
		{
			
			e.printStackTrace();
		}
		return massegeRes;
	}
	
	
	
	private static ObjectMessage searchBook(ObjectMessage msg, Connection connToSQL)
	{
		ObjectMessage answer;
		Book askedBook=(Book)msg.getObjectList().get(0);
		String asked,input;
		ArrayList <IEntity> result=new ArrayList<IEntity>();
		if(askedBook.getAuthorName()!=null)
		{
			asked="authorName";
			input=askedBook.getAuthorName();
		}
		else if(askedBook.getBookName()!=null)
		{
			asked="bookName";
			input=askedBook.getBookName();
		}
		else
		{
			asked="topic";
			input=askedBook.getTopic();
		}
		
		PreparedStatement ps;
		try 
		{
			ps = connToSQL.prepareStatement("SELECT * FROM obl.book WHERE ?=?");
			ps.setString(1,asked);
			ps.setString(2,input);
			ResultSet rs = ps.executeQuery();
	 		while(rs.next())
	 		{
	 			result.add(new Book(rs.getString(2),rs.getString(3),rs.getInt(4),rs.getString(5),rs.getBoolean(6)));
			} 
	 		answer=new ObjectMessage(result,"BooksFound");
		}
		catch (SQLException e) 
		{
			answer=new ObjectMessage("NoBookFound");
		}
		return answer;
	}
	
}
