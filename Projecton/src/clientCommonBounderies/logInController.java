package clientCommonBounderies;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import Common.IGUIController;
import Common.ObjectMessage;
import Common.User;
import clientBounderiesLibrarian.StartPanelLibrarianController;
import clientConrollers.OBLClient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class logInController  implements IGUIController
{
	OBLClient client;

	public static int currentID;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);
    }
    
    
    @FXML
    void cancelLogIn(ActionEvent event) 
    {

    }

    @FXML
    void makeLogIn(ActionEvent event) 
    {
    	String id = logInIDTextField.getText();
    	String password = logInPasswordTextField.getText();
    	
    	System.out.println(id + "  " + password);
    	
    	User user = new User(id,password);
    	ObjectMessage msg = new ObjectMessage(user,"user try to log in");
    	
    	System.out.println(msg);
    	
    	client.handleMessageFromClient(msg); 
    }

    
    @Override
	public void display(ObjectMessage msg) {
		// TODO Auto-generated method stub
		
	}
}
