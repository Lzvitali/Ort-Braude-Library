package Server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;


import Common.Book;
import Common.IEntity;
import Common.ObjectMessage;
import Common.ReaderAccount;

public class AReaderAccountDBController 
{

	/**
	 * This function sorts the request in the 'msg' to the relevant function and returns the answer
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	public static ObjectMessage selection(ObjectMessage msg, Connection connToSQL)
	{
		ObjectMessage answer = new ObjectMessage();
		if (((msg.getMessage()).equals("SearchReader")))
		{
			return searchReaderAccount(msg, connToSQL);
		}
		if (((msg.getMessage()).equals("try to register new account")))
		{
			return registerNewReaderAccount(msg, connToSQL);
		}
		return answer;
	}
	private static ObjectMessage registerNewReaderAccount(ObjectMessage msg, Connection connToSQL)
	{
			ObjectMessage answer = new ObjectMessage();
			String permition;
			PreparedStatement checkID = null; 
			PreparedStatement checkPhoneDB = null;
			PreparedStatement checkEmailDB = null;
			PreparedStatement updateUser ;
			PreparedStatement updateReaderAccount ;
			ResultSet rs = null; 
			ResultSet rs2 = null; 
			ResultSet rs3 = null; 
			ResultSet rs4 = null; 

			try 
			{
				ReaderAccount reader=(ReaderAccount)msg.getObjectList().get(0);
				//get the data of the student from the BD
				checkID = (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM user WHERE ID = ? ");
				checkID.setString(1,reader.getId()); 
				rs =checkID.executeQuery();
				//check if the id is exist in the DB
				if(rs.next())
				{
					answer.setMessage("id is already exist in the system");
				}
				else
				{
					//check if the phone is exist in the DB
					checkPhoneDB = (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM readeraccount WHERE phone = ? ");
					checkPhoneDB.setString(1,reader.getPhone()); 
					rs2 =checkPhoneDB.executeQuery();
					if(rs2.next())
					{
						answer.setMessage("phone is already exist in the system");
					}
					else
					{
						//check if the email is exist in the DB
						checkEmailDB = (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM readeraccount WHERE email = ? ");
						checkEmailDB.setString(1,reader.getEmail()); 
						rs3 =checkEmailDB.executeQuery(); //TODO: should be r3
						if(rs3.next())
						{
							answer.setMessage("email is already exist in the system");
						}
					}
	
				}
				updateUser = connToSQL.prepareStatement("INSERT INTO `user` (`ID`,`password`,`permission`,`isOnline`) VALUES (?,?,?,?); "); 
	
				updateUser.setString(1,(String)reader.getId()); 
				updateUser.setString(2,(String)reader.getPassword()); 
				updateUser.setInt(3,(int)reader.getPermission()); 
				updateUser.setBoolean(4,(Boolean)reader.isOnline()); 
				updateUser.executeUpdate();

				updateReaderAccount = connToSQL.prepareStatement("INSERT INTO `readeraccount` (`ID`,`firstName`,`lastName`,`phone`,`email`,`address`,`educationYear`,`status`,`numOfDelays`) VALUES(?,?,?,?,?,?,?,?,?)"); 
	
				updateReaderAccount.setString(1,(String)reader.getId()); 
				updateReaderAccount.setString(2,(String)reader.getFirstName()); 
				updateReaderAccount.setString(3,(String)reader.getLastName()); 
				updateReaderAccount.setString(4,(String)reader.getPhone());
				updateReaderAccount.setString(5,(String)reader.getEmail());
				updateReaderAccount.setString(6,(String)reader.getAdress()); 
				updateReaderAccount.setInt(7,(int)Integer.parseInt(reader.getEducationYear())); 
				updateReaderAccount.setString(8,(String)reader.getStatus()); 
				updateReaderAccount.setInt(9,(int)reader.getNumOfDelays()); 
				updateReaderAccount.executeUpdate();
				answer.setMessage("successful registration");
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}	
			return answer;
	}
	
	private static ObjectMessage searchReaderAccount(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		ObjectMessage answer;
		ReaderAccount askedReader=(ReaderAccount)msg.getObjectList().get(0);
		String input;
		ArrayList <IEntity> result=new ArrayList<IEntity>();
		try 
		{
			if(askedReader.getId()!=null)
			{
				ps = connToSQL.prepareStatement("SELECT * FROM obl.ReaderAccount WHERE ID=?");
				input=askedReader.getId();
			}
			else if(askedReader.getFirstName()!=null)
			{
				ps = connToSQL.prepareStatement("SELECT * FROM obl.ReaderAccount WHERE firstName=?");
				input=askedReader.getFirstName();
			}
			else
			{
				ps = connToSQL.prepareStatement("SELECT * FROM obl.ReaderAccount WHERE lastName=?");
				input=askedReader.getLastName();
			}
			ps.setString(1,input);
			ResultSet rs = ps.executeQuery();
	 		while(rs.next())
	 		{
	 			result.add(new ReaderAccount(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(8)));
			} 
	 		if(!result.isEmpty())
	 		{
	 			answer=new ObjectMessage(result,"ReaderAccountSearch","ReaderFound");
	 		}
	 		else
	 		{
	 			answer=new ObjectMessage("ReaderAccountSearch","NoReaderFound");
	 		}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			answer=new ObjectMessage("ReaderAccountSearch","NoReaderFound");
		}
		return answer;
	}
}
