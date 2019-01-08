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

public class AddBookController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField BookTitleTextField;

    @FXML
    private JFXTextField BookAuthorTextField;

    @FXML
    private JFXTextField PublishedYearTextField;

    @FXML
    private JFXTextField EditionTextField;

    @FXML
    private JFXTextField ShelfNumberTextField;

    @FXML
    private JFXCheckBox DesiredCheckBox;

    @FXML
    private ChoiceBox<?> TopicChoiceBox;

    @FXML
    private DatePicker PurchaseDateDatePicker;

    @FXML
    private Button UploadBtn;

    @FXML
    private JFXButton CancelBtn;

    @FXML
    private JFXButton SaveBtn;

    @FXML
    void cancelBtnClicked(ActionEvent event) {

    }

    @FXML
    void saveAddNewBook(ActionEvent event) {

    }

    @FXML
    void uploadTableOfContents(ActionEvent event) {

    }

    @FXML
    void initialize() {
        

    }
}
