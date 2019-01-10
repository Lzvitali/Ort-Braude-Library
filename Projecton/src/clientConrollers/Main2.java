package clientConrollers;

	

import java.io.IOException;

import clientBounderiesLibrarian.StartPanelLibrarianController;
import clientCommonBounderies.StartPanelController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

//main to check the gui 
public class Main2 extends Application 
{
	public static Stage PrimaryStage;
	public static FXMLLoader temp;
	public static String arr[]=new String[2]; 
	
	@Override
	public void start(Stage primaryStage) throws IOException
	{
		
		temp = new FXMLLoader(getClass().getResource("/clientCommonBounderies/StartPanel.fxml"));
		Parent root = temp.load();
			
		//attach scene graph to scene
		Scene scene = new Scene(root);
		PrimaryStage=primaryStage;
		//setting the stage
		PrimaryStage.setOnCloseRequest(e->
		{ 
			System.exit(0);
		});
		
		StartPanelController startPanelController=temp.getController(); 
		
		arr[0]="localhost"; // host
    	arr[1]="5555";  // port
    	
    	startPanelController.initialize(arr);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Try");
		primaryStage.show();
		
	}
	
	public static void main(String[] args) 
	{
		//hi everyone my name is Nata
		launch(args);
	}
}
