/**
 * Sample Skeleton for 'MyOrdersAndBorrows.fxml' Controller Class
 */

package clientBounderiesReaderAccount;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class MyOrdersAndBorrowsController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="BookNameBorrowColumn"
    private TableColumn<?, ?> BookNameBorrowColumn; // Value injected by FXMLLoader

    @FXML // fx:id="AuthorNameBorrowColumn"
    private TableColumn<?, ?> AuthorNameBorrowColumn; // Value injected by FXMLLoader

    @FXML // fx:id="YearBorrowColumn"
    private TableColumn<?, ?> YearBorrowColumn; // Value injected by FXMLLoader

    @FXML // fx:id="TopicBorrowColumn"
    private TableColumn<?, ?> TopicBorrowColumn; // Value injected by FXMLLoader

    @FXML // fx:id="BorrowDateColumn"
    private TableColumn<?, ?> BorrowDateColumn; // Value injected by FXMLLoader

    @FXML // fx:id="ReturnDateColumn"
    private TableColumn<?, ?> ReturnDateColumn; // Value injected by FXMLLoader

    @FXML // fx:id="DelayBorrowButtonColumn"
    private TableColumn<?, ?> DelayBorrowButtonColumn; // Value injected by FXMLLoader

    @FXML // fx:id="BookNameReservColumn"
    private TableColumn<?, ?> BookNameReservColumn; // Value injected by FXMLLoader

    @FXML // fx:id="AuthorNameReservColumn"
    private TableColumn<?, ?> AuthorNameReservColumn; // Value injected by FXMLLoader

    @FXML // fx:id="YeareReservColumn"
    private TableColumn<?, ?> YeareReservColumn; // Value injected by FXMLLoader

    @FXML // fx:id="TopicReservColumn"
    private TableColumn<?, ?> TopicReservColumn; // Value injected by FXMLLoader

    @FXML // fx:id="ButtonOrderColumn"
    private TableColumn<?, ?> ButtonOrderColumn; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        

    }
}
