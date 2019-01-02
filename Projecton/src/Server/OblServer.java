package Server;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
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
	
}

/**
* This method overrides the one in the superclass.  Called
* when the server starts listening for connections.
*/
protected void serverStarted()
{
	System.out.println("The server is up ");
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
		 System.out.println("Enter the username of MYSQL" );
		 user=reader.nextLine();
		 System.out.println("Enter the password of MYSQL" );
		 password=reader.nextLine();
		 System.out.println("Enter the name of schema in MYSQL" );
		 schema=reader.nextLine();
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
