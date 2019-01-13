package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;

import Common.Book;
import Common.IGUIController;
import Common.ObjectMessage;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

public class AddBookController implements IGUIController
{
	OBLClient client;
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

    @FXML // fx:id="TopicTextField"
    private JFXTextField TopicTextField; 
    @FXML
    private JFXTextField BookTitleTextField;

    @FXML 
    private JFXTextField numberOfCopies; 

	@FXML
	private JFXTextField BookAuthorTextField;

	@FXML
	private JFXTextField PublishedYearTextField;

	@FXML
	private JFXTextField EditionTextField;


	@FXML
	private JFXCheckBox DesiredCheckBox;

	@FXML
	private ChoiceBox<String> TopicChoiceBox;

	@FXML
	private DatePicker PurchaseDateDatePicker;

	@FXML
	private Button UploadBtn;

	@FXML
	private JFXButton CancelBtn;

	@FXML
	private JFXButton SaveBtn;

	
	//function for cancel button. if librarian doesn't want  continue to add a book
	@FXML
	void cancelBtnClicked(ActionEvent event) 
	{
		AClientCommonUtilities.backToStartPanel();
	}

	//add new book or only copy
	@FXML
	void saveAddNewBook(ActionEvent event) 
	{ 
		/*
		if(validationFields().equals("correct"))//if all fields correctly
		{
			System.out.println("all right)))))");
			boolean isDesired= DesiredCheckBox.isSelected();
			Book book=new Book(BookTitleTextField.getText(), BookAuthorTextField.getText(),PublishedYearTextField.getText(),TopicTextField.getText(),String.valueOf(isDesired),EditionTextField.getText(),numberOfCopies.getText());
			System.out.println(String.valueOf(isDesired));
			ObjectMessage msg= new ObjectMessage(book,"addBook","Book");
			client.handleMessageFromClient(msg);
		}
		else
		{
			System.out.println("Errorrrrrrr!");
			AClientCommonUtilities.alertError("The fields you filled are not correctly.","Error"); // open error message
		}*/
		
		boolean isDesired= DesiredCheckBox.isSelected();
		Book book=new Book(BookTitleTextField.getText(), BookAuthorTextField.getText(),PublishedYearTextField.getText(),TopicTextField.getText(),String.valueOf(isDesired),EditionTextField.getText(),numberOfCopies.getText());
		System.out.println(String.valueOf(isDesired));
		ObjectMessage msg= new ObjectMessage(book,"addBook","Book");
		client.handleMessageFromClient(msg);
		
		
	}
	
	
	//lo asiti the kashur lhaalat kovetz
	@FXML
	void uploadTableOfContents(ActionEvent event) 
	{
		
	}

	
	@FXML
	void initialize() 
	{
		client=StartPanelController.connToClientController;
		client.setClientUI(this);
		

	}
	
	//check validation
	 private String validationFields()
	 {
		 String result,finalResult="";

		 result=AValidationInput.checkValidationBook("bookName",BookTitleTextField.getText());
		 if(!result.equals("correct"))
		 {
			 finalResult+=result+"\n";
		 }
			 

		 result=AValidationInput.checkValidationBook("authorName",BookAuthorTextField.getText());
		 if(!result.equals("correct"))
		 {
			 finalResult+=result+"\n";
		 }

		 result=AValidationInput.checkValidationBook("dateOfBook",PublishedYearTextField.getText());
		 if(!result.equals("correct"))
		 {
			 finalResult+=result+"\n";
		 }

		 result=AValidationInput.checkValidationBook("topic",TopicChoiceBox.getAccessibleText());
		 if(!result.equals("correct"))
		 {
			 finalResult+=result+"\n";
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

	//lo asiti
	@Override
	public void display(ObjectMessage msg)
	{
		// TODO Auto-generated method stub
		
	}
}
