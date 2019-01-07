package clientBounderiesLibrarian;
  
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DeleteBookController 
{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="CopyIdTextField"
    private JFXTextField CopyIdTextField; // Value injected by FXMLLoader

    @FXML // fx:id="OkDeleteBtn"
    private JFXButton OkDeleteBtn; // Value injected by FXMLLoader

    @FXML // fx:id="CancelBtn"
    private JFXButton CancelBtn; // Value injected by FXMLLoader

    @FXML
    void cancelBtnClicked(ActionEvent event) {

    }

    @FXML
    void okDeleteBtnClicked(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert CopyIdTextField != null : "fx:id=\"CopyIdTextField\" was not injected: check your FXML file 'DeleteBook.fxml'.";
        assert OkDeleteBtn != null : "fx:id=\"OkDeleteBtn\" was not injected: check your FXML file 'DeleteBook.fxml'.";
        assert CancelBtn != null : "fx:id=\"CancelBtn\" was not injected: check your FXML file 'DeleteBook.fxml'.";

    }
}
