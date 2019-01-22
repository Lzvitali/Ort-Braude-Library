package clientBounderiesLibrarian;

import Common.Book;
import Common.IGUIController;
import Common.ObjectMessage;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.StartPanelController;
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

    @FXML
    void showBookInfo(KeyEvent event) 
    {
    	Platform.runLater(()->
		{ 
			int bookID;	

			if(bookIDTextField.getText().equals("") || null == bookIDTextField.getText())
			{
				bookID=0;
			}
			else
			{
				bookID=Integer.parseInt(bookIDTextField.getText());
			}

			Book book=new Book(bookID);
			ObjectMessage msg= new ObjectMessage(book,"showBookInfo","Book");
			client.handleMessageFromClient(msg);
		});

    }
    

	@FXML//Natasha do it 
	void updateAddNewBook(ActionEvent event)//update information about book
	{
		//String checkResult = validationFields();
		Book book;
		/*if(checkResult.equals("correct"))//if all fields correctly
		{*/
			String bookLocation;

			if( ( bookLocationLetter.getValue() == null  || (bookLocationLetter.getValue().toString()).equals(null) ) 
					|| ( BookLocationNumber.getValue() == null  || (BookLocationNumber.getValue().toString()).equals(null) ) )
			{
				bookLocation = "";
			}
			else
			{
				bookLocation =  bookLocationLetter.getValue() + "-" + BookLocationNumber.getValue();
			}

			String numOfCopies="";
			boolean isDesired= DesiredCheckBox.isSelected();
			book=new Book(BookTitleTextField.getText(), BookAuthorTextField.getText(),PublishedYearTextField.getText(),TopicTextField.getText(),String.valueOf(isDesired),EditionTextField.getText(),numOfCopies, bookLocation);
			ObjectMessage msg= new ObjectMessage(book,"changeBookInfo","Book");
			//book.setFileIsLoaded(isUploaded);
			client.handleMessageFromClient(msg);
		//}
	/*	else
		{
			AClientCommonUtilities.alertErrorWithOption(checkResult,"Wrong","Back");		
		}*/


		
	}

	


	@Override
	public void display(ObjectMessage msg) 
	{


		if (msg.getMessage().equals("FoundBook"))
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
			AClientCommonUtilities.alertErrorWithOption(msg.getMessage(), msg.getNote(),"Back");
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
