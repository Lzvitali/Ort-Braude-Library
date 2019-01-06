package Server;


import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import Common.ObjectMessage;

public abstract class  BookDBController 
{

	
	public static ObjectMessage  Selection(Connection conn,ObjectMessage objectMessage)
	{
		if(objectMessage.getMessage().equals("AddBook"))
		{
			AddBook(conn,objectMessage);
		}
			
	}
	
	
	private static ObjectMessage AddBook(Connection conn,ObjectMessage object)
	{
		Book book=(Book)object.getObjectList().get(0);
	}
}
