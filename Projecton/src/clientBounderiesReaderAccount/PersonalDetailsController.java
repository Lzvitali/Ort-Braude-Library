package clientBounderiesReaderAccount;

import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PersonalDetailsController implements IGUIController
{
	OBLClient client;
	

    @FXML 
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField IDTextField;

    @FXML
    private TextField FirstNameTextField;

    @FXML
    private TextField LastnameTextField;

    @FXML
    private TextField PhoneTextField;

    @FXML
    private TextField AdressTextField;

    @FXML
    private TextField EducationYearTextField;

    @FXML
    private TextField EmailTextField;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXButton UpdatePersonalDetailsBtn;
    
    
    @FXML
    void initialize() 
    {
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);
    }
    
    

    @FXML
    void cancelBtnClicked(ActionEvent event) 
    {

    }

    @FXML
    void updatePersonalDetailsClicked(ActionEvent event) 
    {
    	String result=validationResult();
    	if(result.equals("correct"))
    	{
    		String userID=IDTextField.getText();
    		String phoneNum=PhoneTextField.getText();
    		String email=EmailTextField.getText();
    		String firstName=FirstNameTextField.getText();
    		String lastName=LastnameTextField.getText();
    		String adress=AdressTextField.getText();
    		ReaderAccount reader=new ReaderAccount(userID, 3, false, firstName,lastName,phoneNum,email, "Active",0,adress); 
    		ObjectMessage msg = new ObjectMessage(reader,"changePersonalDetails","ReaderAccount");
        	client.handleMessageFromClient(msg);
    	}
    	else
    		AClientCommonUtilities.infoAlert(result,"Invaild Input");
    }

    private String validationResult()
    {
    	String result,finalResult="";
    	result=AValidationInput.checkValidationUser("UserID",IDTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	
    	result=AValidationInput.checkValidationUser("Last Name",LastnameTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	
    	result=AValidationInput.checkValidationUser("First Name",FirstNameTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	
    	result=AValidationInput.checkValidationUser("Phone Number",PhoneTextField.getText());
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

    
    
	@Override
	public void display(ObjectMessage msg) 
	{
		if(msg.getMessage().equals("successful change details"))
    	{
    		String showPassword="successful change details";
    		AClientCommonUtilities.infoAlert(showPassword,"successful change details");
    	}
    	else
    	{
    		AClientCommonUtilities.infoAlert(msg.getMessage(),"unsuccessful change details");
    	}
		
	}
}
