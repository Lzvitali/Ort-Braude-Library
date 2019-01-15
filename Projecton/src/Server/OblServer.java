package Server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import Common.Book;
import Common.ObjectMessage;
import Common.User;
import ocsf.server.*;

/**
* This class overrides some of the methods in the abstract 
* superclass in order to give more functionality to the server.
*/
public class OblServer extends AbstractServer 
{
//Class variables *************************************************

/**
* The default port to listen on.
*/
	
final public static int DEFAULT_PORT = 5555;
private  static Connection connToSQL;

//Constructors ****************************************************

/**
* Constructs an instance of the echo server.
*
* @param port The port number to connect on.
*/
public OblServer(int port) 
{
	super(port);
}


//Instance methods ************************************************

/**
* This method handles any messages received from the client.
*
* @param msg The message received from the client.
* @param client The connection from which the message originated.
*/


public void handleMessageFromClient(Object msg, ConnectionToClient client)
{
	
	ObjectMessage objectMessage = (ObjectMessage)msg;
	ObjectMessage answer;
	
	if( (objectMessage.getNote()).equals("User") ) 
	{
		answer = AUserDBController.selection(objectMessage,connToSQL);
		try 
		{
			client.sendToClient(answer);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	} 
	
	else if( (objectMessage.getNote()).equals("ReaderAccount") ) 
	{
		answer = AReaderAccountDBController.selection(objectMessage,connToSQL);
		try 
		{
			client.sendToClient(answer);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	else if( (objectMessage.getNote()).equals("Book") ) 
	{
		answer = ABookDBController.selection(objectMessage,connToSQL);
		try 
		{
			client.sendToClient(answer);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	else if( (objectMessage.getNote()).equals("AddPDF") ) 
	{
		
		Socket sock;
		try
		{
			sock = new Socket("localhost", 5643);
			byte[] mybytearray = new byte[Integer.parseInt(objectMessage.getMessage())];
			InputStream is = sock.getInputStream();
			FileOutputStream fos = new FileOutputStream("pdfFiles\\v2.pdf");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			int bytesRead = is.read(mybytearray,0, Integer.parseInt(objectMessage.getMessage()));
			int current = bytesRead; 

			do 
			{
				bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
				if(bytesRead >= 0) 
				{
					current += bytesRead;
				}
			} while(bytesRead > -1);

			bos.write(mybytearray, 0 , current);
			bos.flush();

			fos.close();
			bos.close();
			sock.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		  
		
	}
	
	else if( (objectMessage.getNote()).equals("Copy") ) 
	{
		answer = ACopyDBController.selection(objectMessage,connToSQL);
		try 
		{
			client.sendToClient(answer);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
}

/**
* This method overrides the one in the superclass.  Called
* when the server starts listening for connections.
*/
protected void serverStarted()
{
	System.out.println("The server is online ");
}

public static void ConnectToDB(String user,String password,String schema) // make the connection for DB according the user and password that entered
{
	
	try 
	{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
    } 
	catch (Exception ex) 
	{
		System.out.println("Couldnt find the driver");
	}
    try 
    {
    	connToSQL=DriverManager.getConnection("jdbc:mysql://localhost/"+schema,user,password);
	} 
    catch (SQLException e) 
    {
			System.out.println("Error while connect to DB , check your username/password/schema");
			System.exit(0);
	} 

}

protected void serverStopped()
{
	System.out.println("Server has stopped listening for connections.");
}

@Override
public void finalize() 
{ 
	 ObjectMessage objectMessage=new ObjectMessage("User","MakeAllOffline");
	 AUserDBController.selection(objectMessage,connToSQL);
} 

public static void main(String[] args) 
{
	 int port = 0; //Port to listen on
	 try
	 {
	   port = Integer.parseInt(args[0]); //Get port from command line
	 }
	 catch(Throwable t)
	 {
	   port = DEFAULT_PORT; //Set port to 5555
	 }
	 String user,password,schema;
	 try //get the username and the password for MYSQL
	 {
		 Scanner reader = new Scanner(System.in);
		// System.out.println("Enter the username of MYSQL" );
		 user="root";//reader.nextLine();
		// System.out.println("Enter the password of MYSQL" );
		 password="root";//reader.nextLine();
		// System.out.println("Enter the name of schema in MYSQL" );
		 schema="obl";//reader.nextLine();
		 ConnectToDB(user,password,schema);
		 reader.close();
	 }
	 catch (Exception ex) 
	 {
		System.out.println("Cya,you enter invaild input");
		System.exit(0);
	 }	
	 OblServer sv = new OblServer(port); // start the server
	 try 
	 {
	   sv.listen(); //Start listening for connections
	 } 
	 catch (Exception ex) 
	 {
	   System.out.println("ERROR - Could not listen for clients!");
	 }
	 
	}




}
