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
import clientCommonBounderies.AStartClient;
import clientCommonBounderies.LogInController;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

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
import javafx.stage.FileChooser;
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

	public static String readerAccountID; //when the librarian wants to access into the borrows and reserves of a reader account after a search 
	private static String tempReaderAccountID;


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
    private TableColumn<IEntity, String> locationColumn;

    @FXML
    private TableColumn<IEntity, Boolean> inTheLibraryColumn;

    @FXML
    private TableColumn<IEntity, Date> ClosestReturnColumn;
    
    @FXML
    private TableColumn<IEntity, Integer> editionColumn;
    
    @FXML
    private Button changeBookInfoBtn;

    @FXML
    private TabPane TabPaneSelect;

	private ToggleGroup toggleGroupForBooks; 
	private ToggleGroup toggleGroupForAccounts; 



	//for the Library Director only
	///////////////////////////////////////////////////////////////////////////////////

	@FXML // fx:id="reportBtn"
	private Button reportBtn; 

	@FXML
	private TableColumn<IEntity, Button> freezeColumn;

	@FXML
	void openReportWindow(ActionEvent event) 
	{
		AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesLibrarian/Reports.fxml","Reporst");
	}
	///////////////////////////////////////////////////////////////////////////////////



	@FXML // This method is called by the FXMLLoader when initialization is complete
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
			if(AValidationInput.checkValidationBook("bookName", searchBookTextField.getText()).equals("correct"))
			{
				askedBook.setBookName(searchBookTextField.getText());
			}
			else
			{
				AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("bookName", searchBookTextField.getText()),"Invaild Input" ,"continue" );
				searchBookTextField.setText("");
			}
				
		}
		else if(selectedString.equals("Author name"))
		{
			if(AValidationInput.checkValidationBook("authorName", searchBookTextField.getText()).equals("correct"))
			{
				askedBook.setAuthorName(searchBookTextField.getText());
			}
			else
			{
				AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("authorName", searchBookTextField.getText()),"Invaild Input","continue" );
				searchBookTextField.setText("");
			}
		}
		else if(selectedString.equals("Topic"))
		{
			if(AValidationInput.checkValidationBook("topic", searchBookTextField.getText()).equals("correct"))
			{
				askedBook.setTopic(searchBookTextField.getText());
			}
			else
			{
				AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("topic", searchBookTextField.getText()), "Invaild Input","continue" );
				searchBookTextField.setText("");
			}
		}
		else
		{
			askedBook.setFreeSearch(searchBookTextField.getText());;
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
			if(AValidationInput.checkValidationUser("UserID", searchReaderAccountSearchField.getText()).equals("correct"))
			{
				askedReader.setId(searchReaderAccountSearchField.getText());
			}
			else
			{
				AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("UserID", searchReaderAccountSearchField.getText()), "Invaild Input","continue" );
				searchReaderAccountSearchField.setText("");
			}
			
		}
		else if(selectedString.equals("First name"))
		{
			if(AValidationInput.checkValidationUser("First Name", searchReaderAccountSearchField.getText()).equals("correct"))
			{
				askedReader.setFirstName(searchReaderAccountSearchField.getText());
			}
			else
			{
				AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("First Name", searchReaderAccountSearchField.getText()), "Invaild Input","continue" );
				searchReaderAccountSearchField.setText("");
			}
			
		}
		else if(selectedString.equals("Last name"))
		{
			if(AValidationInput.checkValidationUser("Last Name", searchReaderAccountSearchField.getText()).equals("correct"))
			{
				askedReader.setLastName(searchReaderAccountSearchField.getText());
			}
			else
			{
				AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("Last Name", searchReaderAccountSearchField.getText()), "Invaild Input","continue" );
				searchReaderAccountSearchField.setText("");
			}
			
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
		else if(msg.getMessage().equals("successful log out"))
		{
			AClientCommonUtilities.loadStartPanelWindow(getClass(),"/clientCommonBounderies/StartPanel.fxml","Start Panel");
		}
		else if(msg.getMessage().equals("pfdRecieve"))
		{
			getPDF(msg);
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
				editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
				locationColumn.setCellValueFactory(new PropertyValueFactory<>("bookLocation"));
				inTheLibraryColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(((Book)cellData.getValue()).getInLibrary()).asObject());
				ClosestReturnColumn.setCellValueFactory(new PropertyValueFactory<>("closetReturn"));
				int i;
				ArrayList <IEntity> result=msg.getObjectList();
				for(i=0;i<result.size();i++)
				{
					((Book)result.get(i)).setDetails(new Button("Open PDF"));
					Book book = ((Book)result.get(i));
					((Book)result.get(i)).getDetails().setOnAction(e -> openPDF(e,book));
					searchResultTable.getItems().add(result.get(i));
				}
			});

		}
	}
	
	/**
	 * this function displays to the user file chooser and sends to the server the request for the pdf
	 * @param e -event
	 * @param book - the book instance
	 */
	private void openPDF(ActionEvent e, Book book)
	{
		String bookName = book.getBookName() + " " + book.getAuthorName() + " " + book.getYear() + " " + book.getEdition();

		FileChooser fc=new FileChooser();
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF File","*.pdf"));
		fc.setTitle("Save to PDF");
		fc.setInitialFileName(bookName+".pdf");
		File file =fc.showSaveDialog(null);

		String str = null; 

		if (null != file)
		{
			str = file.getAbsolutePath();
			ObjectMessage sendToServer=new ObjectMessage(bookName, "getPDF");
			sendToServer.setExtra(str);
			client.handleMessageFromClient(sendToServer); 
		}

	}

	/**
	 * this function manages the connection with the server for file transfer
	 * @param msg - contains the size and the name of the file
	 */
	private void getPDF(ObjectMessage msg)
	{
		Socket sock;
		try
		{
			sock = new Socket("localhost", 5643);
			byte[] mybytearray = new byte[Integer.parseInt(msg.getNote())];
			InputStream is = sock.getInputStream();
			FileOutputStream fos = new FileOutputStream(msg.getExtra());
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			int bytesRead = is.read(mybytearray,0, Integer.parseInt(msg.getNote()));
			int current = bytesRead; 

			do 
			{
				bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
				if(bytesRead >= 0) 
				{
					current += bytesRead;
				}
			} while(bytesRead < -1);

			bos.write(mybytearray, 0 , current);
			bos.flush();

			fos.close();
			bos.close();
			sock.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
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
				if(LogInController.permission==1)
				{
					freezeColumn.setCellValueFactory(new PropertyValueFactory<>("freeze"));
				}
				int i;
				ArrayList <IEntity> result=msg.getObjectList();
				for(i=0;i<result.size();i++)
				{

					((ReaderAccount)result.get(i)).setBorrowsAndReserves(new Button("Open"));
					
					//readerAccountID = ((ReaderAccount)result.get(i)).getId();
					tempReaderAccountID = ((ReaderAccount)result.get(i)).getId();
					((ReaderAccount)result.get(i)).getBorrowsAndReserves().setOnAction(e -> openBorrowsAndReserves(e,tempReaderAccountID));
					
					if(LogInController.permission==1)
					{
						if(((ReaderAccount)result.get(i)).getStatus().equals("Active"))
						{
							((ReaderAccount)result.get(i)).setFreeze(new Button("Freeze"));
						}
						else
						{
							((ReaderAccount)result.get(i)).setFreeze(new Button("Activate"));
						}

					}
					searchReaderAccountTable.getItems().add(result.get(i));
				}
			});

		}
	}
	
	void openBorrowsAndReserves(ActionEvent e, String readerID) 
	{
		readerAccountID = readerID;
		AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesReaderAccount/BorrowsAndReservations.fxml","Orders and borrows");
	}




    @FXML
    void openChangeBookInfo(ActionEvent event) 
    {
    	AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesLibrarian/UpdateBook.fxml","Update book info");
    }
}

