package Common;

public class User implements IEntity
{
	private String id; // if server returns here null -> user not exist
					  // in the Client we will first check this atribute
	private String password;
	private int permission; // 1 = Library Director , 2 = Librarian , 3 = reader account
	private boolean isOnline; 
}
