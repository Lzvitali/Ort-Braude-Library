package Common;

import javafx.scene.control.Button;

public class Book implements IEntity 
{
	private int bookID;
	private String bookName;
	private String authorName;
	private String year;
	private String topic;
	private boolean isDesired; 
	//buttonss
	private Button reserve;
	private Button details;
	
	
	public Book(String bookName)
	{
		this.bookName=bookName;
	}

	
	
	

	public String getBookName() {
		return bookName;
	}

}
