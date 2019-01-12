package clientBounderiesReaderAccount;

import com.jfoenix.controls.JFXButton;

import Common.IGUIController;
import Common.ObjectMessage;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.StartPanelController;
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
    	AClientCommonUtilities.backToStartPanel();
    }

    
    @FXML
    void updatePersonalDetailsClicked(ActionEvent event) 
    {

    }

	@Override
	public void display(ObjectMessage msg) 
	{
		
		
	}

   
}



