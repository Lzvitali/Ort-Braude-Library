package Common;

import java.io.Serializable;

import javafx.scene.control.Button;

public class Borrow implements IEntity , Serializable 
{
	private String bookName;
	private String authorName;
	private Integer year;
	private Integer edition;
	private String topic;
	private Boolean isDesired; 
	
	private String borrowDate;
	private String returnDate;
	
	private transient Button askForDelay;
	private transient Button LostCopy;
	

	public Borrow(String bookName, String authorName, Integer year, Integer edition, String topic, Boolean isDesired,
			String borrowDate, String returnDate, Button askForDelay, Button lostCopy) 
	{
		super();
		this.bookName = bookName;
		this.authorName = authorName;
		this.year = year;
		this.edition = edition;
		this.topic = topic;
		this.isDesired = isDesired;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
		this.askForDelay = askForDelay;
		LostCopy = lostCopy;
	}


	public Borrow(String bookName, String authorName, Integer year, String topic,
			Boolean isDesired, Integer edition, String borrowDate, String returnDate, Button askForDelay) 
	{
		this.bookName = bookName;
		this.authorName = authorName;
		this.year = year;
		this.edition = edition;
		this.topic = topic;
		this.isDesired = isDesired;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
		this.askForDelay = askForDelay;
	}

	
	public Borrow(String bookName, String authorName, Integer year, String topic,
			Boolean isDesired, Integer edition) 
	{
		this.bookName = bookName;
		this.authorName = authorName;
		this.year = year;
		this.edition = edition;
		this.topic = topic;
		this.isDesired = isDesired;

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


	public Integer getEdition() {
		return edition;
	}


	public void setEdition(Integer edition) {
		this.edition = edition;
	}


	public String getTopic() {
		return topic;
	}


	public Button getLostCopy() {
		return LostCopy;
	}


	public void setLostCopy(Button lostCopy) {
		LostCopy = lostCopy;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}


	public Boolean isDesired() {
		return isDesired;
	}


	public void setIsDesired(Boolean isDesired) {
		this.isDesired = isDesired;
	}


	public String getBorrowDate() {
		return borrowDate;
	}


	public void setBorrowDate(String borrowDate) {
		this.borrowDate = borrowDate;
	}


	public String getReturnDate() {
		return returnDate;
	}


	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}


	public Button getAskForDelay() {
		return askForDelay;
	}


	public void setAskForDelay(Button askForDelay) {
		this.askForDelay = askForDelay;
	}


	@Override
	public String toString() {
		return "BorrowsTable [bookName=" + bookName + ", authorName=" + authorName + ", year=" + year + ", edition="
				+ edition + ", topic=" + topic + ", isDesired=" + isDesired + ", borrowDate=" + borrowDate
				+ ", returnDate=" + returnDate + ", askForDelay=" + askForDelay + "]";
	}
	
	

}
