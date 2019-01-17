package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import Common.Copy;
import Common.IGUIController;
import Common.ObjectMessage;
import clientCommonBounderies.AClientCommonUtilities;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class DeleteBookController implements IGUIController 
{
	OBLClient client;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField CopyIdTextField;

    @FXML
    private JFXButton OkDeleteBtn;

    @FXML
    private JFXButton CancelBtn;

    @FXML
    void cancelBtnClicked(ActionEvent event) {
    	AClientCommonUtilities.backToStartPanel();
    }

    @FXML
    void okDeleteBtnClicked(ActionEvent event) {
    	String result=validationResult();
    	if(result.equals("correct"))
    	{
    		//entered valid id copy
    		String copyID=CopyIdTextField.getText();
    		int idOfCopy=Integer.parseInt(copyID);
    		Copy copyBook=new Copy(idOfCopy);
    		ObjectMessage msg = new ObjectMessage(copyBook,"DeleteBook","Copy");
    		client.handleMessageFromClient(msg);
    	}
    	else
    		//entered invalid id copy
    		AClientCommonUtilities.infoAlert(result,"Invaild Input");
    }

    @FXML
    void initialize() {
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);

    }
    
    //check if id of the copy is valid
    private String validationResult()
    {
    	String result,finalResult="";
    	result=AValidationInput.checkValidationBook("bookID",CopyIdTextField.getText());
    	if(!result.equals("correct"))
    		finalResult+=result+"\n";
    	
    	if(finalResult.equals(""))
    		return "correct";
    	else return finalResult;
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
