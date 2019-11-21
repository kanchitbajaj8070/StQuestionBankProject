package application.controllers;
import application.alert.alertMaker;
import application.database.*;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.input.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
public class loginController implements Initializable{
	private databaseHelper handler= null;
	public static Stage adminStage=null;
	public static FXMLLoader  adminLoader=null;
    @FXML
    private JFXTextField usernameText;

    @FXML
    private JFXPasswordField passwordText;

    @FXML
    private JFXTextField passwordDuplicate;
    public static String loggedUser;
    public static String loggedUserPassword;
    @FXML
    private Label welcomeLabel1;
   private static String  currentValueInPasswordField;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private Button loginButton;

    @FXML
    private Button forgotPasswordButton;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        
		handler=databaseHelper.getInstance();
		passwordDuplicate.visibleProperty().bind(showPasswordCheckBox.selectedProperty());
        passwordText.textProperty().bindBidirectional(passwordDuplicate.textProperty());
      
	}
	public void handleMouseEnteredAction(javafx.scene.input.MouseEvent event) throws Exception
	{    
		//usernameText.setEditable(false);
		//passwordText.setEditable(false);

		
	}
	   public void handleShowPasswordCheckBoxAction(ActionEvent actionEvent) {
	        if(showPasswordCheckBox.isSelected()) {
	            passwordDuplicate.setText(passwordText.getText());
	            passwordText.setVisible(false);
	        }
	        else
	        {
	            passwordText.setVisible(true);

	        }
	        System.out.println(passwordDuplicate.getText());
	        System.out.println(passwordText.getText());
	    }
	public void handleMouseEnteredInTextareaAction()
	{
		usernameText.setEditable(true);
		passwordText.setEditable(true);
	}
	public void handleLoginButtonAction(javafx.scene.input.MouseEvent event) throws Exception
	{
		String username=usernameText.getText();
		String password=passwordText.getText();
		int count=0;
		System.out.println(username);
		System.out.println(password);
		  if(handler.getInstance().checkLogin(username, password))
		  {
	    	System.out.println("LOGIN SUCCESFULL!!!!!");
	    	 loadHomeScreen();
	    	 loggedUser=username;
	    	 loggedUserPassword=password;
	    	}
	    else
	    {  
	    	application.alert.alertMaker.showErrorMessage("Wrong Credantials", "Please check your username and password again");
	    }
		/*
		 * ResultSet result=null; String query =
		 * "SELECT username,password from qb_user_profiles Where  username = ? and  password = ?"
		 * ; PreparedStatement preparedStatement =
		 * handler.getInstance().getConnection().prepareStatement(query);
		 * preparedStatement.setString(1, username); preparedStatement.setString(2,
		 * password); System.out.println(preparedStatement.toString());
		 * result=preparedStatement.executeQuery();
		 * 
		 * try { System.out.println("in try"); while(result.next())
		 * System.out.println(result.getString("username")+"	"+result.getString(
		 * "password"));
		 * 
		 * } catch (SQLException e) { // TODO: handle exception
		 * System.out.println(e.getLocalizedMessage()); }
		 */
}
	public void handleEnterKeyAction(KeyEvent event) throws Exception
	{
		if(event.getCode()==KeyCode.ENTER)
		{
			try
			{String username=usernameText.getText();
			String password=passwordText.getText();
			int count=0;
			System.out.println(username);
			System.out.println(password);
      if(handler.getInstance().checkLogin(username, password))
		    {
		    
		    	System.out.println("LOGIN SUCCESFULL!!!!!");
		    	loadHomeScreen();
		    	loggedUser=username;
		    	 loggedUserPassword=password;
		    }else
		    	application.alert.alertMaker.showErrorMessage("Wrong Credantials", "Please check your username and password again");
		}
			catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			}
	}
		
 }
	public void loadHomeScreen() throws Exception
	{
	      Stage current = (Stage) usernameText.getScene().getWindow();
          current.close(); 
          Stage dashboardStage = new Stage();
          adminStage=dashboardStage;
          adminLoader= new FXMLLoader();
         Parent root=adminLoader.load(getClass().getResource("/application/Fxmls/qbitAdminDashboard.fxml" ));
 		 	dashboardStage.setTitle("Question bank Internship Test System");
		    dashboardStage.setScene(new Scene(root));//,Screen.getPrimary().getBounds().getWidth()/2,Screen.getPrimary().getBounds().getHeight()/2));
		    dashboardStage.setResizable(false);
		    dashboardStage.initStyle(StageStyle.DECORATED);
		    dashboardStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent e) {
					e.consume();
				boolean ans=	alertMaker.showConformationMessage("Confirmation", "Are you sure you want to sign out ?","");
				if(ans==true)
				{
				databaseHelper.closeConnection();
				dashboardStage.close();

				}
					
				}
			});
		    dashboardStage.show();
		   
	}
	public void handleShowPasswordAction(ActionEvent event) throws Exception
	{
		if(showPasswordCheckBox.isSelected())
		{   currentValueInPasswordField=passwordText.getText();
			passwordText.clear();
			passwordText.setPromptText(currentValueInPasswordField);
		}else
		{
			passwordText.setText(currentValueInPasswordField);
			passwordText.setPromptText("Enter your password");
		}
	}
}

