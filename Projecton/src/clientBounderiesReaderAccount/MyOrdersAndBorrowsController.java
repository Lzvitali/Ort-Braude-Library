package clientBounderiesReaderAccount;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import Common.Book;
import Common.Copy;
import Common.IEntity;
import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import clientBounderiesLibrarian.StartPanelLibrarianController;
import clientCommonBounderies.LogInController;
import clientCommonBounderies.StartPanelController;
import clientConrollers.OBLClient;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MyOrdersAndBorrowsController implements IGUIController
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
    private TableView<IEntity> borrowsTable;
    
    @FXML
    private TableView<IEntity> ordersTable;


    @FXML
    void initialize() 
    {
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);
    	
    	ReaderAccount reader = new ReaderAccount();
    	
    	// 1 = Library Director , 2 = Librarian , 3 = reader account
    	
    	//if the reader account opening the window
    	if(LogInController.permission == 3)
    	{
        	reader.setId(LogInController.currentID);
    	}
    	
    	//if the librarian or the library director opening the window
    	else 
    	{
    		if(LogInController.permission == 2 || LogInController.permission == 2)
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
			//TODO show the table of the result
			
			BookNameBorrowColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
			AuthorNameBorrowColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
			YearBorrowColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Book)cellData.getValue()).getYear()).asObject());
			isDesiredBorrowColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(((Book)cellData.getValue()).isDesired()).asObject());
			TopicBorrowColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
			EditionBorrowColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
			
			btnForBorrows.setCellValueFactory(new PropertyValueFactory<>("askForDelay"));
			//BorrowDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(( (Copy)cellData.getValue()).getBorrowDate() ).asObject());
		
			
			ArrayList <IEntity[]> result=msg.getObjectArray();
			
			for(int i=0;i<result.size();i++)
			{
				
				IEntity[] tempArray;
				tempArray = result.get(i);
				
				( (Copy)tempArray[0] ).setAskForDelay(new Button(" "));
				
				/*
				if(((Book)result.get(i)).getReserve()!=null)
				{
					((Book)result.get(i)).getReserve().setOnAction(e -> AskToReserve(e));
				}
				searchResultTable.getItems().add(result.get(i));*/
				
				borrowsTable.getItems().add(tempArray[1]);
				//borrowsTable.getItems().add(tempArray[0]);
			}
		}
		else if((msg.getMessage()).equals("NoBorrows"))
		{
			//TODO: deal also with "no-results"
		}
		
		
		//TODO: do the reserves also
		
	}
}
