package clientBounderiesReaderAccount;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import clientCommonBounderies.LogInController;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AClientCommonUtilities;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
 
public class PersonalDetailsController implements IGUIController
{
	
	OBLClient client;

	
    @FXML
    private TextField IDTextField;

    @FXML
    private TextField FirstNameTextField;

    @FXML
    private TextField LastnameTextField;

    @FXML
    private JFXTextField PhoneTextField;

    @FXML
    private JFXTextField EmailTextField;

    @FXML
    private JFXTextField AdressTextField;

    @FXML
    private TextField EducationYearTextField;


    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXButton UpdatePersonalDetailsBtn;
    
    @FXML
    void initialize() 
    {
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);
    	
    	ReaderAccount readerAccount=new ReaderAccount();
    	readerAccount.setId(LogInController.currentID);
    	ObjectMessage objectMessage=new ObjectMessage(readerAccount,"SearchReader","ReaderAccount");
    	client.setClientUI(this);
    	client.handleMessageFromClient(objectMessage);
    }

    
    @FXML
    void cancelBtnClicked(ActionEvent event) 
    {
    	AClientCommonUtilities.backToStartPanel();
    }

    
    @FXML
    void updatePersonalDetailsClicked(ActionEvent event) 
    {
    	String result=validationResultForChangePersonalDetails();
    	if(result.equals("correct"))
    	{
    		String userID=IDTextField.getText();
    		String phoneNum=PhoneTextField.getText();
    		String email=EmailTextField.getText();
    		String firstName=FirstNameTextField.getText();
    		String lastName=LastnameTextField.getText();
    		String adress=AdressTextField.getText();
    		String educationYear=EducationYearTextField.getText();
    		
    		if(educationYear == null  || (educationYear.equals(null)))
    		{
    			educationYear = " ";
    		}
    		else
    		{
    			educationYear= EducationYearTextField.getText();
    		}
    		
    		if(AdressTextField.getText() == null  || (AdressTextField.getText().toString()).equals(null))
    		{
    			adress = " ";
    		}
    		else
    		{
    			adress=AdressTextField.getText();
    		}
    		ReaderAccount reader=new ReaderAccount(userID, 3, true, firstName,lastName,phoneNum,email,adress,educationYear);
    		ObjectMessage msg = new ObjectMessage(reader,"changePersonalDetails","ReaderAccount");
    		client.setClientUI(this);
        	client.handleMessageFromClient(msg);
    	}
    	else
    		AClientCommonUtilities.infoAlert(result,"Invaild Input");
    }


    private String validationResultForChangePersonalDetails()
    {
    	String result,finalResult="";
    	
    	result=AValidationInput.checkValidationUser("Last Name",LastnameTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	
    	result=AValidationInput.checkValidationUser("First Name",FirstNameTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	
    	result=AValidationInput.checkValidationUser("Phone Number",PhoneTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
		result=AValidationInput.checkValidationUser("Education Year",EducationYearTextField.getText());
		if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	if(finalResult.equals(""))
    		return "correct";
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
		if(msg.getMessage().equals("change details"))
    	{
			if(msg.getNote().equals("successful change details"))
			{
	    		String show="successful change details";
	    		AClientCommonUtilities.infoAlert(show,"successful change details");
	    		AClientCommonUtilities.backToStartPanel();
			}
			else
			{
				AClientCommonUtilities.infoAlert(msg.getMessage(),"unsuccessful change details");
			}

    	}
		else if(msg.getMessage().equals("ReaderAccountSearch"))
		{
			ReaderAccount reader=(ReaderAccount) msg.getObjectList().get(0);
			IDTextField.setText(reader.getId());
    		PhoneTextField.setText(reader.getPhone());
    		EmailTextField.setText(reader.getEmail());
    		FirstNameTextField.setText(reader.getFirstName());
    		LastnameTextField.setText(reader.getLastName());
    		AdressTextField.setText(reader.getAdress());
    		EducationYearTextField.setText(reader.getEducationYear());
		}		
		
	}

   
}



