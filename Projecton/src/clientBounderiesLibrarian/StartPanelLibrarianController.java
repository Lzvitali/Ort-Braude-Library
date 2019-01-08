package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import Common.IGUIController;
import Common.ObjectMessage;
import clientConrollers.OBLClient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


/**
 *  Controller Class for 'StartPanelLibrarian.fxml' 
 */


public class StartPanelLibrarianController implements IGUIController
{
	//Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	OBLClient client;

	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="logOutBtn"
    private Button logOutBtn; // Value injected by FXMLLoader

    @FXML // fx:id="borrowBookBtn"
    private Button borrowBookBtn; // Value injected by FXMLLoader

    @FXML // fx:id="returnBookBtn"
    private Button returnBookBtn; // Value injected by FXMLLoader

    @FXML // fx:id="addBookBtn"
    private Button addBookBtn; // Value injected by FXMLLoader

    @FXML // fx:id="deleteBookBtn"
    private Button deleteBookBtn; // Value injected by FXMLLoader

    @FXML // fx:id="registerNewAccountBtn"
    private Button registerNewAccountBtn; // Value injected by FXMLLoader

    @FXML // fx:id="searchBookTab"
    private Tab searchBookTab; // Value injected by FXMLLoader

    @FXML // fx:id="searchBookTextField"
    private JFXTextField searchBookTextField; // Value injected by FXMLLoader

    @FXML // fx:id="searchBookBtn"
    private Button searchBookBtn; // Value injected by FXMLLoader

    @FXML // fx:id="bookNameRB"
    private JFXRadioButton bookNameRB; // Value injected by FXMLLoader

    @FXML // fx:id="authorNameRB"
    private JFXRadioButton authorNameRB; // Value injected by FXMLLoader

    @FXML // fx:id="topicRB"
    private JFXRadioButton topicRB; // Value injected by FXMLLoader

    @FXML // fx:id="freeSearchBookRB"
    private JFXRadioButton freeSearchBookRB; // Value injected by FXMLLoader

    @FXML // fx:id="searchResultTable"
    private TableView<?> searchResultTable; // Value injected by FXMLLoader

    @FXML // fx:id="bookNameColumn"
    private TableColumn<?, ?> bookNameColumn; // Value injected by FXMLLoader

    @FXML // fx:id="authorNameColumn"
    private TableColumn<?, ?> authorNameColumn; // Value injected by FXMLLoader

    @FXML // fx:id="bookYearColumn"
    private TableColumn<?, ?> bookYearColumn; // Value injected by FXMLLoader

    @FXML // fx:id="BookTopicColumn"
    private TableColumn<?, ?> BookTopicColumn; // Value injected by FXMLLoader

    @FXML // fx:id="isDesiredBookColumn"
    private TableColumn<?, ?> isDesiredBookColumn; // Value injected by FXMLLoader

    @FXML // fx:id="viewIntroColumn"
    private TableColumn<?, ?> viewIntroColumn; // Value injected by FXMLLoader

    @FXML // fx:id="searchReaderAccountTab"
    private Tab searchReaderAccountTab; // Value injected by FXMLLoader

    @FXML // fx:id="searchReaderAccountSearchField"
    private JFXTextField searchReaderAccountSearchField; // Value injected by FXMLLoader

    @FXML // fx:id="searchReaderAccountBtn"
    private Button searchReaderAccountBtn; // Value injected by FXMLLoader

    @FXML // fx:id="iDRB"
    private JFXRadioButton iDRB; // Value injected by FXMLLoader

    @FXML // fx:id="firstNameRB"
    private JFXRadioButton firstNameRB; // Value injected by FXMLLoader

    @FXML // fx:id="lastNameRB"
    private JFXRadioButton lastNameRB; // Value injected by FXMLLoader

    @FXML // fx:id="freeSearchReaderAccountRB"
    private JFXRadioButton freeSearchReaderAccountRB; // Value injected by FXMLLoader

    @FXML // fx:id="searchReaderAccountTable"
    private TableView<?> searchReaderAccountTable; // Value injected by FXMLLoader

    @FXML // fx:id="accountIDColumn"
    private TableColumn<?, ?> accountIDColumn; // Value injected by FXMLLoader

    @FXML // fx:id="accountLastNameColumn"
    private TableColumn<?, ?> accountLastNameColumn; // Value injected by FXMLLoader

    @FXML // fx:id="accountStatusColumn"
    private TableColumn<?, ?> accountStatusColumn; // Value injected by FXMLLoader

    @FXML // fx:id="accountPhoneColumn"
    private TableColumn<?, ?> accountPhoneColumn; // Value injected by FXMLLoader

    @FXML // fx:id="borrowsAndReservesColumn"
    private TableColumn<?, ?> borrowsAndReservesColumn; // Value injected by FXMLLoader
    
    
    ///////////////////////////////////////////////////////////////////////////////////
    //for the Library Director only
    @FXML // fx:id="reportBtn"
    private Button reportBtn; // Value injected by FXMLLoader
    
    @FXML
    void openReportWindow(ActionEvent event) 
    {

    }
    ///////////////////////////////////////////////////////////////////////////////////
    
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize(String []connectionDetails) 
    {
    	
    	String ip = connectionDetails[0];
    	int port = Integer.parseInt(connectionDetails[0]);
    	
    	try 
        {
          client= new OBLClient(ip, port, this);
        } 
        catch(IOException exception) 
        {
        	System.out.println("Error: Can't setup connection!"+ " Terminating client.");
        	System.exit(1);
        }
    	
    }
    

    @FXML
    void makeLogOut(ActionEvent event) 
    {

    }

    @FXML
    void makeSearch(ActionEvent event) 
    {

    }

    @FXML
    void openAddBook(ActionEvent event) 
    {

    }

    @FXML
    void openBorrowBook(ActionEvent event) 
    {

    }

    @FXML
    void openDeleteBookBtn(ActionEvent event) 
    {

    }

    @FXML
    void openRegisterNewAccount(ActionEvent event) 
    {

    }

    @FXML
    void openReturnBook(ActionEvent event) 
    {

    }


	@Override
	public void display(ObjectMessage msg) 
	{
		// TODO Auto-generated method stub
		
	}

   
}

