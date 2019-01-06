/**
 * Sample Skeleton for 'StartPanel.fxml' Controller Class
 */

package OldBounderies_ForDeleteLater;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;



Clientconsole conn;
public class StartPanelController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="logInBtn"
    private Button logInBtn; // Value injected by FXMLLoader

    @FXML // fx:id="searchByMenuButton"
    private MenuButton searchByMenuButton; // Value injected by FXMLLoader

    @FXML // fx:id="searchTextField"
    private TextField searchTextField; // Value injected by FXMLLoader

    @FXML // fx:id="searchBtn"
    private Button searchBtn; // Value injected by FXMLLoader

    @FXML // fx:id="searchResultTable"
    private TableView<?> searchResultTable; // Value injected by FXMLLoader

    @FXML // fx:id="bookNameColumn"
    private TableColumn<?, ?> bookNameColumn; // Value injected by FXMLLoader

    @FXML // fx:id="authorNameColumn"
    private TableColumn<?, ?> authorNameColumn; // Value injected by FXMLLoader

    @FXML // fx:id="yearColumn"
    private TableColumn<?, ?> yearColumn; // Value injected by FXMLLoader

    @FXML // fx:id="viewIntroColumn"
    private TableColumn<?, ?> viewIntroColumn; // Value injected by FXMLLoader

    @FXML
    void makeLogIn(ActionEvent event) {

    }

    @FXML
    void makeSearch(ActionEvent event) 
    {


    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        

    }
}
