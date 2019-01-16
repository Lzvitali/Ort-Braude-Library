package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Common.Book;
import Common.Copy;
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
		else if (((msg.getMessage()).equals("get borrows and reserves")))
		{
			return getBorrowsAndReserves(msg, connToSQL);
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
	
	
	public static ObjectMessage getBorrowsAndReserves(ObjectMessage msg, Connection connToSQL)
	{
		ObjectMessage answer = new ObjectMessage();
		ReaderAccount reader=(ReaderAccount) msg.getObjectList().get(0);
		
		PreparedStatement ps = null; 
		ResultSet rs = null; 

		try 
		{
			//get the copies that the reader account borrowed 
			ps = connToSQL.prepareStatement("SELECT * FROM Copy WHERE borrowerId = ? ");
			ps.setString(1, reader.getId() ); 
			rs =ps.executeQuery();

			while(rs.next())
			{
				
				int bookID = rs.getInt(2);
				
				
				
				//if exist and not connected
				if(rs.getString(4).equals("0"))
				{
					PreparedStatement updateTime;
					  try 
					  {
						  updateTime = connToSQL.prepareStatement("UPDATE user "+"SET isOnline = ? WHERE ID = ?");
						  updateTime.setString(1, "1");
						  updateTime.setString(2, ((User)msg.getObjectList().get(0)).getId());
						  updateTime.executeUpdate();
					  } 
					  catch (SQLException e) 
					  {
						  e.printStackTrace();
					  }
					  

				}
				else //if exist but already connected
				{
					answer.setMessage("unsuccessful");
				}
			}
			 
			//client.sendToClient(askedStudent);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		
		
		
		
		return answer;
	}

}
