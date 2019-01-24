package Common;

import java.util.Date;

public class History  implements IEntity
{
	private String userID;
	private String action;
	private int bookId;
	private int copyId;
	private Date actionDate;
	private String note;
	
	
	public History(String userID, String action, int bookId, int copyId, Date actionDate, String note) 
	{

		this.userID = userID;
		this.action = action;
		this.bookId = bookId;
		this.copyId = copyId;
		this.actionDate = actionDate;
		this.note = note;
	}


	public History(String userID, String action,int bookId, int copyId, Date actionDate) 
	{
		this.userID = userID;
		this.action = action;
		this.bookId=bookId;
		this.copyId=copyId;
		this.actionDate = actionDate;
	}


	public int getBookId() 
	{
		return bookId;
	}


	public void setBookId(int bookId)
	{
		this.bookId = bookId;
	}


	public int getCopyId()
	{
		return copyId;
	}


	public void setCopyId(int copyId) 
	{
		this.copyId = copyId;
	}


	public String getUserID()
	{
		return userID;
	}


	public void setUserID(String userID) 
	{
		this.userID = userID;
	}


	public String getAction()
	{
		return action;
	}


	public void setAction(String action)
	{
		this.action = action;
	}


	public Date getActionDate()
	{
		return actionDate;
	}


	public void setActionDate(Date actionDate) 
	{
		this.actionDate = actionDate;
	}


	public String getNote() 
	{
		return note;
	}


	public void setNote(String note)
	{
		this.note = note;
	}
	
	
}
