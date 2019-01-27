package Common;

import java.io.Serializable;
import java.util.ArrayList;

public class Report implements IEntity , Serializable
{
	private float average; //for Report2 & Report3
	private float median; //for Report2 & Report3
	private int total; //for Report3
	
	private ArrayList<Long> detailsArray; //for Report2 & Report3
	
	
	
	private String activeReaderAccounts; //for Report1 & Daily
	private String frozenReaderAccounts; //for Report1 & Daily
	private String lockedReaderAccounts; //for Report1 & Daily
	private String totalCopies; //for Report1
	private String numOfDidntReturnOnTime; //for Report1
	
	private ArrayList<String> comboBoxOptions; //for Report1
	
	

	public Report(float average,float median)
	{
		this.average=average;
		this.median=median;	
	}
	public Report()
	{

	}

	public String getActiveReaderAccounts() 
	{
		return activeReaderAccounts;
	}
	public void setActiveReaderAccounts(String activeReaderAccounts) 
	{
		this.activeReaderAccounts = activeReaderAccounts;
	}
	public String getFrozenReaderAccounts() 
	{
		return frozenReaderAccounts;
	}
	public void setFrozenReaderAccounts(String frozenReaderAccounts) 
	{
		this.frozenReaderAccounts = frozenReaderAccounts;
	}
	public String getLockedReaderAccounts() 
	{
		return lockedReaderAccounts;
	}
	public void setLockedReaderAccounts(String lockedReaderAccounts) 
	{
		this.lockedReaderAccounts = lockedReaderAccounts;
	}

	public float getAverage() 
	{
		return average;
	}



	public void setAverage(float average) 
	{
		this.average = average;
	}



	public float getMedian() 
	{
		return median;
	}



	public void setMedian(float median) 
	{
		this.median = median;
	}

	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList<Long> getDetailsArray() {
		return detailsArray;
	}
	public void setDetailsArray(ArrayList<Long> detailsArray) {
		this.detailsArray = detailsArray;
	}
	public String getTotalCopies() {
		return totalCopies;
	}
	public void setTotalCopies(String totalCopies) {
		this.totalCopies = totalCopies;
	}
	public String getNumOfDidntReturnOnTime() {
		return numOfDidntReturnOnTime;
	}
	public void setNumOfDidntReturnOnTime(String numOfDidntReturnOnTime) {
		this.numOfDidntReturnOnTime = numOfDidntReturnOnTime;
	}
	public ArrayList<String> getComboBoxOptions() {
		return comboBoxOptions;
	}
	public void setComboBoxOptions(ArrayList<String> comboBoxOptions) {
		this.comboBoxOptions = comboBoxOptions;
	}
	
	

}
