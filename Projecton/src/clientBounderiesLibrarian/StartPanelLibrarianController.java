package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import Common.IGUIController;
import Common.IGUIStartPanel;
import Common.ObjectMessage;
import Common.User;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.LogInController;
import clientCommonBounderies.StartPanelController;
import clientConrollers.OBLClient;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/**
 *  Controller Class for 'StartPanelLibrarian.fxml' AND for 'StartPanelLibraryDirector.fxml'
 */


public class StartPanelLibrarianController implements IGUIController,IGUIStartPanel
{
	//Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	OBLClient client;

	private static int numOfActiveWindows=0;
	
	
	
	//FXML attibutes ****************************************************
	
    @FXML 
    private Button logOutBtn; 

    @FXML 
    private Button borrowBookBtn; 

    @FXML 
    private Button returnBookBtn;

    @FXML 
    private Button addBookBtn; 

    @FXML 
    private Button deleteBookBtn; 

    @FXML
    private Button registerNewAccountBtn; 

    @FXML 
    private Tab searchBookTab; 

    @FXML 
    private JFXTextField searchBookTextField; 

    @FXML 
    private Button searchBookBtn; 

    @FXML 
    private JFXRadioButton bookNameRB; 

    @FXML 
    private JFXRadioButton authorNameRB; 

    @FXML 
    private JFXRadioButton topicRB; 

    @FXML 
    private JFXRadioButton freeSearchBookRB; 

    @FXML 
    private TableView<?> searchResultTable; 

    @FXML 
    private TableColumn<?, ?> bookNameColumn; 

    @FXML 
    private TableColumn<?, ?> authorNameColumn; 

    @FXML
    private TableColumn<?, ?> bookYearColumn; 

    @FXML
    private TableColumn<?, ?> BookTopicColumn; 

    @FXML
    private TableColumn<?, ?> isDesiredBookColumn; 

    @FXML
    private TableColumn<?, ?> viewIntroColumn; 

    @FXML 
    private Tab searchReaderAccountTab; 

    @FXML 
    private JFXTextField searchReaderAccountSearchField; 

    @FXML 
    private Button searchReaderAccountBtn; 

    @FXML 
    private JFXRadioButton iDRB; 

    @FXML 
    private JFXRadioButton firstNameRB; 

    @FXML 
    private JFXRadioButton lastNameRB; 

    @FXML 
    private JFXRadioButton freeSearchReaderAccountRB; 

    @FXML 
    private TableView<?> searchReaderAccountTable; 

    @FXML 
    private TableColumn<?, ?> accountIDColumn; 

    @FXML
    private TableColumn<?, ?> accountLastNameColumn; 

    @FXML
    private TableColumn<?, ?> accountStatusColumn; 

    @FXML 
    private TableColumn<?, ?> accountPhoneColumn;

    @FXML 
    private TableColumn<?, ?> borrowsAndReservesColumn; 
    
    
    private ToggleGroup toggleGroupForBooks; 
    private ToggleGroup toggleGroupForAccounts; 
    
    
    
    //for the Library Director only
    ///////////////////////////////////////////////////////////////////////////////////
  
    @FXML // fx:id="reportBtn"
    private Button reportBtn; 
    
    @FXML
    private TableColumn<?, ?> freezeColumn;
    
    @FXML
    void openReportWindow(ActionEvent event) 
    {

    }
    ///////////////////////////////////////////////////////////////////////////////////
    

    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize() 
    {
    	
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);
    	
    	setRedioButtonsForBooksSearch();
    	setRedioButtonsForAccountsSearch();
    }
    
    void setRedioButtonsForBooksSearch()
    {
    	toggleGroupForBooks = new ToggleGroup();
        this.bookNameRB.setToggleGroup(toggleGroupForBooks);
        this.authorNameRB.setToggleGroup(toggleGroupForBooks);
        this.topicRB.setToggleGroup(toggleGroupForBooks);
        this.freeSearchBookRB.setToggleGroup(toggleGroupForBooks);
    }
    
    void setRedioButtonsForAccountsSearch()
    {
    	toggleGroupForBooks = new ToggleGroup();
        this.iDRB.setToggleGroup(toggleGroupForBooks);
        this.firstNameRB.setToggleGroup(toggleGroupForBooks);
        this.lastNameRB.setToggleGroup(toggleGroupForBooks);
        this.freeSearchReaderAccountRB.setToggleGroup(toggleGroupForBooks);
        
    }
    
    
    @FXML
    void makeSearch(ActionEvent event) 
    {

    }
    
    /**
     * When button Log out will be pressed it will take you to the StartPanel
     * @param event
     */
    @FXML
    void makeLogOut(ActionEvent event) 
    {
    	//change the status of that user in the DB
    	User user = new User(LogInController.currentID);
    	ObjectMessage msg = new ObjectMessage(user,"user try to log out","User");
    	client.handleMessageFromClient(msg);
    	
    	//got to StartPannel
		try 
		{
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			primaryStage.setTitle("Ort Braude Library");
			Pane root;
			root = FXMLLoader.load(getClass().getResource("/clientCommonBounderies/StartPanel.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);		
			primaryStage.show();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
    }

    @FXML
    void openAddBook(ActionEvent event) 
    {
    	AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesLibrarian/AddBook.fxml","Add book copy");
    }

    @FXML
    void openBorrowBook(ActionEvent event) 
    {
    	client.handleMessageFromClient(new ObjectMessage("boom")); 
    	AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesLibrarian/BorrowBook.fxml","Borrow book");
    }

    @FXML
    void openDeleteBookBtn(ActionEvent event) 
    {
    	AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesLibrarian/DeleteBook.fxml","Delete book");
    }

    @FXML
    void openRegisterNewAccount(ActionEvent event) 
    {
    	AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesLibrarian/RegisterNewAccount.fxml","Registrate new reader account");
    }

    @FXML
    void openReturnBook(ActionEvent event) 
    {
    	AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesLibrarian/ReturnBook.fxml","Return book");
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
	
	
	@Override
	public void display(ObjectMessage msg) 
	{
		// TODO Auto-generated method stub
		
	}

   
}

