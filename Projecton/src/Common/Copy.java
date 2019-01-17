package Common;

import java.util.Date;

import javafx.scene.control.Button;

public class Copy implements IEntity  
{
	private int copyID;
	private int bookID;
	private String borrowerID;
	
	private Date borrowDate;
	private Date returnDate;
	
	private Button askForDelay;
	
	
	public Copy(int copyID,int bookID,String borrowerID)
	{
		this.copyID=copyID;
		this.bookID=bookID;
		this.borrowerID=borrowerID;
	}
	
	


	public Button getAskForDelay() {
		return askForDelay;
	}




	public void setAskForDelay(Button askForDelay) {
		this.askForDelay = askForDelay;
	}




	public Copy(int copyID, int bookID, String borrowerID, Date borrowDate, Date returnDate) 
	{
		this.copyID = copyID;
		this.bookID = bookID;
		this.borrowerID = borrowerID;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
	}




	public int getCopyID() {
		return copyID;
	}


	public void setCopyID(int copyID) {
		this.copyID = copyID;
	}


	public int getBookID() {
		return bookID;
	}


	public void setBookID(int bookID) {
		this.bookID = bookID;
	}




	public String getBorrowerID() {
		return borrowerID;
	}




	public void setBorrowerID(String borrowerID) {
		this.borrowerID = borrowerID;
	}




	public Date getBorrowDate() {
		return borrowDate;
	}




	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}




	public Date getReturnDate() {
		return returnDate;
	}




	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	
	
	
}
