package clientBounderiesLibrarian;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ReturnBookController
{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="CopyIdTextFielf"
    private JFXTextField CopyIdTextFielf; // Value injected by FXMLLoader

    @FXML // fx:id="OkBtn"
    private JFXButton OkBtn; // Value injected by FXMLLoader

    @FXML // fx:id="CancelBtn"
    private JFXButton CancelBtn; // Value injected by FXMLLoader

    @FXML
    void CancelBtnClicked(ActionEvent event) 
    {

    }

    @FXML
    void OkBtnClicked(ActionEvent event)
    {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert CopyIdTextFielf != null : "fx:id=\"CopyIdTextFielf\" was not injected: check your FXML file 'ReturnBook.fxml'.";
        assert OkBtn != null : "fx:id=\"OkBtn\" was not injected: check your FXML file 'ReturnBook.fxml'.";
        assert CancelBtn != null : "fx:id=\"CancelBtn\" was not injected: check your FXML file 'ReturnBook.fxml'.";

    }
}
