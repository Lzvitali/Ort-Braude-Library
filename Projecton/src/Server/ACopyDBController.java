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
			
			while(rs1.next())
			{
				resultExist = true;
				
				IEntity []CopyAndBook = new IEntity[2];
				
				CopyAndBook[0]=(new Copy(rs1.getInt(1), rs1.getInt(2), rs1.getString(3), rs1.getDate(4), rs1.getDate(5)));
				
				System.out.println( ((Copy)CopyAndBook[0]).getBorrowDate() + " ---" + ((Copy)CopyAndBook[0]).getReturnDate());
				
				int bookId = rs1.getInt(2);
				ArrayList <Book> books=new ArrayList<Book>();
				
				//get the book of that copy
				getBook = connToSQL.prepareStatement("SELECT * FROM Book WHERE bookId = ? ");
				getBook.setInt(1, bookId ); 
				rs2 =getBook.executeQuery();
				if(rs2.next())
				{
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
			
			//TODO: deal also with "no-results"
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		

		return answer;
	}

}
