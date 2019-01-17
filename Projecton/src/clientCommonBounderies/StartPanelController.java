package clientCommonBounderies;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import Common.Book;
import Common.IEntity;
import Common.IGUIController;
import Common.IGUIStartPanel;
import Common.ObjectMessage;
import Common.User;
import clientConrollers.OBLClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This class is a Controller for StartPanel.fxml AND for LogInFxml
 * @author Vitali
 *
 */

public class StartPanelController implements IGUIController, IGUIStartPanel
{
	//Instance variables **********************************************
	protected static int numOfActiveWindows=0; 
	private static int alreadyInitilized=0;
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
    private TableView<IEntity> searchResultTable; 
    
    @FXML
    private TableColumn<?, ?> locationColumn;

    @FXML
    private TableColumn<?, ?> inTheLibraryColumn;

    @FXML
    private TableColumn<?, ?> ClosestReturnColumn;
    
    @FXML
    private TableColumn<?, ?> editionColumn;
    
    
    private ToggleGroup toggleGroupForBooks; 
    
    

    @FXML 
    public void initialize(String[] arr) 
    {
    	if(alreadyInitilized==0)
    	{
    		connect(arr[0],Integer.parseInt(arr[1])); // start the connection to our ClientController
    		alreadyInitilized++;
    	}
    	else
    	{
    		connToClientController.setClientUI(this);
    	}
    	setRedioButtonsForBooksSearch();
    }

   
    public  void connect(String ip,int port) //make the connection to ClientController.
    {
        try 
        {
        	connToClientController= new OBLClient(ip, port,this);
		} catch (IOException e) 
        {
			AClientCommonUtilities.alertErrorWithExit("Please check server connection","No Server Connection ");
		}
    }
    
    @FXML
    void openLogin(ActionEvent event) 
    {
    	AClientCommonUtilities.loadWindow(getClass(),"/clientCommonBounderies/LogIn.fxml","Log in");
    	LogInController.startPanelController = getClass(); 
    }
    
    @FXML
    void makeSearch(ActionEvent event) 
    {
    	//lets example that will be here valdaion for book(still not exist so didnt write)
    	
    	//if success do this and if selected book :
		Platform.runLater(()->
		{
			JFXRadioButton  selectedRadioButton;
	    	try 
	    	{
	    		selectedRadioButton = (JFXRadioButton) toggleGroupForBooks.getSelectedToggle();
	    	}
	    	catch(NullPointerException e)
	    	{
	    		selectedRadioButton=bookNameRB;
	    	}	
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
	    	connToClientController.handleMessageFromClient(sendToServer);   	
		});
    }
    


	@Override
	public void display(ObjectMessage msg) 
	{
		if(msg.getMessage().equals("BookSearch"))
		{
			searchBookResult(msg);
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
				yearColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Book)cellData.getValue()).getYear()).asObject());
				isDesiredColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(((Book)cellData.getValue()).isDesired()).asObject());
				topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
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
	
    void setRedioButtonsForBooksSearch()
    {
    	toggleGroupForBooks = new ToggleGroup();
        this.bookNameRB.setToggleGroup(toggleGroupForBooks);
        this.authorNameRB.setToggleGroup(toggleGroupForBooks);
        this.topicRB.setToggleGroup(toggleGroupForBooks);
        this.freeSearchRB.setToggleGroup(toggleGroupForBooks);
    }
	
    
    
    
}
