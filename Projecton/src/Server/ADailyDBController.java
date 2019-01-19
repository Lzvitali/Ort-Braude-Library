package Server;

import java.sql.Connection;

import Common.ObjectMessage;

public abstract class ADailyDBController 
{
	public static ObjectMessage selection(ObjectMessage msg, Connection connToSQL)
	{

		if (((msg.getMessage()).equals("checkIfAllBorrowed")))
		{
			return null;
		}
		else
		{
			return null; 
		}
	}
}

