package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;

import Common.IGUIController;
import Common.ObjectMessage;
import clientCommonBounderies.StartPanelController;
import clientConrollers.OBLClient;

import java.net.URL;
import java.util.ResourceBundle;
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

	@FXML
	private JFXTextField BookTitleTextField;

	@FXML
	private JFXTextField BookAuthorTextField;

	@FXML
	private JFXTextField PublishedYearTextField;

	@FXML
	private JFXTextField EditionTextField;

	@FXML
	private JFXTextField ShelfNumberTextField;

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

	
	//lo asiti
	@FXML
	void cancelBtnClicked(ActionEvent event) 
	{
		client.handleMessageFromClient(new ObjectMessage("booooom2"));
	}

	//lo asiti
	@FXML
	void saveAddNewBook(ActionEvent event) 
	{
		client.handleMessageFromClient(new ObjectMessage("booooom2"));
	}

	//lo asiti
	@FXML
	void uploadTableOfContents(ActionEvent event) 
	{
		client.handleMessageFromClient(new ObjectMessage("booooom2"));
	}

	@FXML
	void initialize() 
	{
		client=StartPanelController.connToClientController;
		client.setClientUI(this);

	}

	//lo asiti
	@Override
	public void display(ObjectMessage msg)
	{
		// TODO Auto-generated method stub
		
	}
}
