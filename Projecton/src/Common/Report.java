package Common;

import java.io.Serializable;

public class Report implements IEntity , Serializable
{
	private float average;
	private float median;
	
	private String activeReaderAccounts;
	private String frozenReaderAccounts;
	private String lockedReaderAccounts;

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
	
	
}
