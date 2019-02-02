package clientConrollers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import Common.IGUIController;
import Common.IGUIStartPanel;
import clientBounderiesLibrarian.StartPanelLibrarianController;
import clientCommonBounderies.StartPanelController;


/**
 * Start the client application , open the StartPanel with the request host and port
 */


public class AStartClient extends Application
{
	public static Stage primaryStagePanel;// the primarystage that running
	public static IGUIStartPanel startPanelController;//The main panel that running at the moment
	private String choosen="/clientBounderiesLibrarian/StartPanelLibrarian.fxml"; //Librarian path of fxml
	private String choosen1="/clientCommonBounderies/StartPanel.fxml";//Guest path of fxml
	private String choosen2="/clientBounderiesReaderAccount/StartPanelReaderAccount.fxml";//Reader path of fxml
	public static String serverIP ;
	
	
	/**
	 * lunch the client application gui , opening the guest form.
	 */
    @Override
    public void start(Stage primaryStage) throws Exception //start the client (open FXML/Controller)
    {
        FXMLLoader loader=new FXMLLoader(getClass().getResource(choosen1)); // load the FXML file
        Parent root = (Parent) loader.load();
        startPanelController = loader.getController();//get the controller of fxml
        primaryStagePanel=primaryStage;
		Parameters params = getParameters();
		List<String> list = params.getRaw(); 
		String arr[]=new String[2];
        try // try to send the arguments from command line to GUIController
        {
        	arr[0]=list.get(0);
        	arr[1]=list.get(1);
        	((StartPanelController)(startPanelController)).initialize(arr);
        }
        catch(IndexOutOfBoundsException e) // if entered bad arguments/didnt enter at all send default 
        {
        	arr[0]="localhost";
        	arr[1]="5555";
        	((StartPanelController)(startPanelController)).initialize(arr);
        }
        serverIP = arr[0];
        root.setId("pane");
        primaryStage.setTitle("OBL");
        Scene scene=new Scene(root, 1400, 774);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e->{ System.exit(0);}); // if the user press "cross" button , its close all threads ( exit from process);
        primaryStage.show(); //start the scene
    }

    /**
     * Start the client application , first parmater is the ip , secound argument is port
     */
    
    public static void main(String[] args)
    
    {
        launch(args); // lunch the scene
    }

}