package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXTextField;

import Common.IEntity;
import Common.IGUIController;
import Common.ObjectMessage;
import Common.Report;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class ReportsController implements IGUIController
{

	OBLClient client;

	@FXML
	private TabPane TabPaneSelect;

	@FXML
	private TextField activeReaderAccounts;

	@FXML
	private TextField freezedReaderAccounts;

	@FXML
	private TextField lockedReaderAccounts;

	@FXML
	private TextField totalNumOfCopies;

	@FXML
	private TextField numOfdelayedReaderAccounts;

	@FXML
	private ComboBox<?> monthComboBox;

	@FXML
	private ComboBox<?> yearComboBox;

	@FXML
	private ComboBox<?> chooseFromPreviousComboBox;

	@FXML
	private TextField avgForRegular;

	@FXML
	private TextField medianForRegular;

	@FXML
	private BarChart<?, ?> diagramForRegular;

	@FXML
	private TextField avgForDesired;

	@FXML
	private TextField medianForDesired;

	@FXML
	private BarChart<?, ?> diagramForDesired;

	@FXML
	private TextField avgForAll;

	@FXML
	private TextField medianForAll;

	@FXML
	private BarChart<?, ?> diagramForAll;

	@FXML
	private TextField totalForLateReturns;

	@FXML
	private TextField avgForDurationLateReturns;

	@FXML
	private TextField medianForDurationLateReturns;

	@FXML
	private BarChart<?, ?> diagramForNumLateReturns;

	@FXML
	private JFXTextField BookIDReport3;

    @FXML
    private VBox resultsForReport3;


	@FXML
	void initialize() 
	{
		client=StartPanelController.connToClientController;
		client.setClientUI(this);

		resultsForReport3.setVisible(false);
		
		//ObjectMessage sendToServer=new ObjectMessage("Ask for report2","History");
		//client.handleMessageFromClient(sendToServer); 

	}

	@Override
	public void display(ObjectMessage msg) 
	{		
		if(msg.getNote().equals("Report number 2"))
		{
			setReport2Result(msg);
		}
		else if(msg.getNote().equals("Report number 3")) //for Report3
		{
			setReport3Result(msg);
		}
		else if(msg.getNote().equals("Report number 3 - no results")) //for Report3
		{
			resultsForReport3.setVisible(false);
		}

	}

	/**
	 * This function sets the results of Report3 to the GUI (only if the asked book was found)
	 * @param msg- the received object from the server
	 */
	private void setReport3Result(ObjectMessage msg) 
	{
		resultsForReport3.setVisible(true);
		
		ArrayList <IEntity> result = msg.getObjectList();
		
		totalForLateReturns.setText(String.valueOf(((Report)result.get(0)).getTotal()));
		avgForDurationLateReturns.setText(String.valueOf(((Report)result.get(0)).getAverage()));
		medianForDurationLateReturns.setText(String.valueOf(((Report)result.get(0)).getMedian()));
		
	}

	//TODO: Inbar, complete the comment for the JavaDocs
	/**
	 * 
	 * @param msg
	 */
	public void setReport2Result(ObjectMessage msg)
	{
		ArrayList <IEntity> result=msg.getObjectList(); //get the array list received from the server

		float avgRegular=((Report)result.get(0)).getAverage();
		float medianRegular=((Report)result.get(0)).getMedian();
		avgForRegular.setText(String.valueOf(avgRegular));
		medianForRegular.setText(String.valueOf(medianRegular));

		float averageDesired=((Report)result.get(1)).getAverage();
		float medianDesired=((Report)result.get(1)).getMedian();
		avgForDesired.setText(String.valueOf(averageDesired));
		medianForDesired.setText(String.valueOf(medianDesired));

		float averageAll=((Report)result.get(2)).getAverage();
		float medianAll=((Report)result.get(2)).getMedian();
		avgForAll.setText(String.valueOf(averageAll));
		medianForAll.setText(String.valueOf(medianAll));
	}

	
    @FXML
    void getBookInfo(KeyEvent event) 
    {
    	String bookID;	

		if (AValidationInput.checkValidationBook("bookID", BookIDReport3.getText()).equals("correct"))
		{	
			bookID=BookIDReport3.getText();
		}
		else
		{
			bookID="0";
		}
		ObjectMessage sendToServer=new ObjectMessage("Ask for report3","History");
		sendToServer.setExtra(bookID); 
		client.handleMessageFromClient(sendToServer);
    }
}
