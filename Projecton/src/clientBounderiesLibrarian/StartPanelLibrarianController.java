package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import Common.Book;
import Common.IEntity;
import Common.IGUIController;
import Common.IGUIStartPanel;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.User;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.LogInController;
import clientCommonBounderies.StartPanelController;
import clientConrollers.OBLClient;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableView<IEntity> searchResultTable; 

    @FXML 
    private TableColumn<IEntity, String> bookNameColumn; 

    @FXML 
    private TableColumn<IEntity, String> authorNameColumn; 

    @FXML
    private TableColumn<IEntity, Integer> bookYearColumn; 

    @FXML
    private TableColumn<IEntity, String> BookTopicColumn; 

    @FXML
    private TableColumn<IEntity, Boolean> isDesiredBookColumn; 

    @FXML
    private TableColumn<IEntity, Button> viewIntroColumn; 

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
    private TableView<IEntity> searchReaderAccountTable; 

    @FXML 
    private TableColumn<IEntity, String> accountIDColumn; 

    @FXML
    private TableColumn<IEntity, String> accountFirstNameColumn;
    
    @FXML
    private TableColumn<IEntity, String> accountLastNameColumn; 

    @FXML
    private TableColumn<IEntity, String> accountStatusColumn; 

    @FXML 
    private TableColumn<IEntity, String> accountPhoneColumn;

    @FXML 
    private TableColumn<IEntity, Button> borrowsAndReservesColumn; 
    
    @FXML
    private TabPane TabPaneSelect;
    
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
    	toggleGroupForAccounts = new ToggleGroup();
        this.iDRB.setToggleGroup(toggleGroupForAccounts);
        this.firstNameRB.setToggleGroup(toggleGroupForAccounts);
        this.lastNameRB.setToggleGroup(toggleGroupForAccounts);
        this.freeSearchReaderAccountRB.setToggleGroup(toggleGroupForAccounts);
        
    }
    
    
    @FXML
    void makeSearch(ActionEvent event) 
    {
    	//lets example that will be here valdaion for book(still not exist so didnt write)
    	
    	//if success do this and if selected book :
    	if(TabPaneSelect.getSelectionModel().getSelectedItem().getText().equals("Search book"))
    	{
    		searchBookPressed();
    	}
    	else
    	{
    		searchReaderAccountPressed();
    	}
 	
    }
    
   private  void  searchBookPressed()
   {
   	JFXRadioButton selectedRadioButton = (JFXRadioButton) toggleGroupForBooks.getSelectedToggle();
   	String selectedString = selectedRadioButton.getText();
   	Book askedBook=new Book();
   	if(selectedString.equals("Book name"))
   	{
   		askedBook.setBookName(searchBookTextField.getText());
   	}
   	else if(selectedString.equals("Author name"))
   	{
   		askedBook.setAuthorName(searchBookTextField.getText());
   	}
   	else if(selectedString.equals("Topic"))
   	{
   		askedBook.setTopic(searchBookTextField.getText());;
   	}
   	else
   	{
   		askedBook.setBookName("needtocheckthis");
   	}
   	ObjectMessage sendToServer=new ObjectMessage(askedBook,"SearchBook","Book");
   	client.handleMessageFromClient(sendToServer); 
   }
   
  
   private  void searchReaderAccountPressed()
    {
    	JFXRadioButton selectedRadioButton = (JFXRadioButton) toggleGroupForAccounts.getSelectedToggle();
    	String selectedString = selectedRadioButton.getText();
    	ReaderAccount askedReader=new ReaderAccount();
    	if(selectedString.equals("ID"))
    	{
    		askedReader.setId(searchReaderAccountSearchField.getText());
    	}
    	else if(selectedString.equals("First name"))
    	{
    		askedReader.setFirstName(searchReaderAccountSearchField.getText());
    	}
    	else if(selectedString.equals("Last name"))
    	{
    		askedReader.setLastName(searchReaderAccountSearchField.getText());;
    	}
    	else
    	{
    		askedReader.setLastName("needtocheckthis");
    	}
    	ObjectMessage sendToServer=new ObjectMessage(askedReader,"SearchReader","ReaderAccount");
    	client.handleMessageFromClient(sendToServer);  
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
    	AClientCommonUtilities.loadStartPanelWindow(getClass(),"/clientCommonBounderies/StartPanel.fxml","Start Panel");
		
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
		if(msg.getMessage().equals("BookSearch"))
		{
			searchBookResult(msg);
		}
		else if(msg.getMessage().equals("ReaderAccountSearch"))
		{
			searchReaderAccountResult(msg);
		}
		
	}
    private void searchBookResult(ObjectMessage msg)
	{
		searchResultTable.getItems().clear();
		if(msg.getNote().equals("NoBookFound"))
		{
			AClientCommonUtilities.infoAlert("No books found , try insert other value", "No books found");
		}
		else
		{
			Platform.runLater(()->
			{
				bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
				authorNameColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
				bookYearColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Book)cellData.getValue()).getYear()).asObject());
				isDesiredBookColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(((Book)cellData.getValue()).isDesired()).asObject());
				BookTopicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
				viewIntroColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
			int i;
			ArrayList <IEntity> result=msg.getObjectList();
			for(i=0;i<result.size();i++)
			{
				((Book)result.get(i)).setDetails(new Button("Open PDF"));
				searchResultTable.getItems().add(result.get(i));
			}
			});
			
		}
	}
    
    private void searchReaderAccountResult(ObjectMessage msg)
	{
    	searchReaderAccountTable.getItems().clear();
		if(msg.getNote().equals("NoReaderFound"))
		{
			AClientCommonUtilities.infoAlert("No readers found , try insert other value", "No readers found");
		}
		else
		{
			Platform.runLater(()->
			{
				accountIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
				accountFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
				accountLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
				accountStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
				accountPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
				borrowsAndReservesColumn.setCellValueFactory(new PropertyValueFactory<>("borrowsAndReserves"));
			int i;
			ArrayList <IEntity> result=msg.getObjectList();
			for(i=0;i<result.size();i++)
			{
				((ReaderAccount)result.get(i)).setBorrowsAndReserves(new Button("Open"));
				searchReaderAccountTable.getItems().add(result.get(i));
			}
			});
			
		}
	}


   
}

