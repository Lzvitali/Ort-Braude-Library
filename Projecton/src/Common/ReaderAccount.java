package Common;

import javafx.scene.control.Button;

public class ReaderAccount extends User
{
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String status; // active/frozen/locked
	private int numOfDelays;
	private String adress;//add to table column 
	private String educationYear;//add to table column 

	//buttons for the Librarian and for the Library director
	private Button borrowsAndReserves;
	private Button freeze;
	
	
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
	
	
	
}
