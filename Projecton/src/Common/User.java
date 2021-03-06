package Common;

import java.io.Serializable;

public class User implements IEntity,Serializable
{

	private static final long serialVersionUID = 1L;
	private String id; // if server returns here null -> user not exist
					  // in the Client we will first check this atribute
	private String password;// user password
	private int permission; // 1 = Library Director , 2 = Librarian , 3 = reader account
	private boolean isOnline;
	
	
	
	//constructors
	public User(String id, String password, int permission, boolean isOnline) 
	{
		this.id = id;
		this.password = password;
		this.permission = permission;
		this.isOnline = isOnline;
	}	
	
	public User(String id, int permission, boolean isOnline) 
	{
		this.id = id;
		this.password = null;
		this.permission = permission;
		this.isOnline = isOnline;
	}
	
	public User() 
	{
		this.id = null;
		this.password = null;
		this.permission = -1;
		this.isOnline = false;
	}	

	
	public User(String id, String password) 
	{
		this.id = id;
		this.password = password;
	}
	

	public User(String id) 
	{
		this.id = id;
	}


	//getters and setters
	public String getId() 
	{
		return id;
	}
	
	public void setId(String id) 
	{
		this.id = id;
	}
	
	public String getPassword() 
	{
		return password;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public int getPermission() 
	{
		return permission;
	}
	
	public void setPermission(int permission) 
	{
		this.permission = permission;
	}
	
	public boolean isOnline() 
	{
		return isOnline;
	}
	
	public void setOnline(boolean isOnline) 
	{
		this.isOnline = isOnline;
	}




	@Override
	public String toString() 
	{
		return "User [id=" + id + ", password=" + password + ", permission=" + permission + ", isOnline=" + isOnline
				+ "]";
	} 
	
	
	
}
