package Common;

import java.io.Serializable;
import java.util.ArrayList;

public class Report implements IEntity , Serializable
{
	private float average; //for Report2 & Report3
	private float median; //for Report2 & Report3
	private int total; //for Report3

	private String activeReaderAccounts; //for Report2
	private String frozenReaderAccounts; //for Report2
	private String lockedReaderAccounts; //for Report2
	
	ArrayList<Long> detailsArray;

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
	
	

}
