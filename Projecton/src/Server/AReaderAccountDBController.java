package Server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.sql.PreparedStatement;


import Common.Book;
import Common.Copy;
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
		if (((msg.getMessage()).equals("RegistrationNewReaderAccount")))
		{
			return registerNewReaderAccount(msg, connToSQL);
		}
		if (((msg.getMessage()).equals("changePersonalDetails")))
		{
			return changePersnalDetails(msg, connToSQL);
		}
		else if(((msg.getMessage()).equals("CheckIfExist")))
		{
			return checkIfExistID(msg, connToSQL);
		}
		else if(((msg.getMessage()).equals("ChangeStatus")))
		{
			return changeStatus(msg, connToSQL);
		}
		else
		{
			return answer;
		}
	}
	
	private static ObjectMessage changeStatus(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		ReaderAccount readerAccount=(ReaderAccount)msg.getObjectList().get(0);
		ObjectMessage answer=new ObjectMessage("StatusChanged","The Status of "+readerAccount.getId()+" have changed to "+readerAccount.getStatus());
		try 
		{
			ps = connToSQL.prepareStatement("UPDATE `readeraccount` SET `status`=? WHERE ID=?");
			ps.setString(1,readerAccount.getStatus());
			ps.setString(2,readerAccount.getId());
			ps.executeUpdate();
			ps = connToSQL.prepareStatement("INSERT INTO `UserStatusHistory` (`readerAccountID`,`status`) VALUES (?,?); ");
			ps.setString(1,readerAccount.getId());
			ps.setString(2,readerAccount.getStatus());
			ps.executeUpdate();
			
			//TODO: For Nata: call your history function
			
			return answer;
		}
		catch(Exception e)
		{
			return answer;
		}
	}
	
	/**
	 * This function check if reader account can borrow specific book
	 * @param msg- the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	private static ObjectMessage checkIfExistID(ObjectMessage msg, Connection connToSQL) 
	{
		try 
		{
			
			ReaderAccount reader=(ReaderAccount)msg.getObjectList().get(0);
			Copy copy=(Copy)msg.getObjectList().get(1);
			//get the data of the student from the BD
			PreparedStatement checkID = (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM readeraccount WHERE ID = ? ");
			checkID.setString(1,reader.getId()); 
			ResultSet rs = checkID.executeQuery();
			
			//check if the id is exist in the DB
			if(rs.next())
			{
								
				if(rs.getString(8).equals("Active"))//check if status of reader is Active
				{
					String massege=ACopyDBController.checkIfBookIsAvailable(msg, connToSQL);
					
					if(massege.equals("Desired")|| massege.equals("NotDesired"))//copy available and book desired
					{
						return new ObjectMessage("The book was successfuly borrowed.","ExistAndAvailable");
					}
					
					else if(massege.equals("CopyNotExist"))//copy  not available 
					{
						return new ObjectMessage("The book with this copyId is not Exist in DB.","NotExistCopy");
					}
					else if(massege.equals("CopyAlreadyBorrowed"))//copy not available 
					{
						return new ObjectMessage("The book with this copyId is already borrowed.","CopyAlreadyBorrowed");
					}
					
				}
				else // status of reader is NOT Active
				{
					return new ObjectMessage("Status of the reader account is NOT active. You can't borrow book! ","ExistButNotActive");
				}
				
			}
			else//  reader is NOT exist in obl DB
			{
				return new ObjectMessage("Reader account with this ID does not exist in DB. ","ReaderNotExist");
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return new ObjectMessage("Reader account with this ID does not exist in DB.","ReaderNotExist");
	}

			
			
	private static ObjectMessage changePersnalDetails(ObjectMessage msg, Connection connToSQL)
	{
		ObjectMessage answer = new ObjectMessage(); 
		answer.setMessage("change details");
		PreparedStatement checkPhoneDB = null;
		PreparedStatement checkEmailDB = null;
		PreparedStatement updateUser ;
		PreparedStatement updateReaderAccount ;
		ResultSet rs2 = null; 
		ResultSet rs3 = null; 

		try 
		{
			ReaderAccount reader=(ReaderAccount)msg.getObjectList().get(0);
			checkPhoneDB = (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM readeraccount WHERE phone = ? AND ID NOT LIKE ? ");
			checkPhoneDB.setString(1,reader.getPhone()); 
			checkPhoneDB.setString(2,reader.getId()); 
			rs2 =checkPhoneDB.executeQuery();
			if(rs2.next())
			{
				answer.setNote("phone is already exist in the system");
			}
			else
			{
				checkEmailDB = (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM readeraccount WHERE email = ?  AND ID NOT LIKE ?");
				checkEmailDB.setString(1,reader.getEmail()); 
				checkEmailDB.setString(2,reader.getId()); 
				rs3 =checkEmailDB.executeQuery(); 
				if(rs3.next())
				{
					answer.setNote("email is already exist in the system");
				}
				else
				{ 
					updateReaderAccount = connToSQL.prepareStatement("UPDATE `readeraccount` SET `phone`=?,`email`=?,`address`=?,`educationYear`=? WHERE ID=?");
					updateReaderAccount.setString(1,(String)reader.getPhone());
					updateReaderAccount.setString(2,(String)reader.getEmail());
					updateReaderAccount.setString(3,(String)reader.getAdress()); 
					updateReaderAccount.setString(4,reader.getEducationYear()); 
					updateReaderAccount.setString(5,reader.getId());
					updateReaderAccount.executeUpdate();
					answer.setNote("successful change details");
					answer.addObject(msg.getObjectList().get(0));
				}
			}
		}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}	
			return answer;
			
}
		

	
	
	private static ObjectMessage registerNewReaderAccount(ObjectMessage msg, Connection connToSQL)
	{
			ObjectMessage answer = new ObjectMessage();
			PreparedStatement checkID = null; 
			PreparedStatement checkPhoneDB = null;
			PreparedStatement checkEmailDB = null;
			PreparedStatement updateUser ;
			PreparedStatement updateReaderAccount ;
			ResultSet rs = null; 
			ResultSet rs2 = null; 
			ResultSet rs3 = null; 
    		Random rand = new Random();
    		int randPassword = rand.nextInt(10000) + 1;
    		String password=Integer.toString(randPassword);
			try 
			{
				ReaderAccount reader=(ReaderAccount)msg.getObjectList().get(0);
				reader.setPassword(password);
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
				updateReaderAccount.setString(7,(String)reader.getEducationYear()); 
				updateReaderAccount.setString(8,(String)reader.getStatus()); 
				updateReaderAccount.setInt(9,(int)reader.getNumOfDelays()); 
				updateReaderAccount.executeUpdate();
				answer.setMessage("successful registration");
				answer.addObject(msg.getObjectList().get(0));
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}	
			return answer;
	}
	
	private static ObjectMessage searchReaderAccount(ObjectMessage msg, Connection connToSQL)
	{
		int isFreeSearch=0;
		PreparedStatement ps = null;
		ObjectMessage answer;
		ReaderAccount askedReader=(ReaderAccount)msg.getObjectList().get(0);
		String input = null;
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
				ps = connToSQL.prepareStatement("SELECT * FROM obl.ReaderAccount WHERE firstName LIKE ?");
				input=askedReader.getFirstName();
			}
			else if(askedReader.getLastName()!=null)
			{
				ps = connToSQL.prepareStatement("SELECT * FROM obl.ReaderAccount WHERE lastName LIKE ?");
				input=askedReader.getLastName();
			}
			else
			{
				isFreeSearch=1;
				String freeSearch=askedReader.getFreeSearch();
				String[] arrFreeSearch = freeSearch.split("\\s+");
				//pass on every word in free search and check if exist in db
				for ( String ss : arrFreeSearch)
				{
					ps = connToSQL.prepareStatement("SELECT * FROM obl.readeraccount WHERE ID=? OR firstName LIKE ? OR lastname LIKE ? OR phone LIKE ? OR email LIKE ? OR address LIKE ? ");
					ps.setString(1,ss);
					ps.setString(2,"%"+ss+"%");
					ps.setString(3,"%"+ss+"%");
					ps.setString(4,"%"+ss+"%");
					ps.setString(5,"%"+ss+"%");
					ps.setString(6,"%"+ss+"%");
					ResultSet rs= ps.executeQuery();
					while(rs.next())
					{
						result.add(new ReaderAccount(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(8),rs.getString(7)));
					}
				}
			}
			
			//if it is not freeSearch
			if(isFreeSearch==0)
			{
				if(askedReader.getId()!=null)
				{
					ps.setString(1,input);
				}
				else
				{
					ps.setString(1,"%"+input+"%");
				}
			ResultSet rs = ps.executeQuery();
			
			
				while(rs.next())
				{
					result.add(new ReaderAccount(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(8),rs.getString(7)));
				} 
	 		
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
