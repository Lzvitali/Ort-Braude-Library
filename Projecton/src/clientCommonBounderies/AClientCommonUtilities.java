package clientCommonBounderies;

import java.io.IOException;
import java.util.Optional;

import clientBounderiesLibrarian.StartPanelLibrarianController;
import clientConrollers.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * @author Vitali
 * This Class contains the common action for the GUIs controllers
 */

public abstract class AClientCommonUtilities 
{
	/**
	 * This method opens a new GUI window 
	 * @param classThatAsk - in the original Class just type: getClass() 
	 * @param loc - is the full location of the fxml file. for example: "/clientBounderiesLibrarian/AddBook.fxml"
	 * @param title - title is the title of the new window
	 */
	public static void loadWindow(Object classThatAsk, String loc, String title)
	{
		try 
		{
			if(StartPanelLibrarianController.ActiveWindows==0)
			{
				Parent parent = FXMLLoader.load(((Class<?>)classThatAsk).getResource(loc));
				Stage stage = new Stage(StageStyle.DECORATED);
				stage.setTitle(title);
				stage.setScene(new Scene(parent));
				stage.show();
				stage.setOnCloseRequest(e->
				{
					stage.close();
					StartPanelLibrarianController.ActiveWindows--;
				});
				StartPanelLibrarianController.ActiveWindows++;
			}
			else
			{
				infoAlert("You have already active window ,close him first","Already activate task");
			}

		} 
		catch (IOException e) 
		{
			alertError("An error has accourd. window can't load","Error"); // open error message
			e.printStackTrace();
		}

	}
	
	
	/**
	 * This method prompts an alert Error Message 
	 * @param headerText - the text message to the user
	 * @param title - the title for the window
	 */
	public static void alertError(String headerText,String title) 
    { 
		Platform.runLater(()->
		{  
			Alert alert = new Alert(Alert.AlertType.ERROR);
			ButtonType bttexit = new ButtonType("exit", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().clear();
			alert.setHeaderText(headerText);
			alert.setTitle(title);
			alert.getButtonTypes().addAll(bttexit);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) 
			{
				System.exit(0);
			}
		});
    }
	
	
	/**
	 * This function prompts an alert message to the user (not the Error one) 
	 * @param headerText
	 * @param title
	 */
	public static void infoAlert(String headerText,String title)  
	{
		Platform.runLater(()->
		{   
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			ButtonType bttCountiue = new ButtonType("countiue", ButtonBar.ButtonData.FINISH);
			alert.getButtonTypes().clear();
			alert.setHeaderText(headerText);
			alert.setTitle(title);
			alert.getButtonTypes().addAll(bttCountiue);
			Optional<ButtonType> result = alert.showAndWait();
		});
	}


}
