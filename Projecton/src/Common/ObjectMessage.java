package Common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * communication class between client and server , and the opposite
*/

public class ObjectMessage implements Serializable 
{
	
	private ArrayList<IEntity> objectList;
	private ArrayList<IEntity[]> objectArray;
	private String message;

	//Constructors
	public ObjectMessage(String message)
	{
		objectList=new ArrayList<IEntity>();
		objectArray=new ArrayList<IEntity[]>();;
		this.message=message;
	}
	
	public ObjectMessage(IEntity entityOne,String Message)
	{
		objectList=new ArrayList<IEntity>();
		objectList.add(entityOne);
		objectArray=new ArrayList<IEntity[]>();;
		this.message=message;
	}
	
	public ObjectMessage(IEntity entityOne,IEntity entityTwo ,String Message)
	{
		objectList=new ArrayList<IEntity>();
		objectList.add(entityOne);
		objectList.add(entityTwo);
		objectArray=new ArrayList<IEntity[]>();;
		this.message=message;
	}
	
	public ObjectMessage(IEntity arrayObject[],String Message)
	{
		objectList=new ArrayList<IEntity>();
		objectArray=new ArrayList<IEntity[]>();
		objectArray.add(arrayObject);
		this.message=Message;
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
		objectArray.add(entity);
	}
	//Getters And Setters
	
	public ArrayList<IEntity> getObjectList() 
	{
		return objectList;
	}

	public ArrayList<IEntity[]> getObjectArray() 
	{
		return objectArray;
	}

	public String getMessage() 
	{
		return message;
	}
	
	


}
