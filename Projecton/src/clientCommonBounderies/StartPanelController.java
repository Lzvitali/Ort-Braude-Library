/**
 * Sample Skeleton for 'StartPanel.fxml' Controller Class
 */

package clientCommonBounderies;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import Common.Book;
import Common.IGUIController;
import Common.ObjectMessage;
import clientConrollers.OBLClient;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StartPanelController implements IGUIController
{

    @FXML // ResourceBundle that was given to the FXMLLoader 
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="loginBtn"
    private Button loginBtn; // Value injected by FXMLLoader

    @FXML // fx:id="searchTextField"
    private JFXTextField searchTextField; // Value injected by FXMLLoader

    @FXML // fx:id="searchBtn"
    private Button searchBtn; // Value injected by FXMLLoader

    @FXML // fx:id="bookNameRB"
    private JFXRadioButton bookNameRB; // Value injected by FXMLLoader

    @FXML // fx:id="authorNameRB"
    private JFXRadioButton authorNameRB; // Value injected by FXMLLoader

    @FXML // fx:id="topicRB"
    private JFXRadioButton topicRB; // Value injected by FXMLLoader

    @FXML // fx:id="freeSearchRB"
    private JFXRadioButton freeSearchRB; // Value injected by FXMLLoader

    @FXML // fx:id="searchResultTable"
    private TableView<?> searchResultTable; // Value injected by FXMLLoader

    @FXML // fx:id="bookNameColumn"
    private TableColumn<?, ?> bookNameColumn; // Value injected by FXMLLoader

    @FXML // fx:id="authorNameColumn"
    private TableColumn<?, ?> authorNameColumn; // Value injected by FXMLLoader

    @FXML // fx:id="yearColumn"
    private TableColumn<?, ?> yearColumn; // Value injected by FXMLLoader

    @FXML // fx:id="topicColumn"
    private TableColumn<?, ?> topicColumn; // Value injected by FXMLLoader

    @FXML // fx:id="isDesiredColumn"
    private TableColumn<?, ?> isDesiredColumn; // Value injected by FXMLLoader

    @FXML // fx:id="viewIntroColumn"
    private TableColumn<?, ?> viewIntroColumn; // Value injected by FXMLLoader

    OBLClient connToClientController;
    
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize(String[] arr) 
   {
    	connect(arr[0],Integer.parseInt(arr[1])); // start the connection to our ClientController

   }
    
    @FXML
    void makeLogin(ActionEvent event) 
    {

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
        try 
        {
        	connToClientController= new OBLClient(ip, port,this);
		} catch (IOException e) 
        {
			e.printStackTrace();
			alertError("Please check server connection","No Server Connection ");
		}
    }
    
    
    
    private void alertError(String headerText,String title) // if dont have connection to server print propper message and exit
    { 
        Alert alert = new Alert(Alert.AlertType.ERROR);
        ButtonType bttexit = new ButtonType("exit", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.setHeaderText(headerText);
        alert.setTitle(title);
        alert.getButtonTypes().addAll(bttexit);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
            System.exit(0);
        }
    }

	@Override
	public void display(ObjectMessage msg) {
		// TODO Auto-generated method stub
		
	}

}
