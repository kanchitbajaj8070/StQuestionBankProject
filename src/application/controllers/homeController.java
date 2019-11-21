package application.controllers;
import application.alert.alertMaker;
import application.database.*;
import application.model.usersModel;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
public class homeController implements Initializable {

	@FXML
	private Label label0;

	@FXML
	private Label label1;

	@FXML
	private Label setUsername;

	@FXML
	private Label label2;

	@FXML
	private Label setRole;

	@FXML
	private Label label3;

	@FXML
	private Label setCategory;

	@FXML
	private Label label7;

	@FXML
	private Label label4;

	@FXML
	private PasswordField oldPasswordLabel;

	@FXML
	private Label label5;

	@FXML
	private PasswordField newPasswordLabel;

	@FXML
	private Label label6;

	@FXML
	private PasswordField confirmPasswordLabel;

	@FXML
	private JFXButton changePasswordButton;

 private static databaseHelper handler=null;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {


		//resizeFunctionality();
		initLoad();
	}

	@FXML
	private AnchorPane anchorPaneHome;
	private void resizeFunctionality() {
		double width =loginController.adminStage.getWidth();
		anchorPaneHome.setPrefWidth(width);
		anchorPaneHome.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double width=(double)newValue;
				label0.setPrefWidth(width*.45);
				label1.setPrefWidth(width*.35);
				label2.setPrefWidth(width*.35);
				label3.setPrefWidth(width*.35);
				label4.setPrefWidth(width*.35);
				label5.setPrefWidth(width*.35);
				label6.setPrefWidth(width*.35);
				label7.setPrefWidth(width*.45);

				label1.setLayoutX(width*.2);
				label2.setLayoutX(width*.2);
				label3.setLayoutX(width*.2);
				label4.setLayoutX(width*.2);
				label5.setLayoutX(width*.2);
				label6.setLayoutX(width*.2);

			   setCategory.setPrefWidth(width*.5);
				setUsername.setPrefWidth(width*.5);
				setRole.setPrefWidth(width*.5);
				setUsername.setLayoutX(width*.4);
				setCategory.setLayoutX(width*.4);
				setRole.setLayoutX(width*.4);
				changePasswordButton.setLayoutX(width*.3);
				changePasswordButton.setPrefWidth(width*.4);
				oldPasswordLabel.setLayoutX(width*.4);
				oldPasswordLabel.setPrefWidth(width*.45);
				newPasswordLabel.setLayoutX(width*.4);
				newPasswordLabel.setPrefWidth(width*.45);
				confirmPasswordLabel.setLayoutX(width*.4);
				confirmPasswordLabel.setPrefWidth(width*.45);

			}
		});
	}

	public void initLoad()
	{		try
	{ ArrayList<usersModel> list =handler.getInstance().getHomeDetials();
	Platform.runLater(()->{
		setUsername.setText(list.get(0).getUserName().getValue());
		setCategory.setText(list.get(0).getCategoryAssigned().getValue());
		setRole.setText(list.get(0).getRole().getValue());
	});
	}
	catch (Exception e) {
alertMaker.showErrorMessage("error loading user detials", "");
	}
}


	
	public void handleChangePasswordButtonAction(javafx.scene.input.MouseEvent event) throws Exception
	{
		 String oldPassword = oldPasswordLabel.getText();
		 String newPassword = newPasswordLabel.getText();
		 String confirmPassword = confirmPasswordLabel.getText();
		 System.out.println(oldPassword+"  "+ newPassword +" "+confirmPassword );
		 
	        if (oldPassword==null||newPassword==null||confirmPassword==null||oldPassword.isEmpty()||newPassword.isEmpty()||confirmPassword.isEmpty()) {
	        	alertMaker.showErrorMessage("Error","Empty fields \n Please fill all the fields");
	            return ;
	        }
	        if (newPassword.length() < 6) {
	       
	            alertMaker.showErrorMessage("Error", "new password should be greater than 6 characters");
	            return ;

	        }
	        if (!oldPassword.equals(loginController.loggedUserPassword)) {
	        	alertMaker.showErrorMessage("Error", "Incorrect currect password entered");
	            return ;

	        }
	        if (!newPassword.equals(confirmPassword))
	        {
	        	alertMaker.showErrorMessage("Error", "New password and confirm password doesnt match");
	            return ;
	        }
	        if (newPassword.equals(confirmPassword)&&oldPassword.equals(loginController.loggedUserPassword)) {
	             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	             alert.setTitle("Update Password ");
			    alert.setHeaderText("Your password will be updated  ");
			   alert.setContentText(" Are you sure you want to proceed?");
			   alert.showAndWait().ifPresent(rs -> { 
				   if(rs == ButtonType.OK) {
			 	   		 System.out.println(oldPassword+"  "+ newPassword +" "+confirmPassword );
	        	     try {
						handler.getInstance().updatePassword(newPassword);
						loginController.loggedUserPassword=newPassword;
					alertMaker.showInfoMessage("Succesful updation","Your password was updated succesfully", "");
					} 
	        	     catch (Exception e) {
						alertMaker.showErrorMessage("Failure", "Cant update your password");
						e.printStackTrace();
					}
	        	   
	        	     oldPasswordLabel.clear();
	        	     confirmPasswordLabel.clear();
	        	     newPasswordLabel.clear();
	        	     
				   }else
				   {
	        	     return ;
				   }
	});
	}
		
}	
	
}	
	

