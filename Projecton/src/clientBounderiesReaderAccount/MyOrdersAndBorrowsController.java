package clientBounderiesReaderAccount;

import java.net.URL;
import java.util.ResourceBundle;

import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import clientBounderiesLibrarian.StartPanelLibrarianController;
import clientCommonBounderies.LogInController;
import clientCommonBounderies.StartPanelController;
import clientConrollers.OBLClient;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class MyOrdersAndBorrowsController implements IGUIController
{
	
	OBLClient client;

    @FXML
    private TableColumn<?, ?> BookNameBorrowColumn;

    @FXML
    private TableColumn<?, ?> AuthorNameBorrowColumn;

    @FXML
    private TableColumn<?, ?> YearBorrowColumn;

    @FXML
    private TableColumn<?, ?> TopicBorrowColumn;

    @FXML
    private TableColumn<?, ?> BorrowDateColumn;

    @FXML
    private TableColumn<?, ?> ReturnDateColumn;

    @FXML
    private TableColumn<?, ?> btnForBorrows;

    @FXML
    private TableColumn<?, ?> BookNameReservColumn;

    @FXML
    private TableColumn<?, ?> AuthorNameReservColumn;

    @FXML
    private TableColumn<?, ?> YeareReservColumn;

    @FXML
    private TableColumn<?, ?> TopicReservColumn;

    @FXML
    private TableColumn<?, ?> BtnForOrders;

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
    	
    	ObjectMessage msg = new ObjectMessage(reader, "get borrows and reserves", "Copy");
    	client.handleMessageFromClient(msg); 
    	
    	
    }
    
    
    

	@Override
	public void display(ObjectMessage msg) 
	{
		// TODO Auto-generated method stub
		
	}
}
