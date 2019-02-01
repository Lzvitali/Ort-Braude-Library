package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Common.Book;
import Common.Copy;
import Common.Mail;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.Report;
import Common.Reservation;

public abstract class ADailyDBController 
{
	
    static String testDay="01"; //Date when the threads start run(monlthy)
	
	
	
	final static String userName = "OBLManager2019@gmail.com";//The mail adresss of server
	final static String password = "Aa112233";//The mail password of server
	
    static ScheduledThreadPoolExecutor executor; //Scheduled thread pool for manage threads
    static ExecutorService pool = Executors.newFixedThreadPool(15); // regular thread pool for send mails
    private static Object lock1 = new Object(); // for syronyizhed
    
    
    
    
	/**
	 * This function start the timing of threads,triggered when the server is starting 
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 */
    public static void startThreads(Connection connToSQL)
    {
    	executor=new ScheduledThreadPoolExecutor(10);
        executor.scheduleAtFixedRate(() -> checkDelayDaily(connToSQL), 0, 12, TimeUnit.HOURS);
    	executor.scheduleAtFixedRate(() -> checkIfDidntImplementReservation(connToSQL), 0, 4, TimeUnit.HOURS);
        executor.scheduleAtFixedRate(() -> resetUserStatusHistory(connToSQL),20, 86399, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(() -> countQuantityOfCopyEveryMounth(connToSQL),15, 86399, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(() -> checkNotifyReturnDaily(connToSQL), 0, 1, TimeUnit.DAYS);
    }
    
	/**
	 * This function sorts the request in the 'msg' to the relevant function and returns the answer
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 */
	public static void selection(ObjectMessage msg, Connection connToSQL)
	{

		if (((msg.getMessage()).equals("sendMail")))
		{
			pool.execute(()->sendMail(msg, connToSQL));
		}
	}

	/**
	 * This function send mail to asked email with asked details
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 */
	private static void sendMail(ObjectMessage msg, Connection connToSQL) 
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
	        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
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
	}
	 

	/**
	 * This function insert all readerAccounts with their status to table once to month
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 */
	private static void monthlyUpdateUserStatusHistory(Connection connToSQL)
	{
		PreparedStatement ps=null;
		ResultSet query1 = null;
		PreparedStatement ps2=null;
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

	/**
	 * This function insert all readerAccounts with their status to table once to month
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 */
	private static void  countQuantityOfCopyEveryMounth(Connection connToSQL)
	{
		PreparedStatement ps=null;
		PreparedStatement ps2=null;
		ResultSet query=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
		Date date = new Date();
		String today=sdf.format(date);
		String todayDay=sdf2.format(date);
   	 	if(todayDay.equals(testDay))
   	 	{
			try
			{
				ps=connToSQL.prepareStatement("SELECT COUNT(*) FROM copy");		
				query=ps.executeQuery();
				query.next();
				ps2=connToSQL.prepareStatement("INSERT INTO `history`(`action`, `date`,`Note`) VALUES (?,?,?)");
				ps2.setString(1, "quantity of copies");
				ps2.setString(2,today);
				ps2.setString(3, query.getString(1));
				ps2.executeUpdate();
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
   	 	}
	}
	
	

	/**
	 * This function add asked copies to the Quantity of copies of current month
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 */
	public static void  countQuantityOfCopyInCaseAddCopyOrBookToDB(Connection connToSQL, Integer numOfAddedCopies)
	{
		PreparedStatement ps=null;
		PreparedStatement ps2=null;
		ResultSet query=null;
		LocalDate now = LocalDate.now();
		int mounth=now.getMonthValue();
		int year=now.getYear();
		int day=Integer.parseInt(testDay);
		LocalDate today=now.of(year, mounth, day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = java.sql.Date.valueOf(today);
		String todayString;
		todayString=sdf.format(date);
		try
		{
			ps=connToSQL.prepareStatement("SELECT * FROM history WHERE Date=? AND action=?");	
			ps.setString(1, todayString);
			ps.setString(2, "quantity of copies");
			query=ps.executeQuery();
			query.next();
			String note= query.getString(7);
			Integer currentCopies=numOfAddedCopies+Integer.parseInt(note);
			ps2=connToSQL.prepareStatement("UPDATE `history` SET `note`=? WHERE Date=? AND action=?");
			ps2.setString(1,Integer.toString(currentCopies));
			ps2.setString(2, todayString);
			ps2.setString(3, "quantity of copies");	
			ps2.executeUpdate();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * This function drop the table of UserStatusHistory and Restore it as empty
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 */
	private static void  resetUserStatusHistory(Connection connToSQL)
	{
		synchronized(lock1) 
		{
			SimpleDateFormat format = new SimpleDateFormat("dd");
			SimpleDateFormat format2 = new SimpleDateFormat("MM");
			Date now =  Calendar.getInstance().getTime();
	   	 	String today=format.format(now);
	   	 	if(today.equals(testDay))
	   	 	{
	   	 		PreparedStatement ps,ps2;
	   	 		try 
	   	 		{
					updateHistoryStatusReader(connToSQL,Integer.parseInt(format2.format(now)));
	   	 			ps= connToSQL.prepareStatement("DROP TABLE IF EXISTS `UserStatusHistory`");
					ps2= connToSQL.prepareStatement("CREATE TABLE `UserStatusHistory` (\r\n" + 
							"  `readerAccountID` varchar(9) NOT NULL ,\r\n" + 
							"  `status` enum('Active','Frozen','Locked')  NOT NULL,\r\n" + 
							"  PRIMARY KEY  (`readerAccountID`,`status`),\r\n" + 
							"  FOREIGN KEY (`readerAccountID`) REFERENCES `ReaderAccount` (`ID`)\r\n" + 
							") ENGINE=InnoDB;");
					ps.executeUpdate();
					ps2.executeUpdate();
					monthlyUpdateUserStatusHistory(connToSQL);					
	   	 		} 
	   	 		catch (SQLException e) 
	   	 		{
	   	 			e.printStackTrace();
	   	 		}
	   	 	}
		}
	}

	/**
	 * This function count reader accounts per status(Active\Freeze\Locked)
	 *  and insert it to History table
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @param month - the month that need to update
	 */
	private static void  updateHistoryStatusReader(Connection connToSQL,int month)
	{
		PreparedStatement ps;
		ResultSet rs;
		Report members=new Report();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = Date.from(ZonedDateTime.now().minusMonths(1).toInstant());
		String today=sdf.format(date);
		try 
		{
			ps = connToSQL.prepareStatement("SELECT COUNT(*) FROM UserStatusHistory WHERE status=?"); 
			ps.setString(1, "Active");
			rs =ps.executeQuery();
			rs.next();
			members.setActiveReaderAccounts(Integer.toString(rs.getInt(1)));
			ps.setString(1, "Frozen");
			rs =ps.executeQuery();
			rs.next();
			members.setFrozenReaderAccounts(Integer.toString(rs.getInt(1)));
			ps.setString(1, "Locked");
			rs =ps.executeQuery();
			rs.next();
			members.setLockedReaderAccounts(Integer.toString(rs.getInt(1)));
			ps=connToSQL.prepareStatement("INSERT INTO `History` (`action`,`date`,`Note`) VALUES ('Active readerAccounts',?,?); "); 
			ps.setString(1, today);
			ps.setString(2, members.getActiveReaderAccounts());
			ps.executeUpdate();
			ps=connToSQL.prepareStatement("INSERT INTO `History` (`action`,`date`,`Note`) VALUES ('Freezed readerAccounts',?,?); "); 
			ps.setString(1, today);
			ps.setString(2, members.getFrozenReaderAccounts());
			ps.executeUpdate();
			ps=connToSQL.prepareStatement("INSERT INTO `History` (`action`,`date`,`Note`) VALUES ('Locked readerAccounts',?,?); "); 
			ps.setString(1, today);
			ps.setString(2, members.getLockedReaderAccounts());
			ps.executeUpdate();
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * This function check if have readerAccount in delay of return that still didnt notify the ReaderAccount
	 * happen once per delay in borrow
	 *  and insert it to History table
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 */
	@SuppressWarnings("resource")
	private static void  checkDelayDaily(Connection connToSQL)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now =  Calendar.getInstance().getTime();
   	 	String today=format.format(now);
   	 	ArrayList <Copy> copies=new ArrayList<Copy>();
   	 	Copy copyInput;
   	 	ResultSet rs;
   	 	PreparedStatement ps;
   	 	try 
   	 	{
			ps=connToSQL.prepareStatement("SELECT * FROM copy WHERE borrowerId IS NOT NULL AND returnDate < ?");
			ps.setString(1, today);
			rs =ps.executeQuery();
			while(rs.next())
			{
				copyInput=new Copy();
				copyInput.setCopyID(rs.getInt(1));
				copyInput.setBookID(rs.getInt(2));
				copyInput.setBorrowerID(rs.getString(3));
				copyInput.setReturnDate(rs.getString(5));
				copies.add(copyInput);
			}
			synchronized(lock1) 
			{
				for(Copy copy: copies)
				{
					ps=connToSQL.prepareStatement("SELECT COUNT(*) FROM History WHERE readerAccountID= ? AND copyId =? AND action= 'Late in return' AND Date= ?" );	
					ps.setString(1, copy.getBorrowerID());
					ps.setInt(2, copy.getCopyID());
					ps.setString(3, copy.getReturnDate());
					rs=ps.executeQuery();
					rs.next();
					if(rs.getInt(1)==0)
					{
						ps=connToSQL.prepareStatement("INSERT INTO `history`(`readerAccountID`, `bookId`,`copyId`,`action`,`date`) VALUES (?,?,?,'Late in return',?)");
						ps.setString(1, copy.getBorrowerID());
						ps.setInt(2, copy.getBookID());
						ps.setInt(3, copy.getCopyID());
						ps.setString(4, copy.getReturnDate());
						ps.executeUpdate();
						ps=connToSQL.prepareStatement("SELECT `numOfDelays` FROM `ReaderAccount` WHERE ID=?");
						ps.setString(1, copy.getBorrowerID());
						rs=ps.executeQuery();
						rs.next();
						ReaderAccount readerAccount=new ReaderAccount();
						readerAccount.setId(copy.getBorrowerID());
						ObjectMessage askTheFirstReader=new ObjectMessage(readerAccount,"SearchReader","ReaderAccount");
						readerAccount =(ReaderAccount) (AReaderAccountDBController.selection(askTheFirstReader, connToSQL)).getObjectList().get(0);
						Book askedBook=new Book();
						askedBook.setBookID(copy.getBookID());
						ObjectMessage bookDetails=new ObjectMessage(askedBook,"searchBookID","Book");
						Book book = (Book) (ABookDBController.selection(bookDetails, connToSQL)).getObjectList().get(0);
						ObjectMessage notify=new ObjectMessage("sendMail","Daily");
						Mail mail=new Mail();
						mail.setTo(readerAccount.getEmail());
						String subject="Return your book "+book.getBookName();
						mail.setSubject(subject);
						if(rs.getInt(1)<3)
						{
							readerAccount.setStatus("Frozen");
							ps=connToSQL.prepareStatement("UPDATE `ReaderAccount` SET `numOfDelays`=? , status='Frozen' WHERE ID=?");
							ps.setInt(1, (rs.getInt(1)+1));
							ps.setString(2, copy.getBorrowerID());
							ps.executeUpdate();
							
							String body="Hello "+readerAccount.getFirstName()+"\nWe want to notfiy you that you need to come to library"
									+ " and return "+book.getBookName()
									+ ".\nUntill that your account is Frozen."
									+"\n 		Thank you , Ort Braude Library";
							mail.setBody(body);
							notify.addObject(mail);
							ADailyDBController.selection(notify, connToSQL);
							
							
						}
						else
						{
							readerAccount.setStatus("Locked");
							ps=connToSQL.prepareStatement("UPDATE `ReaderAccount` SET `numOfDelays`=? , status='Locked' WHERE ID=?");
							ps.setInt(1, (rs.getInt(1)+1));
							ps.setString(2, copy.getBorrowerID());
							ps.executeUpdate();
							
							String body="Hello "+readerAccount.getFirstName()+"\nWe want to notfiy you that you need to come to library"
									+ " and return "+book.getBookName()
									+ ".\nUntill that your account is Locked because you have more then 3 delays in your history."
									+"\n 		Thank you , Ort Braude Library";
							mail.setBody(body);
							notify.addObject(mail);
							ADailyDBController.selection(notify, connToSQL);
							
						}
						ObjectMessage objectMessage=new ObjectMessage(readerAccount,"ChangeStatus","ReaderAccount");
						AReaderAccountDBController.selection(objectMessage, connToSQL);
					}		
				}
			}
		}
   	 	catch (SQLException e) 
   	 	{
			e.printStackTrace();
		}		
	}

	/**
	 * This function check if have reader account that didnt implement his reservation in the 48 hours he had,
	 * if he didnt implement cancel his reservaion and let the next one to implmenent(if have one)
	 *  and insert it to History table
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 */
	private static void  checkIfDidntImplementReservation(Connection connToSQL)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -2);
		String lessTwoDays = (String)(sdf.format(calendar.getTime()));
 	 	ResultSet rs;
   	 	ArrayList <Reservation> reservations=new ArrayList<Reservation>();
   	 	Reservation reservationInput;
   	 	ReaderAccount readerAccount;
   	 	PreparedStatement ps;
   	 	try 
   	 	{
			ps=connToSQL.prepareStatement("SELECT * FROM reservations WHERE startTimerImplement IS NOT NULL AND startTimerImplement < ?");
			ps.setString(1, lessTwoDays);
			rs =ps.executeQuery();
			while(rs.next())
			{
				reservationInput=new Reservation();
				reservationInput.setBookID(rs.getInt(1));
				reservationInput.setReaderAccountID(rs.getString(2));
				reservations.add(reservationInput);
				readerAccount=new ReaderAccount();
				readerAccount.setId(rs.getString(2));
				ObjectMessage newMsg = new ObjectMessage(readerAccount, reservationInput, "cancel reservation", "Reservation");
				AReservationDBController.selection(newMsg, connToSQL);
			}
		
   	 	}
   	 	catch(Exception e)
   	 	{
   	 		e.printStackTrace();
   	 	}
	}

	/**
	 * This function check if have reader account that suppose to return his borrow book in the next day
	 * if have somone like this , it will notify him
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 */
	private static void  checkNotifyReturnDaily(Connection connToSQL)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now =  Calendar.getInstance().getTime();
   	 	String today=format.format(now);
   	 	Calendar cal = Calendar.getInstance();
   	 	cal.setTime(now);
   	 	cal.add(Calendar.DATE, 1); //minus number would decrement the days
     	String tommorow=format.format(cal.getTime()); 
   	 	ArrayList <Copy> copies=new ArrayList<Copy>();
   	 	Copy copyInput;
   	 	ResultSet rs;
   	 	PreparedStatement ps;
   	 	try 
   	 	{
			ps=connToSQL.prepareStatement("SELECT * FROM copy WHERE borrowerId IS NOT NULL AND returnDate = ?");
			ps.setString(1, tommorow);
			rs =ps.executeQuery();
			while(rs.next())
			{
				copyInput=new Copy();
				copyInput.setCopyID(rs.getInt(1));
				copyInput.setBookID(rs.getInt(2));
				copyInput.setBorrowerID(rs.getString(3));
				copyInput.setReturnDate(rs.getString(5));
				copies.add(copyInput);
			}
			
			for(Copy copy:copies)
			{
				ReaderAccount readerAccount=new ReaderAccount();
				readerAccount.setId(copy.getBorrowerID());
				ObjectMessage askTheFirstReader=new ObjectMessage(readerAccount,"SearchReader","ReaderAccount");
				readerAccount =(ReaderAccount) (AReaderAccountDBController.selection(askTheFirstReader, connToSQL)).getObjectList().get(0);
				
				Book askedBook=new Book();
				askedBook.setBookID(copy.getBookID());
				ObjectMessage bookDetails=new ObjectMessage(askedBook,"searchBookID","Book");
				Book book = (Book) (ABookDBController.selection(bookDetails, connToSQL)).getObjectList().get(0);
				
				ObjectMessage notify=new ObjectMessage("sendMail","Daily");
				Mail mail=new Mail();
				mail.setTo(readerAccount.getEmail());
				String subject="Return your book "+book.getBookName()+" Untill tommorow";
				mail.setSubject(subject);
				String body="Hello "+readerAccount.getFirstName()+"\nWe want to notfiy you that you need to come to library"
							+ " and return  "+book.getBookName()
							+ ".\nUntill tommorow."
							+"\n 		Thank you , Ort Braude Library";
				mail.setBody(body);
				notify.addObject(mail);
				ADailyDBController.selection(notify, connToSQL);
			}
					
   	 	}
   	 	catch(Exception e)
   	 	{
   	 		e.printStackTrace();
   	 	}
	}


}

