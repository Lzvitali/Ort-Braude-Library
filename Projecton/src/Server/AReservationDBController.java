package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Common.Book;
import Common.Copy;
import Common.ObjectMessage;
import Common.ReaderAccount;

public abstract class AReservationDBController 
{
	
	/**
	 * This function sorts the request in the 'msg' to the relevant function and returns the answer
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	public static ObjectMessage selection(ObjectMessage msg, Connection connToSQL)
	{

		if (((msg.getMessage()).equals("get reserves")))
		{
			return getReserves(msg, connToSQL);
		}
		else if (((msg.getMessage()).equals("reserveBook")))
		{
			return reserveBook(msg, connToSQL);
		}
		else
		{
			return null; 
		}
	}

	private static ObjectMessage getReserves(ObjectMessage msg, Connection connToSQL) 
	{
		// TODO Auto-generated method stub
		return null;
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
 			ObjectMessage resultOfExistReserve=AReservationDBController.alreadyReserveBook(message,connToSQL);
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
			ps = connToSQL.prepareStatement("SELECT COUNT(*) FROM obl.Reservations WHERE bookId=? AND readerAcID=?");
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
