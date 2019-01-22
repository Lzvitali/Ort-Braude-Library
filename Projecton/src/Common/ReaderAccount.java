package Common;

import java.io.Serializable;

import javafx.scene.control.Button;

public class ReaderAccount extends User
{

	private static final long serialVersionUID = 1L; 
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String status; // active/frozen/locked
	private int numOfDelays;
	private String adress;//add to table column 
	private String educationYear;//add to table column 

	//buttons for the Librarian and for the Library director
	private transient Button borrowsAndReserves;
	private transient Button freeze;
	
	
	//Constructors
	public ReaderAccount(String id, String password, int permission, boolean isOnline, String firstName,
			String lastName, String phone, String email, String status, int numOfDelays, String adress,
			String educationYear) 
	{
		super(id, password, permission, isOnline);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.status = status;
		this.numOfDelays = numOfDelays;
		this.adress = adress;
		this.educationYear = educationYear;
		
		this.borrowsAndReserves= new Button("Open");
		this.freeze= new Button("Freeze");
	}
	
	public ReaderAccount(String id, int permission, boolean isOnline, String firstName,
			String lastName, String phone, String email, String status, int numOfDelays, String adress,
			String educationYear) 
	{
		super(id, permission, isOnline);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.status = status;
		this.numOfDelays = numOfDelays;
		this.adress = adress;
		this.educationYear = educationYear;
		
		this.borrowsAndReserves= new Button("Open");
		this.freeze= new Button("Freeze");
	}
	public ReaderAccount(String id, int permission, boolean isOnline, String firstName,
			String lastName, String phone, String email, String status, int numOfDelays, String adress) 
	{
		super(id, permission, isOnline);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.status = status;
		this.numOfDelays = numOfDelays;
		this.adress = adress;
		this.educationYear = null;
		
		this.borrowsAndReserves= new Button("Open");
		this.freeze= new Button("Freeze");
	}
	
	//without numOfDelayes and status
	public ReaderAccount(String id, int permission, boolean isOnline, String firstName,
			String lastName, String phone, String email, String adress, String educationYear) 
	{
		super(id, permission, isOnline);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.adress = adress;
		this.educationYear =educationYear;
		
		this.borrowsAndReserves= new Button("Open");
		this.freeze= new Button("Freeze");
	}
	
	
	public ReaderAccount(String id,String firstName,String lastName, String phone,String email,String address,String status,String educationYear) 
	{
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.status = status;
		this.numOfDelays = -1;
		this.adress = address;
		this.educationYear = educationYear;
		this.borrowsAndReserves= null;
		this.freeze= null;
	}
	
	public ReaderAccount(String id,String firstName,String lastName, String phone,String status) 
	{
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = null;
		this.status = status;
		this.numOfDelays = -1;
		this.adress = null;
		this.educationYear = null;
		this.borrowsAndReserves= null;
		this.freeze= null;
	}
	
	
	public ReaderAccount() 
	{
		super();
		this.firstName = null;
		this.lastName = null;
		this.phone = null;
		this.email = null;
		this.status = null;
		this.numOfDelays = -1;
		this.adress = null;
		this.educationYear = null;
		
		this.borrowsAndReserves= null;
		this.freeze= null;
	}

	public ReaderAccount(String id) 
	{
		super(id);
		this.firstName = null;
		this.lastName = null;
		this.phone = null;
		this.email = null;
		this.status = null;
		this.numOfDelays = -1;
		this.adress = null;
		this.educationYear = null;
	}
	
	//getters and setters
	public String getFirstName() 
	{
		return firstName;
	}


	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}


	public String getLastName() 
	{
		return lastName;
	}


	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}


	public String getPhone() 
	{
		return phone;
	}


	public void setPhone(String phone) 
	{
		this.phone = phone;
	}


	public String getEmail() 
	{
		return email;
	}


	public void setEmail(String email)
	{
		this.email = email;
	}


	public String getStatus() 
	{
		return status;
	}


	public void setStatus(String status) 
	{
		this.status = status;
	}


	public int getNumOfDelays() 
	{
		return numOfDelays;
	}


	public void setNumOfDelays(int numOfDelays) {
		this.numOfDelays = numOfDelays;
	}


	public String getAdress() {
		return adress;
	}


	public void setAdress(String adress) {
		this.adress = adress;
	}


	public String getEducationYear() {
		return educationYear;
	}


	public void setEducationYear(String educationYear) {
		this.educationYear = educationYear;
	}


	public Button getBorrowsAndReserves() {
		return borrowsAndReserves;
	}


	public void setBorrowsAndReserves(Button borrowsAndReserves) {
		this.borrowsAndReserves = borrowsAndReserves;
	}


	public Button getFreeze() {
		return freeze;
	}


	public void setFreeze(Button freeze) {
		this.freeze = freeze;
	}


	@Override
	public String toString() 
	{
		return "ReaderAccount [firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + ", email="
				+ email + ", status=" + status + ", numOfDelays=" + numOfDelays + ", adress=" + adress
				+ ", educationYear=" + educationYear + ", borrowsAndReserves=" + borrowsAndReserves + ", freeze="
				+ freeze + "]";
	}
	
	
	
}
