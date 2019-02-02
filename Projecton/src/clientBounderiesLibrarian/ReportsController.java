package clientBounderiesLibrarian;

import com.jfoenix.controls.JFXTextField;

import Common.IEntity;
import Common.IGUIController;
import Common.ObjectMessage;
import Common.Report;
import clientCommonBounderies.StartPanelController;
import clientConrollers.AClientCommonUtilities;
import clientConrollers.AValidationInput;
import clientConrollers.OBLClient;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ReportsController implements IGUIController
{

	OBLClient client;

	@FXML
	private VBox report1;
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
	private ComboBox<Integer> monthComboBox;

	@FXML
	private ComboBox<Integer> yearComboBox;

	@FXML
	private ComboBox<String> chooseFromPreviousComboBox;

	@FXML
	private TextField avgForRegular;

	@FXML
	private TextField medianForRegular;

	@FXML
	private BarChart<String,Number> diagramForRegular;

	@FXML
	private TextField avgForDesired;

	@FXML
	private TextField medianForDesired;

	@FXML
	private BarChart<String,Number> diagramForDesired;

	@FXML
	private TextField avgForAll;

	@FXML
	private TextField medianForAll;

	@FXML
	private BarChart<String,Number> diagramForAll;

	@FXML
	private TextField totalForLateReturns;

	@FXML
	private TextField avgForDurationLateReturns;

	@FXML
	private TextField medianForDurationLateReturns;

	@FXML
	private BarChart<String,Number> diagramForNumLateReturns;

	@FXML
	private JFXTextField bookIDReport3;

	@FXML
	private VBox resultsForReport3;

	@FXML
	private Button showNewReportBtn;

	@FXML
	private Button showPreviousReportBtn;
	

    @FXML
    private Label noResultLabel;
    

	ObservableList<Integer> list1;
	ObservableList<Integer> list2;
	ObservableList<String> list3;

	@FXML
	void initialize() 
	{
		client=StartPanelController.connToClientController;
		client.setClientUI(this);

		resultsForReport3.setVisible(false);
		report1.setVisible(false);
		combo();

		ObjectMessage sendToServer=new ObjectMessage("Ask for report2","History");
		client.handleMessageFromClient(sendToServer); 

	}


	@Override
	public void display(ObjectMessage msg) 
	{		
		if(msg.getNote().equals("Report number 2"))
		{
			setReport2Result(msg);

			ArrayList <IEntity> result = msg.getObjectList();
			//set first diagram
			ArrayList<Long> detailsArray0 = ((Report)result.get(0)).getDetailsArray();
			setDiagram(detailsArray0,diagramForRegular);

			//set second diagram
			ArrayList<Long> detailsArray1 = ((Report)result.get(1)).getDetailsArray();
			setDiagram(detailsArray1,diagramForDesired);

			//set thread diagram
			ArrayList<Long> detailsArray2 = ((Report)result.get(2)).getDetailsArray();
			setDiagram(detailsArray2,diagramForAll);

			//set the comboBox for the Report1
			ObjectMessage sendToServer=new ObjectMessage("get previous reports options","History");
			client.handleMessageFromClient(sendToServer); 

		}
		else if(msg.getNote().equals("Report number 3")) //for Report3
		{
			noResultLabel.setVisible(false);
			
			setReport3Result(msg);

			ArrayList <IEntity> result = msg.getObjectList();
			ArrayList<Long> detailsArray = ((Report)result.get(0)).getDetailsArray();
			setDiagram(detailsArray, diagramForNumLateReturns);
		}
		else if(msg.getNote().equals("Report number 3 - no results")) //for Report3
		{
			resultsForReport3.setVisible(false);
			 
			if(!bookIDReport3.getText().equals("") && !bookIDReport3.getText().equals(" ") && !bookIDReport3.getText().equals("   "))
			{
				noResultLabel.setVisible(true);
			}
		}
		else if(msg.getNote().equals("Results  active,frozen,locked for report1"))
		{

			Report newRes=(Report) msg.getObjectList().get(0);
			activeReaderAccounts.setText(newRes.getActiveReaderAccounts());
			freezedReaderAccounts.setText(newRes.getFrozenReaderAccounts());
			lockedReaderAccounts.setText(newRes.getLockedReaderAccounts());
			totalNumOfCopies.setText(newRes.getTotalCopies());
			numOfdelayedReaderAccounts.setText(newRes.getNumOfDidntReturnOnTime());
			report1.setVisible(true);

			//set the comboBox for old reports
			ObjectMessage sendToServer=new ObjectMessage("get previous reports options","History");
			client.handleMessageFromClient(sendToServer); 

		}

		else if((msg.getNote().equals("no result for report1")))
		{
			report1.setVisible(false);
			AClientCommonUtilities.infoAlert("No data for the choosen period", "No results"); 
		}

		else if(msg.getNote().equals("No options for old reports comboBox")) 
		{
			//Do nothing..
		}
		else if(msg.getNote().equals("options for old reports comboBox")) 
		{
			Platform.runLater(()->
			{
				ObservableList<String> list;
				ArrayList <IEntity> result = msg.getObjectList();

				list = FXCollections.observableArrayList(((Report)result.get(0)).getComboBoxOptions());
				chooseFromPreviousComboBox.setItems( list );
			});
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


	/**
	 * This function sets the results of Report2 to the GUI
	 * @param msg- the received object from the server
	 */
	public void setReport2Result(ObjectMessage msg)
	{
		ArrayList <IEntity> result=msg.getObjectList(); //get the array list received from the server
		
		//set the format of numbers that will be displayed in the diagrams
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		//df.format(i)

		float avgRegular=((Report)result.get(0)).getAverage();
		float medianRegular=((Report)result.get(0)).getMedian();
		avgForRegular.setText(df.format(avgRegular));
		medianForRegular.setText(df.format(medianRegular));

		float averageDesired=((Report)result.get(1)).getAverage();
		float medianDesired=((Report)result.get(1)).getMedian();
		avgForDesired.setText(df.format(averageDesired));
		medianForDesired.setText(df.format(medianDesired));

		float averageAll=((Report)result.get(2)).getAverage();
		float medianAll=((Report)result.get(2)).getMedian();
		avgForAll.setText(df.format(averageAll));
		medianForAll.setText(df.format(medianAll));
	}


	/**
	 * This function will be committed each time a button on the keyboard pressed
	 * will ask the server for checking if a book with that id exist
	 * @param event
	 */
	@FXML
	void getBookInfo(KeyEvent event) 
	{
		String bookID;	

		if (AValidationInput.checkValidationBook("bookID", bookIDReport3.getText()).equals("correct"))
		{	
			bookID=bookIDReport3.getText();
		}
		else
		{
			bookID="0";
		}
		ObjectMessage sendToServer=new ObjectMessage("Ask for report3","History");
		sendToServer.setExtra(bookID); 
		client.handleMessageFromClient(sendToServer);
	}

	/**
	 * The function activated when user press button for option choose by Year & Month
	 * and ask send to server this date for creating report
	 * @param event
	 */
	@FXML 
	void showNewReport(ActionEvent event) 
	{
		chooseFromPreviousComboBox.setValue(" ");
		int mounth;
		int year;
		if (monthComboBox.getValue()==12)
		{
			mounth=1;
			year=yearComboBox.getValue()+1;

		}
		else
		{
			mounth=monthComboBox.getValue()+1;
			 year=yearComboBox.getValue();
		}

		
		LocalDate now = LocalDate.of(year, mounth,1 );
		Date dateForReport=java.sql.Date.valueOf(now);//make date from combobox

		Report newReport=new Report(dateForReport);
		ObjectMessage sendToServer=new ObjectMessage(newReport,"Ask for new report1","History");
		client.handleMessageFromClient(sendToServer);


	}


	@FXML //choose from comboBox
	void showPreviousReport(ActionEvent event) 
	{
		monthComboBox.setValue(null);
		yearComboBox.setValue(null);
		
		
		if(null != chooseFromPreviousComboBox.getValue() && !(chooseFromPreviousComboBox.getValue()).equals(""))
		{
			String month = (chooseFromPreviousComboBox.getValue()).substring(0, 1);
			String year = (chooseFromPreviousComboBox.getValue()).substring(4,8);

			Report report = new Report();
			report.setYear(year);
			report.setMonth(month);

			ObjectMessage sendToServer=new ObjectMessage(report,"Ask for old report1","History");
			client.handleMessageFromClient(sendToServer);
		}
		
	}


	/**
	 * This function sets the BarChart diagram with the details that came from the server
	 * @param detailsArray - arrayList with the data for the diagram
	 * @param diagram - the reference for the diagram to set for the data
	 */
	private void setDiagram(ArrayList<Long> detailsArray, BarChart<String,Number> diagram)
	{ 	
		Platform.runLater(()->
		{
			diagram.getData().clear();

			//if there is no data for the diagram- finish
			if(0 == detailsArray.size())
			{
				XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
				diagram.getData().add(series);
				series.setName("");
				return;
			}

			//get the max value
			Long maxValue = detailsArray.get(detailsArray.size() -1 );


			XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();


			//find the range in the columns
			float range = (float) (maxValue/10.0) ;
			float addTOi = (float) 1.0;
			if(range <= 1.0)
			{
				range = 0;
			}
			else
			{
				addTOi = range;
			}

			for(float i=1; i<maxValue || i<=10 ; i+=addTOi)
			{
				//find the value for the i-column
				Integer cnt = 0;
				for(int j=0; j<detailsArray.size(); j++)
				{

					//check if in the range
					if( ((float)detailsArray.get(j) >= i) && ((float)detailsArray.get(j) <= i+range) )
					{
						cnt++; 
					}
				}

				//set the format of numbers that will be displayed in the diagrams
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);

				//set the column
				if(0 == range)
				{
					series.getData().add(new XYChart.Data<String,Number>((String.valueOf(i)), cnt));
				}
				else
				{

					series.getData().add(new XYChart.Data<String,Number>((df.format(i) + "-" + df.format(i+range)), cnt));
				}

			}


			diagram.getData().add(series);
		});
	}
	
/**
 * this function set data in comboBox (year and month)
 */
	public void combo() 
	{
		ArrayList <Integer> s=new ArrayList<Integer>();
		LocalDate now = LocalDate.now();
		int year=now.getYear();

		for(int i=2016; i<=year; i++)
		{
			s.add(i);
		}

		list1 = FXCollections.observableArrayList(s);
		yearComboBox.setItems( list1);

		ArrayList <Integer> b=new ArrayList<Integer>();

		for(int i= 1; i<= 12; i++)
		{
			b.add(i);
		}
		list2 = FXCollections.observableArrayList(b);
		monthComboBox.setItems( list2);
	}
}
