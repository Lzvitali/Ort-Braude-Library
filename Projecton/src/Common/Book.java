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
	private Integer year;
	private Integer edition;
	private String topic;
	private Boolean isDesired; 
	private String bookLocation;

	private Integer numberOfCopies;//only for add book(if we want in same time to add number of copies)
	private boolean fileIsLoaded;
	private boolean isReserved;
	private Boolean inLibrary; 
	
	//buttons
	private transient Button reserve;
	private transient Button details;
	
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
	
	public Book(int idBook)
	{
		this.bookID = idBook;
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

	public Book(String bookName, String authorName, String year, String topic, String isDesired,String edition,String numberOfCopies) 
	{
		super();
	//	this.bookID =Integer.parseInt(bookID);
		this.bookName = bookName;
		this.authorName = authorName;
		this.topic = topic;
		this.isDesired =  Boolean.parseBoolean(isDesired);
		this.year=Integer.parseInt(year);
		this.edition=Integer.parseInt(edition);
		this.numberOfCopies=Integer.parseInt(numberOfCopies);
		if(this.numberOfCopies>0)
			inLibrary=true;
		else
			inLibrary=false;
		Button reserve = new Button();
		//reserve.setText("Reservation");
		Button details= new Button();
	}
	
	public Book(String bookName, String authorName, String year, String topic, String isDesired,String edition,String numberOfCopies, String bookLocation) 
	{
		super();
	//	this.bookID =Integer.parseInt(bookID);
		this.bookName = bookName;
		this.authorName = authorName;
		this.topic = topic;
		this.isDesired =  Boolean.parseBoolean(isDesired);
		this.year=Integer.parseInt(year);
		this.edition=Integer.parseInt(edition);
		this.numberOfCopies=Integer.parseInt(numberOfCopies);
		if(this.numberOfCopies>0)
			inLibrary=true;
		else
			inLibrary=false;
		Button reserve = new Button();
		//reserve.setText("Reservation");
		Button details= new Button();
		this.bookLocation = bookLocation;
	}
	
	
	public Book( String bookID,String bookName, String authorName, int year, String topic, Boolean isDesired,int edition,String bookLocation) 
	{
		super();
		this.bookID =Integer.parseInt(bookID);
		this.bookName = bookName;
		this.authorName = authorName;
		this.topic = topic;
		this.isDesired = isDesired;
		this.year=year;
		this.edition=edition;
		this.bookLocation=bookLocation;
		Button reserve = null;
		Button details= null;
	}

	
	

	public Book(String bookName, String authorName, String year, String topic, String isDesired, int edition) 
	{
		this.bookName = bookName;
		this.authorName = authorName;
		this.year = Integer.parseInt(year);
		this.topic = topic;
		this.isDesired =  Boolean.parseBoolean(isDesired);
		this.edition = edition;
	}

	
	
	public Book(String bookName, String authorName, Integer year, Integer edition)
	{
		this.bookName = bookName;
		this.authorName = authorName;
		this.year = year;
		this.edition = edition;
	}

	public String getBookName() 
	{
		return bookName;
	}

	
	public int getBookID()
	{
		return bookID;
	}
	
	

	public boolean isFileIsLoaded()
	{
		return fileIsLoaded;
	}

	public void setFileIsLoaded(boolean fileIsLoaded) 
	{
		this.fileIsLoaded = fileIsLoaded;
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


	public Boolean isDesired() 
	{
		return isDesired;
	}


	public void setDesired(Boolean isDesired) 
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
		this.reserve.setAccessibleText(String.valueOf(bookID));
	}


	public Button getDetails()
	{
		return details;
	}


	public void setDetails(Button details)
	{
		this.details = details;
	}


	public Boolean getInLibrary() {
		return inLibrary;
	}

	public void setInLibrary(Boolean inLibrary) {
		this.inLibrary = inLibrary;
	}

	public void setBookName(String bookName) 
	{
		this.bookName = bookName;
	}

	


	public Integer getYear() 
	{
		return year;
	}

	public void setYear(Integer year) 
	{
		this.year = year;
	}

	public Integer getNumberOfCopies()
	{
		return numberOfCopies;
	}
	public Integer getEdition()
	{
		return edition;
	}

	public void setNumberOfCopies(Integer numberOfCopies) 
	{
		this.numberOfCopies = numberOfCopies;
		if(this.numberOfCopies>0)
			inLibrary=true;
		else
			inLibrary=false;
	}
	
	public void setEdition(Integer edition)
	{
		this.edition = edition;
	}

	public Boolean getIsDesired() 
	{
		return isDesired;
	}

	public void setIsDesired(Boolean isDesired)
	{
		this.isDesired = isDesired;
	}

	public boolean isReserved()
	{
		return isReserved;
	}

	public void setReserved(boolean isReserved)
	{
		this.isReserved = isReserved;
	}

	
	public String getBookLocation()
	{
		return bookLocation;
	}

	public void setBookLocation(String bookLocation)
	{
		this.bookLocation = bookLocation;
	}
	@Override
	public String toString() {
		return "Book [bookID=" + bookID + ", bookName=" + bookName + ", authorName=" + authorName + ", year=" + year
				+ ", edition=" + edition + ", topic=" + topic + ", isDesired=" + isDesired + ", reserve=" + reserve
				+ ", details=" + details + "]";
	}
	

}
