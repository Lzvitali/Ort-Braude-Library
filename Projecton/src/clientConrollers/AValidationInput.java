package clientConrollers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @author Ester Asulin
 * this class chack if the fields of the registration user form are valid
 *
 */
public abstract class AValidationInput 
{/**
*this method call to the relevant method according the field that was sent
*/
	public static String checkValidationUser(String field, String context)
	{
		String result="correct";
		switch(field)
		{
			case "UserID":
			{	
				result=checkValidationID(context);
				break;
			}
			case "Last Name":
			{
				result=checkValidationLastName(context);
				break;
			}
			case "First Name":
			{
				result=checkValidationFirstName(context);
				break;
			}
			case "Phone Number":
			{
				result=checkValidationPhoneNumber(context);
				break;

			}
			case "Email":
			{
				result=checkValidationEmail(context);
				break;

			}
			case "bookID":
			{
				result=checkValidationBookID(context); 
				break;

			}
			case "bookName":
			{
				result=checkValidationBookName(context);
				break;

			}
			case "authorName":
			{
				result=checkValidationAuthorName(context);
				break;

			}
			case "dateOfBook":
			{
				result=checkValidationDateOfBook(context); 
				break;

			}
			case "topic":
			{
				result=checkValidationTopic(context);
				break;

			}
				
		}
		return result;
	}
	/**
	 * this method chack if the email adress is valid
	 * @param context is the value of the field
	 * @return correct if context passed all the Tests else return the match massage by the test how's fails 
	 */
	private static String checkValidationEmail(String context)
	{
		if(context.equals(""))
		{
			return"Insert Email";	
		}
		if(context.length()>30)
		{
			return "Insert 30 characters";
		}
		if(!correctEmail(context))
		{
			return "Please Enter a correct Email";
		}
		return "correct";
	}
/**
 * this method check if the phone number is valid
 * @param context is the value of the field
 * @return correct if context passed all the Tests else return the match massage by the test how's fails 
*/
	private static String checkValidationPhoneNumber(String context)
	{
		if(context.equals(""))
		{
			return"Insert Phone Number";	
		}
		if(context.length()!=10)
		{
			return "Insert 10 digit phone number";
		}
		if(!onlyNumbers(context))
		{
			return "You must be fill only numbers";
		}
		if(!phoneNumber(context))
		{
			return "Please Insert your Phone Numbers by pattern 05_ _ _ _ _ _ _ _ _ _";
		}
		return "correct";
	}
	/**
	 * this method check if the first name is valid
	 * @param context is the value of the field
	 * @return correct if context passed all the Tests else return the match massage by the test how's fails 
	*/
	private static String checkValidationFirstName(String context)
	{
		if(context.equals(""))
		{
			return"Insert First Name";	
		}
		if(context.length()>15)
		{
			return "The First Name is too long";
		}
		if(!onlyCharacters(context))
		{
			return "You must fill only character";
		}
		return "correct";
	}
	/**
	 * this method check if the Last name is valid
	 * @param context is the value of the field
	 * @return correct if context passed all the Tests else return the match massage by the test how's fails 
	*/
	private static String checkValidationLastName(String context)
	{
		if(context.equals(""))
		{
			return"Insert Last Name";	
		}
		if(context.length()>15)
		{
			return "The Last Name is too long";
		}
		if(!onlyCharacters(context))
		{
			return "You must fill only character";
		}
		return "correct";
	}
	/**
	 * this method check if the ID is valid
	 * @param context is the value of the field
	 * @return correct if context passed all the Tests else return the match massage by the test how's fails 
	*/
	private static String checkValidationID(String context)
	{
		if(context.equals(""))
		{
			return"Insert 9 digit user id";	
		}
		if(context.length()<9)
		{
			return "Insert 9 digit user id or fill zero before";
		}
		if(!onlyNumbers(context))
		{
			return "You must be fill only numbers";
		}
		if(!chackDigit(context))
		{
			return "Insert correct user id";
		}
		return "correct";
	}
	/**
	 * this method is called by chackValidationEmail
	 * it check if the email contain '@' and '.' and the suffix email is legal
	 * @param context is the value of the field
	 * @return true if context passed all the Tests else return false 
	 */
	private static Boolean correctEmail(String context)
	{
		int saveIndexStrudel=-1,j,saveIndexPoint=-1;
		for(int i=0;i<context.length();i++)
		{
			if(context.charAt(i)=='@')
			{
				saveIndexStrudel=i;
			}
		}
		if(saveIndexStrudel==-1)
		{
			return false;
		}
		
		for(j=saveIndexStrudel;j<context.length();j++)
		{
			if(context.charAt(j)=='.')
			{
				saveIndexPoint=j;
			}
		}
		String temp=context.substring((saveIndexPoint+1),context.length());
		if(saveIndexPoint==-1)
		{
			return false;
		}
		if(!temp.equals("com")&&!temp.equals("net")&&!temp.equals("co.il")&&!temp.equals("ac.il"))
		{
			return false;
		}
		return true;
	}
	/**
	 * this method is called by chackValidationPhoneNumber
	 * it checks if the thid char is legal
	 * @param context is the value of the field
	 * @return true if context passed all the Tests else return false 
	 */
	private static Boolean phoneNumber(String context)
	{
		int i=2;
		if(context.charAt(i)=='1'||context.charAt(i)=='5'||context.charAt(i)=='6'||context.charAt(i)=='9')
		{
			return false;
		}
		return true;
	}
	/**
	 * this method is called by chackValidationLastName or chackValidationfirstName
	 * is checks if the context contain only characters
	* @param context is the value of the field
	 * @return true if context passed all the Tests else return false 
	 */
	private static Boolean onlyCharacters(String context)
	{
		for(int i=0;i<context.length();i++)
		{
			if (!((context.charAt(i) >= 'a' && context.charAt(i) <= 'z') || (context.charAt(i)) >= 'A' && (context.charAt(i) <= 'Z')))
			{
				return false;
			}

		}
		return true;
	}
	
	private static boolean checkDateOfBookBeforeToday(String context)
	{
		
		
		//check if date before is before today
	    Date enteredDate=null;
	    try
	    {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
	    enteredDate = sdf.parse(context);
	    }catch (Exception ex)
	    {
	        // enteredDate will be null if date="287686";
	    }
	    Date currentDate = new Date();      
	    if(enteredDate.after(currentDate)){
	        return false;
	    }
	    else 
	    	return true;
	    
		
	}
	
	/**
	 * this method is called by checkValidationPhoneNumbers or chackValidationID
	 * it checks if the string contains only numbers
	 * @param context is the value of the field
	 * @return true if context passed all the Tests else return false 
	 */
	private static Boolean onlyNumbers(String context)
	{
		for(int i=0;i<9;i++) 
		{
			if(context.charAt(i)<'0' ||context.charAt(i)>'9')
			{
				return false;
			}
		}
		return true;
	}
/**
 * this method is called by checkValidID 
 * this method check the last digit of the ID by the known algorithem for this
 * @param context is the value of the field
 * @return true if context passed all the Tests else return false 
 */
	private static Boolean chackDigit(String context)
	{   //sum= sum of all weight digits, digitWeight= current weight numbers, temp= temporary variable in case the weightdigit bigger than 9
		int sum=0, digitWeight=0,temp=0;
		for(int i=0;i<9;i++)
		{
			int currentChar=Character.getNumericValue(context.charAt(i));
			if(i%2==0)
			{
				digitWeight=currentChar;
			}
			else
			{
				digitWeight=currentChar*2;
				if(digitWeight>9)
				{
					temp=(digitWeight%10);
					temp+=(digitWeight/10);
					digitWeight=temp;
				}
				
				
			}
			sum+=digitWeight;
			digitWeight=0;
			temp=0;
		}
		if(sum%10!=0)
		{
			return false;
		}
		return true;
	}
	
	private static String checkValidationBookID(String context)
	{
		if(context.equals(""))
		{
			return"Insert 9 digit book id";	
		}
		if(context.length()<9)
		{
			return "Insert 9 digit book id or fill zero before";
		}
		if(!onlyNumbers(context))
		{
			return "You must fill only numbers";
		}
		
		return "correct";
	}
	
	private static String checkValidationBookName(String context)
	{
		if(context.equals(""))
		{
			return"Insert Name Of A Book";	
		}
		if(context.length()>15)
		{
			return "The Book Name is too long";
		}
		
		return "correct";
	}
	
	
	private static String checkValidationAuthorName(String context)
	{
		if(context.equals(""))
		{
			return "Insert Author Name";	
		}
		if(context.length()>15)
		{
			return "The Author Name is too long";
		}
		if(!onlyCharacters(context))
		{
			return "You must fill only character";
		}
		return "correct";
	}
	
	private static String checkValidationTopic(String context)
	{
		if(context.equals(""))
		{
			return "Insert Topic";	
		}
		if(context.length()>15)
		{
			return "The Topic is too long"; 
		}
		if(!onlyCharacters(context))
		{
			return "You must fill only character";
		}
		return "correct";
	}
	
	private static String checkValidationDateOfBook(String context)
	{
		if(!onlyNumbers(context))
		{
			return "Year must be only numbers";
		}
		if(!checkDateOfBookBeforeToday(context)) 
		{
			return "Date is bigger than the date of today";	
		}
		return "correct";
	}
}