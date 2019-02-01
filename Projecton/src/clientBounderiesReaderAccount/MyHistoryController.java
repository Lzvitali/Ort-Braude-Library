package clientBounderiesReaderAccount;

import com.jfoenix.controls.JFXButton;

import Common.History;
import Common.IEntity;
import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.Reservation;
import clientCommonBounderies.LogInController;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AClientCommonUtilities;
import clientConrollers.OBLClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MyHistoryController implements IGUIController 
{

	OBLClient client;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private TableView<IEntity> tableHistory;
    @FXML
    private TableColumn<IEntity,Integer> nomberOfRow;
    @FXML 
    private TableColumn<IEntity, String> ActionDateColumn; 

    @FXML 
    private TableColumn<IEntity, String> ActionColumn; 

    @FXML
    private TableColumn<IEntity, String> Details;

    @FXML 
    private JFXButton ExitBtn;

    @FXML
    void exitBtnClicked(ActionEvent event) 
    {
    	AClientCommonUtilities.backToStartPanel();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize()
    {
    	client=StartPanelController.connToClientController;
		client.setClientUI(this);
		
		ReaderAccount reader = new ReaderAccount();
		reader.setId(LogInController.currentID);
		ObjectMessage msg = new ObjectMessage(reader, "get reader account History", "History");
		client.setClientUI(this);
		client.handleMessageFromClient(msg); 

    }

	@Override
	public void display(ObjectMessage msg) 
	{
		if (msg.getMessage().equals("SetHistory"))
		{
			setHistiryToTable(msg);
		}
		else if(msg.getMessage().equals("NoHistory"))
		{
			//do nothing
		}
		
	}

	
	/**
	 * the function add data about users history from server to user gui window
	 * @param msg
	 */
	private void setHistiryToTable(ObjectMessage msg) 
	{
		
		Platform.runLater(()->
		{
			//set columns 
			ActionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
			Details.setCellValueFactory(new PropertyValueFactory<>("nameOfBook"));
			ActionDateColumn.setCellValueFactory(new PropertyValueFactory<>("actionDate"));
			nomberOfRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(((History)cellData.getValue()).getNo()).asObject());
			
			
			ArrayList <IEntity> result = msg.getObjectList(); //get the array list received from the server

			//set to the results to the table
			for(int i=0; i<result.size(); i++)
			{
				tableHistory.getItems().add(result.get(i));
			}
		});
	}
}
