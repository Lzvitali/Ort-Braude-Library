package clientBounderiesReaderAccount;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import Common.Book;
import Common.Borrow;
import Common.Copy;
import Common.IEntity;
import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.Reservation;
import clientBounderiesLibrarian.StartPanelLibrarianController;
import clientCommonBounderies.LogInController;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AClientCommonUtilities;
import clientConrollers.AStartClient;
import clientConrollers.OBLClient;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This class is the controller class for the window of 'borrows and reserves' of the reader account
 * same window will be displayed for both reader account, librarian and library director
 */

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
    private TableColumn<IEntity, Button> bookLostColumn;

	@FXML
	private TableView<Borrow> borrowsTable;

	@FXML
	private TableView<IEntity> ordersTable;

	@FXML
	private TableColumn<?, ?> reservationDateColumn;

	ObservableList<IEntity> list1;
	ObservableList<IEntity> list2;

	private ReaderAccount reader;

	private Copy tempCopy;

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
		client.setClientUI(this);
		client.handleMessageFromClient(msg); 
		//TODO: get the reserves in the display

	}




	@Override
	public void display(ObjectMessage msg) 
	{
		if((msg.getMessage()).equals("TheBorrows"))
		{
			borrowsTable.getItems().clear();
			setBorrowsResults(msg);
			ordersTable.getItems().clear();
			ObjectMessage newMsg = new ObjectMessage(reader, "get reserves", "Reservation");
			client.setClientUI(this);
			client.handleMessageFromClient(newMsg);	
		}
		else if((msg.getMessage()).equals("NoBorrows"))
		{
			ordersTable.getItems().clear();
			ObjectMessage newMsg = new ObjectMessage(reader, "get reserves", "Reservation");
			client.setClientUI(this);
			client.handleMessageFromClient(newMsg);	
		}
		else if((msg.getMessage()).equals("TheReserves"))
		{
			ordersTable.getItems().clear();
			setReservationsTable(msg); 
		}
		else if((msg.getMessage()).equals("NoReserves"))
		{
			//do nothing...
		}
		else if((msg.getMessage()).equals("Delayed"))
		{
			AClientCommonUtilities.infoAlert("The borrow successfully delayed", "Success");

			//set again the table view
			borrowsTable.getItems().clear();	
			ObjectMessage msg2 = new ObjectMessage(reader, "get borrows", "Copy");
			client.setClientUI(this);
			client.handleMessageFromClient(msg2); 
		} 
		else if((msg.getMessage()).equals("ReservationCanceled"))
		{
			AClientCommonUtilities.infoAlert("The reservation was successfully canceled", "Success");

			//set again the table view
			ordersTable.getItems().clear();
			ObjectMessage newMsg = new ObjectMessage(reader, "get reserves", "Reservation");
			client.setClientUI(this);
			client.handleMessageFromClient(newMsg);	
		}
		else if((msg.getMessage()).equals("ReservationImplemented"))
		{
			AClientCommonUtilities.infoAlert("The reservation was successfully implemented", "Success");
			
			//set again the table view
			borrowsTable.getItems().clear();	
			ObjectMessage msg2 = new ObjectMessage(reader, "get borrows", "Copy");
			client.setClientUI(this);
			client.handleMessageFromClient(msg2); 			
		}
		else if((msg.getMessage()).equals("ReservationNotImplemented"))
		{
			AClientCommonUtilities.alertErrorWithOption(msg.getNote(), "Unuccessfull", "Ok"); 	
		}

	}


	/**
	 * this function sets the reserves of the reader account to the table
	 * @param msg- received answer from the server
	 */
	private void setReservationsTable(ObjectMessage msg) 
	{
		Platform.runLater(()->
		{
			//set columns 
			BookNameReservColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
			AuthorNameReservColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
			YeareReservColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Reservation)cellData.getValue()).getYear()).asObject());
			isDesiredReserveColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(((Reservation)cellData.getValue()).getIsDesired()).asObject());
			TopicReservColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
			editionReserveColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));

			BtnForOrders.setCellValueFactory(new PropertyValueFactory<>("reservationTableBtn"));
			reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


			ArrayList <IEntity> result = msg.getObjectList(); //get the array list received from the server

			//set to the results to the table
			for(int i=0;i<result.size();i++)
			{
				//if the reader account opening the window
				if(LogInController.permission == 3)
				{
					((Reservation)result.get(i)).setReservationTableBtn(new Button("Cancel"));
					Reservation resevation = (Reservation)result.get(i);
					((Reservation)result.get(i)).getReservationTableBtn().setOnAction(e -> cancelReservation(e,resevation ));
				}

				//if the librarian or the library director opening the window
				else 
				{
					if(LogInController.permission == 1 || LogInController.permission == 2)
					{
						BtnForOrders.setText("Implement reservation"); 
						((Reservation)result.get(i)).setReservationTableBtn(new Button("Implement"));
						Reservation resevation = (Reservation)result.get(i);
						((Reservation)result.get(i)).getReservationTableBtn().setOnAction(e -> implementReservation(e, resevation));
					} 
				}

				ordersTable.getItems().add(result.get(i)); 
			}
		});

	}


	private void implementReservation(ActionEvent e, Reservation reservation) 
	{
		ObjectMessage newMsg = new ObjectMessage(reader, reservation, "implement reservation", "Reservation");
		client.setClientUI(this);
		client.handleMessageFromClient(newMsg);
	}




	private void cancelReservation(ActionEvent e, Reservation reservation)  
	{
		ObjectMessage newMsg = new ObjectMessage(reader, reservation, "cancel reservation", "Reservation");
		client.setClientUI(this);
		client.handleMessageFromClient(newMsg);
	}




	/**
	 * this function sets the borrows of the reader account to the table
	 * (same for the reader account and the librarian)
	 * @param msg- received answer from the server
	 */
	void setBorrowsResults(ObjectMessage msg)
	{ 
		Platform.runLater(()->
		{
			//set columns
			BookNameBorrowColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
			AuthorNameBorrowColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
			YearBorrowColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Borrow)cellData.getValue()).getYear()).asObject());
			isDesiredBorrowColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(((Borrow)cellData.getValue()).isDesired()).asObject());
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

				tempCopy = new Copy((Copy)tempArray[0]);

				( (Copy)tempArray[0] ).setAskForDelay(new Button("delay"));
				( (Copy)tempArray[0] ).getAskForDelay().setOnAction(e -> askForDelay(e, (Copy)tempArray[0]));

				if(LogInController.permission == 1 || LogInController.permission == 2)
				{
					( (Copy)tempArray[0] ).setLostCopy(new Button("handle"));
					( (Copy)tempArray[0] ).getAskForDelay().setOnAction(e -> handleLostBook(e, (Copy)tempArray[0]));
				}

				Borrow borrowsTableList = new Borrow( ((Book)tempArray[1]).getBookName(), ((Book)tempArray[1]).getAuthorName(), 
						((Book)tempArray[1]).getYear(), ((Book)tempArray[1]).getTopic(), ((Book)tempArray[1]).isDesired(), ((Book)tempArray[1]).getEdition(),
						((Copy)tempArray[0]).getBorrowDate(), ((Copy)tempArray[0]).getReturnDate(), ((Copy)tempArray[0]).getAskForDelay());
				if(LogInController.permission == 1 || LogInController.permission == 2)
				{
					bookLostColumn.setCellValueFactory(new PropertyValueFactory<>("LostCopy"));
					borrowsTableList.setLostCopy(((Copy)tempArray[0]).getLostCopy()); 
				}

				borrowsTable.getItems().add(borrowsTableList); 
			}
		});
	}



	private void handleLostBook(ActionEvent e, Copy copy) 
	{
		
	}




	/**
	 * this function handles the event for pressing the button of 'ask for delay' for both reader account, librarian and library director
	 * @param e-the event
	 * @param copy-the copy instance 
	 */
	private void askForDelay(ActionEvent e, Copy copy) 
	{ 
		if( !copy.isCanDelay() )
		{ 
			if(copy.getReasonForCantDelay().equals("The date of return is the most updated"))
			{
				AClientCommonUtilities.alertErrorWithOption("The date of return is the most updated", "Rejection", "Ok");
			}
			else
			{
				AClientCommonUtilities.alertErrorWithOption("you can't delay the return date for this book", "Rejection", "Ok");
			}		
		}
		else
		{
			Copy msgCopy = new Copy(copy);
			ObjectMessage msg = new ObjectMessage(msgCopy, "ask for delay", "Copy");
			tempCopy=copy;
			client.setClientUI(this);
			client.handleMessageFromClient(msg);

		}

	}
}
