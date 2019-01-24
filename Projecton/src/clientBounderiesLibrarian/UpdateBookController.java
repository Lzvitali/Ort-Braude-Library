package clientBounderiesLibrarian;

import Common.Book;
import Common.IGUIController;
import Common.ObjectMessage;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AClientCommonUtilities;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class UpdateBookController implements IGUIController 
{

	OBLClient client;


	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXTextField BookTitleTextField;

	@FXML
	private JFXTextField BookAuthorTextField;

	@FXML
	private JFXTextField PublishedYearTextField;

	@FXML
	private JFXTextField EditionTextField;

	@FXML
	private JFXTextField TopicTextField;

	@FXML
	private JFXTextField numberOfCopies;

	@FXML
	private JFXCheckBox DesiredCheckBox;

	@FXML
	private ComboBox<String> bookLocationLetter;

	@FXML
	private ComboBox<String> BookLocationNumber;

	@FXML
	private JFXButton CancelBtn;

	@FXML
	private JFXButton updateBtn;

	@FXML
	private TextField bookIDTextField;


	@FXML
	private VBox bookInfoVBox;

	@FXML
	void initialize() 
	{
		client=StartPanelController.connToClientController;
		client.setClientUI(this);

		bookInfoVBox.setVisible(false); 
		combo();
	}

	//the function insert chars in to the Combo box in the gui window
	public void combo() 
	{
		ArrayList <String> s=new ArrayList<String>();

		for(int i=1; i<=6; i++)
		{
			s.add(Integer.toString(i));
		}

		ObservableList<String> list1 = FXCollections.observableArrayList(s);
		BookLocationNumber.setItems( list1);

		ArrayList <String> b=new ArrayList<String>();
		char ch = 'A';

		for(int i= 0; i<= ('Z'- 'A'); i++)
		{
			b.add(String.valueOf(ch));
			ch++;
		}
		ObservableList<String> list2 = FXCollections.observableArrayList(b);
		bookLocationLetter.setItems((ObservableList) list2);
	}

	@FXML
	void cancelBtnClicked(ActionEvent event) 
	{
		AClientCommonUtilities.backToStartPanel();

	}


	//the function activated every time when user type in `bookId` box and ask server if this book exist . 
	@FXML
	void showBookInfo(KeyEvent event) 
	{

		Platform.runLater(()->
		{ 

			int bookID;	

			if (AValidationInput.checkValidationBook("bookID", bookIDTextField.getText()).equals("correct"))
			{	
				bookID=Integer.parseInt(bookIDTextField.getText());

			}

			else
			{
				bookID=0;
			}

			Book book=new Book(bookID);
			ObjectMessage msg= new ObjectMessage(book,"showBookInfo","Book");
			client.handleMessageFromClient(msg);
		});

	}

	//the function activated when user press button for update book`s information
	@FXML
	void updateAddNewBook(ActionEvent event)//update information about book
	{
		String checkResult = validationFields();
		Book book;

		if(checkResult.equals("correct"))//if all fields correctly
		{
			String bookLocation;
			bookLocation =  bookLocationLetter.getValue() + "-" + BookLocationNumber.getValue();
			boolean isDesired= DesiredCheckBox.isSelected();
			book=new Book(bookIDTextField.getText(),BookTitleTextField.getText(), BookAuthorTextField.getText(),Integer.parseInt(PublishedYearTextField.getText()),TopicTextField.getText(),isDesired,Integer.parseInt(EditionTextField.getText()), bookLocation);
			ObjectMessage msg= new ObjectMessage(book,"changeBookInfo","Book");
			client.handleMessageFromClient(msg);
		}
		else
		{
			AClientCommonUtilities.alertErrorWithOption(checkResult,"Wrong","Back");		
		}



	}




	@Override
	public void display(ObjectMessage msg) 
	{


		if (msg.getMessage().equals("FoundBook"))//for showing all information about asked book
		{
			Platform.runLater(()->
			{
				bookInfoVBox.setVisible(true); 
				Book tempBook=(Book)msg.getObjectList().get(0);

				BookTitleTextField.setText(tempBook.getBookName());
				BookAuthorTextField.setText(tempBook.getAuthorName());
				PublishedYearTextField.setText(Integer.toString(tempBook.getDateOfBook()));
				EditionTextField.setText(Integer.toString(tempBook.getEdition()));
				TopicTextField.setText(tempBook.getTopic());
				DesiredCheckBox.setSelected(tempBook.getIsDesired());
				bookLocationLetter.setValue(String.valueOf(tempBook.getBookLocation().charAt(0)));
				BookLocationNumber.setValue(String.valueOf(tempBook.getBookLocation().charAt(2)));
			});
		}
		else if(msg.getMessage().equals("Wrong")) 
		{
			AClientCommonUtilities.alertErrorWithOption("You try to update data of book with data of another book that already in the DB.","Wrong","Back");
			//AClientCommonUtilities.alertErrorWithOption(msg.getMessage(), msg.getNote(),"Back");
		}
		else if(msg.getMessage().equals("UpdateBookInfoSccessfully"))
		{
			AClientCommonUtilities.infoAlert("You successfuly updated data of this book .","Successful" );
			AClientCommonUtilities.backToStartPanel();
		}
		else if(msg.getMessage().equals("Book not exist in DB"))
		{
			bookInfoVBox.setVisible(false); 
		}
	}


	//The function checks validation of input
	private String validationFields()
	{
		String result,finalResult="";

		result=AValidationInput.checkValidationBook("bookName",BookTitleTextField.getText());
		if(!result.equals("correct"))
		{
			finalResult+=result+'\n';
		}
		result=AValidationInput.checkValidationBook("authorName",BookAuthorTextField.getText());
		if(!result.equals("correct"))
		{
			finalResult+=result+'\n';
		}

		result=AValidationInput.checkValidationBook("dateOfBook",PublishedYearTextField.getText());
		if(!result.equals("correct"))
		{
			finalResult+=result+'\n';
		}

		result=AValidationInput.checkValidationBook("topic",TopicTextField.getText());
		if(!result.equals("correct"))
		{
			finalResult+=result+'\n';
		}

		if(EditionTextField.getText().equals("") || null == EditionTextField.getText())
		{
			finalResult+="Insert number of edition";

		}
		else
		{
			for(int i=0;i<EditionTextField.getText().length();i++)//check validation for edition text field
			{

				if(EditionTextField.getText().charAt(i)<'0' ||EditionTextField.getText().charAt(i)>'9')        
				{
					finalResult+="Edition must contain only numbers";
					break;
				}

			}
		}

		if(finalResult.equals(""))
		{
			return "correct";
		}

		else
		{
			return finalResult;
		}

	}













}
