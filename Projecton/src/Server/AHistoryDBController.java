package Server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import Common.Book;
import Common.Copy;
import Common.History;
import Common.IEntity;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.Report;

public abstract class AHistoryDBController
{


	public static ObjectMessage selection(ObjectMessage msg, Connection connToSQL)
	{	
		if (((msg.getMessage()).equals("Ask for report2")))
		{
			return getReportTwo(msg, connToSQL);
		}
		else if(((msg.getMessage()).equals("Ask for report3")))
		{
			return getReportThree(msg, connToSQL);
		}
		else if(((msg.getMessage()).equals("get reader account History")))
		{
			return getReaderAccountHistory(msg, connToSQL);
		}
		else
			return null;	

	}

	private static ObjectMessage getReaderAccountHistory(ObjectMessage msg, Connection connToSQL) 
	{
		
		
		return new ObjectMessage("");
	}

	/**
	 * This function receives book number and returns it's info that need for Report3
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	private static ObjectMessage getReportThree(ObjectMessage msg, Connection connToSQL) 
	{
		ObjectMessage answer = new ObjectMessage(); 
		int bookID = Integer.parseInt(msg.getExtra());
		
		boolean bookIsExist = false; // if book with given id is exist
		int cnt = 0; //counter for total number of late returns
		long totalDurationofLates = 0;
		float average;
		float median;
		ArrayList<Long> durationOfTheLate=new ArrayList<Long>();
		
		PreparedStatement booksWithLates = null; 
		PreparedStatement getReturnDate = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		
		try 
		{
			booksWithLates = connToSQL.prepareStatement("SELECT * FROM history WHERE bookId = ? AND action = ? ");
			booksWithLates.setInt(1, bookID); 
			booksWithLates.setString(2, "Late in Return");  
			rs1 =booksWithLates.executeQuery();
			
			while(rs1.next())
			{
				bookIsExist = true; //book exist 
				cnt++;
				
				int readerAccountID = rs1.getInt(2);
				int copyID = rs1.getInt(4);
				Date startOfLate = rs1.getDate(6);
				Date returnDate = null;
				
				//find the return date for this book
				getReturnDate = connToSQL.prepareStatement("select * from history where `date` > ? and `copyid` = ? and `readerAccountID` = ? and `action` = ? order by `date` ");
				getReturnDate.setDate(1, startOfLate);
				getReturnDate.setInt(2, copyID); 
				getReturnDate.setInt(3, readerAccountID); 
				getReturnDate.setString(4,"Return book");  
				rs2 =getReturnDate.executeQuery();
				
				//get the first result
				if(rs2.next())
				{
					returnDate = rs2.getDate(6);
				}
				
				//calculate the duration of the borrow
				long diff = Math.abs(startOfLate.getTime() -returnDate.getTime()); 
				Long dif2 = new Long( TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));	
				
				totalDurationofLates += dif2;
				durationOfTheLate.add(dif2);

			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();

		}

		if(bookIsExist)
		{
			//get the median
			Collections.sort(durationOfTheLate);
			if (durationOfTheLate.size()%2 == 1)
			{
				median = (float)(durationOfTheLate.get(durationOfTheLate.size() / 2));

			}
			else 
			{
				median =(float)((durationOfTheLate.get(durationOfTheLate.size()/2) +durationOfTheLate.get(durationOfTheLate.size()/2 - 1))/2);
			}
			  
			
			average = (float)totalDurationofLates/(float)cnt;
			
			Report report = new Report();
			report.setAverage(average);
			report.setMedian(median);
			report.setTotal(cnt); 
			
			answer.addObject(report);
			answer.setNote("Report number 3"); 
		}
		else
		{
			answer.setNote("Report number 3 - no results"); 
		}
		
		 
		
		return answer;
	}

	/**
	 * The function enter users actions to history table in the obl DB
	 * @param sendObject- the object from controller where happened action
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 */
	public static void enterActionToHistory( History sendObject, Connection connToSQL) 
	{
		if(sendObject.getAction().equals("Borrow book")||sendObject.getAction().equals("Delayed borrow book")||sendObject.getAction().equals("Lose book"))
		{

			try 
			{
				PreparedStatement updateHistory = connToSQL.prepareStatement("INSERT INTO `history` (`readerAccountID`,`bookId`,`copyId`,`action`,`date`) VALUES (?,?,?,?,?); "); 
				updateHistory.setString(1,sendObject.getUserID()); 
				updateHistory.setInt(2,sendObject.getBookId()); 
				updateHistory.setInt(3,sendObject.getCopyId());
				updateHistory.setString(4,sendObject.getAction()); 
				updateHistory.setDate(5,(Date) sendObject.getActionDate()); 
				updateHistory.executeUpdate();
			} 
			catch (SQLException e) 
			{

				e.printStackTrace();
			} 

		}
		else if(sendObject.getAction().equals("Return book"))
		{
			try 
			{

				PreparedStatement updateHistory = connToSQL.prepareStatement("INSERT INTO `history` (`readerAccountID`,`bookId`,`copyId`,`action`,`date`,`Note`) VALUES (?,?,?,?,?,?); "); 
				updateHistory.setString(1,sendObject.getUserID()); 
				updateHistory.setInt(2,sendObject.getBookId()); 
				updateHistory.setInt(3,sendObject.getCopyId());
				updateHistory.setString(4,sendObject.getAction()); 
				updateHistory.setDate(5,(Date) sendObject.getActionDate()); 
				updateHistory.setString(6,sendObject.getNote());
				updateHistory.executeUpdate();
			} 
			catch (SQLException e) 
			{

				e.printStackTrace();
			} 
		}


		else if(sendObject.getAction().equals("Cancel reservation book")||sendObject.getAction().equals("Reserve book"))
		{
			try 
			{

				PreparedStatement updateHistory = connToSQL.prepareStatement("INSERT INTO `history` (`readerAccountID`,`bookId`,`action`,`date`) VALUES (?,?,?,?); "); 
				updateHistory.setString(1,sendObject.getUserID()); 
				updateHistory.setInt(2,sendObject.getBookId()); 
				updateHistory.setString(3,sendObject.getAction()); 
				updateHistory.setDate(4,(Date) sendObject.getActionDate()); 
				updateHistory.executeUpdate();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			} 
		}
		
		else if(sendObject.getAction().equals("Registration to OBL"))
		{
			try 
			{

				PreparedStatement updateHistory = connToSQL.prepareStatement("INSERT INTO `history` (`readerAccountID`,`action`,`date`) VALUES (?,?,?); "); 
				updateHistory.setString(1,sendObject.getUserID()); 
				updateHistory.setString(2,sendObject.getAction()); 
				updateHistory.setDate(3,(Date) sendObject.getActionDate()); 
				updateHistory.executeUpdate();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			} 
		}

		else if(sendObject.getAction().equals("Change status"))
		{
			try 
			{

				PreparedStatement updateHistory = connToSQL.prepareStatement("INSERT INTO `history` (`readerAccountID`,`action`,`date`,`Note`) VALUES (?,?,?,?); "); 
				updateHistory.setString(1,sendObject.getUserID()); 
				updateHistory.setString(2,sendObject.getAction()); 
				updateHistory.setDate(3,(Date) sendObject.getActionDate()); 
				updateHistory.setString(4,sendObject.getNote()); 
				updateHistory.executeUpdate();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			} 
		}

	}


	private static ObjectMessage getReportTwo( ObjectMessage msg, Connection connToSQL) 
	{
		ArrayList<IEntity> result=new ArrayList<IEntity>();
		ArrayList<Integer> numberOfDaysOdBorrowingArrReg=new ArrayList<Integer>();
		ArrayList<Integer> numberOfDaysOdBorrowingArrDes=new ArrayList<Integer>();
		ArrayList<Integer> numberOfDaysOdBorrowingArrAll=new ArrayList<Integer>();
		int countOfDesired=0;
		int countOfRegular=0;
		PreparedStatement getAverageAllBooks;
		PreparedStatement getAverageDesired;
		PreparedStatement getReturnedBooks;
		PreparedStatement getAverageRegular;
		int sumOfDesired=0;
		int sumOfRegular=0;
		ResultSet rs1 = null; 
		ResultSet rs2 = null;
		int isDesired;
		int idOfBook;
		float average;
		float median;
		
		int daysOfBorrows;
		ObjectMessage answer = null; 
		try 
		{
			//average of Regular books
			getAverageDesired = connToSQL.prepareStatement("SELECT* FROM obl.history WHERE action=?" );
			getAverageDesired.setString(1,"return Book"); 
			rs1 =getAverageDesired.executeQuery();

			while(rs1.next())
			{
				daysOfBorrows=Integer.parseInt(rs1.getString(7));
				idOfBook=rs1.getInt(3);
				getAverageRegular = connToSQL.prepareStatement("SELECT* FROM obl.book WHERE bookId=?" );
				getAverageRegular.setInt(1,idOfBook); 
				rs2 =getAverageRegular.executeQuery();
				rs2.next();
				isDesired=rs2.getInt(6);
				if(isDesired==0)
				{
					numberOfDaysOdBorrowingArrReg.add(daysOfBorrows); //for the median
					countOfRegular++;
					sumOfRegular+=daysOfBorrows;
				}
				
				
			}
			average=(float)sumOfRegular/(float)countOfRegular;
			
			//get the median
			Collections.sort(numberOfDaysOdBorrowingArrReg);
			 if (numberOfDaysOdBorrowingArrReg.size()%2 == 1)
			 {
				 median = (float)(numberOfDaysOdBorrowingArrReg.get(numberOfDaysOdBorrowingArrReg.size() / 2));
						
			 }
			 else 
			 {
				 median =(float)((numberOfDaysOdBorrowingArrReg.get(numberOfDaysOdBorrowingArrReg.size()/2) +numberOfDaysOdBorrowingArrReg.get(numberOfDaysOdBorrowingArrReg.size()/2 - 1))/2);
			 }
			 
			
			Report reportRegularBook=new Report(average, median);
			result.add(reportRegularBook);

			
			

			//average of desired books
			getAverageDesired = connToSQL.prepareStatement("SELECT* FROM obl.history WHERE action=?" );
			getAverageDesired.setString(1,"return Book"); 
			rs1 =getAverageDesired.executeQuery();

			while(rs1.next())
			{
				daysOfBorrows=Integer.parseInt(rs1.getString(7));
				idOfBook=rs1.getInt(3);
				getAverageDesired = connToSQL.prepareStatement("SELECT* FROM obl.book WHERE bookId=?" );
				getAverageDesired.setInt(1,idOfBook); 
				rs2 =getAverageDesired.executeQuery();
				rs2.next();
				isDesired=rs2.getInt(6);
				if(isDesired==1)
				{
					numberOfDaysOdBorrowingArrDes.add(daysOfBorrows); //for the median
					countOfDesired++;
					sumOfDesired+=daysOfBorrows;
				}

			}
			average=(float)sumOfDesired/(float)countOfDesired;
			
			//get the median
			Collections.sort(numberOfDaysOdBorrowingArrDes);
			 if (numberOfDaysOdBorrowingArrDes.size()%2 == 1)
			 {
				 median= (float)(numberOfDaysOdBorrowingArrDes.get(numberOfDaysOdBorrowingArrDes.size() / 2));
						
			 }
			 else 
			 {
				 median =(float)((numberOfDaysOdBorrowingArrDes.get(numberOfDaysOdBorrowingArrDes.size()/2) +numberOfDaysOdBorrowingArrDes.get(numberOfDaysOdBorrowingArrDes.size()/2 - 1))/2);
			 }
			 
			 
			
			
			Report reportDesiredBook=new Report(average,median);
			result.add(reportDesiredBook);

			
			
				
			//average of all books
			getAverageAllBooks = connToSQL.prepareStatement("SELECT AVG(Note) FROM obl.history WHERE action=?");
			getAverageAllBooks.setString(1,"return Book"); 
			rs1 =getAverageAllBooks.executeQuery();
			rs1.next();
			average=rs1.getFloat(1);
			
			//get the median
			getReturnedBooks = connToSQL.prepareStatement("SELECT* FROM obl.history WHERE action=?" );
			getReturnedBooks.setString(1,"return Book"); 
			rs1 =getReturnedBooks.executeQuery();

			while(rs1.next())
			{
				daysOfBorrows=Integer.parseInt(rs1.getString(7));
				numberOfDaysOdBorrowingArrAll.add(daysOfBorrows); //for the median
			}
			Collections.sort(numberOfDaysOdBorrowingArrAll);
			 if (numberOfDaysOdBorrowingArrAll.size()%2 == 1)
			 {
				 median= (float)(numberOfDaysOdBorrowingArrAll.get(numberOfDaysOdBorrowingArrAll.size() / 2));
						
			 }
			 else 
			 {
				 median =(float) ((numberOfDaysOdBorrowingArrAll.get(numberOfDaysOdBorrowingArrAll.size()/2) +numberOfDaysOdBorrowingArrAll.get(numberOfDaysOdBorrowingArrAll.size()/2 - 1))/2);
			 }
			 
			
			Report reportAllBooks=new Report(average,median);
			result.add(reportAllBooks);


			answer = new ObjectMessage(result,"succssful reporting", "Report number 2");

	}


		catch(SQLException e) 
		{
			e.printStackTrace();
			answer=null;
		}


		return answer;
	}






}


