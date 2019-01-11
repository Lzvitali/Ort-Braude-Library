package clientBounderiesReaderAccount;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import Common.IGUIController;
import Common.IGUIStartPanel;
import Common.ObjectMessage;
import Common.User;
import clientCommonBounderies.LogInController;
import clientCommonBounderies.StartPanelController;
import clientConrollers.OBLClient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartPanelReaderAccountController implements IGUIController,IGUIStartPanel
{

	OBLClient client;
	
	
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
    private Button historyBtn1;

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
    private TableView<?> searchResultTable;

    @FXML
    private TableColumn<?, ?> bookNameColumn;

    @FXML
    private TableColumn<?, ?> authorNameColumn;

    @FXML
    private TableColumn<?, ?> yearColumn;

    @FXML
    private TableColumn<?, ?> topicColumn;

    @FXML
    private TableColumn<?, ?> isDesiredColumn;

    @FXML
    private TableColumn<?, ?> viewIntroColumn;

    @FXML
    private TableColumn<?, ?> reserveBtn;
    
    
    
    @FXML
    void initialize() 
    {
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);
    }

    @FXML
    void makeLogOut(ActionEvent event) 
    {
    	//change the status of that user in the DB
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
		}
    }

    @FXML
    void makeSearch(ActionEvent event) 
    {

    }

    @FXML
    void openBorrowsAndReserves(ActionEvent event) 
    {

    }

    @FXML
    void openHistory(ActionEvent event) 
    {

    }

	@Override
	public int getActivateWindows() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setActivateWindows(int newWindows) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void display(ObjectMessage msg) {
		// TODO Auto-generated method stub
		
	}


}
