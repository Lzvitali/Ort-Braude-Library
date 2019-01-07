/**
 * Sample Skeleton for 'StartPanel.fxml' Controller Class
 */

package clientBounderies;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartPanelController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="loginBtn"
    private Button loginBtn; // Value injected by FXMLLoader

    @FXML // fx:id="searchTextField"
    private JFXTextField searchTextField; // Value injected by FXMLLoader

    @FXML // fx:id="searchBtn"
    private Button searchBtn; // Value injected by FXMLLoader

    @FXML // fx:id="bookNameRB"
    private JFXRadioButton bookNameRB; // Value injected by FXMLLoader

    @FXML // fx:id="authorNameRB"
    private JFXRadioButton authorNameRB; // Value injected by FXMLLoader

    @FXML // fx:id="topicRB"
    private JFXRadioButton topicRB; // Value injected by FXMLLoader

    @FXML // fx:id="freeSearchRB"
    private JFXRadioButton freeSearchRB; // Value injected by FXMLLoader

    @FXML
    void makeLogin(ActionEvent event) {

    }

    @FXML
    void makeSearch(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert loginBtn != null : "fx:id=\"loginBtn\" was not injected: check your FXML file 'StartPanel.fxml'.";
        assert searchTextField != null : "fx:id=\"searchTextField\" was not injected: check your FXML file 'StartPanel.fxml'.";
        assert searchBtn != null : "fx:id=\"searchBtn\" was not injected: check your FXML file 'StartPanel.fxml'.";
        assert bookNameRB != null : "fx:id=\"bookNameRB\" was not injected: check your FXML file 'StartPanel.fxml'.";
        assert authorNameRB != null : "fx:id=\"authorNameRB\" was not injected: check your FXML file 'StartPanel.fxml'.";
        assert topicRB != null : "fx:id=\"topicRB\" was not injected: check your FXML file 'StartPanel.fxml'.";
        assert freeSearchRB != null : "fx:id=\"freeSearchRB\" was not injected: check your FXML file 'StartPanel.fxml'.";

    }
}
