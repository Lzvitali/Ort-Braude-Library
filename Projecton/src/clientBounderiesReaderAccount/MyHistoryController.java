package clientBounderiesReaderAccount;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class MyHistoryController 
{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="ActionDateColumn"
    private TableColumn<?, ?> ActionDateColumn; // Value injected by FXMLLoader

    @FXML // fx:id="ActionColumn"
    private TableColumn<?, ?> ActionColumn; // Value injected by FXMLLoader

    @FXML // fx:id="Details"
    private TableColumn<?, ?> Details; // Value injected by FXMLLoader

    @FXML // fx:id="ExitBtn"
    private JFXButton ExitBtn; // Value injected by FXMLLoader

    @FXML
    void exitBtnClicked(ActionEvent event) 
    {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert ActionDateColumn != null : "fx:id=\"ActionDateColumn\" was not injected: check your FXML file 'MyHistory.fxml'.";
        assert ActionColumn != null : "fx:id=\"ActionColumn\" was not injected: check your FXML file 'MyHistory.fxml'.";
        assert Details != null : "fx:id=\"Details\" was not injected: check your FXML file 'MyHistory.fxml'.";
        assert ExitBtn != null : "fx:id=\"ExitBtn\" was not injected: check your FXML file 'MyHistory.fxml'.";

    }
}
