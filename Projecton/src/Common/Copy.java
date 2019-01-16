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
	
	
	public Copy(int copyID,int bookID,String borrowerID)
	{
		this.copyID=copyID;
		this.bookID=bookID;
		this.borrowerID=borrowerID;
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
	
	
}
