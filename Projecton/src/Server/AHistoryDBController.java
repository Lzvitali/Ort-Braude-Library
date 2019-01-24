package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Common.Copy;
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

	public static void enterActionToHistory( ObjectMessage msg, Connection connToSQL) 
	{
		
		
	}
	
	
	private static ObjectMessage getReportTwo( ObjectMessage msg, Connection connToSQL) 
	{
		ArrayList<Report> result=new ArrayList<Report>();
		int countOfDesired=0;
		PreparedStatement getAverageAllBooks;
		PreparedStatement getAverageDesired;
		int sumOfDesired=0;
		ResultSet rs1 = null; 
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
			Report report2=new Report(average,average);
			result.add(report2);

			//report2[2]=new Report(average, average);



			////average of desired books
			getAverageDesired = connToSQL.prepareStatement("SELECT* FROM obl.history WHERE action=?" );
			getAverageDesired.setString(1,"return Book"); 
			rs1 =getAverageDesired.executeQuery();

			while(rs1.next())
			{
				daysOfBorrows=Integer.parseInt(rs1.getString(7));
				idOfBook=rs1.getInt(3);
				getAverageDesired = connToSQL.prepareStatement("SELECT* FROM obl.book WHERE bookId=?" );
				getAverageDesired.setInt(1,idOfBook); 
				rs1 =getAverageDesired.executeQuery();
				isDesired=rs1.getInt(6);
				if(isDesired==1)
				{
					countOfDesired++;
					sumOfDesired+=daysOfBorrows;
				}

			}
			average=(float)sumOfDesired/(float)countOfDesired;
			report2[1]=new Report(average, average);

			result.add(report2);
			answer = new ObjectMessage((IEntity)result,"succssful reporting", " ");

		}


		catch(SQLException e) 
		{
			e.printStackTrace();
			answer=null;
		}


		return answer;
	}






}


