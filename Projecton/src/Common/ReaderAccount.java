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
}
