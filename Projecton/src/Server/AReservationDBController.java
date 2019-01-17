package Server;

import java.sql.Connection;

import Common.ObjectMessage;

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

}
