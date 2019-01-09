package clientConrollers;

	

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

//main to check the gui 
public class Main extends Application 
{
	public static Stage PrimaryStage;
	@Override
	public void start(Stage primaryStage) throws IOException
	{
		Parent root;
		 
		root = FXMLLoader.load(getClass().getResource("/clientBounderiesLibrarian/StartPanelLibrarian.fxml"));
			
		//attach scene graph to scene
		Scene scene = new Scene(root);
		PrimaryStage=primaryStage;
		//setting the stage
		PrimaryStage.setOnCloseRequest(e->
		{ 
			System.exit(0);
		});
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
