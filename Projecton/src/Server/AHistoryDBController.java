package Server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
		else
			return null;	

	}

	public static void enterActionToHistory( History sendObject, Connection connToSQL) 
	{
		if(sendObject.getAction().equals("Borrow book"))
		{

			try {
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
			try {

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

	}


	private static ObjectMessage getReportTwo( ObjectMessage msg, Connection connToSQL) 
	{
		ArrayList<IEntity> result=new ArrayList<IEntity>();
		int countOfDesired=0;
		int countOfUnDesired=0;
		PreparedStatement getAverageAllBooks;
		PreparedStatement getAverageDesired;
		PreparedStatement getAverageUnDesired;
		int sumOfDesired=0;
		int sumOfUnDesired=0;
		ResultSet rs1 = null; 
		ResultSet rs2 = null;
		int isDesired;
		int idOfBook;
		Float average;
		int daysOfBorrows;
		ObjectMessage answer = null; 
		try 
		{
			//average of all books
			getAverageAllBooks = connToSQL.prepareStatement("SELECT AVG(Note) FROM obl.history WHERE action=?");
			getAverageAllBooks.setString(1,"return Book"); 
			rs1 =getAverageAllBooks.executeQuery();
			rs1.next();
			average=rs1.getFloat(1);
			Report reportAllBooks=new Report(average,average);
			result.add(reportAllBooks);





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
					countOfDesired++;
					sumOfDesired+=daysOfBorrows;
				}

			}
			average=(float)sumOfDesired/(float)countOfDesired;
			Report reportDesiredBook=new Report(average,average);
			result.add(reportDesiredBook);


			answer = new ObjectMessage(result,"succssful reporting", "Report number 2 ");

		
		
		
		
		
		//average of UN-desired books
		getAverageDesired = connToSQL.prepareStatement("SELECT* FROM obl.history WHERE action=?" );
		getAverageDesired.setString(1,"return Book"); 
		rs1 =getAverageDesired.executeQuery();

		while(rs1.next())
		{
			daysOfBorrows=Integer.parseInt(rs1.getString(7));
			idOfBook=rs1.getInt(3);
			getAverageUnDesired = connToSQL.prepareStatement("SELECT* FROM obl.book WHERE bookId=?" );
			getAverageUnDesired.setInt(1,idOfBook); 
			rs2 =getAverageUnDesired.executeQuery();
			rs2.next();
			isDesired=rs2.getInt(6);
			if(isDesired==0)
			{
				countOfUnDesired++;
				sumOfUnDesired+=daysOfBorrows;
			}

		}
		average=(float)sumOfUnDesired/(float)countOfUnDesired;
		Report reportUnDesiredBook=new Report(average,average);
		result.add(reportUnDesiredBook);


		answer = new ObjectMessage(result,"succssful reporting", "Report number 2 ");

	}


		catch(SQLException e) 
		{
			e.printStackTrace();
			answer=null;
		}


		return answer;
	}






}


