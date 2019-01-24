package Common;

import java.io.Serializable;

public class Report implements IEntity , Serializable
{
	private float average;
	private float median;



	public Report(float average,float median)
	{
		this.average=average;
		this.median=median;	
	}



	public float getAverage() {
		return average;
	}



	public void setAverage(float average) {
		this.average = average;
	}



	public float getMedian() {
		return median;
	}



	public void setMedian(float median) {
		this.median = median;
	}
	
	
}
