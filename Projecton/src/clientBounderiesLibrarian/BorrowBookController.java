package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class BorrowBookController  {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="AproveBtn"
    private JFXButton AproveBtn; // Value injected by FXMLLoader

    @FXML // fx:id="CancelBtn"
    private JFXButton CancelBtn; // Value injected by FXMLLoader

    @FXML // fx:id="CopyIdBtn"
    private JFXTextField CopyIdBtn; // Value injected by FXMLLoader

    @FXML // fx:id="BorrowDateText"
    private Text BorrowDateText; // Value injected by FXMLLoader

    @FXML // fx:id="ReturnDateText"
    private Text ReturnDateText; // Value injected by FXMLLoader

    @FXML
    void aproveBorrowBook(ActionEvent event)
    {

    }

    @FXML
    void cancelBorrrow(ActionEvent event)
    {

    }

    @FXML
    void setDateForBorrowBook(ActionEvent event)
    {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert AproveBtn != null : "fx:id=\"AproveBtn\" was not injected: check your FXML file 'BorrowBook.fxml'.";
        assert CancelBtn != null : "fx:id=\"CancelBtn\" was not injected: check your FXML file 'BorrowBook.fxml'.";
        assert CopyIdBtn != null : "fx:id=\"CopyIdBtn\" was not injected: check your FXML file 'BorrowBook.fxml'.";
        assert BorrowDateText != null : "fx:id=\"BorrowDateText\" was not injected: check your FXML file 'BorrowBook.fxml'.";
        assert ReturnDateText != null : "fx:id=\"ReturnDateText\" was not injected: check your FXML file 'BorrowBook.fxml'.";

    }
}
