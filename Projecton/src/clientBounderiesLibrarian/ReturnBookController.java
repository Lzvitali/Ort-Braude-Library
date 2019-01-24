package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import Common.Copy;
import Common.IGUIController;
import Common.ObjectMessage;
import Common.ReaderAccount;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AClientCommonUtilities;
import clientConrollers.OBLClient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ReturnBookController implements IGUIController {

	OBLClient client;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField CopyIdTextFielf;

    @FXML
    private JFXButton OkBtn;

    @FXML
    private JFXButton CancelBtn;

    @FXML
    void CancelBtnClicked(ActionEvent event) 
    {
    	AClientCommonUtilities.backToStartPanel();

    }

    @FXML
    void OkBtnClicked(ActionEvent event) 
    {
    	int copyID=Integer.parseInt(CopyIdTextFielf.getText());
    	Copy copy=new Copy(copyID);
    	ObjectMessage msg = new ObjectMessage(copy,"ReturnCopy","Copy");
    	client.setClientUI(this);
    	client.handleMessageFromClient(msg);
    	
    }

    @FXML
    void initialize() 
    {
    	client=StartPanelController.connToClientController;
    	client.setClientUI(this);

    }
    
	public void display(ObjectMessage msg) 
	{
		if(msg.getNote().equals("successful ReturnCopy"))
    	{
			String successful="successful ReturnCopy";
    		AClientCommonUtilities.infoAlert(successful,"successful ReturnCopy");
    	}
    	else
    	{
    		AClientCommonUtilities.infoAlert(msg.getMessage(),"Return book is unsuccessful");
    	}	
	}
}
