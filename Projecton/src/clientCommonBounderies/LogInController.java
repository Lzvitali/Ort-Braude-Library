package clientCommonBounderies;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import Common.IGUIController;
import Common.ObjectMessage;
import Common.User;
import clientBounderiesLibrarian.StartPanelLibrarianController;
import clientConrollers.AClientCommonUtilities;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * This Class is the controller for log in GUI
 *
 */

public class LogInController  implements IGUIController 
{
	OBLClient client;

	public static String currentID; //will keep the ID of the current user 
	protected static Object startPanelController; //will keep the reference for the current StartPanel stage
	public static int permission; //will keep who logged in (librarian/ director/ reader account)
								 // 1 = Library Director , 2 = Librarian , 3 = reader account
	
    @FXML
    private JFXTextField logInIDTextField;

    @FXML
    private JFXPasswordField logInPasswordTextField;

    @FXML
    private JFXButton LogInBtn;

    @FXML
    private JFXButton cancelLogInBtn;

    
    @FXML
    void initialize()
    {
    	//get the client from the StartPanelController
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);
    }
    
    
    @FXML
    void cancelLogIn(ActionEvent event) 
    {
    	AClientCommonUtilities.backToStartPanel();
    }

	/**
	 * This function send request to server to login the user that requested
	 * the client will send the id and password only if valid else will print
	 * propper message
	 */
    @FXML
    void makeLogIn(ActionEvent event) 
    {
    	
    	String id = logInIDTextField.getText();
    	String password = logInPasswordTextField.getText();
    	
    	String checkResult = AValidationInput.checkValidationUser("UserID", id);
    	
    	if(id.equals("") || password.equals(""))
    	{
    		AClientCommonUtilities.alertErrorWithOption("Please fill both: ID and Password", "Wrong input", "Ok");
    	}    	
    	else if(checkResult.equals("correct"))
		{
    		currentID=id;

        	User user = new User(id,password);
        	ObjectMessage msg = new ObjectMessage(user,"user try to log in","User");
        	client.setClientUI(this);
        	client.handleMessageFromClient(msg); 
		}
    	else
    	{
    		AClientCommonUtilities.alertErrorWithOption(checkResult, "Wrong input", "Ok");
    	}
    	
    }
    

	/**
	 * This function trigger login function if pressed enter on password textfield
	 */
	@FXML
    void makeLoginWithEnterBtn(KeyEvent event)
    {
		client.setClientUI(this);
    	if(event.getCode().equals(KeyCode.ENTER))
    	{
    		makeLogIn(new ActionEvent());
       }
    }

    
    @Override
	public void display(ObjectMessage msg) 
    {
    	String ms = msg.getMessage();
    	//if successful go to the relevant start panel
		if((msg.getMessage()).equals("successful"))
		{
			try 
			{
				Platform.runLater(()->
				{ 
					AClientCommonUtilities.stage.close();
				});
			}
			catch (Exception e) 
			{
		
				e.printStackTrace();
			}
			 // 1 = Library Director , 2 = Librarian , 3 = reader account
			if((msg.getNote()).equals("1"))
			{
				permission=1;
				AClientCommonUtilities.loadStartPanelWindow(startPanelController,"/clientBounderiesLibrarian/StartPanelLibraryDirector.fxml","Librarian Managear Start Panel");
			}
			else if((msg.getNote()).equals("2"))
			{
				permission=2;
				AClientCommonUtilities.loadStartPanelWindow(startPanelController,"/clientBounderiesLibrarian/StartPanelLibrarian.fxml","Librarian Start Panel");
			}
			else if((msg.getNote()).equals("3"))
			{
				permission=3;
				AClientCommonUtilities.loadStartPanelWindow(startPanelController,"/clientBounderiesReaderAccount/StartPanelReaderAccount.fxml","Librarian Start Panel");
			}
		}
		//if unsuccessful show proper error message
		else
		{
			if((msg.getMessage()).equals("unsuccessful"))
			{
				if((msg.getNote()).equals("User already connected"))
				{
					AClientCommonUtilities.alertError("User already connected", "Error");
				}
				else if((msg.getNote()).equals("User is Locked"))
				{
					AClientCommonUtilities.alertError("User is Locked", "Error");
				}
				
			}
			else if((msg.getMessage()).equals("not exist"))
			{
				AClientCommonUtilities.alertError("ID or Password not match", "Error");
			}
		}
		
	}
}
