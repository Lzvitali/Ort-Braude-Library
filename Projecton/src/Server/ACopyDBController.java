package Server;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import Common.Book;
import Common.Copy;
import Common.History;
import Common.IEntity;
import Common.Mail;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.User;

/**
 * This class make the functionality for the server that includes a connection to the DB.
 * The Focus of this class is on functions that deal with 'Copies'
 */

public abstract class ACopyDBController 
{

	private static final String NULL = null;


	/**
	 * This function sorts the request in the 'msg' to the relevant function and returns the answer
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	public static ObjectMessage selection(ObjectMessage msg, Connection connToSQL)
	{

		if (((msg.getMessage()).equals("checkIfAllBorrowed")))
		{
			return checkIfAllBorrowed(msg, connToSQL);
		}
		else if (((msg.getMessage()).equals("get borrows")))
		{
			return getBorrows(msg, connToSQL);
		}
		else if (((msg.getMessage()).equals("DeleteBook")))
		{
			return deleteBook(msg, connToSQL);
		}
		else if(((msg.getMessage()).equals("ReturnCopy")))
		{
			return tryToReturnBook(msg, connToSQL);
		} 
		else if(((msg.getMessage()).equals("ask for delay")))
		{
			return askForDelay(msg, connToSQL);
		} 
		else if(((msg.getMessage()).equals("CheckReturnDate")))
		{
			return checkReturnDate(msg, connToSQL);
		} 
		else if(((msg.getMessage()).equals("closetReturnDate")))
		{
			return closetReturnDate(msg, connToSQL);
		} 
		else if(((msg.getMessage()).equals("checkIfUserGotAlreadyCopy")))
		{
			return checkIfUserGotAlreadyCopy(msg, connToSQL);
		} 
		else if(((msg.getMessage()).equals("showCopyInfo")))
		{
			return getCopyInfo(msg, connToSQL);
		} 
		else
		{
			return null; 
		}
	}

	/**
	 * This function return an object with with the info of the copy that it gets (if its exist)
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	private static ObjectMessage getCopyInfo(ObjectMessage msg, Connection connToSQL) 
	{
		ObjectMessage answer = new ObjectMessage(); 
		Copy copy=(Copy) msg.getObjectList().get(0);
		
		Book returnedBookInfo;
		ReaderAccount borrower = new ReaderAccount();
		int bookID;
		String borrowerId;
		
		PreparedStatement checkCopy=null;
		PreparedStatement getBorrower=null;
		ResultSet query1 = null;
		ResultSet query2 = null;
		
		try
		{
			//get the copy
			checkCopy= (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM copy WHERE copyId = ?");
			checkCopy.setInt(1,copy.getCopyID());
			query1 =checkCopy.executeQuery();
			
			if(query1.next())
			{
				//if the copy exist 
				bookID = query1.getInt(2); //get it's bookID
				borrowerId = query1.getString(3); //get it's borrowerID
				
				//get the book info
				Book book=new Book(bookID);
				ObjectMessage theBookOfTheCopy= new ObjectMessage(book,"showBookInfo","Book"); 
				ObjectMessage returnedAnswer = ABookDBController.selection(theBookOfTheCopy, connToSQL);
				returnedBookInfo = (Book)returnedAnswer.getObjectList().get(0);
				
				answer.addObject(returnedBookInfo);
				
				//get the borrower
				getBorrower= (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM ReaderAccount WHERE ID = ?");
				getBorrower.setString(1,borrowerId);
				query2 =getBorrower.executeQuery();
				
				if(query2.next())
				{
					borrower.setFirstName(query2.getString(2));
					borrower.setLastName(query2.getString(3));
					
					answer.addObject(borrower); 
					answer.setNote("copy found and borrowed");
				}
				else
				{
					answer.setNote("copy found but NOT borrowed");
				}
				
			}
			else
			{
				answer.setNote("copy not exist"); 
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return answer;
	}

	/**
	 * This function check if the book is exist in DB and if desired or not 
	 * @param msg -the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client if book `Desire book` ,`Not Desire book` or `Not exist book`
	 */
	private static ObjectMessage checkReturnDate(ObjectMessage msg, Connection connToSQL) 
	{
		Copy copy=(Copy)msg.getObjectList().get(0);

		try 
		{
			//get the copies that the reader account want to borrow
			PreparedStatement getCopy = connToSQL.prepareStatement("SELECT * FROM Copy WHERE copyId = ? ");
			System.out.println();
			getCopy.setInt(1, copy.getCopyID()); 
			ResultSet rs1 = getCopy.executeQuery();

			if(rs1.next())//if there is copy with this id exist in DB
			{
				//get book id
				PreparedStatement getBook = connToSQL.prepareStatement("SELECT * FROM Book WHERE bookId = ? ");
				getBook.setInt(1, rs1.getInt(2));
				ResultSet rs2 = getBook.executeQuery();

				rs2.next();
				if(rs2.getBoolean(6))
				{
					return new ObjectMessage("Desire book");
				}
				else 
				{
					return new ObjectMessage("Not Desire book");
				}
			}
			else
			{
				return new ObjectMessage("Not exist book");
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return new ObjectMessage("Not exist book");
	}


	/**
	 * This function make the change of the 'delay borrowed book' in the DB
	 * and sends back to the client the new date
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	private static ObjectMessage askForDelay(ObjectMessage msg, Connection connToSQL) 
	{
		ObjectMessage answer = new ObjectMessage("Delayed"); 
		Copy copy=(Copy) msg.getObjectList().get(0);
		PreparedStatement ps;
		ResultSet rs;
		PreparedStatement setDate = null; 

		try 
		{			
			LocalDate nowPlus14 = LocalDate.now().plusDays(14);
			Date nowPlus14Date = java.sql.Date.valueOf(nowPlus14); 

			setDate = connToSQL.prepareStatement("UPDATE Copy "+"SET returnDate = ? WHERE copyId = ?");
			setDate.setDate(1, (java.sql.Date) nowPlus14Date ); 
			setDate.setInt(2, copy.getCopyID() ); 
			setDate.executeUpdate(); 
			
			
			//send mail by ziv
			
			ps = connToSQL.prepareStatement("SELECT bookId FROM copy WHERE copyId = ?");
			ps.setInt(1, copy.getCopyID() ); 
			rs=ps.executeQuery();
			rs.next();
			
			Book book=new Book();
			book.setBookID(rs.getInt(1));
			ObjectMessage askBookDetails=new ObjectMessage(book,"searchBookID","Book");
			Book bookDetails = (Book) (ABookDBController.selection(askBookDetails, connToSQL)).getObjectList().get(0);
			
			ObjectMessage notify=new ObjectMessage("sendMail","Daily");
			Mail mail=new Mail();
			mail.setTo("obllibrarians@gmail.com");
			String body="Hello librarians, "+"\nWe want to notfiy you that the user id "+copy.getBorrowerID()
					+ " have delayed his book "+bookDetails.getBookName()+ "."
					+"\n 		Thank you , Ort Braude Library";
			mail.setBody(body);
			String subject="User "+copy.getBorrowerID()+" delayed his book "+bookDetails.getBookName();
			mail.setSubject(subject);
			notify.addObject(mail);
			ADailyDBController.selection(notify, connToSQL);
		}

		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
		LocalDateTime now = LocalDateTime.now();  
		LocalDateTime newDate = LocalDateTime.now().plusDays(14); 

		answer.setNote(dtf.format(newDate));

		//add `delayed book` to HISTORY
				LocalDate now1 = LocalDate.now(); 
				Date today = java.sql.Date.valueOf(now1);
				History sendObject =new History( copy.getBorrowerID(),"Delayed borrow book",copy.getBookID(),copy.getCopyID(),(java.sql.Date) today);
				AHistoryDBController.enterActionToHistory(sendObject, connToSQL);

		return answer;

	}

	/**
	 * This function makes the 'return book' and all the actions that might happen after it
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	private static ObjectMessage tryToReturnBook(ObjectMessage msg, Connection connToSQL) 
	{

		ObjectMessage answer=new ObjectMessage();
		answer.setMessage(""); 
		Date borrowDate;

		PreparedStatement checkCopy=null;
		PreparedStatement updateCopy=null;
		PreparedStatement checkReaderAccountDelays=null;
		PreparedStatement updateReaderAccount=null;

		ResultSet query1 = null;
		ResultSet query2 = null;
		ResultSet query3=null;
		int numOfDelay;
		Book askedbook=new Book();
		Copy tempCopy=(Copy)msg.getObjectList().get(0);
		try
		{

			checkCopy= (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM copy WHERE copyId = ? AND borrowerId IS NOT NULL");
			checkCopy.setInt(1,tempCopy.getCopyID());
			query1 =checkCopy.executeQuery();
			Boolean temp=query1.next();
			if(temp == false)
			{
				answer.setMessage("The copyID is wrong OR not borrowed");
				answer.setNote("Unsuccessful ReturnCopy"); 
			}
			else
			{
				borrowDate = query1.getDate(5);  
				checkCopy=(PreparedStatement) connToSQL.prepareStatement("SELECT * FROM copy WHERE copyId = ?");
				checkCopy.setInt(1,tempCopy.getCopyID());
				query2=checkCopy.executeQuery();
				query2.next();
				askedbook.setBookID(query2.getInt(2));
				String id=query2.getString(3);
				
				
				updateCopy = connToSQL.prepareStatement("UPDATE `copy` SET `borrowerId`=NULL ,`borrowDate`=NULL ,`returnDate`=NULL WHERE copyId=?");
				updateCopy.setInt(1,tempCopy.getCopyID());
				updateCopy.executeUpdate();
				answer.setNote("successful ReturnCopy");
				answer.addObject(msg.getObjectList().get(0));

				//get number of days that book was in `borrowing`
				LocalDate now = LocalDate.now(); 
				Date today1 = java.sql.Date.valueOf(now);
				long diff = Math.abs(today1.getTime() -((Date)query1.getDate(4)).getTime());
				long dif2 = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

				// send to  HISTORY
				try 
				{
					History sendObject = new History(query2.getString(3),"Return book",Integer.parseInt(query2.getString(2)),tempCopy.getCopyID(),today1,Long.toString(dif2));
					AHistoryDBController.enterActionToHistory(sendObject, connToSQL);
				} 
				catch (NumberFormatException e) 
				{
					e.printStackTrace();
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				
				
				//update the status of the reader if it's relevant 
				checkReaderAccountDelays=(PreparedStatement) connToSQL.prepareStatement("SELECT * FROM readeraccount WHERE ID = ?");
				checkReaderAccountDelays.setString(1,id);
				query3=checkReaderAccountDelays.executeQuery();

				query3.next();
				numOfDelay=query3.getInt(9);
				String status=query3.getString(8);
				if(numOfDelay<3 && status.equals("Frozen"))
				{
					//check if he returned all the books that were in late
					boolean returnedAllTheBooks = true;
					PreparedStatement booksWithLates = null; 
					PreparedStatement getReturnDate = null;
					ResultSet rs1 = null;
					ResultSet rs2 = null;
					
					try 
					{
						//get all the 'late returns' of that reader account
						booksWithLates = connToSQL.prepareStatement("SELECT * FROM history WHERE readerAccountID = ? AND action = ? ");
						booksWithLates.setString(1, id); 
						booksWithLates.setString(2, "Late in Return");  
						rs1 =booksWithLates.executeQuery();

						while(rs1.next())
						{ 
							int copyID = rs1.getInt(4);
							Date startOfLate = rs1.getDate(6);
							Date returnDate = null;

							//find the return date for this book
							getReturnDate = connToSQL.prepareStatement("select * from history where `date` > ? and `copyid` = ? and `readerAccountID` = ? and `action` = ? order by `date` ");
							getReturnDate.setDate(1, (java.sql.Date) startOfLate);
							getReturnDate.setInt(2, copyID); 
							getReturnDate.setInt(3, Integer.parseInt(id)); 
							getReturnDate.setString(4,"Return book");  
							rs2 =getReturnDate.executeQuery();

							//if there is a result- the reader account return that book
							if(rs2.next())
							{
								returnDate = rs2.getDate(6);
							}
							
							if(null == returnDate)
							{
								returnedAllTheBooks = false;
							}

						}

					}
					catch (SQLException e)
					{
						e.printStackTrace();

					}
					
					if(returnedAllTheBooks)
					{
						//update to active
						ObjectMessage obj0 = new ObjectMessage();
						obj0.setMessage("ChangeStatus");
						ReaderAccount reader = new ReaderAccount();
						reader.setId(id);
						reader.setStatus("Active");
						obj0.addObject(reader);
						AReaderAccountDBController.selection(obj0, connToSQL);
						answer.setMessage("successful ReturnCopy.\nThe status of the reader account changed back to 'Active'"); 
					}
				}
				
				
				
				//send Mail if book have reservation
				ObjectMessage askTheFirstReader=new ObjectMessage();
				askTheFirstReader.setMessage("getReaderThatCanImplement");
				askTheFirstReader.addObject(askedbook);
				ObjectMessage result=AReservationDBController.selection(askTheFirstReader,connToSQL);
				if(result.getNote().equals("Found"))
				{
					askTheFirstReader=new ObjectMessage(result.getObjectList().get(0),"SearchReader","ReaderAccount");
					ReaderAccount readerAccount =(ReaderAccount) (AReaderAccountDBController.selection(askTheFirstReader, connToSQL)).getObjectList().get(0);
					ObjectMessage bookDetails=new ObjectMessage(askedbook,"searchBookID","Book");
					Book book = (Book) (ABookDBController.selection(bookDetails, connToSQL)).getObjectList().get(0);
					ObjectMessage implementReservation=new ObjectMessage();
					implementReservation.setMessage("letImplementReservation");
					implementReservation.addObject(readerAccount, book);
					AReservationDBController.selection(implementReservation,connToSQL);
				}


			}

		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return new ObjectMessage("Unexpected Error.","Unsucessfull");		
		}	

		return answer;
	}

	/**
	 * This function check if all the copies of specific book are borrowed
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	private static ObjectMessage checkIfAllBorrowed(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		ObjectMessage answer;
		Copy askedBook=(Copy)msg.getObjectList().get(0);
		try 
		{
			ps = connToSQL.prepareStatement("SELECT COUNT(*) FROM obl.copy WHERE bookId=? AND borrowerId IS NULL");
			ps.setInt(1,askedBook.getBookID());
			ResultSet rs = ps.executeQuery();
			rs.next();
			int x =rs.getInt(1);
			if(rs.getInt(1)!= 0)
			{
				return new ObjectMessage("checkIfAllBorrowed","FoundBook");
			}
			else
			{
				return new ObjectMessage("checkIfAllBorrowed","NoFoundBook");
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This function will return all the borrows of specific reader account
	 * also it will provide whether the reader account can ask for delay for each one of his borrows
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */ 
	public static ObjectMessage getBorrows(ObjectMessage msg, Connection connToSQL)
	{
		ObjectMessage answer = null; 
		ReaderAccount reader=(ReaderAccount) msg.getObjectList().get(0);
		boolean resultExist = false;
		boolean canDelay = false;

		PreparedStatement getCopies = null; 
		PreparedStatement getBook = null;
		PreparedStatement getReaderAccount = null;
		PreparedStatement getReservs = null;
		ResultSet rs1 = null; 
		ResultSet rs2 = null; 
		ResultSet rs3 = null;
		ResultSet rs4 = null; 

		try 
		{
			//get the copies that the reader account borrowed 
			getCopies = connToSQL.prepareStatement("SELECT * FROM Copy WHERE borrowerId = ? ");
			getCopies.setString(1, reader.getId() ); 
			rs1 =getCopies.executeQuery();

			ArrayList <IEntity[]> result=new ArrayList<IEntity[]>(); 

			//go by all the copies the reader account borrowing and get the book of each one
			while(rs1.next())
			{
				resultExist = true;

				IEntity []CopyAndBook = new IEntity[2]; //CopyAndBook[0]->the Copy info , CopyAndBook[1]->the Book info

				//set the copy info from the first query
				CopyAndBook[0]=(new Copy(rs1.getInt(1), rs1.getInt(2), rs1.getString(3), rs1.getString(4), rs1.getString(5)));

				int bookId = rs1.getInt(2); //the bookID of the current copy

				//get the book of that copy
				getBook = connToSQL.prepareStatement("SELECT * FROM Book WHERE bookId = ? ");
				getBook.setInt(1, bookId ); 
				rs2 =getBook.executeQuery();
				if(rs2.next())
				{
					//set the book info from the second query
					CopyAndBook[1]=( new Book(rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getString(6), rs2.getInt(7)) );
				}


				//////////////////////////////////////////////////////////
				//check if the reader account can ask for delay this book
				//////////////////////////////////////////////////////////

				String reason=" ";

				//check if the return date is the most updated 20    28
				LocalDate nowPlus7 = LocalDate.now().plusDays(7);
				Date nowPlus7Date = java.sql.Date.valueOf(nowPlus7);
				Date dateOfReturn = rs1.getDate(5); 
				if(dateOfReturn.after(nowPlus7Date))
				{
					canDelay = false;
					reason = "The date of return is the most updated";
				}
				else
				{
					//check if the book is desired
					if(rs2.getBoolean(6))
					{
						canDelay = false;
						reason = "That book is desired and can't be delayed";
					}
					else
					{

						//check if it reserved by someone
						getReservs = connToSQL.prepareStatement("SELECT * FROM Reservations WHERE bookId = ? ");
						getReservs.setInt(1, rs1.getInt(2) ); 
						rs4 =getReservs.executeQuery();

						if(rs4.next())
						{
							canDelay = false;
							reason = "That book was reserved by someone.\nSo you can't delay it's return date";
						}
						else
						{
							//check if he is not already in delay
							
							//Date dateOfReturn =  rs1.getDate(5);
							//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
							//LocalDateTime now = LocalDateTime.now();

							LocalDate now = LocalDate.now(); 
							Date today = java.sql.Date.valueOf(now);

							if( today.after(dateOfReturn) )
							{
								canDelay = false;
								reason = "You already was late in returning that book.\nSo you can't delay it's return date";
							}
							else
							{
								//check if reader account is active
								getReaderAccount = connToSQL.prepareStatement("SELECT * FROM ReaderAccount WHERE ID = ? ");
								getReaderAccount.setString(1, reader.getId() ); 
								rs3 =getReaderAccount.executeQuery();

								if(rs3.next())
								{
									//if the reader account is active
									if(!rs3.getString(8).equals("Active"))
									{
										canDelay = false;
										reason = "Your status is not Active.\nSo you can't delay it's return date";
									}
									else
									{
										canDelay = true;
									}
								}
							}	
						}

					}
				}

				((Copy)(CopyAndBook[0])).setReasonForCantDelay(reason);

				if(canDelay)
				{
					((Copy)(CopyAndBook[0])).setCanDelay(true);
					//answer.setExtra("canDelay");
				}
				else
				{
					((Copy)(CopyAndBook[0])).setCanDelay(false); 
					//answer.setExtra("canNotDelay");
				}

				result.add(CopyAndBook);
			}

			if(resultExist)
			{
				answer = new ObjectMessage(result,"TheBorrows");
			}
			else
			{
				answer = new ObjectMessage(result,"NoBorrows");
			}

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}


		return answer;
	}
	

	/**
	 * This function will delete the requested copy if it possible
	 * The function checks if the copy is not borrowed and if there is a reservation to the fitting book and the requested copy is the last in the library->can not delete the copy
	 * @param msg - the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return ObjectMessage with the answer to the client
	 */
	private static ObjectMessage deleteBook(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		PreparedStatement getBook = null;
		PreparedStatement getNumOfCopies = null;
		PreparedStatement checksReservations=null;
		ObjectMessage answer;
		ResultSet rs2 = null; 
		ResultSet rs3 = null;
		Copy askedCopy=(Copy)msg.getObjectList().get(0);
		int countNumOfCopies = 0;
		


		try 
		{
			
			//checks if the copy is exist
			getBook = connToSQL.prepareStatement("SELECT * FROM obl.copy WHERE copyId = ? ");
			getBook.setInt(1, askedCopy.getCopyID()); 
			rs2 =getBook.executeQuery();
			if(!rs2.next())
			{
				answer= new ObjectMessage("The copy is not exist in obl,you can not delete it","Unsucessfull");
			}

			else
			{
				
				//the requested copy is exist
				//checks if the requested copy is not borrowed
				String borrowerId=rs2.getString(3);
				if(borrowerId!=NULL && !msg.getExtra().equals("after book lost"))
				{
					answer= new ObjectMessage("The copy is borrowed,you can not delete it","Unsucessfull");
				}
				else
				{
					int bookOfCopyID=rs2.getInt(2);
					
					//there is one or more reservation
					getNumOfCopies = connToSQL.prepareStatement("SELECT COUNT(*) FROM copy WHERE bookID=? ");
					getNumOfCopies.setInt(1, bookOfCopyID); 
					rs3 =getNumOfCopies.executeQuery();
					rs3.next();
					countNumOfCopies=rs3.getInt(1);
					
					//the requested copy is not borrowed
					//check if there is reservations to the fitting book
					checksReservations = connToSQL.prepareStatement("SELECT * FROM obl.reservations WHERE bookId = ? ");
					checksReservations.setInt(1, bookOfCopyID);
					rs2 =checksReservations.executeQuery();
					if(rs2.next())
					{
						if(countNumOfCopies==1 && !msg.getExtra().equals("after book lost"))
						{
							answer= new ObjectMessage("There is a reservation to this book and this is the last copy, you can not delete it","Unsucessfull");
							return answer;
						}
					}
					
					//For deleting the file
					//get name ob book for delete pdf
					PreparedStatement forBookName = (PreparedStatement) connToSQL.prepareStatement("SELECT * FROM Book WHERE bookId = ? ");
					ResultSet rsBookName = null;
					forBookName.setInt(1, bookOfCopyID);
					rsBookName=forBookName.executeQuery();
					rsBookName.next();
					String bookName=rsBookName.getString(2)+" "+rsBookName.getString(3)+" "+rsBookName.getString(4)+" "+rsBookName.getString(7);
					
					
					//if there is no reservation to this book or if there is a reservation but there is more than one copy
					//get number of copies of this bookID
					/*getNumOfCopies = connToSQL.prepareStatement("SELECT COUNT(*) FROM copy WHERE bookID=? ");
					getNumOfCopies.setInt(1, bookOfCopyID); 
					rs3 =getNumOfCopies.executeQuery();*/
					
					//countNumOfCopies=rs3.getInt(1);
	
					//delete the copy
					ps = connToSQL.prepareStatement("DELETE copy FROM obl.copy WHERE copyId=?");
					ps.setInt(1,askedCopy.getCopyID());
					ps.executeUpdate();
					countNumOfCopies--;
	
	
					//there is no copies to the book
					if(countNumOfCopies==0)
					{
						try 
						{
							if(msg.getExtra().equals("after book lost"))
							{
								ObjectMessage objectMessage=new ObjectMessage();
								Book book=new Book();
								book.setBookID(bookOfCopyID);
								objectMessage.setMessage("deleteAllReservations");
								objectMessage.addObject(book);
								AReservationDBController.selection(objectMessage, connToSQL);
							}
							
							ps = connToSQL.prepareStatement("DELETE book FROM obl.book WHERE bookId=?");
							ps.setInt(1,bookOfCopyID);
							ps.executeUpdate();
							

							
							//delete pdf file of book
							//File myFile = new File("pdfFiles\\"+bookName+".pdf");
							//myFile.delete();  // TODO:NEED TO UNCOMMENT
						}
						catch (SQLException e) 
						{
							e.printStackTrace();
							answer= new ObjectMessage("Unexpected Error.","Unsucessfull");
						}
					}
					
					//add to history of the reader account if he lost the book
					if(!msg.getExtra().equals("") && null != msg.getExtra())
					{
						if(msg.getExtra().equals("after book lost"))
						{
							// `lose book` send to history
							LocalDate now1 = LocalDate.now(); 
							Date today = java.sql.Date.valueOf(now1);
							History sendObject =new History(askedCopy.getBorrowerID(),"Lose book",bookOfCopyID,askedCopy.getCopyID(),(java.sql.Date) today);
							AHistoryDBController.enterActionToHistory(sendObject, connToSQL);
							
						}
					}
					answer=new ObjectMessage("This Book was successfully deleted ","Successfull");
				}
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			answer=new ObjectMessage("Unexpected Error.","Unsucessfull");
		}

		return answer;
	}
	
	

	/**
	 * This function check if specifically copy is exist and available for borrow and enter all data of borrow in `obl` DB table 
	 * @param msg- the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return String with result to function that called it
	 */
	public static String checkIfBookIsAvailableForBorrow(ObjectMessage msg, Connection connToSQL) 
	{
		ReaderAccount reader=(ReaderAccount)msg.getObjectList().get(0);
		Copy copy=(Copy)msg.getObjectList().get(1);

		LocalDate now = LocalDate.now();  
		LocalDate desireDate = LocalDate.now().plusDays(3);  
		LocalDate notDesireDate = LocalDate.now().plusDays(14);

		Date today = java.sql.Date.valueOf(now);
		Date todayPlus3 = java.sql.Date.valueOf(desireDate);
		Date todayPlus7 = java.sql.Date.valueOf(notDesireDate);


		try 
		{
			//get the copies that the reader account want to borrow
			PreparedStatement getCopy = connToSQL.prepareStatement("SELECT * FROM Copy WHERE copyId = ? ");
			getCopy.setInt(1, copy.getCopyID()); 
			ResultSet rs1 = getCopy.executeQuery();

			if(rs1.next())//if there is copy with this id exist in DB
			{
				if(rs1.getString(3)==null)
				{  
					//get book id
					PreparedStatement getBook = connToSQL.prepareStatement("SELECT * FROM Book WHERE bookId = ? ");
					getBook.setInt(1, rs1.getInt(2));
					ResultSet rs2 = getBook.executeQuery();

					//set borrower id to the table copies
					PreparedStatement setBorroweID =connToSQL.prepareStatement("UPDATE `obl`.`copy` SET `borrowerId`=?  WHERE `copyId` = ?");	
					setBorroweID.setString(1, reader.getId());
					setBorroweID.setInt(2,copy.getCopyID()); 

					setBorroweID.executeUpdate();
					rs2.next();

					if(!rs2.getBoolean(6)) //check if desire or not
					{
						PreparedStatement setReturnDay =connToSQL.prepareStatement("UPDATE copy "+"SET borrowDate = ? , returnDate = ? WHERE copyId = ?");

						setReturnDay.setDate(1, (java.sql.Date) today);
						setReturnDay.setDate(2,(java.sql.Date) todayPlus7);
						setReturnDay.setInt(3, copy.getCopyID());
						setReturnDay.executeUpdate();

						// send to history
						History sendObject =new History(reader.getId(),"Borrow book",rs1.getInt(2),copy.getCopyID(),(java.sql.Date) today);
						AHistoryDBController.enterActionToHistory(sendObject, connToSQL);

						return "NotDesired";// Success borrowed not desired book
					}
					else
					{
						PreparedStatement setReturnDay =connToSQL.prepareStatement("UPDATE copy "+"SET borrowDate = ? , returnDate = ? WHERE copyId = ?");
						setReturnDay.setDate(1, (java.sql.Date) today);
						setReturnDay.setDate(2,(java.sql.Date) todayPlus3);
						setReturnDay.setInt(3, copy.getCopyID());
						setReturnDay.executeUpdate();

						// send to  history
						History sendObject =new History(reader.getId(),"Borrow book",rs1.getInt(2),copy.getCopyID(),(java.sql.Date) today);
						AHistoryDBController.enterActionToHistory(sendObject, connToSQL);
						return "Desired";// Success borrowed desired book
					}		
				}
				else //this copy was already borrowed
				{
					return "CopyAlreadyBorrowed";
				}
			}
			else //this copy not exist in obl DB
			{
				return "CopyNotExist";
			}

		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			new ObjectMessage("Unexpected Error.","Unsucessfull");
		}
		return "CopyNotExist";

	}

	private static ObjectMessage closetReturnDate(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		Copy askedBook=(Copy)msg.getObjectList().get(0);
		try 
		{
			ps = connToSQL.prepareStatement("SELECT * FROM obl.copy WHERE bookId=? order by returnDate");
			ps.setInt(1,askedBook.getBookID());
			ResultSet rs = ps.executeQuery();
			rs.next();
			Copy askedDate=new Copy();
			if(rs.getDate(5).equals(null))
			{
				return new ObjectMessage("closetReturnDate","HaveAvaiableBook");
			}
			else
			{
				askedDate.setReturnDate(rs.getDate(5).toString());
				return new ObjectMessage(askedDate,"closetReturnDate","ReturnTheCloset");
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This function check if the readerAccount have already copy of spesifec book
	 * @param msg- the object from the client
	 * @param connToSQL - the connection to the MySQL created in the Class OBLServer
	 * @return String with result to function that called it
	 */
	private static ObjectMessage checkIfUserGotAlreadyCopy(ObjectMessage msg, Connection connToSQL)
	{
		PreparedStatement ps;
		ObjectMessage answer;
		Book askedBook=(Book)msg.getObjectList().get(0);
		ReaderAccount readerAccount=(ReaderAccount)msg.getObjectList().get(1);
		try 
		{
			ps = connToSQL.prepareStatement("SELECT COUNT(*) FROM obl.copy WHERE bookId= ? AND borrowerId= ?");
			ps.setInt(1,askedBook.getBookID());
			ps.setString(2,readerAccount.getId());
			ResultSet rs = ps.executeQuery();
			rs.next();
			int x =rs.getInt(1);
			if(rs.getInt(1)== 0)
			{
				return new ObjectMessage("checkIfUserGotAlreadyCopy","DontHaveCopy");
			}
			else
			{
				return new ObjectMessage("checkIfUserGotAlreadyCopy","HaveCopy");
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	

}
