package clientCommonBounderies;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import Common.Book;
import Common.IGUIController;
import Common.IGUIStartPanel;
import Common.ObjectMessage;
import Common.User;
import clientConrollers.OBLClient;

import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * This class is a Controller for StartPanel.fxml AND for LogInFxml
 * @author Vitali
 *
 */

public class StartPanelController implements IGUIController, IGUIStartPanel
{
	//Instance variables **********************************************
	private static int numOfActiveWindows=0;
	
	/**
	 * this is the details of the current user that we need in all the next controllers 
	 */
	public static User user; 
	
	public  static OBLClient connToClientController;
	
	
	
	// attributes for StartPanel Only
    @FXML 
    private Button loginBtn; 

    @FXML 
    private JFXTextField searchTextField; 

    @FXML 
    private Button searchBtn; 

    @FXML 
    private JFXRadioButton bookNameRB; 

    @FXML 
    private JFXRadioButton authorNameRB; 

    @FXML 
    private JFXRadioButton topicRB; 

    @FXML
    private JFXRadioButton freeSearchRB; 

    @FXML 
    private TableView<?> searchResultTable; 

    @FXML 
    private TableColumn<?, ?> bookNameColumn; 

    @FXML 
    private TableColumn<?, ?> authorNameColumn; 

    @FXML 
    private TableColumn<?, ?> yearColumn; 

    @FXML 
    private TableColumn<?, ?> topicColumn; 

    @FXML 
    private TableColumn<?, ?> isDesiredColumn; 

    @FXML 
    private TableColumn<?, ?> viewIntroColumn; 


    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize(String[] arr) 
    {
    	connect(arr[0],Integer.parseInt(arr[1])); // start the connection to our ClientController

    }

    @FXML
    void openLogin(ActionEvent event) 
    {
    	AClientCommonUtilities.loadWindow(getClass(),"/clientCommonBounderies/LogIn.fxml","Log in");
    }

    @FXML
    void makeSearch(ActionEvent event) 
    {
    	//lets example that will be here valdaion for book(still not exist so didnt write)
    	
    	//if success do this and if selected book :
    	
    	Book book= new Book(searchTextField.getText());
    	ObjectMessage objectMessage=new ObjectMessage(book,"SearchBook");
    	connToClientController.handleMessageFromClient(objectMessage);
    	
    }

    
    
    
    public  void connect(String ip,int port) //make the connection to ClientController.
    {
    	System.out.println(ip + "  " + port);
        try 
        {
        	connToClientController= new OBLClient(ip, port,this);
		} catch (IOException e) 
        {
			e.printStackTrace();
			AClientCommonUtilities.alertError("Please check server connection","No Server Connection ");
		}
    }
    
    
    


	@Override
	public void display(ObjectMessage msg) 
	{
		
		
	}

	@Override
	public int getActivateWindows() 
	{
		return numOfActiveWindows;
	}

	@Override
	public void setActivateWindows(int newWindows) 
	{
		numOfActiveWindows=newWindows;
		
	}

}
