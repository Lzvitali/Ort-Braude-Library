package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import Common.Copy;
import Common.ObjectMessage;
import clientConrollers.OBLClient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ReturnBookController {

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
    void CancelBtnClicked(ActionEvent event) {

    }

    @FXML
    void OkBtnClicked(ActionEvent event) 
    {
    	int copyID=Integer.parseInt(CopyIdTextFielf.getText());
    	Copy copy=new Copy(copyID);
    	ObjectMessage msg = new ObjectMessage(copy,"ReturnCopy","Book");
    	client.handleMessageFromClient(msg);
    	
    }

    @FXML
    void initialize() {
        

    }
}
