package application;
	
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
 
	public void start(Stage primaryStage) throws Exception  
	{
		 
		 Parent root = FXMLLoader.load(getClass().getResource("/application/Fxmls/qbitLogin.fxml" ));
    		 primaryStage.setTitle("Question bank Internship Test System"); 
  		    primaryStage.setScene(new Scene(root));
  		    primaryStage.initStyle(StageStyle.UTILITY);
  		    primaryStage.setResizable(false);
  		    primaryStage.show();
  		    
	
	}
	
	public static void main(String[] args) {

		launch(args);
	}
	
}
/* change password implemetation still left */
/* implementation of getting question type in database handler and questionsController*/
/* delete functionality not fully functional *//* delete is functional as of now but testing require */
/*category delete last node problem -*/
/* users  delete testing pending*/
/*discuss paper table with inderjeet sir , regarding columns required in it*/
/* fix unneceesary appearances of labels in add question , search filter */
/* handle edit action still left ,database implementation of edit also left */
/* some conversion of result set to arraylist left*/

/*sequence creation*/
/*ldap integration still left*/
/*make paper tab into two and give a button for generating new ones and a list having three option view , download , download answer key*/