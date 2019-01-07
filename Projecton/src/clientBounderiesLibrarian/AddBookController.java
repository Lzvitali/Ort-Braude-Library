package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

public class AddBookController 
{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="BookTitleTextField"
    private JFXTextField BookTitleTextField; // Value injected by FXMLLoader

    @FXML // fx:id="BookAuthorTextField"
    private JFXTextField BookAuthorTextField; // Value injected by FXMLLoader

    @FXML // fx:id="PublishedYearTextField"
    private JFXTextField PublishedYearTextField; // Value injected by FXMLLoader

    @FXML // fx:id="EditionTextField"
    private JFXTextField EditionTextField; // Value injected by FXMLLoader

    @FXML // fx:id="ShelfNumberTextField"
    private JFXTextField ShelfNumberTextField; // Value injected by FXMLLoader

    @FXML // fx:id="DesiredCheckBox"
    private JFXCheckBox DesiredCheckBox; // Value injected by FXMLLoader

    @FXML // fx:id="CancelBtn"
    private JFXButton CancelBtn; // Value injected by FXMLLoader

    @FXML // fx:id="SaveBtn"
    private JFXButton SaveBtn; // Value injected by FXMLLoader

    @FXML // fx:id="TopicChoiceBox"
    private ChoiceBox<?> TopicChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="PurchaseDateDatePicker"
    private DatePicker PurchaseDateDatePicker; // Value injected by FXMLLoader

    @FXML // fx:id="UploadBtn"
    private Button UploadBtn; // Value injected by FXMLLoader

    @FXML
    void cancelBtnClicked(ActionEvent event) 
    {

    }

    @FXML
    void saveAddNewBook(ActionEvent event)
    {

    }

    @FXML
    void uploadTableOfContents(ActionEvent event)
    {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert BookTitleTextField != null : "fx:id=\"BookTitleTextField\" was not injected: check your FXML file 'AddBook.fxml'.";
        assert BookAuthorTextField != null : "fx:id=\"BookAuthorTextField\" was not injected: check your FXML file 'AddBook.fxml'.";
        assert PublishedYearTextField != null : "fx:id=\"PublishedYearTextField\" was not injected: check your FXML file 'AddBook.fxml'.";
        assert EditionTextField != null : "fx:id=\"EditionTextField\" was not injected: check your FXML file 'AddBook.fxml'.";
        assert ShelfNumberTextField != null : "fx:id=\"ShelfNumberTextField\" was not injected: check your FXML file 'AddBook.fxml'.";
        assert DesiredCheckBox != null : "fx:id=\"DesiredCheckBox\" was not injected: check your FXML file 'AddBook.fxml'.";
        assert CancelBtn != null : "fx:id=\"CancelBtn\" was not injected: check your FXML file 'AddBook.fxml'.";
        assert SaveBtn != null : "fx:id=\"SaveBtn\" was not injected: check your FXML file 'AddBook.fxml'.";
        assert TopicChoiceBox != null : "fx:id=\"TopicChoiceBox\" was not injected: check your FXML file 'AddBook.fxml'.";
        assert PurchaseDateDatePicker != null : "fx:id=\"PurchaseDateDatePicker\" was not injected: check your FXML file 'AddBook.fxml'.";
        assert UploadBtn != null : "fx:id=\"UploadBtn\" was not injected: check your FXML file 'AddBook.fxml'.";

    }
}
