package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import Common.Book;
import Common.Copy;
import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.User;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class BorrowBookController implements IGUIController
{

	OBLClient client;


	@FXML
	private JFXTextField readerAccountID;

	@FXML
	private JFXTextField CopyIdTextField;

	@FXML
	private Text BorrowDateText;

	@FXML
	private Text ReturnDateText;

	@FXML
	private JFXButton CancelBtn;

	@FXML
	private JFXButton AproveBtn;


	@FXML
	void initialize() 
	{
		client=StartPanelController.connToClientController;
		client.setClientUI(this);
		setVisibleDateOfToday();
	}

	@FXML
	void aproveBorrowBook(ActionEvent event) 
	{
//		String checkResult =checkInputValidation();
//
//		if(checkResult.equals("correct"))//if all fields correctly
//		{
//			ReaderAccount reader=new ReaderAccount(readerAccountID.getText());
//			ObjectMessage msg = new ObjectMessage(reader,"CheckIfExist","ReaderAccount",reader.getId());
//			client.handleMessageFromClient(msg);
//			/*bookForSend = new (bookName, authorName, year,edition );
//			ObjectMessage msg= new ObjectMessage(bookForSend,"setLocation","Book");
//			client.handleMessageFromClient(msg);*/
//			//returnDate=BookTitleTextField.getText();
//			
//		}
//		else 
//		{
//			AClientCommonUtilities.alertErrorWithOption(checkResult,"Wrong","Back");	
//		}
		
		
		ReaderAccount reader=new ReaderAccount(readerAccountID.getText());
		Copy copy=new Copy(CopyIdTextField.getText());
		ObjectMessage msg = new ObjectMessage(reader,copy,"CheckIfExist","ReaderAccount",reader.getId());
		client.handleMessageFromClient(msg);
	}

	//func to check validation input
	private String checkInputValidation() 
	{

		String result,finalResult="";

		result=AValidationInput.checkValidationUser("UserID",readerAccountID.getText());
		if(!result.equals("correct"))
		{
			finalResult+=result+'\n';
		}
		
		result=AValidationInput.checkValidationBook("bookID",CopyIdTextField.getText());
		if(!result.equals("correct"))
		{
			finalResult+=result+'\n';
		}
		return finalResult;
	}

	
	@FXML
	void cancelBorrrow(ActionEvent event) 
	{
		AClientCommonUtilities.backToStartPanel();
	}

	@FXML
	void setDateForBorrowBook(ActionEvent event) 
	{
		String returnDate; 

		if(CopyIdTextField.getText().equals("") || null == CopyIdTextField.getText())
		{
			returnDate= " ";
		}
		
	}
	public void setVisibleDateOfToday()
	{
		String borrowDate;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
		LocalDateTime now = LocalDateTime.now();  
		borrowDate= dtf.format(now);
		BorrowDateText.setText(borrowDate);
	}


	@Override
	public void display(ObjectMessage msg) 
	{
		if (msg.getNote().equals("Successfull"))
		{
			//if book was borrowed successfully
			AClientCommonUtilities.infoAlert(msg.getMessage(), msg.getNote());
			AClientCommonUtilities.backToStartPanel();

		}
		
		if (msg.getNote().equals("ExistAndAvailable"))
		{
			//the User is Exist in DB 'status is active and the copy of book is available 
			AClientCommonUtilities.infoAlert(msg.getMessage(), msg.getNote());
			AClientCommonUtilities.backToStartPanel();
		}
	
		else if (msg.getNote().equals("ExistButNotActive")|| msg.getNote().equals("CopyAlreadyBorrowed"))
		{
			AClientCommonUtilities.alertErrorWithExit(msg.getMessage(), msg.getNote());
			AClientCommonUtilities.backToStartPanel();
		}
		else if (msg.getNote().equals("ReaderNotExist")||msg.getNote().equals("CopyNotExist"))
		{
			AClientCommonUtilities.alertErrorWithOption(msg.getMessage(), msg.getNote(),"Back");
		}
		

	}
}
