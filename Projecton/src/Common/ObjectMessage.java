package Common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * communication class between client and server , and the opposite
*/

public class ObjectMessage implements Serializable 
{
	
	private static final long serialVersionUID = 1L; // lo yodea im tzarih (Vitali)
	private ArrayList<IEntity> objectList;
	private ArrayList<IEntity[]> objectListOfArrays; 
	private String message;
	private String note;
	//Constructors
	public ObjectMessage()
	{
		objectList=new ArrayList<IEntity>();
		objectListOfArrays=new ArrayList<IEntity[]>();;
		this.message="";
	}
	public ObjectMessage(String message)
	{
		objectList=new ArrayList<IEntity>();
		objectListOfArrays=new ArrayList<IEntity[]>();;
		this.message=message;
	}
	
	public ObjectMessage(IEntity entityOne,String message)
	{
		objectList=new ArrayList<IEntity>();
		objectList.add(entityOne);
		objectListOfArrays=new ArrayList<IEntity[]>();;
		this.message=message;
	}
	
	public ObjectMessage(IEntity entityOne,IEntity entityTwo ,String message)
	{
		objectList=new ArrayList<IEntity>();
		objectList.add(entityOne);
		objectList.add(entityTwo);
		objectListOfArrays=new ArrayList<IEntity[]>();;
		this.message=message;
	}
	
	public ObjectMessage(IEntity arrayObject[],String message)
	{
		objectList=new ArrayList<IEntity>();
		objectListOfArrays=new ArrayList<IEntity[]>();
		objectListOfArrays.add(arrayObject);
		this.message=message;
	}
	
	
	//Private Methods
	
	
	//Public Methods 
	
	public void addObject(IEntity entity)
	{
		objectList.add(entity);
	}
	
	public void addObject(IEntity entityOne,IEntity entityTwo)
	{
		objectList.add(entityOne);
		objectList.add(entityTwo);
	}

	public int getSizeOfEntitys()
	{
		return objectList.size();
	}
	
	public void addObjectArray(IEntity entity[])
	{
		objectListOfArrays.add(entity);
	}
	
	
	//Getters And Setters
	
	public ArrayList<IEntity> getObjectList() 
	{
		return objectList;
	}

	public ArrayList<IEntity[]> getObjectArray() 
	{
		return objectListOfArrays;
	}

	public String getMessage() 
	{
		return message;
	}
	
	
	public void setMessage(String msg) 
	{
		message=msg;
	}
	
	
	//toString
	@Override
	public String toString() 
	{
		return "ObjectMessage [objectList=" + objectList + ", objectListOfArrays=" + objectListOfArrays + ", message="
				+ message + "]";
	}

	
	

}
