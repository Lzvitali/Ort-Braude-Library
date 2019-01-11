package Common;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javafx.scene.control.Button;

public class Book implements IEntity, Serializable
{
	private int bookID;
	private String bookName;
	private String authorName;
	private int year;
	private String topic;
	private boolean isDesired; 
	//buttons
	private transient Button reserve;
	private transient Button details;
	static int nextRow=58;
	
//	public Book(int bookID, String bookName, String authorName, Date dateOfBook, String topic, boolean isDesired) 
//	{
//		super();
//		this.bookID =(int) bookID;
//		this.bookName = bookName;
//		this.authorName = authorName;
//		this.dateOfBook =(Date) dateOfBook;
//		this.topic = topic;
//		this.isDesired = (Boolean)isDesired;
//		Button reserve = new Button();
//		Button details= new Button();
//	}
	  
	

	public Book(String bookName)
	{
		this.bookName = bookName;
	}

	public Book()
	{
			this.bookName = null;
			this.authorName = null;
			this.topic = null;
			this.isDesired = false;
			this.year=-1;
			Button reserve = null;
			Button details= null;
	}

	public Book( String bookName, String authorName, String year, String topic, String isDesired) 
	{
		super();
	//	this.bookID =Integer.parseInt(bookID);
		this.bookName = bookName;
		this.authorName = authorName;
		this.topic = topic;
		this.isDesired =  Boolean.parseBoolean(isDesired);
		this.year=Integer.parseInt(year);
		Button reserve = new Button();
		//reserve.setText("Reservation");
		Button details= new Button();
	}
	
	
	public Book( String bookName, String authorName, int year, String topic, Boolean isDesired) 
	{
		super();
		this.bookName = bookName;
		this.authorName = authorName;
		this.topic = topic;
		this.isDesired = isDesired;
		this.year=year;
		Button reserve = new Button();
		Button details= new Button();
	}


	public String getBookName() 
	{
		return bookName;
	}

	@Override
	public String toString()
	{
		return "Book [bookID=" + bookID + ", bookName=" + bookName + ", authorName=" + authorName + ", dateOfBook="
				+ year + ", topic=" + topic + ", isDesired=" + isDesired + ", reserve=" + reserve + ", details="
				+ details + "]";
	}


	public int getBookID()
	{
		return bookID;
	}

	public void setBookID(int bookID) 
	{
		this.bookID = bookID;
	}

	public String getAuthorName() 
	{
		return authorName;
	}


	public void setAuthorName(String authorName)
	{
		this.authorName = authorName;
	}


	public int getDateOfBook() 
	{
		return year;
	}


	public void setDateOfBook(int year) 
	{
		this.year= year;
	}



	public String getTopic() 
	{
		return topic;
	}


	public void setTopic(String topic) 
	{
		this.topic = topic;
	}


	public boolean isDesired() 
	{
		return isDesired;
	}


	public void setDesired(boolean isDesired) 
	{
		this.isDesired = isDesired;
	}


	public Button getReserve()
	{
		return reserve;
	}


	public void setReserve(Button reserve) 
	{
		this.reserve = reserve;
	}


	public Button getDetails()
	{
		return details;
	}


	public void setDetails(Button details)
	{
		this.details = details;
	}


	public void setBookName(String bookName) 
	{
		this.bookName = bookName;
	}

	public static int getNextRow() {
		return nextRow;
	}


	public static void setNextRow(int nextRow) {
		Book.nextRow = nextRow;
	}

}
