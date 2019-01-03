package Common;
	

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class MainForBounberyCheck extends Application 
{
	
	@Override
	public void start(Stage primaryStage) throws IOException
	{
		Parent root;
		
		root = FXMLLoader.load(getClass().getResource("TtyJFoenix.fxml"));
			
		//attach scene graph to scene
		Scene scene = new Scene(root);
		
		//setting the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("Try");
		primaryStage.show();
		
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
