package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import Common.IGUIController;
import Common.IGUIStartPanel;
import Common.ObjectMessage;
import clientCommonBounderies.AClientCommonUtilities;
import clientConrollers.OBLClient;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;


/**
 *  Controller Class for 'StartPanelLibrarian.fxml' AND for 'StartPanelLibraryDirector.fxml'
 */


public class StartPanelLibrarianController implements IGUIController,IGUIStartPanel
{
	//Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	public static OBLClient client;

	private static int numOfActiveWindows=0;  
	
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
    public void initialize(String[] parameters) 
    {
    	try 
        {
          client= new OBLClient(parameters[0], Integer.parseInt(parameters[1]), this);
        } 
        catch(IOException exception) 
        {
        	System.out.println("Error: Can't setup connection!"+ " Terminating client.");
        	System.exit(1);
        }
    	setRedioButtonsForBooksSearch();
    }
    
    void setRedioButtonsForBooksSearch()
    {
    	toggleGroupForBooks = new ToggleGroup();
        this.bookNameRB.setToggleGroup(toggleGroupForBooks);
        this.authorNameRB.setToggleGroup(toggleGroupForBooks);
        this.topicRB.setToggleGroup(toggleGroupForBooks);
        this.freeSearchBookRB.setToggleGroup(toggleGroupForBooks);
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
    	AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesLibrarian/AddBook.fxml","Add book copy");
    }

    @FXML
    void openBorrowBook(ActionEvent event) 
    {
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

