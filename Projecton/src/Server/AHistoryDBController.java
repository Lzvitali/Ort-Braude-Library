package Server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

	//function get history of specific reader account and sent it to client 
	private static ObjectMessage getReaderAccountHistory(ObjectMessage msg, Connection connToSQL) 
	{
		ReaderAccount askedReader=(ReaderAccount)msg.getObjectList().get(0);
		askedReader.getId();
		Boolean resultExist=false;
		Integer no=0;
		ResultSet rs,rs2;
		ArrayList <IEntity> result=new ArrayList<IEntity>(); 
		try 
		{
			//statement for if user exist. take all his History
			PreparedStatement reader = (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM history WHERE readerAccountID = ? ");
			reader.setString(1,askedReader.getId());
			rs = reader.executeQuery();
			History history;
			//check if the id have History
			while(rs.next())
			{
				resultExist = true;
				if(rs.getString(5).equals("Changed status"))
				{
					history= new History(no++,rs.getString(6),rs.getString(5),rs.getString(7));
					result.add(history);
				}
				else if(rs.getString(5).equals("Registration to OBL"))
				{
					history= new History(no++,rs.getString(6),rs.getString(5));
					result.add(history);
				}
				else//for actions with book
				{
					//get Book name for set it in window of history
					PreparedStatement forBookName = (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM Book WHERE bookId = ? ");
					forBookName.setInt(1, rs.getInt(3));
					rs2=forBookName.executeQuery();
					rs2.next();
					history= new History(no++,rs.getString(6),rs.getString(5),rs2.getString(2));
					result.add(history);
				}
			}
			
			if(resultExist)
			{
				return new ObjectMessage(result,"SetHistory"," ");
			}
			else
			{
				return new ObjectMessage(result,"NoHistory"," ");
			}

			
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		return new ObjectMessage(result,"NoHistory"," ");
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
		boolean thereIsResults = false;
		int cnt = 0; //counter for total number of late returns
		long totalDurationofLates = 0;
		float average = 0;
		float median = 0;
		ArrayList<Long> durationOfTheLate=new ArrayList<Long>();

		PreparedStatement booksWithLates = null; 
		PreparedStatement existenceOfTheBook = null;
		PreparedStatement getReturnDate = null;
		ResultSet rs0 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		
		try 
		{
			//check if the book exist
			existenceOfTheBook = connToSQL.prepareStatement("SELECT * FROM Book WHERE bookId = ? ");
			existenceOfTheBook.setInt(1, bookID);   
			rs0 =existenceOfTheBook.executeQuery();
			
			if(rs0.next())
			{
				bookIsExist = true; //book exist
			}
			
			booksWithLates = connToSQL.prepareStatement("SELECT * FROM history WHERE bookId = ? AND action = ? ");
			booksWithLates.setInt(1, bookID); 
			booksWithLates.setString(2, "Late in Return");  
			rs1 =booksWithLates.executeQuery();
			
			while(rs1.next())
			{ 
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
				
				if(null == returnDate)
				{
					LocalDate now = LocalDate.now(); 
					Date today = java.sql.Date.valueOf(now);
					returnDate = today;
				}
				
				//calculate the duration of the borrow
				long diff = Math.abs(startOfLate.getTime() -returnDate.getTime()); 
				Long dif2 = new Long( TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));	
				
				totalDurationofLates += dif2;
				durationOfTheLate.add(dif2);
				thereIsResults = true;

			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();

		}

		if(bookIsExist)
		{
			
			if(thereIsResults)
			{
				average = (float)totalDurationofLates/(float)cnt;
				
				//get the median
				Collections.sort(durationOfTheLate);
				if (durationOfTheLate.size()%2 == 1)
				{
					median = (float)(durationOfTheLate.get(durationOfTheLate.size() / 2));

				}
				else 
				{
					median =(float)((durationOfTheLate.get(durationOfTheLate.size()/2) +durationOfTheLate.get(durationOfTheLate.size()/2 - 1))/2.0);
				}
			}
			else
			{
				median = 0;
			}
			  
			
			
			
			Report report = new Report();
			report.setAverage(average);
			report.setMedian(median);
			report.setTotal(cnt); 
			report.setDetailsArray(durationOfTheLate);
			
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

		else if(sendObject.getAction().equals("Changed status"))
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
		ArrayList<Long> numberOfDaysOdBorrowingArrReg=new ArrayList<Long>();
		ArrayList<Long> numberOfDaysOdBorrowingArrDes=new ArrayList<Long>();
		ArrayList<Long> numberOfDaysOdBorrowingArrAll=new ArrayList<Long>();
		int countOfDesired=0;
		int countOfRegular=0;
		int countOfAll=0;
		PreparedStatement getAverageAllBooks;
		PreparedStatement getAverageDesired;
		PreparedStatement getReturnedBooks;
		PreparedStatement getAverageRegular;
		int sumOfDesired=0;
		long sumOfRegular=0;
		ResultSet rs1 = null; 
		ResultSet rs2 = null;
		int isDesired;
		int idOfBook;
		float average;
		float median;
		
		Long daysOfBorrows;
		ObjectMessage answer = null; 
		try 
		{
			//average of Regular books
			getAverageDesired = connToSQL.prepareStatement("SELECT* FROM obl.history WHERE action=?" );
			getAverageDesired.setString(1,"return Book"); 
			rs1 =getAverageDesired.executeQuery();

			while(rs1.next())
			{
				daysOfBorrows=Long.parseLong(rs1.getString(7));
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
			
			if(countOfRegular==0)
			{
				average=0;
			}
			
			else
			{
				average=(float)sumOfRegular/(float)countOfRegular;
			}
			
			//get the median
			Collections.sort(numberOfDaysOdBorrowingArrReg);
			if(countOfRegular==0)
			{
				median=0;
			}
			
			else if (numberOfDaysOdBorrowingArrReg.size()%2 == 1)
			{
				 median = (float)(numberOfDaysOdBorrowingArrReg.get((int) (numberOfDaysOdBorrowingArrReg.size() / 2.0)));		
			}
			
			else 
			{
				 median =(float)((numberOfDaysOdBorrowingArrReg.get((int) (numberOfDaysOdBorrowingArrReg.size()/2.0)) +numberOfDaysOdBorrowingArrReg.get((int) (numberOfDaysOdBorrowingArrReg.size()/2.0 - 1)))/2.0);
			}
			 
			
			Report reportRegularBook=new Report(average, median);
			result.add(reportRegularBook);
			reportRegularBook.setDetailsArray(numberOfDaysOdBorrowingArrReg); 
			
			
			

			//average of desired books
			getAverageDesired = connToSQL.prepareStatement("SELECT* FROM obl.history WHERE action=?" );
			getAverageDesired.setString(1,"return Book"); 
			rs1 =getAverageDesired.executeQuery();

			while(rs1.next())
			{
				daysOfBorrows=Long.parseLong(rs1.getString(7));
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
			
			if(countOfDesired==0)
			{
				average=0;
			}
			
			else
			{
				average=(float)sumOfDesired/(float)countOfDesired;
			}
			
			
			//get the median
			Collections.sort(numberOfDaysOdBorrowingArrDes);
			
			if(countOfDesired==0)
			{
				median=0;
			}
			
			else if (numberOfDaysOdBorrowingArrDes.size()%2 == 1)
			{
				 median= (float)(numberOfDaysOdBorrowingArrDes.get((int) (numberOfDaysOdBorrowingArrDes.size() / 2.0)));
						
			}
			
			 else 
			{
				 median =(float)((numberOfDaysOdBorrowingArrDes.get((int) (numberOfDaysOdBorrowingArrDes.size()/2.0)) +numberOfDaysOdBorrowingArrDes.get((int) (numberOfDaysOdBorrowingArrDes.size()/2.0 - 1)))/2.0);
			}
			 
			 
			Report reportDesiredBook=new Report(average,median);
			//reportDesiredBook.set
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
				daysOfBorrows=Long.parseLong(rs1.getString(7));
				countOfAll++;
				numberOfDaysOdBorrowingArrAll.add(daysOfBorrows); //for the median
			}
			
			Collections.sort(numberOfDaysOdBorrowingArrAll);
			
			if(countOfAll==0)
			{
				median=0;
			}
			
			else if (numberOfDaysOdBorrowingArrAll.size()%2 == 1)
			{
				median= (float)(numberOfDaysOdBorrowingArrAll.get((int) (numberOfDaysOdBorrowingArrAll.size() / 2.0)));
						
			}
			
			else 
			{
				median =(float) ((numberOfDaysOdBorrowingArrAll.get((int) (numberOfDaysOdBorrowingArrAll.size()/2.0)) +numberOfDaysOdBorrowingArrAll.get((int) (numberOfDaysOdBorrowingArrAll.size()/2.0 - 1)))/2.0);
			}
			 
			
			Report reportAllBooks=new Report(average,median);
			result.add(reportAllBooks);


			answer = new ObjectMessage(result,"successful reporting", "Report number 2");

		}


		catch(SQLException e) 
		{
			e.printStackTrace();
			answer=null;
		}


		return answer;
	}


}


