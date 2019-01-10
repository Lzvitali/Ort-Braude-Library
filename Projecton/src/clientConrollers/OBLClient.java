package clientConrollers;
import ocsf.client.*;
import java.io.*;

import Common.IGUIController;
import Common.ObjectMessage;


/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class OBLClient extends AbstractClient //Make connection between our IGUIController and server(OBLServer)
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
	 IGUIController clientUI; 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public OBLClient(String host, int port, IGUIController clientUI) throws IOException   
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    	clientUI.display((ObjectMessage)msg); // Send the Info that come from server to GUIcontroller
	 
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */

  public void handleMessageFromClient(Object msg) 
  {
	  try 
	  {
		  //System.out.println(msg + "!!"); // TO_DELETE later
		 // System.out.println(getHost() + "   " + getPort()); // TO_DELETE later
		 // System.out.println(clientUI.getClass()); // TO_DELETE later
		  sendToServer(msg);
	  } 
	  catch (IOException e) 
	  {
		  System.out.println("Failed to send to server");
		  e.printStackTrace();
	  } // Send the Info that come from server to GUIcontroller
	 
  }
  
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }


public void setClientUI(IGUIController clientUI) // TO_DELETE later
{
	this.clientUI = clientUI;
}
  
  
}
//End of OBLClient class
