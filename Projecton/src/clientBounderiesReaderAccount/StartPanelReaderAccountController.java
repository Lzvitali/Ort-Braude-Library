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
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.stage.FileChooser;
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
    private TableColumn<IEntity, String> locationColumn;

    @FXML
    private TableColumn<IEntity, Boolean> inTheLibraryColumn;

    @FXML
    private TableColumn<IEntity, Date> ClosestReturnColumn;
    
    @FXML
    private TableColumn<IEntity, Integer> editionColumn;
    
    
    private ToggleGroup toggleGroupForBooks; 
    
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
    		if(AValidationInput.checkValidationBook("bookName", searchTextField.getText()).equals("correct"))
			{
				askedBook.setBookName(searchTextField.getText());
			}
			else
			{
				AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("bookName", searchTextField.getText()),"Invaild Input" ,"continue" );
				searchTextField.setText("");
			}
    	}
    	else if(selectedString.equals("Author name"))
    	{
    		if(AValidationInput.checkValidationBook("authorName", searchTextField.getText()).equals("correct"))
			{
				askedBook.setAuthorName(searchTextField.getText());
			}
			else
			{
				AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("authorName", searchTextField.getText()),"Invaild Input","continue" );
				searchTextField.setText("");
			}
    	}
    	else if(selectedString.equals("Topic"))
    	{
    		if(AValidationInput.checkValidationBook("topic", searchTextField.getText()).equals("correct"))
			{
				askedBook.setTopic(searchTextField.getText());
			}
			else
			{
				AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("topic", searchTextField.getText()), "Invaild Input","continue" );
				searchTextField.setText("");
			}
    	}
    	else
		{
			askedBook.setFreeSearch(searchTextField.getText());
		}
    	ObjectMessage sendToServer=new ObjectMessage(askedBook,"SearchBook","Book");
    	client.handleMessageFromClient(sendToServer);   
    }

    @FXML
    void openBorrowsAndReserves(ActionEvent event) 
    {
    	AClientCommonUtilities.loadWindow(getClass(),"/clientBounderiesReaderAccount/BorrowsAndReservations.fxml","My orders and borrows");
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
		else if(msg.getMessage().equals("pfdRecieve"))
		{
			getPDF(msg);
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
				editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
				locationColumn.setCellValueFactory(new PropertyValueFactory<>("bookLocation"));
				inTheLibraryColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(((Book)cellData.getValue()).getInLibrary()).asObject());
				ClosestReturnColumn.setCellValueFactory(new PropertyValueFactory<>("closetReturn"));
				reserveBtn.setCellValueFactory(new PropertyValueFactory<>("reserve"));
			
				int i;
				ArrayList <IEntity> result=msg.getObjectList();
				for(i=0;i<result.size();i++)
				{
					
					((Book)result.get(i)).setDetails(new Button("Open PDF"));
					Book book = ((Book)result.get(i));
					((Book)result.get(i)).getDetails().setOnAction(e -> openPDF(e,book));
					
					if(((Book)result.get(i)).getNumberOfCopies()==0)
					{
						((Book)result.get(i)).setReserve(new Button("Reserve"));
					}
					if(((Book)result.get(i)).getReserve()!=null)
					{
						((Book)result.get(i)).getReserve().setOnAction(e -> AskToReserve(e));
					}
					searchResultTable.getItems().add(result.get(i));
				}	
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
			sock = new Socket(AStartClient.serverIP, 5643);
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

	@FXML
	void AskToReserve(ActionEvent event)
	{
		String id=((Button)event.getSource()).getAccessibleText();
    	ObjectMessage objectMessage;
    	Book book=new Book();
    	book.setBookID(Integer.parseInt(id));
    	objectMessage=new ObjectMessage(book,"reserveBook","Reservation");
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
    	else if(msg.getNote().equals("HaveCopy"))
    	{
    		AClientCommonUtilities.infoAlert("You already got copy of this book", "Already Got Copy");
    	}
    	else
    	{
    		searchResultTable.getItems().clear();
    		searchTextField.clear();
    		AClientCommonUtilities.infoAlert("You reserved this book", "Reserved");
    	}
    }
}
