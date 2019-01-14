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
import clientCommonBounderies.AClientCommonUtilities;


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

		if (((msg.getMessage()).equals("addBook")))
		{
			return tryToAddBook(msg, connToSQL);
		}

		if (((msg.getMessage()).equals("SearchBook")))
		{
			return searchBook(msg, connToSQL);
		}

		return null; // TODO: delete it. did it only to escape the error 
	}

	//Natasha (lo lagaat)!!!!!!!!
	private static ObjectMessage tryToAddBook(ObjectMessage msg, Connection connToSQL)
	{
		ObjectMessage massegeRes;
		PreparedStatement checkBook = null; 
		PreparedStatement addCopy=null ;
		PreparedStatement addBook=null ;
		ResultSet rs1 = null; 


		Book tempBook=(Book)msg.getObjectList().get(0);

		try 
		{
			String query= "SELECT * FROM book WHERE bookName = ? AND authorName = ? AND year = ? AND edition = ? ";
			checkBook = connToSQL.prepareStatement(query);
			checkBook.setString(1,tempBook.getBookName()); 
			checkBook.setString(2, tempBook.getAuthorName());
			checkBook.setInt(3, tempBook.getDateOfBook());
			checkBook.setInt(4,tempBook.getEdition());
			rs1 =checkBook.executeQuery();

			//add like copy
			if(rs1.next())
			{
				if(Boolean.parseBoolean(rs1.getString(6))==(tempBook.isDesired()))//check if same desire
				{
					if (rs1.getString(5).equals(tempBook.getTopic()))//if it is same book 
					{
						for(int i=0;i<tempBook.getNumberOfCopies();i++)//add copies for book
						{
							addCopy  = connToSQL.prepareStatement(" INSERT INTO `Copy` (`bookId`) VALUES (?)"); 
							addCopy.setInt(1, Integer.parseInt(rs1.getString(1)));
							addCopy.executeUpdate();
						}

						return new ObjectMessage("This Book is already exist in the system,so successfully added it like copy.","Successfull");
					}
					else //if there is different topics
					{
						System.out.println("SameBook but different topics");
						//if there is same book but different topics
						return new ObjectMessage("Error!Please change topic. There is also book with the same name,author ,year and edition.","Wrong");
					}

				}
				else  //different desire
				{
					System.out.println("SameBook and different desire");
					//if there is same book but different desired
					return new ObjectMessage("Error!Please change desired choise. There is also book with the same name,author ,year and edition.","Wrong");

				}
			}

			//add like book
			else 
			{   
				//add new book in table
				addBook=connToSQL.prepareStatement("INSERT INTO `Book` (`bookName`,`authorName`,`year`,`topic`,`isDesired`,`edition`) VALUES (?,?,?,?,?,?)");
				addBook.setString(1, (String)tempBook.getBookName());
				addBook.setString(2, (String)tempBook.getAuthorName());
				addBook.setInt(3, (int)tempBook.getDateOfBook());
				addBook.setString(4, "somethingTemp");//tempBook.getTopic()
				addBook.setBoolean(5,(Boolean) tempBook.isDesired());//(Boolean) tempBook.isDesired()
				addBook.setInt(6, (int)tempBook.getEdition());
				addBook.executeUpdate();

				//add copy of this book
				String queryForNewBook= "SELECT * FROM book WHERE bookName = ? AND authorName = ? AND year = ? AND edition = ?";
				checkBook = connToSQL.prepareStatement(queryForNewBook);
				checkBook.setString(1,tempBook.getBookName()); 
				checkBook.setString(2, tempBook.getAuthorName());
				checkBook.setInt(3, tempBook.getDateOfBook());
				checkBook.setInt(4,tempBook.getEdition());
				rs1 =checkBook.executeQuery();

				if(rs1.next())
				{
					for(int i=0;i<tempBook.getNumberOfCopies();i++)//add copies for book
					{
						addCopy  = connToSQL.prepareStatement(" INSERT INTO `Copy` (`bookId`) VALUES (?)"); 
						addCopy.setInt(1, Integer.parseInt(rs1.getString(1)));
						addCopy.executeUpdate();
					}

				}
				return new ObjectMessage("This Book was successfully added like book and like copy.","Successfull");
				
			}	
		}	
		catch (SQLException e)
		{
			e.printStackTrace();
			return new ObjectMessage("Unexpected Error.","Unsucessfull");
			
		}
		
	}


	
	private static ObjectMessage searchBook(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		ObjectMessage answer;
		Book askedBook=(Book)msg.getObjectList().get(0);
		String input;
		ArrayList <IEntity> result=new ArrayList<IEntity>();
		try 
		{
			if(askedBook.getAuthorName()!=null)
			{
				ps = connToSQL.prepareStatement("SELECT * FROM obl.book WHERE authorName=?");
				input=askedBook.getAuthorName();
			}
			else if(askedBook.getBookName()!=null)
			{
				ps = connToSQL.prepareStatement("SELECT * FROM obl.book WHERE bookName=?");
				input=askedBook.getBookName();
			}
			else
			{
				ps = connToSQL.prepareStatement("SELECT * FROM obl.book WHERE topic=?");
				input=askedBook.getTopic();
			}
			ps.setString(1,input);
			ResultSet rs = ps.executeQuery();
	 		while(rs.next())
	 		{
	 			result.add(new Book(rs.getString(2),rs.getString(3),rs.getInt(4),rs.getString(5),rs.getBoolean(6),rs.getInt(7)));
			} 
	 		if(!result.isEmpty())
	 		{
	 			answer=new ObjectMessage(result,"BookSearch","BooksFound");
	 		}
	 		else
	 		{
	 			answer=new ObjectMessage("BookSearch","NoBookFound");
	 		}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			answer=new ObjectMessage("BookSearch","NoBookFound");
		}
		return answer;
	}
	
}
