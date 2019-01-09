package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import clientConrollers.OBLClient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class BorrowBookController 
{
	
	OBLClient client;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField readerAccountID;

    @FXML
    private JFXTextField CopyIdTextField;

    @FXML
    private Text BorrowDateText;

    @FXML
    private Text ReturnDateText;

    @FXML
    private JFXButton CancelBtn;

    @FXML
    private JFXButton AproveBtn;

    @FXML
    void aproveBorrowBook(ActionEvent event) 
    {

    }

    @FXML
    void cancelBorrrow(ActionEvent event) 
    {

    }

    @FXML
    void setDateForBorrowBook(ActionEvent event) 
    {
    	
    }

    @FXML
    void initialize() 
    {
    	client=StartPanelLibrarianController.client;

    }
}
