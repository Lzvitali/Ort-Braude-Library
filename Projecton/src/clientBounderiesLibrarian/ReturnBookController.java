package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import Common.Book;
import Common.Copy;
import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AClientCommonUtilities;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

/**
 * This Class is the controller for fxml file: ReturnBook.fxml
 */

public class ReturnBookController implements IGUIController {

	OBLClient client;
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXTextField CopyIdTextField;

	@FXML
	private JFXButton OkBtn;

	@FXML
	private JFXButton CancelBtn;

	@FXML
	private Label bookInfoLabel;

	@FXML
	private Label borrowerInfoLabel;

	@FXML
	private HBox borrowerInfoHBox;


	@FXML
	void initialize() 
	{
		client=StartPanelController.connToClientController;
		client.setClientUI(this);

		borrowerInfoHBox.setVisible(false);
		bookInfoLabel.setVisible(false); 
	}

	@FXML
	void CancelBtnClicked(ActionEvent event) 
	{
		AClientCommonUtilities.backToStartPanel();

	}

	/**
	 * This function send request to return copy that selected only if the input is valid
	 */
	@FXML
	void OkBtnClicked(ActionEvent event) 
	{
		int copyID = 0;	 
		String validationCheck = AValidationInput.checkValidationBook("copyID", CopyIdTextField.getText());
		
		if (validationCheck.equals("correct"))
		{	
			copyID=Integer.parseInt(CopyIdTextField.getText());
			Copy copy=new Copy(copyID);
			ObjectMessage msg = new ObjectMessage(copy,"ReturnCopy","Copy");
			client.setClientUI(this);
			client.handleMessageFromClient(msg);
		}
		else
		{
			AClientCommonUtilities.alertErrorWithOption(validationCheck, "Error", "Ok");
		}

	}

	/**
	 * This function will be committed each time a button on the keyboard pressed
	 * will ask the server for checking if a book with that CopyId exist
	 * @param event
	 */
	@FXML
	void showCopyInfo(KeyEvent event)
	{
		Platform.runLater(()->
		{ 
			int copyID = 0;	 
			String validationCheck = AValidationInput.checkValidationBook("copyID", CopyIdTextField.getText());
			boolean canContinue = true;
			
			if (validationCheck.equals("correct"))
			{	
				copyID=Integer.parseInt(CopyIdTextField.getText());
			}
			else if(validationCheck.equals("You must fill only numbers"))
			{
				borrowerInfoHBox.setVisible(false);
				bookInfoLabel.setVisible(true);

				bookInfoLabel.setText("Copy ID must contain only numbers");
				
				canContinue = false;
			}
			else
			{
				copyID=0;
			}

			if(canContinue)
			{
				Copy copy = new Copy(copyID);
				ObjectMessage msg= new ObjectMessage(copy,"showCopyInfo","Copy");
				client.setClientUI(this);
				client.handleMessageFromClient(msg);
			}
		});
	}


	public void display(ObjectMessage msg) 
	{
		if(msg.getNote().equals("successful ReturnCopy"))
		{
			String successful;
			if(msg.getMessage().equals("successful ReturnCopy.\nThe status of the reader account changed back to 'Active'"))
			{
				successful=msg.getMessage();				
			}
			else
			{
				successful="successful ReturnCopy";
			}
			AClientCommonUtilities.infoAlert(successful,"successful ReturnCopy");
			AClientCommonUtilities.backToStartPanel();
		}
		else if(msg.getNote().equals("Unsuccessful ReturnCopy"))
		{
			AClientCommonUtilities.alertErrorWithOption(msg.getMessage(), "Return book is unsuccessful", "Ok");
		}	
		else if(msg.getNote().equals("copy found and borrowed"))
		{
			Platform.runLater(()->
			{
				Book book=(Book) msg.getObjectList().get(0);
				ReaderAccount reader=(ReaderAccount) msg.getObjectList().get(1);

				borrowerInfoHBox.setVisible(true);
				bookInfoLabel.setVisible(true);

				bookInfoLabel.setText("Book name:  " + book.getBookName());
				borrowerInfoLabel.setText(reader.getFirstName() + " " + reader.getLastName());
			});
		}
		else if(msg.getNote().equals("copy found but NOT borrowed"))
		{
			Platform.runLater(()->
			{
				Book book=(Book) msg.getObjectList().get(0);

				borrowerInfoHBox.setVisible(true);
				bookInfoLabel.setVisible(true);

				bookInfoLabel.setText("Book name:  " + book.getBookName());
				borrowerInfoLabel.setText("Not borrowed");
			});
		}
		else if(msg.getNote().equals("copy not exist"))
		{
			Platform.runLater(()->
			{
				if(CopyIdTextField.getText().equals("") || CopyIdTextField.getText().equals(" "))
				{
					borrowerInfoHBox.setVisible(false);
					bookInfoLabel.setVisible(false); 
				}
				else
				{
					borrowerInfoHBox.setVisible(false);
					bookInfoLabel.setVisible(true);

					bookInfoLabel.setText("-Book with that copy ID doesn't exist-");
				}
			});
		}
	}
}
