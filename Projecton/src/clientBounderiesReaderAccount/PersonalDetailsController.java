package clientBounderiesReaderAccount;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PersonalDetailsController 
{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="FirstNameTextField"
    private TextField FirstNameTextField; // Value injected by FXMLLoader

    @FXML // fx:id="LastnameTextField"
    private TextField LastnameTextField; // Value injected by FXMLLoader

    @FXML // fx:id="IDTextField"
    private TextField IDTextField; // Value injected by FXMLLoader

    @FXML // fx:id="PhoneTextField"
    private TextField PhoneTextField; // Value injected by FXMLLoader

    @FXML // fx:id="AdressTextField"
    private TextField AdressTextField; // Value injected by FXMLLoader

    @FXML // fx:id="EducationYearTextField"
    private TextField EducationYearTextField; // Value injected by FXMLLoader

    @FXML // fx:id="EmailTextField"
    private TextField EmailTextField; // Value injected by FXMLLoader

    @FXML // fx:id="cancelBtn"
    private JFXButton cancelBtn; // Value injected by FXMLLoader

    @FXML // fx:id="UpdatePersonalDetailsBtn"
    private JFXButton UpdatePersonalDetailsBtn; // Value injected by FXMLLoader

    @FXML
    void cancelBtnClicked(ActionEvent event)
    {

    }

    @FXML
    void updatePersonalDetailsClicked(ActionEvent event) 
    {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert FirstNameTextField != null : "fx:id=\"FirstNameTextField\" was not injected: check your FXML file 'PersonalDetails.fxml'.";
        assert LastnameTextField != null : "fx:id=\"LastnameTextField\" was not injected: check your FXML file 'PersonalDetails.fxml'.";
        assert IDTextField != null : "fx:id=\"IDTextField\" was not injected: check your FXML file 'PersonalDetails.fxml'.";
        assert PhoneTextField != null : "fx:id=\"PhoneTextField\" was not injected: check your FXML file 'PersonalDetails.fxml'.";
        assert AdressTextField != null : "fx:id=\"AdressTextField\" was not injected: check your FXML file 'PersonalDetails.fxml'.";
        assert EducationYearTextField != null : "fx:id=\"EducationYearTextField\" was not injected: check your FXML file 'PersonalDetails.fxml'.";
        assert EmailTextField != null : "fx:id=\"EmailTextField\" was not injected: check your FXML file 'PersonalDetails.fxml'.";
        assert cancelBtn != null : "fx:id=\"cancelBtn\" was not injected: check your FXML file 'PersonalDetails.fxml'.";
        assert UpdatePersonalDetailsBtn != null : "fx:id=\"UpdatePersonalDetailsBtn\" was not injected: check your FXML file 'PersonalDetails.fxml'.";

    }
}
