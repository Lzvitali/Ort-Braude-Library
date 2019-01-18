package clientBounderiesReaderAccount;
 
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import Common.Book;
import Common.Borrows;
import Common.Copy;
import Common.IEntity;
import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import clientBounderiesLibrarian.StartPanelLibrarianController;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.AStartClient;
import clientCommonBounderies.LogInController;
import clientCommonBounderies.StartPanelController;
import clientConrollers.OBLClient;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MyBorrowsAndReservesController implements IGUIController
{
	
	OBLClient client;

	@FXML
    private TableColumn<IEntity, String> BookNameBorrowColumn;

    @FXML
    private TableColumn<IEntity, String> AuthorNameBorrowColumn;

    @FXML
    private TableColumn<IEntity, Integer> YearBorrowColumn;

    @FXML
    private TableColumn<IEntity, String> TopicBorrowColumn;

    @FXML
    private TableColumn<IEntity, Boolean> isDesiredBorrowColumn;

    @FXML
    private TableColumn<IEntity, Integer> EditionBorrowColumn;

    @FXML
    private TableColumn<IEntity, Date> BorrowDateColumn;

    @FXML
    private TableColumn<IEntity, Date> ReturnDateColumn;

    @FXML
    private TableColumn<IEntity, Button> btnForBorrows;

    @FXML
    private TableColumn<IEntity, String> BookNameReservColumn;

    @FXML
    private TableColumn<IEntity, String> AuthorNameReservColumn;

    @FXML
    private TableColumn<IEntity, Integer> YeareReservColumn;

    @FXML
    private TableColumn<IEntity, String> TopicReservColumn;

    @FXML
    private TableColumn<IEntity, Boolean> isDesiredReserveColumn;

    @FXML
    private TableColumn<IEntity, Integer> editionReserveColumn;

    @FXML
    private TableColumn<IEntity, Button> BtnForOrders;
    
    @FXML
    private TableView<Borrows> borrowsTable;
    
    @FXML
    private TableView<IEntity> ordersTable;
    
    ObservableList<IEntity> list1;
    ObservableList<IEntity> list2;
    
    private ReaderAccount reader;


    @FXML
    void initialize() 
    {
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);
    	
    	
    	reader = new ReaderAccount();
    	
    	// 1 = Library Director , 2 = Librarian , 3 = reader account
    	
    	//if the reader account opening the window
    	if(LogInController.permission == 3)
    	{
        	reader.setId(LogInController.currentID);
    	}
    	
    	//if the librarian or the library director opening the window
    	else 
    	{
    		if(LogInController.permission == 1 || LogInController.permission == 2)
        	{
            	reader.setId(StartPanelLibrarianController.readerAccountID);
        	}
    	}
    	
    	ObjectMessage msg = new ObjectMessage(reader, "get borrows", "Copy");
    	client.handleMessageFromClient(msg); 
    	//TODO: get the reserves in the display
    	
    }
    
    
    

	@Override
	public void display(ObjectMessage msg) 
	{
		if((msg.getMessage()).equals("TheBorrows"))
		{
			setBorrowsResukts(msg);
			
			/*ObjectMessage newMsg = new ObjectMessage(reader, "get reserves", "Reservation");
	    	client.handleMessageFromClient(newMsg);*/ 
		}
		else if((msg.getMessage()).equals("NoBorrows"))
		{
			/*ObjectMessage newMsg = new ObjectMessage(reader, "get reserves", "Reservation");
	    	client.handleMessageFromClient(newMsg);*/
		}
		else if((msg.getMessage()).equals("TheReserves"))
		{
			//TODO: add function to add the reserves to the table
			//consider the user (reader account/librarian)
		}
		else if((msg.getMessage()).equals("NoeReserves"))
		{
			//do nothing...
		}
		
	}
	
	/**
	 * this function sets the borrows of the reader account to the table
	 * (same for the reader account and the librarian)
	 * @param msg- received answer from the server
	 */
	void setBorrowsResukts(ObjectMessage msg)
	{
		Platform.runLater(()->
		{
			//set columns
			BookNameBorrowColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
			AuthorNameBorrowColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
			YearBorrowColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Borrows)cellData.getValue()).getYear()).asObject());
			isDesiredBorrowColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(((Borrows)cellData.getValue()).isDesired()).asObject());
			TopicBorrowColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
			EditionBorrowColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));

			btnForBorrows.setCellValueFactory(new PropertyValueFactory<>("askForDelay"));
			BorrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
			ReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));


			ArrayList <IEntity[]> result=msg.getObjectArray(); //get the array list received from the server

			//set to the results to the table
			for(int i=0;i<result.size();i++)
			{

				IEntity[] tempArray;
				tempArray = result.get(i);

				( (Copy)tempArray[0] ).setAskForDelay(new Button("delay"));

				Borrows borrowsTableList = new Borrows( ((Book)tempArray[1]).getBookName(), ((Book)tempArray[1]).getAuthorName(), 
						((Book)tempArray[1]).getYear(), ((Book)tempArray[1]).getTopic(), ((Book)tempArray[1]).isDesired(), ((Book)tempArray[1]).getEdition(),
						((Copy)tempArray[0]).getBorrowDate(), ((Copy)tempArray[0]).getReturnDate(), ((Copy)tempArray[0]).getAskForDelay());

				borrowsTable.getItems().add(borrowsTableList); 
			}
		});
	}
}
