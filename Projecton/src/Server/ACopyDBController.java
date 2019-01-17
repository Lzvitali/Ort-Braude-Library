package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Common.Book;
import Common.Copy;
import Common.IEntity;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.User;

public abstract class ACopyDBController 
{
	
	/**
	 * This function sorts the request in the 'msg' to the relevant function and returns the answer
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	public static ObjectMessage selection(ObjectMessage msg, Connection connToSQL)
	{

		if (((msg.getMessage()).equals("checkIfAllBorrowed")))
		{
			return checkIfAllBorrowed(msg, connToSQL);
		}
		else if (((msg.getMessage()).equals("get borrows")))
		{
			return getBorrows(msg, connToSQL);
		}
		else if (((msg.getMessage()).equals("DeleteBook")))
		{
			return deleteBook(msg, connToSQL);
		}
		else
		{
			return null; 
		}
	}
	
	
	public static ObjectMessage checkIfAllBorrowed(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		ObjectMessage answer;
		Copy askedBook=(Copy)msg.getObjectList().get(0);
		try 
		{
			ps = connToSQL.prepareStatement("SELECT COUNT(*) FROM obl.copy WHERE bookId=? AND borrowerId IS NULL");
			ps.setInt(1,askedBook.getBookID());
			ResultSet rs = ps.executeQuery();
			rs.next();
			int x =rs.getInt(1);
			if(rs.getInt(1)!= 0)
			{
				return new ObjectMessage("checkIfAllBorrowed","FoundBook");
			}
			else
			{
				return new ObjectMessage("checkIfAllBorrowed","NoFoundBook");
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * This function will return all the borrows of specific reader account
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	public static ObjectMessage getBorrows(ObjectMessage msg, Connection connToSQL)
	{
		ObjectMessage answer = null; 
		ReaderAccount reader=(ReaderAccount) msg.getObjectList().get(0);
		boolean resultExist = false;
		
		PreparedStatement getCopies = null; 
		PreparedStatement getBook = null;
		ResultSet rs1 = null; 
		ResultSet rs2 = null; 

		try 
		{
			//get the copies that the reader account borrowed 
			getCopies = connToSQL.prepareStatement("SELECT * FROM Copy WHERE borrowerId = ? ");
			getCopies.setString(1, reader.getId() ); 
			rs1 =getCopies.executeQuery();
			
			ArrayList <IEntity[]> result=new ArrayList<IEntity[]>(); 
			
			//go by all the copies the the reader account borrowing and get the boot of each one
			while(rs1.next())
			{
				resultExist = true;
				
				IEntity []CopyAndBook = new IEntity[2]; //CopyAndBook[0]->the Copy info , CopyAndBook[1]->the Book info
				
				//set the copy info from the first query
				CopyAndBook[0]=(new Copy(rs1.getInt(1), rs1.getInt(2), rs1.getString(3), rs1.getString(4), rs1.getString(5)));
				
				int bookId = rs1.getInt(2); //the bookID of the current copy
				
				//get the book of that copy
				getBook = connToSQL.prepareStatement("SELECT * FROM Book WHERE bookId = ? ");
				getBook.setInt(1, bookId ); 
				rs2 =getBook.executeQuery();
				if(rs2.next())
				{
					//set the book info from the second query
					CopyAndBook[1]=( new Book(rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getString(6), rs2.getInt(7)) );
				}
				
				result.add(CopyAndBook);
			}
			
			if(resultExist)
			{
				answer = new ObjectMessage(result,"TheBorrows");
			}
			else
			{
				answer = new ObjectMessage(result,"NoBorrows");
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		

		return answer;
	}
	
	
	private static ObjectMessage deleteBook(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		PreparedStatement getBook = null;
		ObjectMessage answer;
		ResultSet rs2 = null; 
		Copy askedCopy=(Copy)msg.getObjectList().get(0);
		
		
		try 
		{
			//get id of the book of the copy
			getBook = connToSQL.prepareStatement("SELECT * FROM obl.copy WHERE copyId = ? ");
			getBook.setInt(1, askedCopy.getCopyID()); 
			rs2 =getBook.executeQuery();
			rs2.next();
			int bookOfCopyID=rs2.getInt(2);
			
			ps = connToSQL.prepareStatement("DELETE copy FROM obl.copy WHERE copyId=?");
			ps.setInt(1,askedCopy.getCopyID());
			ps.executeUpdate();
			int numOfCopiesBefore=bookOfCopyID;
			numOfCopiesBefore--;
			int numOfCopiesAfter=numOfCopiesBefore; //after deleting a copy of the book
			
			//there is no copies from the book
			if(numOfCopiesAfter==0)
			{
				try 
				{
					ps = connToSQL.prepareStatement("DELETE book FROM obl.book WHERE bookId=?");
					ps.setInt(1,bookOfCopyID);
					ps.executeUpdate();
				}
				catch (SQLException e) 
				{
					e.printStackTrace();
					answer= new ObjectMessage("Unexpected Error.","Unsucessfull");
				}
			}
			answer=new ObjectMessage("This Book was successfully deleted ","Successfull");
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			answer=new ObjectMessage("Unexpected Error.","Unsucessfull");
		}
		return answer;
	}

}
