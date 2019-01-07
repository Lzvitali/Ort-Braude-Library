package clientBounderiesLibrarian;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class RegisterNewReaderAccountController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="UserIDTextField"
    private JFXTextField UserIDTextField; // Value injected by FXMLLoader

    @FXML // fx:id="LastNameTextField"
    private JFXTextField LastNameTextField; // Value injected by FXMLLoader

    @FXML // fx:id="EmailTextField2"
    private JFXTextField EmailTextField2; // Value injected by FXMLLoader

    @FXML // fx:id="FirstNameTextField"
    private JFXTextField FirstNameTextField; // Value injected by FXMLLoader

    @FXML // fx:id="PhoneNumberTextField"
    private JFXTextField PhoneNumberTextField; // Value injected by FXMLLoader

    @FXML // fx:id="EmailTextField"
    private JFXTextField EmailTextField; // Value injected by FXMLLoader

    @FXML // fx:id="EditionYearsCmbBox"
    private JFXComboBox<?> EditionYearsCmbBox; // Value injected by FXMLLoader

    @FXML // fx:id="SaveBtn"
    private JFXButton SaveBtn; // Value injected by FXMLLoader

    @FXML // fx:id="CancelBtn"
    private JFXButton CancelBtn; // Value injected by FXMLLoader

    @FXML
    void cancelRegistration(ActionEvent event) 
    {

    }

    @FXML
    void saveRegistration(ActionEvent event) 
    {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert UserIDTextField != null : "fx:id=\"UserIDTextField\" was not injected: check your FXML file 'RegisterNewAccount.fxml'.";
        assert LastNameTextField != null : "fx:id=\"LastNameTextField\" was not injected: check your FXML file 'RegisterNewAccount.fxml'.";
        assert EmailTextField2 != null : "fx:id=\"EmailTextField2\" was not injected: check your FXML file 'RegisterNewAccount.fxml'.";
        assert FirstNameTextField != null : "fx:id=\"FirstNameTextField\" was not injected: check your FXML file 'RegisterNewAccount.fxml'.";
        assert PhoneNumberTextField != null : "fx:id=\"PhoneNumberTextField\" was not injected: check your FXML file 'RegisterNewAccount.fxml'.";
        assert EmailTextField != null : "fx:id=\"EmailTextField\" was not injected: check your FXML file 'RegisterNewAccount.fxml'.";
        assert EditionYearsCmbBox != null : "fx:id=\"EditionYearsCmbBox\" was not injected: check your FXML file 'RegisterNewAccount.fxml'.";
        assert SaveBtn != null : "fx:id=\"SaveBtn\" was not injected: check your FXML file 'RegisterNewAccount.fxml'.";
        assert CancelBtn != null : "fx:id=\"CancelBtn\" was not injected: check your FXML file 'RegisterNewAccount.fxml'.";

    }
}
