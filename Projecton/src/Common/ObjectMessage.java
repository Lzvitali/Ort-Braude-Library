package Common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * communication class between client and server , and the opposite
*/

public class ObjectMessage implements Serializable 
{
	
	private ArrayList<IEntity> objectList;
	private IEntity objectArray[];
	private String message;

	//Constructors
	public ObjectMessage(String message)
	{
		objectList=new ArrayList<IEntity>();
		objectArray=null;
		this.message=message;
	}
	
	public ObjectMessage(IEntity entityOne,String Message)
	{
		objectList=new ArrayList<IEntity>();
		objectList.add(entityOne);
		objectArray=null;
		this.message=message;
	}
	
	public ObjectMessage(IEntity entityOne,IEntity entityTwo ,String Message)
	{
		objectList=new ArrayList<IEntity>();
		objectList.add(entityOne);
		objectList.add(entityTwo);
		objectArray=null;
		this.message=message;
	}
	
	public ObjectMessage(IEntity arrayObject[],String Message)
	{
		objectList=new ArrayList<IEntity>();
		this.objectArray=arrayObject;
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
	
	//Getters And Setters
	
	public ArrayList<IEntity> getObjectList() 
	{
		return objectList;
	}

	public IEntity[] getObjectArray() 
	{
		return objectArray;
	}

	public String getMessage() 
	{
		return message;
	}
	
	


}
