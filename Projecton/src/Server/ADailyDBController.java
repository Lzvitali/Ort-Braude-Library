package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Common.Mail;
import Common.ObjectMessage;

public abstract class ADailyDBController 
{
	final static String userName = "OBLManager2019@gmail.com";
	final static String password = "Aa112233";
	
	public static ObjectMessage selection(ObjectMessage msg, Connection connToSQL)
	{

		if (((msg.getMessage()).equals("sendMail")))
		{
			return sendMail(msg, connToSQL);
		}
		else if (((msg.getMessage()).equals("resetUserStatusHistory")))
		{
			return resetUserStatusHistory(msg, connToSQL);
		}
		else
		{
			return null; 
		}
	}
	
	private static ObjectMessage sendMail(ObjectMessage msg, Connection connToSQL) 
	{
			Mail mail=((Mail)((ObjectMessage)msg).getObjectList().get(0));
			String[] to = { mail.getTo() }; // list of recipient email addresses
	        String subject = mail.getSubject();
	        String body = mail.getBody();
		    Properties props = System.getProperties();
	        String host = "smtp.gmail.com";
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", host);
	        props.put("mail.smtp.user", userName);
	        props.put("mail.smtp.password", password);
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");

	        Session session = Session.getInstance(props);
	        MimeMessage message;
	        try 
	        {
	        	message = new MimeMessage(session);
	        }
	        catch (Exception me) 
	        {
	            me.printStackTrace();
	            message = null;
	        }
	        try 
	        {
	            message.setFrom(new InternetAddress(userName));
	            InternetAddress[] toAddress = new InternetAddress[to.length];

	            // To get the array of addresses
	            for( int i = 0; i < to.length; i++ ) 
	            {
	                toAddress[i] = new InternetAddress(to[i]);
	            }

	            for( int i = 0; i < toAddress.length; i++) 
	            {
	                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
	            }

	            message.setSubject(subject);
	            message.setText(body);
	            Transport transport = session.getTransport("smtp");
	            transport.connect(host, userName, password);
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();
	        }
	        catch (AddressException ae) {
	            ae.printStackTrace();
	        }
	        catch (MessagingException me) {
	            me.printStackTrace();
	        }
	        return null;
	}
	
	private static void writeUserStatusHistory(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps=null;
		ResultSet query1 = null;
		PreparedStatement ps2=null;
		ResultSet query2 = null;
		try 
		{
			ps=connToSQL.prepareStatement("SELECT ID, status FROM readeraccount");
			query1=ps.executeQuery();
			while(query1.next())
			{
				ps2=connToSQL.prepareStatement("INSERT INTO `UserStatusHistory` (`readerAccountID`,`status`) VALUES (?,?)");
				ps2.setString(1, query1.getString(1));
				ps2.setString(2, query1.getString(2));
				ps2.executeUpdate();
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}	
		
	}

	private static ObjectMessage resetUserStatusHistory(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps,ps2;
		try 
		{
			ps= connToSQL.prepareStatement("DROP TABLE IF EXISTS `UserStatusHistory`");
			ps2= connToSQL.prepareStatement("CREATE TABLE `UserStatusHistory` (\r\n" + 
					"  `readerAccountID` varchar(9) NOT NULL ,\r\n" + 
					"  `status` enum('Active','Frozen','Locked')  NOT NULL,\r\n" + 
					"  PRIMARY KEY  (`readerAccountID`,`status`),\r\n" + 
					"  FOREIGN KEY (`readerAccountID`) REFERENCES `ReaderAccount` (`ID`)\r\n" + 
					") ENGINE=InnoDB;");
			ps.executeUpdate();
			ps2.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		
		
		return null;
	}
}

