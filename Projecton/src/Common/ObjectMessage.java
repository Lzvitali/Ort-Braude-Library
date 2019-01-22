package Common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * communication class between client and server , and the opposite
*/

public class ObjectMessage implements Serializable 
{
	
	private static final long serialVersionUID = 1L; 
	private ArrayList<IEntity> objectList;
	private ArrayList<IEntity[]> objectListOfArrays; 
	private String message;
	private String note; //for the sorting in OBLServer. Recommended to fill as: User, Book, Copy, ReaderAccont (the entity name)
						 //by that attribute we will know to witch controller of the Server package to send it
	private String extra;
	
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
	
	
	public ObjectMessage(IEntity entityOne,String message, String note)
	{
		objectList=new ArrayList<IEntity>();
		objectList.add(entityOne);
		objectListOfArrays=new ArrayList<IEntity[]>();;
		this.message=message;
		this.note=note;
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
		objectListOfArrays=new ArrayList<IEntity[]>();
		this.message=message;
	}
	public ObjectMessage(IEntity entityOne,IEntity entityTwo ,String message, String note)
	{
		objectList=new ArrayList<IEntity>();
		objectList.add(entityOne);
		objectList.add(entityTwo);
		objectListOfArrays=new ArrayList<IEntity[]>();
		this.message=message;
		this.note=note;
	}
	
	public ObjectMessage(IEntity entityOne,IEntity entityTwo,String message,String note,String extra)
	{
		objectList=new ArrayList<IEntity>();
		objectList.add(entityOne);
		objectList.add(entityTwo);
		objectListOfArrays=new ArrayList<IEntity[]>();
		this.note=note;
		this.message=message;
		this.extra=extra;
	}
	
	public ObjectMessage(IEntity arrayObject[],String message)
	{
		objectList=new ArrayList<IEntity>();
		objectListOfArrays=new ArrayList<IEntity[]>();
		objectListOfArrays.add(arrayObject);
		this.message=message;
	}
	
	
	public ObjectMessage(ArrayList<IEntity> arrayList,String message,String note)
	{
		objectList=arrayList;
		objectListOfArrays=new ArrayList<IEntity[]>();
		this.message=message;
		this.note=note;
	}
	
	
	public ObjectMessage(String message,String note)
	{
		objectList=new ArrayList<IEntity>();
		objectListOfArrays=new ArrayList<IEntity[]>();
		this.message=message;
		this.note=note;
	}
	
	public ObjectMessage(String message,String note,String extra)
	{
		objectList=new ArrayList<IEntity>();
		objectListOfArrays=new ArrayList<IEntity[]>();
		this.message=message;
		this.note=note;
		this.extra=extra;
	}
	
	
	public ObjectMessage(ArrayList<IEntity[]> objectListOfArrays, String message) 
	{
		this.objectListOfArrays = objectListOfArrays;
		this.message = message;
	}
	
	
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

	public String getNote() 
	{
		return note;
	}
	
	
	
	public void setNote(String note) 
	{
		this.note = note;
	}
	public void setMessage(String msg) 
	{
		message=msg;
	}
	

	public String getExtra()
	{
		return extra;
	}
	
	public void setExtra(String extra) 
	{
		this.extra = extra;
	}
	//toString
	@Override
	public String toString() 
	{
		return "ObjectMessage [objectList=" + objectList + ", objectListOfArrays=" + objectListOfArrays + ", message="
				+ message + "]";
	}

	
	

}
