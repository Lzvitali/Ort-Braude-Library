package Common;

import java.io.Serializable;
import java.util.Date;

import javafx.scene.control.Button;

public class Copy implements IEntity, Serializable  
{
	private int copyID;
	private int bookID;
	private String borrowerID;
	
	private String borrowDate;
	private String returnDate;
	
	private Button askForDelay;
	
	
	public Copy(int copyID,int bookID,String borrowerID)
	{
		this.copyID=copyID;
		this.bookID=bookID;
		this.borrowerID=borrowerID;
	}
	
	public Copy(int copyID)
	{
		this.copyID=copyID;
	}
	


	public Button getAskForDelay() {
		return askForDelay;
	}




	public void setAskForDelay(Button askForDelay) {
		this.askForDelay = askForDelay;
	}




	public Copy(int copyID, int bookID, String borrowerID, String borrowDate, String returnDate) 
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
	
	
	
}
