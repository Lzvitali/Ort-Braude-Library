package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import Common.User;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class RegisterNewReaderAccountController implements IGUIController
{
	OBLClient client;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField UserIDTextField;

    @FXML
    private JFXTextField LastNameTextField;

    @FXML
    private JFXTextField AdressTextField;

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
    
    ObservableList<String> list1;

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
    		String userID=UserIDTextField.getText();
    		String phoneNum=PhoneNumberTextField.getText();
    		String email=EmailTextField.getText();
    		String firstName=FirstNameTextField.getText();
    		String lastName=LastNameTextField.getText();
    		String adress=AdressTextField.getText();
    		ReaderAccount reader=new ReaderAccount(userID, 3, false, firstName,lastName,phoneNum,email, "Active",0,adress,EditionYearsCmbBox.getValue().toString()); 
    		ObjectMessage msg = new ObjectMessage(reader,"try to register new account","ReaderAccount");
        	client.handleMessageFromClient(msg);
    	}
    	else
    		AClientCommonUtilities.infoAlert(result,"Invaild Input");
        	
    }

    @FXML
    void initialize() {
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);
       combo();
    }
    
    
	public void combo() 
	  {
		  ArrayList <String> s=new ArrayList<String>();
		  s.add("0");
		  s.add("1");
		  s.add("2");
		  s.add("3");
		  list1 = FXCollections.observableArrayList(s);
		  EditionYearsCmbBox.setItems((ObservableList) list1);
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
	public void display(ObjectMessage msg) 
	{
		if(msg.getMessage().equals("successful registration"))
    	{
    		String showPassword="successful Registration the new password for the reader is "+ ((ReaderAccount)msg.getObjectList().get(0)).getPassword();
    		AClientCommonUtilities.infoAlert(showPassword,"Registration successful");
    	}
    	else
    	{
    		AClientCommonUtilities.infoAlert(msg.getMessage(),"Registration unsuccessful");
    	}	
	}
}
