package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import Common.Book;
import Common.Copy;
import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.User;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AClientCommonUtilities;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

/**
 * This Class is the controller for fxml file: BorrowBook.fxml
 */

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
		String checkResult =checkInputValidation();
		if(checkResult.equals("correct"))//if all fields correctly
		{
			ReaderAccount reader=new ReaderAccount(readerAccountID.getText());
			Copy copy=new Copy(CopyIdTextField.getText());
			ObjectMessage msg = new ObjectMessage(reader,copy,"CheckIfExist","ReaderAccount",reader.getId());
			client.setClientUI(this);
			client.handleMessageFromClient(msg);	
		}
		else 
		{
			AClientCommonUtilities.alertErrorWithOption(checkResult,"Wrong","Back");	
			
		}
	/*	ReaderAccount reader=new ReaderAccount(readerAccountID.getText());
		Copy copy=new Copy(CopyIdTextField.getText());
		ObjectMessage msg = new ObjectMessage(reader,copy,"CheckIfExist","ReaderAccount",reader.getId());
		client.handleMessageFromClient(msg);	*/


	}

	
	/**
	 * This function checks input validation for `borrow book` window 
	 * @return finalResult with string "correct" or with error message 
	 */
	private String checkInputValidation() 
	{

		String result,finalResult="";

	result=AValidationInput.checkValidationUser("UserID",readerAccountID.getText());//user id validation
		if(!result.equals("correct"))
		{
			finalResult+=result+'\n';
		}

		result=AValidationInput.checkValidationBook("copyID",CopyIdTextField.getText());//copy id validation
		if(!result.equals("correct"))
		{
		    finalResult+=result+'\n';
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


	@FXML
	void cancelBorrrow(ActionEvent event) 
	{
		AClientCommonUtilities.backToStartPanel();
	}

	@FXML
	void setDateForBorrowBook(KeyEvent event) 
	{
		Platform.runLater(()->
		{ 
			String returnDate; 

			if(CopyIdTextField.getText().equals("") || null == CopyIdTextField.getText())
			{
				returnDate= "0";
			}
			
			else
			{
				boolean onlyDigits = true;
				for(int i=0; i<CopyIdTextField.getText().length(); i++) 
				{
					if(CopyIdTextField.getText().charAt(i)<'0' ||CopyIdTextField.getText().charAt(i)>'9')
					{
						onlyDigits = false;
						break;
					}
				}
				
				if(onlyDigits)
				{
					returnDate=CopyIdTextField.getText();
				}
				else
				{
					returnDate= "0";
				}
				
			}

			Copy copy=new Copy(returnDate);
			ObjectMessage msg = new ObjectMessage(copy,"CheckReturnDate","Copy");
			System.out.println(copy.getCopyID()); 
			client.setClientUI(this);
			client.handleMessageFromClient(msg);
		});
	}
	
	/**
	 * This function set visible date of today in `borrow book` window
	 */
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
		
		if (msg.getMessage().equals("Desire book"))
		{
			String returnDate;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
			LocalDateTime now = LocalDateTime.now().plusDays(3);  
			returnDate= dtf.format(now);
			ReturnDateText.setText(returnDate);

		} 

		else if(msg.getMessage().equals("Not Desire book"))
		{
			String returnDate;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
			LocalDateTime now = LocalDateTime.now().plusDays(14);  
			returnDate= dtf.format(now);
			ReturnDateText.setText(returnDate);
		}
		else if(msg.getMessage().equals("Not exist book"))
		{
			ReturnDateText.setText(" ");
		}
		else if (msg.getNote().equals("ExistAndAvailable"))
		{
			//the User is Exist in DB 'status is active and the copy of book is available 
			AClientCommonUtilities.infoAlert(msg.getMessage(), msg.getNote());
			AClientCommonUtilities.backToStartPanel();
		}

		else if (msg.getNote().equals("ExistButNotActive") )
		{
			AClientCommonUtilities.alertErrorWithOption(msg.getMessage(), msg.getNote(),"Back");
		}
		else if(msg.getNote().equals("CopyAlreadyBorrowed"))
		{
			AClientCommonUtilities.alertErrorWithOption(msg.getMessage(), msg.getNote(),"Back");
		}
		else if (msg.getNote().equals("ReaderNotExist"))
		{
			
			AClientCommonUtilities.alertErrorWithOption(msg.getMessage(), msg.getNote(),"Back");
		}
		else if(msg.getNote().equals("NotExistCopy"))
		{
			AClientCommonUtilities.alertErrorWithOption(msg.getMessage(), msg.getNote(),"Back");
		}


	}
}
