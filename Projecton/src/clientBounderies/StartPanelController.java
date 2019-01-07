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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StartPanelController 
{

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

    @FXML // fx:id="searchResultTable"
    private TableView<?> searchResultTable; // Value injected by FXMLLoader

    @FXML // fx:id="bookNameColumn"
    private TableColumn<?, ?> bookNameColumn; // Value injected by FXMLLoader

    @FXML // fx:id="authorNameColumn"
    private TableColumn<?, ?> authorNameColumn; // Value injected by FXMLLoader

    @FXML // fx:id="yearColumn"
    private TableColumn<?, ?> yearColumn; // Value injected by FXMLLoader

    @FXML // fx:id="topicColumn"
    private TableColumn<?, ?> topicColumn; // Value injected by FXMLLoader

    @FXML // fx:id="isDesiredColumn"
    private TableColumn<?, ?> isDesiredColumn; // Value injected by FXMLLoader

    @FXML // fx:id="viewIntroColumn"
    private TableColumn<?, ?> viewIntroColumn; // Value injected by FXMLLoader

    @FXML
    void makeLogin(ActionEvent event) 
    {

    }

    @FXML
    void makeSearch(ActionEvent event) 
    {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() 
    {
       

    }
}
