package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import Common.IGUIController;
import Common.ObjectMessage;
import clientConrollers.AValidationInput;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class RegisterNewReaderAccountController implements IGUIController
{

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
    void cancelRegistration(ActionEvent event) 
    {

    }

    @FXML
    void saveRegistration(ActionEvent event) 
    {
    	String result=validationResult();
    	if(result.equals("correct"))
    	{
    		
    	}
    	else
    		infoAlert(result,"Invaild Input");
    }

    @FXML
    void initialize() {
       
    }
    
    
    private String validationResult()
    {
    	String result,finalResult="";
    	result=AValidationInput.checkValidationUser("UserID",UserIDTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	
    	result=AValidationInput.checkValidationUser("Last Name",LastNameTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	
    	result=AValidationInput.checkValidationUser("First Name",FirstNameTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	
    	result=AValidationInput.checkValidationUser("Phone Number",PhoneNumberTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	
    	result=AValidationInput.checkValidationUser("Email",EmailTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	if(finalResult.equals(""))
    		return "correct";
    	else
    		return finalResult;
    }
    
    
    void infoAlert(String headerText,String title)  //print propper message according to value that enterd
    {
    	Platform.runLater(()->
    	{   
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		ButtonType bttCountiue = new ButtonType("countiue", ButtonBar.ButtonData.FINISH);
    		alert.getButtonTypes().clear();
    		alert.setHeaderText(headerText);
    		alert.setTitle(title);
    		alert.getButtonTypes().addAll(bttCountiue);
    		Optional<ButtonType> result = alert.showAndWait();
    	});
    }

	@Override
	public void display(ObjectMessage msg) {
		// TODO Auto-generated method stub
		
	}
}
