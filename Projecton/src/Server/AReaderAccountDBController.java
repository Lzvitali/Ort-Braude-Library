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
		if (((msg.getMessage()).equals("SearchReader")))
		{
			return searchReaderAccount(msg, connToSQL);
		}
		else
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
					checkPhoneDB.setString(1,reader.getEmail()); //TODO: ??????????????? Meutar
					rs2 =checkPhoneDB.executeQuery();
					if(rs2.next())
					{
						answer.setMessage("phone is already exist in the system");
					}
					else
					{
						//check if the email is exist in the DB
						checkEmailDB = (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM readeraccount WHERE email = ? ");
						checkEmailDB.setString(1,reader.getPhone()); //TODO: ??????????????? Meutar
						checkEmailDB.setString(1,reader.getEmail()); 
						rs2 =checkEmailDB.executeQuery(); //TODO: should be r3
						if(rs3.next())
						{
							answer.setMessage("email is already exist in the system");
						}
					}
	
				}
				updateUser = (PreparedStatement) connToSQL.prepareStatement("INSERT INTO user ('ID','password','permission','isOnline') VALUES \r\n" +" (?,?,?,?); "); 
	
				updateUser.setString(1,reader.getId()); 
				updateUser.setString(2,reader.getPassword()); 
				updateUser.setInt(3,reader.getPermission()); 
				updateUser.setBoolean(4,reader.isOnline()); 
				updateUser.executeQuery();
	
				updateReaderAccount = (PreparedStatement) connToSQL.prepareStatement("INSERT INTO readeraccount ('ID','firstName','lastName','phone','email','address','educationYear','status','numOfDelays') VALUES \r\n" +" (?,?,?,?,?,?,?,?,?); "); 
	
				updateReaderAccount.setString(1,reader.getId()); 
				updateReaderAccount.setString(2,reader.getFirstName()); 
				updateReaderAccount.setString(3,reader.getLastName()); 
				updateReaderAccount.setString(4,reader.getPhone());
				updateReaderAccount.setString(5,reader.getEmail());
				updateReaderAccount.setString(6,reader.getAdress()); 
				updateReaderAccount.setString(7,reader.getEducationYear()); 
				updateReaderAccount.setString(8,reader.getStatus()); 
				updateReaderAccount.setInt(9,reader.getNumOfDelays()); 
				updateReaderAccount.executeQuery();
				answer.setMessage("successful registration");
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
	
			return answer;
		}
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
