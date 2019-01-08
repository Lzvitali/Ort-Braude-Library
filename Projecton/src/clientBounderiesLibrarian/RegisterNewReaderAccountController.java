package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class RegisterNewReaderAccountController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField UserIDTextField;

    @FXML
    private JFXTextField LastNameTextField;

    @FXML
    private JFXTextField EmailTextField2;

    @FXML
    private JFXTextField FirstNameTextField;

    @FXML
    private JFXTextField PhoneNumberTextField;

    @FXML
    private JFXTextField EmailTextField;

    @FXML
    private JFXComboBox<?> EditionYearsCmbBox;

    @FXML
    private JFXButton CancelBtn;

    @FXML
    private JFXButton SaveBtn;

    @FXML
    void cancelRegistration(ActionEvent event) {

    }

    @FXML
    void saveRegistration(ActionEvent event) {

    }

    @FXML
    void initialize() {
       
    }
}
