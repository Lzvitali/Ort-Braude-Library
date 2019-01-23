package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import Common.Book;
import Common.Copy;
import Common.IEntity;
import Common.Mail;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.Reservation;

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
		else if (((msg.getMessage()).equals("implement reservation")))
		{
			return implementReserveBook(msg, connToSQL);
		}
		else if (((msg.getMessage()).equals("cancel reservation")))
		{
			return cancelReservation(msg, connToSQL);
		}
		else if (((msg.getMessage()).equals("letImplementReservation")))
		{
			return letImplementReservation(msg, connToSQL);
		}
		else
		{ 
			return null; 
		}
	}
	
	/**
	 * this method implement reservation it recieve reader account and reservation 
	 * it check if we can to borrow the copy and consider the queue of reservation for each book
	 * the practical borrow progress in the method implementTheBorrow
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	private static ObjectMessage implementReserveBook(ObjectMessage msg, Connection connToSQL) 
	{
		ObjectMessage answer = new ObjectMessage();  
		ReaderAccount reader=(ReaderAccount) msg.getObjectList().get(0);
		Reservation reserve=(Reservation) msg.getObjectList().get(1);

		PreparedStatement isActive = null;
		PreparedStatement numOfCopyAvailable = null;
		PreparedStatement whoIstheFirst=null;
		PreparedStatement numOfCopy=null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		ResultSet temp1 = null;
		int copyAvailable, numOfBookReserve;
		String temp;
		try
		{ 
			///continue from this 
			isActive = connToSQL.prepareStatement("SELECT * FROM readeraccount WHERE ID = ? ");
			isActive.setString(1, reader.getId()); 
			rs1 =isActive.executeQuery();
			rs1.next();
			String status=rs1.getString(8);
			if(!(status.equals("Active")))
			{
				answer.setMessage("ReservationNotImplemented");
				answer.setNote("The reader account is not active");
				return answer;
			}
			else
			{
				numOfCopyAvailable = connToSQL.prepareStatement("SELECT COUNT(*) FROM copy WHERE bookId=? AND borrowerId is null"); 
				numOfCopyAvailable.setInt(1, reserve.getBookID());
				rs2 =numOfCopyAvailable.executeQuery();
				rs2.next();
				copyAvailable=rs2.getInt(1);
				if(copyAvailable==0)
				{
					answer.setMessage("ReservationNotImplemented");
					answer.setNote("No copy available");
					return answer;
				}
				else
				{
					numOfCopyAvailable = connToSQL.prepareStatement("SELECT COUNT(*) FROM reservations WHERE bookId=?"); 
					numOfCopyAvailable.setInt(1, reserve.getBookID());
					rs3 =numOfCopyAvailable.executeQuery();
					rs3.next();
					numOfBookReserve=rs3.getInt(1);
					if(copyAvailable>=numOfBookReserve)
					{
						answer=implementTheBorrow(msg,connToSQL);

					}
					else
					{		
						whoIstheFirst = connToSQL.prepareStatement("SELECT * FROM reservations WHERE bookId=? order by Date"); 
						whoIstheFirst.setInt(1, reserve.getBookID());
						rs4 =whoIstheFirst.executeQuery();
						rs4.next();
						Date date=rs4.getDate(3);
						rs4.beforeFirst();
						while(rs4.next())
						{
							temp=rs4.getString(2);
							if(reader.getId().equals(temp) && (date.equals(rs4.getDate(3))))//the first in the queue
							{
								answer=implementTheBorrow(msg,connToSQL);
								return answer;
							}

						}
							answer.setMessage("ReservationNotImplemented");
							answer.setNote("The reader account is not the first on queue");
							answer.addObject(msg.getObjectList().get(0));
					}
				}
			}
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}	
		return answer;
	}
	private static ObjectMessage implementTheBorrow(ObjectMessage msg, Connection connToSQL) 
{
		ObjectMessage answer = new ObjectMessage();;
		ReaderAccount reader=(ReaderAccount) msg.getObjectList().get(0);
		Reservation reserve=(Reservation) msg.getObjectList().get(1);
		PreparedStatement getBook,numOfCopy;
		ResultSet rs1=null;
		
		try
		{
		getBook = connToSQL.prepareStatement("SELECT * FROM Book WHERE bookId = ? ");
		getBook.setInt(1, reserve.getBookID());
		rs1 = getBook.executeQuery();
		rs1.next();
		numOfCopy = connToSQL.prepareStatement("UPDATE `copy` SET `borrowerId`=?, `borrowDate`=?,`returnDate`=? WHERE bookId=? AND borrowerId is null"); 
		numOfCopy.setString(1, reader.getId());
	
		if(rs1.getBoolean(6))
		{
			LocalDate now = LocalDate.now();
			Date today=java.sql.Date.valueOf(now);
			LocalDate nowPlus3 = LocalDate.now().plusDays(3);
			Date nowPlus3Date = java.sql.Date.valueOf(nowPlus3);
		
			numOfCopy.setDate(2, (java.sql.Date) today);
			numOfCopy.setDate(3, (java.sql.Date) nowPlus3Date);
		}
		else 
		{
			LocalDate now = LocalDate.now();
			Date today=java.sql.Date.valueOf(now);
			LocalDate nowPlus14 = LocalDate.now().plusDays(14);
			Date nowPlus14Date = java.sql.Date.valueOf(nowPlus14);
		
			numOfCopy.setDate(2, (java.sql.Date) today);
			numOfCopy.setDate(3, (java.sql.Date) nowPlus14Date);
		}
	
		numOfCopy.setInt(4, reserve.getBookID());
		numOfCopy.executeUpdate();

		answer.setMessage("ReservationImplemented");
		PreparedStatement deleteReserve = connToSQL.prepareStatement("delete FROM reservations WHERE bookId=? AND readerAccountID=?");
		deleteReserve.setInt(1, reserve.getBookID());
		deleteReserve.setString(2, reader.getId());
		deleteReserve.executeUpdate();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return answer;
	}
	
	/**
	 * This function returns the list of reservation of the reader account
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	private static ObjectMessage getReserves(ObjectMessage msg, Connection connToSQL) 
	{
		ObjectMessage answer = null;  
		ReaderAccount reader=(ReaderAccount) msg.getObjectList().get(0);
		boolean resultExist = false;
		
		PreparedStatement getReserves = null;  
		PreparedStatement getBook = null;
		ResultSet rs1 = null; 
		ResultSet rs2 = null; 

		try 
		{
			//get the copies that the reader account borrowed 
			getReserves = connToSQL.prepareStatement("SELECT * FROM Reservations WHERE readerAccountID = ? ");
			getReserves.setString(1, reader.getId() ); 
			rs1 =getReserves.executeQuery();
			
			ArrayList <IEntity> result=new ArrayList<IEntity>(); 
			
			//go by all the reservations that the reader account reserved and get the book of each one
			while(rs1.next())
			{
				resultExist = true;
				 
				int bookId = rs1.getInt(1); //the bookID of the current copy
				String date = rs1.getString(3);
				
				//get the book of that reserve
				getBook = connToSQL.prepareStatement("SELECT * FROM Book WHERE bookId = ? ");
				getBook.setInt(1, bookId ); 
				rs2 =getBook.executeQuery();
				if(rs2.next())
				{					
					//set the reservation info from the both queries
					Reservation reservation = new Reservation(bookId, date, rs2.getString(2), rs2.getString(3), rs2.getString(4),rs2.getString(5), rs2.getString(6), rs2.getString(7)); 
					result.add(reservation);  
				}
				
			}
			
			if(resultExist)
			{
				answer = new ObjectMessage(result,"TheReserves"," ");
			}
			else
			{
				answer = new ObjectMessage(result,"NoReserves"," ");
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
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
 			ObjectMessage resultOfExistReserve=AReservationDBController.alreadyReserveBook(message,connToSQL);
 			if(resultOfExistReserve.getNote().equals("FoundReserve"))
 			{
				answer=new ObjectMessage("reserveBook","ExistReserve");
				return answer;
 			}
 			else
 			{
 	 			message=new ObjectMessage("checkIfUserGotAlreadyCopy","Copy");
 	 			message.addObject(msg.getObjectList().get(0), msg.getObjectList().get(1));
 	 			ObjectMessage resultIfUserGotAlreadyCopy=ACopyDBController.selection(message,connToSQL);
 	 			if(resultIfUserGotAlreadyCopy.getNote().equals("HaveCopy"))
 	 			{
 					answer=new ObjectMessage("reserveBook","HaveCopy");
 					return answer;
 	 			}
 	 			else
 	 			{
	 				try 
	 				{
	 					Date date = new Date();
	 					Date time= new Date();
	 					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	 					SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
	 					String currentDate = sdf.format(date);
	 					String currentTime = sdf2.format(date);
	 					try 
	 					{
							date=new SimpleDateFormat("yyyy-MM-dd").parse(currentDate);
							time=new SimpleDateFormat("hh:mm:ss").parse(currentTime);
						} 
	 					catch (ParseException e) 
	 					{
	
							e.printStackTrace();
						}
	 					java.sql.Date sqlDate = new java.sql.Date(date.getTime()); 
	 					java.sql.Time sqlTime = new java.sql.Time(time.getTime()); 
						ps = connToSQL.prepareStatement("INSERT INTO `Reservations` (`bookId`,`readerAccountID`, `Date`,`Time`) VALUES (?,?,?,?)");
						ps.setInt(1,askedBook.getBookID());
						ps.setString(2,askedReaderAccount.getId());
						ps.setDate(3, sqlDate);
						ps.setTime(4, sqlTime);
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

	private static ObjectMessage cancelReservation(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		ReaderAccount askedReaderAccount=(ReaderAccount)msg.getObjectList().get(0);
		Reservation reservation=(Reservation)msg.getObjectList().get(1);
		try 
		{
			ps = connToSQL.prepareStatement("DELETE FROM `Reservations` WHERE bookId= ? AND readerAccountID= ?");
			ps.setInt(1,reservation.getBookID());
			ps.setString(2,askedReaderAccount.getId());
			ps.executeUpdate();
			Book askedbook=new Book();
			askedbook.setBookID(reservation.getBookID());
			ObjectMessage askTheFirstReader=new ObjectMessage();
			askTheFirstReader.addObject(askedbook);
			ObjectMessage result=getReaderThatCanImplement(askTheFirstReader,connToSQL);
			askTheFirstReader=new ObjectMessage(result.getObjectList().get(0),"SearchReader","ReaderAccount");
			ReaderAccount readerAccount =(ReaderAccount) (AReaderAccountDBController.selection(askTheFirstReader, connToSQL)).getObjectList().get(0);
			ObjectMessage bookDetails=new ObjectMessage(askedbook,"searchBookID","Book");
			Book book = (Book) (ABookDBController.selection(bookDetails, connToSQL)).getObjectList().get(0);
			ObjectMessage implementReservation=new ObjectMessage();
			implementReservation.addObject(readerAccount, book);
			letImplementReservation(implementReservation,connToSQL);
			return new ObjectMessage("ReservationCanceled","cancelReservation");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}

	}

	private static ObjectMessage letImplementReservation(ObjectMessage msg, Connection connToSQL)
	{
		
		PreparedStatement ps;
		ObjectMessage answer;
		ReaderAccount askedReaderAccount=(ReaderAccount)msg.getObjectList().get(0);
		Book book=(Book)msg.getObjectList().get(1);
		try 
		{
			ps = connToSQL.prepareStatement("UPDATE `reservations` SET `startTimerImplement`=NOW() WHERE bookId= ? AND readerAccountID= ? ");
			ps.setInt(1,book.getBookID());
			ps.setString(2,askedReaderAccount.getId());
			ps.executeUpdate();
			ObjectMessage notify=new ObjectMessage("sendMail","Daily");
			Mail mail=new Mail();
			mail.setTo(askedReaderAccount.getEmail());
			String body="Hello "+askedReaderAccount.getFirstName()+"\nWe glad to notfiy you that you can come to library"
					+ " and implement your reservation for "+book.getBookName()
					+ ".\nTake care , you got only 48 to implement reservation since the time of this mail"
					+"\n 		Thank you , Ort Braude Library";
			mail.setBody(body);
			String subject="Implement your reservation for "+book.getBookName();
			mail.setSubject(subject);
			notify.addObject(mail);
			ADailyDBController.selection(notify, connToSQL);
			
		} 
		catch (SQLException e) 
		{
			
			e.printStackTrace();
			return null;
		} 
		
		return new ObjectMessage("letImplementReservation","SentMail");
	}

	private static ObjectMessage getReaderThatCanImplement(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		Book book=(Book)msg.getObjectList().get(0); 
		ResultSet rs = null; 

			//get the copies that the reader account borrowed 
			try 
			{
				ps = connToSQL.prepareStatement("SELECT COUNT(*) FROM reservations WHERE bookId=?");
				ps.setInt(1, book.getBookID()); 
				rs =ps.executeQuery();
				rs.next();
				if(rs.getInt(1)==0)
				{
					return new ObjectMessage("ReaderThatCanImplement","NoFound");
				}
				else
				{
					ps = connToSQL.prepareStatement("SELECT * FROM Reservations WHERE bookId = ? ORDER BY `Date`,`Time` ");
					ps.setInt(1, book.getBookID()); 
					rs =ps.executeQuery();
					rs.next();
					ReaderAccount readerAccount=new ReaderAccount();
					readerAccount.setId(rs.getString(2));
					return new ObjectMessage(readerAccount,"ReaderThatCanImplement","Found");
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				return new ObjectMessage("ReaderThatCanImplement","NoFound");
			}
	}
	
}
