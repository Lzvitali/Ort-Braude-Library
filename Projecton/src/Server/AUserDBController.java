package Server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Common.ObjectMessage;
import Common.User;

public class AUserDBController 
{
	/**
	 * This function sorts the request in the 'msg' to the relevant function and returns the answer
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	public static ObjectMessage selection(ObjectMessage msg, Connection connToSQL)
	{
		if((msg.getMessage()).equals("user try to log in"))
		{
			return tryToLogIn(msg, connToSQL);
		}
		else if((msg.getMessage()).equals("user try to log out"))
		{
			return tryToLogOut(msg, connToSQL);
		}
		
		
		
		 
		return null; // TODO: delete it. did it only to escape the error 
	}
	
	
	
	
	
	/**
	 * This function attempts to commit the log-in for the user and returns "successful" or "unsuccessful" or "not exist"
	 * @param msg
	 * @param connToSQL
	 * @return a message in ObjectMessage
	 */
	private static ObjectMessage tryToLogIn(ObjectMessage msg, Connection connToSQL)
	{
		ObjectMessage answer = new ObjectMessage();
		
		String permition;
		
		PreparedStatement ps = null; 
		ResultSet rs = null; 

		try 
		{
			//get the data of the student from the BD
			ps = connToSQL.prepareStatement("SELECT * FROM user WHERE ID = ? AND password=? ");
			ps.setString(1, ((User)msg.getObjectList().get(0)).getId() ); 
			ps.setString(2, ((User)msg.getObjectList().get(0)).getPassword()); 
			rs =ps.executeQuery();

			if(rs.next())
			{
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
					  
					  permition = rs.getString(3);
					  answer.setNote(permition); 
					  answer.setMessage("successful");
				}
				else //if exist but already connected
				{
					answer.setMessage("unsuccessful");
				}
			}
			else //if not exist
			{
				answer.setMessage("not exist");
			}
			
			 
			//client.sendToClient(askedStudent);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return answer;
	}
	
	
	/**
	 * This function commits the log out
	 * @param msg
	 * @param connToSQL
	 * @return a message in ObjectMessage
	 */
	private static ObjectMessage tryToLogOut(ObjectMessage msg, Connection connToSQL)
	{
		ObjectMessage answer = new ObjectMessage();
		
		PreparedStatement updateTime;
		try 
		{
			updateTime = connToSQL.prepareStatement("UPDATE user "+"SET isOnline = ? WHERE ID = ?");
			updateTime.setString(1, "0");
			updateTime.setString(2, ((User)msg.getObjectList().get(0)).getId());
			updateTime.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		answer.setMessage("successful log out");
		return answer;
	}

}
