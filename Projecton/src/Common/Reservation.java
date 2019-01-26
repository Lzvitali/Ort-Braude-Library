package Common;

import java.io.Serializable;
import java.util.Date;

import javafx.scene.control.Button;

public class Reservation implements IEntity , Serializable 
{
	private int bookID;
	private String readerAccountID;
	private String date;
	
	private String bookName;
	private String authorName;
	private Integer year;
	private String topic;
	private Boolean isDesired;
	private Integer edition;
	
	private transient Button reservationTableBtn;

	
	public Reservation( String date, String bookName, String authorName, String year, 
			String topic, String isDesired, String edition) 
	{
		//this.readerAccountID = readerAccountID;
		this.date = date;
		this.bookName = bookName;
		this.authorName = authorName;
		this.year=Integer.parseInt(year);
		this.edition=Integer.parseInt(edition);
		this.topic = topic;
		this.isDesired =  Boolean.parseBoolean(isDesired);
		//this.reservationTableBtn = new Button();
	}

	//with bookID for implement reservation 
	public Reservation( int bookID,String date, String bookName, String authorName, String year, 
			String topic, String isDesired, String edition) 
	{
		this.bookID = bookID;
		this.date = date;
		this.bookName = bookName;
		this.authorName = authorName;
		this.year=Integer.parseInt(year);
		this.edition=Integer.parseInt(edition);
		this.topic = topic;
		this.isDesired =  Boolean.parseBoolean(isDesired);
		//this.reservationTableBtn = new Button();
	}


	public Reservation() 
	{
		
	}

	public int getBookID() {
		return bookID;
	}


	/*public void setBookID(int bookID) {
		this.bookID = bookID;
	}*/


/*	public String getReaderAccountID() {
		return readerAccountID;
	}


	public void setReaderAccountID(String readerAccountID) {
		this.readerAccountID = readerAccountID;
	}
*/

	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getBookName() {
		return bookName;
	}


	public void setBookName(String bookName) {
		this.bookName = bookName;
	}


	public String getAuthorName() {
		return authorName;
	}


	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}


	public Integer getYear() {
		return year;
	}


	public void setYear(Integer year) {
		this.year = year;
	}


	public String getTopic() {
		return topic;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}


	public Boolean getIsDesired() {
		return isDesired;
	}


	public void setIsDesired(Boolean isDesired) {
		this.isDesired = isDesired;
	}


	public Integer getEdition() {
		return edition;
	}


	public void setBookID(int bookID) {
		this.bookID = bookID;
	}

	public void setEdition(Integer edition) {
		this.edition = edition;
	}


	public Button getReservationTableBtn() {
		return reservationTableBtn;
	}


	public void setReservationTableBtn(Button reservationTableBtn) {
		this.reservationTableBtn = reservationTableBtn;
	}

	public String getReaderAccountID() {
		return readerAccountID;
	}

	public void setReaderAccountID(String readerAccountID) {
		this.readerAccountID = readerAccountID;
	}



	
	
	
}
