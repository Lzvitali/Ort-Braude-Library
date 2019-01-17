package clientBounderiesReaderAccount;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import Common.Book;
import Common.Copy;
import Common.IEntity;
import Common.IGUIController;
import Common.IGUIStartPanel;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.User;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.AStartClient;
import clientCommonBounderies.LogInController;
import clientCommonBounderies.StartPanelController;
import clientConrollers.OBLClient;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartPanelReaderAccountController implements IGUIController,IGUIStartPanel
{

	OBLClient client;
	
	private static int numOfActiveWindows=0; 
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button logOutBtn;

    @FXML
    private Button myBorrowsAndReserves;

    @FXML
    private Button historyBtn; 

    @FXML
    private Button personalDetailsBtn;

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
    private TableView<IEntity> searchResultTable;

    @FXML 
    private TableColumn<IEntity, String> bookNameColumn; 

    @FXML 
    private TableColumn<IEntity, String> authorNameColumn; 

    @FXML 
    private TableColumn<IEntity, Integer> yearColumn; 

    @FXML 
    private TableColumn<IEntity, String> topicColumn; 

    @FXML 
    private TableColumn<IEntity, Boolean> isDesiredColumn; 

    @FXML 
    private TableColumn<IEntity, Button> viewIntroColumn; 

    @FXML
    private TableColumn<IEntity, Button> reserveBtn;
    
    @FXML
    private TableColumn<?, ?> locationColumn;

    @FXML
    private TableColumn<?, ?> inTheLibraryColumn;

    @FXML
    private TableColumn<?, ?> ClosestReturnColumn;
    
    private ToggleGroup toggleGroupForBooks; 

    @FXML
    private TableColumn<?, ?> editionColumn;
    
    
    @FXML
    public void initialize() 
    {
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);
    	AStartClient.primaryStagePanel.setOnCloseRequest(e->
    	{ 
    		makeLogOut(new ActionEvent());
    		System.exit(0);
    	});
    	setRedioButtonsForBooksSearch();
    }

    @FXML
    void makeLogOut(ActionEvent event) 
    {
    	
    	//change the status of that user in the DB
    	User user = new User(LogInController.currentID);
    	ObjectMessage msg = new ObjectMessage(user,"user try to log out","User");
    	client.handleMessageFromClient(msg);
    	   	
    	
    	//Old log out:
    	
    	/*//change the status of that user in the DB
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
		}*/
    }

    
    //TODO: Ziv-- add comments for JaavaDoc
    @FXML
    void makeSearch(ActionEvent event) 
    {
    	JFXRadioButton selectedRadioButton = (JFXRadioButton) toggleGroupForBooks.getSelectedToggle();
    	String selectedString = selectedRadioButton.getText();
    	Book askedBook=new Book();
    	if(selectedString.equals("Book name"))
    	{
    		askedBook.setBookName(searchTextField.getText());
    	}
    	else if(selectedString.equals("Author name"))
    	{
    		askedBook.setAuthorName(searchTextField.getText());
    	}
    	else if(selectedString.equals("Topic"))
    	{
    		askedBook.setTopic(searchTextField.getText());;
    	}
    	else
    	{
    		askedBook.setBookName("needtocheckthis");
    	}
    	ObjectMessage sendToServer=new ObjectMessage(askedBook,"SearchBook","Book");
    	client.handleMessageFromClient(sendToServer);   
    }

    @FXML
    void openBorrowsAndReserves(ActionEvent event) 
    {
    	AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesReaderAccount/MyOrdersAndBorrows.fxml","My orders and borrows");
    }

    @FXML
    void openHistory(ActionEvent event) 
    {
    	AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesReaderAccount/MyHistory.fxml","My history");
    }
    
    @FXML
    void openPersonalDetails(ActionEvent event) 
    {
    	AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesReaderAccount/PersonalDetails.fxml","Personal details");
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
		else if(msg.getMessage().equals("successful log out"))
		{
			AClientCommonUtilities.loadStartPanelWindow(getClass(),"/clientCommonBounderies/StartPanel.fxml","Start Panel");
		}
		else if(msg.getMessage().equals("reserveBook"))
		{
			reserveBookResult(msg);
		}
		
	}
	
	//TODO: Ziv-- add comments for JaavaDoc
	private void searchBookResult(ObjectMessage msg)
	{
		searchResultTable.getItems().clear();
		if(msg.getNote().equals("NoBookFound"))
		{
			AClientCommonUtilities.infoAlert("No books found , try insert other value", "No books found");
		}
		else
		{
		
				bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
				authorNameColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
				yearColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Book)cellData.getValue()).getYear()).asObject());
				isDesiredColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(((Book)cellData.getValue()).isDesired()).asObject());
				topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
				viewIntroColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
				reserveBtn.setCellValueFactory(new PropertyValueFactory<>("reserve"));
			
				int i;
				ArrayList <IEntity> result=msg.getObjectList();
				for(i=0;i<result.size();i++)
				{
					
					((Book)result.get(i)).setDetails(new Button("Open PDF"));
					if(((Book)result.get(i)).getNumberOfCopies()==0)((Book)result.get(i)).setReserve(new Button("Reserve"));
					if(((Book)result.get(i)).getReserve()!=null)
					{
						((Book)result.get(i)).getReserve().setOnAction(e -> AskToReserve(e));
					}
					searchResultTable.getItems().add(result.get(i));
				}	
		}
	
	}
	@FXML
	void AskToReserve(ActionEvent event)
	{
		String id=((Button)event.getSource()).getAccessibleText();
    	ObjectMessage objectMessage;
    	Book book=new Book();
    	book.setBookID(Integer.parseInt(id));
    	objectMessage=new ObjectMessage(book,"reserveBook","Book");
    	ReaderAccount readerAccount=new ReaderAccount();
    	readerAccount.setId(LogInController.currentID);
    	objectMessage.addObject(readerAccount);
    	client.handleMessageFromClient(objectMessage);
	}
	
	
    void setRedioButtonsForBooksSearch()
    {
    	toggleGroupForBooks = new ToggleGroup();
        this.bookNameRB.setToggleGroup(toggleGroupForBooks);
        this.authorNameRB.setToggleGroup(toggleGroupForBooks);
        this.topicRB.setToggleGroup(toggleGroupForBooks);
        this.freeSearchRB.setToggleGroup(toggleGroupForBooks);
    }
    
    public void availableBook(int ID)
    {
    	ObjectMessage objectMessage;
    	Copy copy=new Copy(-1,ID,null);
    	objectMessage=new ObjectMessage(copy,"checkIfAllBorrowed","Copy");
    	client.handleMessageFromClient(objectMessage);
    }
    
    
    private void reserveBookResult(ObjectMessage msg)
    {
    	if(msg.getNote().equals("HaveAvailableCopy"))
    	{
    		AClientCommonUtilities.infoAlert("Have available copys, ask librarain to borrow", "Have Available Copy");
    	}
    	else if(msg.getNote().equals("ExistReserve"))
    	{
    		AClientCommonUtilities.infoAlert("You already reserved this book", "Already Reserved");
    	}
    	else
    	{
    		searchResultTable.getItems().clear();
    		searchTextField.clear();
    		AClientCommonUtilities.infoAlert("You reserve this book", "Reserved");
    	}
    }
}
