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
import Common.ReaderAccount;
import Common.User;
import clientConrollers.AClientCommonUtilities;
import clientConrollers.AStartClient;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

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
	private TableColumn<IEntity, String> locationColumn;

	@FXML
	private TableColumn<IEntity, Boolean> inTheLibraryColumn;

	@FXML
	private TableColumn<IEntity, Date> ClosestReturnColumn;

	@FXML
	private TableColumn<IEntity, Integer> editionColumn;


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
		searchResultTable.setVisible(false);

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

		connToClientController.setClientUI(this);
		//if success do this and if selected book :
		Platform.runLater(()->
		{	
			boolean valid=false;
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
				if(AValidationInput.checkValidationBook("bookName", searchTextField.getText()).equals("correct"))
				{
					valid=true;
					askedBook.setBookName(searchTextField.getText());
				}
				else
				{
					valid=false;
					searchResultTable.setVisible(false);
					AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("bookName", searchTextField.getText()),"Invaild Input" ,"continue" );
					searchTextField.setText("");
				}

			}
			else if(selectedString.equals("Author name"))
			{
				if(AValidationInput.checkValidationBook("authorName", searchTextField.getText()).equals("correct"))
				{
					valid=true;
					askedBook.setAuthorName(searchTextField.getText());
				}
				else
				{
					valid=false;
					searchResultTable.setVisible(false);
					AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("authorName", searchTextField.getText()),"Invaild Input","continue" );
					searchTextField.setText("");
				}
			}
			else if(selectedString.equals("Topic"))
			{
				if(AValidationInput.checkValidationBook("topic", searchTextField.getText()).equals("correct"))
				{
					valid=true;
					askedBook.setTopic(searchTextField.getText());
				}
				else
				{
					valid=false;
					searchResultTable.setVisible(false);
					AClientCommonUtilities.alertErrorWithOption(AValidationInput.checkValidationBook("topic", searchTextField.getText()), "Invaild Input","continue" );
					searchTextField.setText("");
				}

			}
			else
			{
				valid=true;
				askedBook.setFreeSearch(searchTextField.getText());
			}
			if(askedBook.getBookName()!=null ||askedBook.getAuthorName()!=null || askedBook.getTopic() !=null || askedBook.getFreeSearch() !=null)
			{
				if(valid)
				{
					ObjectMessage sendToServer=new ObjectMessage(askedBook,"SearchBook","Book");
					connToClientController.handleMessageFromClient(sendToServer);   
				}
			}
		});
	}



	@Override
	public void display(ObjectMessage msg) 
	{
		if(msg.getMessage().equals("BookSearch"))
		{
			searchBookResult(msg);
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
			searchResultTable.setVisible(false);
			AClientCommonUtilities.infoAlert("No books found , try insert other value", "No books found");
		}
		else
		{
			Platform.runLater(()->
			{
				searchResultTable.setVisible(true);
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
		/*System.out.println(book.getBookName());
		System.out.println(bookName);*/

		FileChooser fc=new FileChooser();
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF File","*.pdf"));
		fc.setTitle("Save to PDF");
		fc.setInitialFileName(bookName+".pdf");
		File file =fc.showSaveDialog(AStartClient.primaryStagePanel);

		String str = null; 

		if (null != file)
		{
			str = file.getAbsolutePath();
			ObjectMessage sendToServer=new ObjectMessage(bookName, "getPDF");
			sendToServer.setExtra(str);
			connToClientController.handleMessageFromClient(sendToServer); 
		}

	}

	/**
	 * this function manages the connection with the server for file transfer
	 * @param msg - contains the size and the name of the file
	 */
	private void getPDF(ObjectMessage msg)
	{
		Platform.runLater(()->
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
		});
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

	@FXML
	void makeSearchBookWithEnterBtn(KeyEvent event)
	{
		connToClientController.setClientUI(this);
		if(event.getCode().equals(KeyCode.ENTER))
		{
			makeSearch(new ActionEvent());
		}
	}


}
