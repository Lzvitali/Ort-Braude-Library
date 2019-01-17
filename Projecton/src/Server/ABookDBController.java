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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Common.Book;
import Common.Copy;
import Common.IEntity;
import Common.ObjectMessage;
import Common.ReaderAccount;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.LogInController;


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

		else if (((msg.getMessage()).equals("SearchBook")))
		{
			return searchBook(msg, connToSQL);
		}
		else if (((msg.getMessage()).equals("reserveBook")))
		{
			return reserveBook(msg, connToSQL);
		}

		else if(((msg.getMessage()).equals("ReturnCopy")))
		{
			return tryToReturnBook(msg, connToSQL);
		}
		else
			return null; 
	}

	private static ObjectMessage tryToReturnBook(ObjectMessage msg, Connection connToSQL) 
	{

		ObjectMessage answer=new ObjectMessage();
		PreparedStatement checkCopy=null;
		PreparedStatement updateCopy=null;
		PreparedStatement checkReaderAccountDelays=null;
		PreparedStatement updateReaderAccount=null;
		
		ResultSet query1 = null;
		ResultSet query2 = null;
		ResultSet query3=null;
		int numOfDelay;

		Copy tempCopy=(Copy)msg.getObjectList().get(0);
		try
		{

			checkCopy= (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM copy WHERE copyId = ? AND borrowerId IS NOT NULL");
			checkCopy.setInt(1,tempCopy.getCopyID());
			query1 =checkCopy.executeQuery();
			Boolean temp=query1.next();
			if(temp==false)
			{
				answer.setNote("The copyID is wrong or the copyID does not borrowed");
			}
			else
			{
				checkCopy=(PreparedStatement) connToSQL.prepareStatement("SELECT * FROM copy WHERE copyId = ?");
				checkCopy.setInt(1,tempCopy.getCopyID());
				query2=checkCopy.executeQuery();
				
				checkReaderAccountDelays=(PreparedStatement) connToSQL.prepareStatement("SELECT * FROM readeraccount WHERE ID = ?");
				checkReaderAccountDelays.setString(1,query2.getString(3));
				query3=checkReaderAccountDelays.executeQuery();
				
				if(query3.getInt(0)<3 && query3.getString(1).equals("Frozen"))
				{
					String active="Active";
					updateReaderAccount = connToSQL.prepareStatement("UPDATE `readeraccount` SET `status`=? WHERE ID=?");
					updateReaderAccount.setString(1,active);
					updateReaderAccount.setString(2,query2.getString(1));
				}
				updateCopy = connToSQL.prepareStatement("UPDATE `copy` SET `borrowerId`=NULL WHERE copyId=?");
				updateCopy.setInt(1,tempCopy.getCopyID());
				updateCopy.executeUpdate();
				answer.setNote("successful ReturnCopy");
				answer.addObject(msg.getObjectList().get(0));
			}

		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return new ObjectMessage("Unexpected Error.","Unsucessfull");		
		}	 
		return answer;
	}
	
	/**
	 * This function add book to the DB in MySQL. It is check if it is must be added like new book or copy for book that already exist
	 * @param msg- the object from the client
	 * @param connToSQL -the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client if book was added successfully or not or it has wrong .
	 */
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
				if(tempBook.isFileIsLoaded())
				{
					return new ObjectMessage("Error! You can't add file to existing book","Wrong");
				}
				else
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
	 			Book book=new Book(rs.getString(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getString(5),rs.getBoolean(6),rs.getInt(7));
	 			Copy copy=new Copy(-1,rs.getInt(1),null);
	 			ObjectMessage message=new ObjectMessage(copy,"checkIfAllBorrowed","Copy");
	 			ObjectMessage resultofCopy=ACopyDBController.selection(message,connToSQL);
	 			if(resultofCopy.getNote().equals("FoundBook"))
	 			{
	 				book.setNumberOfCopies(1);
	 			}
	 			else
	 			{
	 				book.setNumberOfCopies(0);
	 			}
	 			result.add(book);
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
	
	private static ObjectMessage reserveBook(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		ObjectMessage answer;
		Book askedBook=(Book)msg.getObjectList().get(0);
		ReaderAccount askedReaderAccount=(ReaderAccount)msg.getObjectList().get(1);
		Copy copy=new Copy(-1,askedBook.getBookID(),null);
		ObjectMessage message=new ObjectMessage(copy,"checkIfAllBorrowed","Copy");
		ObjectMessage resultOfCopy=ACopyDBController.selection(message,connToSQL);
		if(resultOfCopy.getNote().equals("FoundBook"))
		{
				answer=new ObjectMessage("reserveBook","HaveAvailableCopy");
				return answer;
		}
 		else
 		{
 			message=new ObjectMessage();
 			message.addObject(msg.getObjectList().get(0), msg.getObjectList().get(1));
 			ObjectMessage resultOfExistReserve=ABookDBController.alreadyReserveBook(message,connToSQL);
 			if(resultOfExistReserve.getNote().equals("FoundReserve"))
 			{
				answer=new ObjectMessage("reserveBook","ExistReserve");
				return answer;
 			}
 			else
 			{
 				try 
 				{
 					Date date = new Date();
 					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
 					String currentTime = sdf.format(date);
 					try 
 					{
						date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(currentTime);
					} 
 					catch (ParseException e) 
 					{

						e.printStackTrace();
					}
 					java.sql.Date sqlDate = new java.sql.Date(date.getTime()); 
					ps = connToSQL.prepareStatement("INSERT INTO `Reservations` (`bookId`,`readerAccountID`,`Date`) VALUES (?,?,?)");
					ps.setInt(1,askedBook.getBookID());
					ps.setString(2,askedReaderAccount.getId());
					ps.setDate(3, sqlDate);
					ps.executeUpdate();
					answer=new ObjectMessage("reserveBook","Reserved");
					return answer;
				} 
 				catch (SQLException e) 
 				{
					e.printStackTrace();
					return null;
				}
 			}	
 		}
		
	}
	
	private static ObjectMessage alreadyReserveBook(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		ObjectMessage answer;
		Book askedBook=(Book)msg.getObjectList().get(0);
		ReaderAccount askedReaderAccount=(ReaderAccount)msg.getObjectList().get(1);
		try 
		{
			ps = connToSQL.prepareStatement("SELECT COUNT(*) FROM obl.Reservations WHERE bookId=? AND readerAccountID=?");
			ps.setInt(1,askedBook.getBookID());
			ps.setString(2,askedReaderAccount.getId());
			ResultSet rs = ps.executeQuery();
			rs.next();
			int x =rs.getInt(1);
			if(rs.getInt(1)!= 0)
			{
				return new ObjectMessage("alreadyReserveBook","FoundReserve");
			}
			else
			{
				return new ObjectMessage("alreadyReserveBook","NoFoundReserve");
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
	
}
