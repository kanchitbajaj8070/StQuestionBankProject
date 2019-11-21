package application.controllers;

import application.alert.alertMaker;
import application.database.*;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.Initializable;

public class adminDashboardController implements Initializable {
	public static FXMLLoader homeLoader= null;
	public static FXMLLoader questionsloader= null;
	private databaseHelper handler=null;
	  public static FXMLLoader categoryLoader=null;
	  public static FXMLLoader papersLoader=null;
	  public static FXMLLoader usersLoader=null;
    @FXML
    private BorderPane borderPane;

	@FXML
	private ScrollPane scrollPaneMain;
	@FXML
    private Label usernameLabel;

    @FXML
    private ContextMenu usernameContext;
    @FXML
    private Label welcomeLabel;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button categoryButton;

    @FXML
    private Button questiionsButton;

    @FXML
    private Button usersButton;


	@FXML
	private VBox vboxTop;

	@FXML
	private HBox hboxTop;
	@FXML
	private Label titleLabel;

	@FXML
	private HBox hboxBottom;



	@FXML
	private Button paperMakerButton;

	@FXML
	private AnchorPane anchorPaneMain;
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	           //setReziseOperations();
		 welcomeLabel.setVisible(true);
		 Platform.runLater(() -> {
			 usernameLabel.setText(loginController.loggedUser);
		    });
		try {
			handleQuestionsButtonAction();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	private void setReziseOperations() {
	
		borderPane.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				double width=(double)newValue;
				anchorPaneMain.setPrefWidth(width);
				System.out.println(width);
				vboxTop.setPrefWidth(width);
				hboxTop.setPrefWidth(width);
				titleLabel.setLayoutX(width*.2);
				titleLabel.setPrefWidth(width*.6);
				welcomeLabel.setLayoutX(width*.8);
				welcomeLabel.setPrefWidth(width*.03);
				usernameLabel.setLayoutX(width*.83);
				usernameLabel.setPrefWidth(width*.07);
				logoutButton.setPrefWidth(width*.1);
				logoutButton.setLayoutX(width*.95);
				homeButton.setPrefWidth(width*.2);
				categoryButton.setPrefWidth(width*.2);
				questiionsButton.setPrefWidth(width*.2);
				paperMakerButton.setPrefWidth(width*.2);
				usersButton.setPrefWidth(width*.2);
			}
		});
	}

	public void handleLogoutButtonAction() throws Exception
	{   boolean ans=alertMaker.showConformationMessage("Confirmation", "Are you sure you want to sign out ","");
        if(ans==true)
        {
		handler.getInstance().getConnection().close();
		Stage current = (Stage) usersButton.getScene().getWindow();
        current.close();  
        }
		 
	}
	
	public void handleHomeButtonAction(javafx.scene.input.MouseEvent event) throws Exception
	{
		anchorPaneMain.setPrefWidth(borderPane.getWidth());
homeLoader=new FXMLLoader(getClass().getResource("/application/Fxmls/qbitHome.fxml"));
	anchorPaneMain=homeLoader.load();
		borderPane.setCenter(anchorPaneMain);
	}
	public void handleCategoriesButtonAction(javafx.scene.input.MouseEvent event) throws Exception
	{	anchorPaneMain.setPrefWidth(borderPane.getWidth());
         categoryLoader =new FXMLLoader(getClass().getResource("/application/Fxmls/qbitCategory.fxml"));
		anchorPaneMain=categoryLoader.load();
		borderPane.setCenter(anchorPaneMain);
	
		
	}	public void handleUsersButtonAction(javafx.scene.input.MouseEvent event) throws Exception
	{	anchorPaneMain.setPrefWidth(borderPane.getWidth());
	   usersLoader= new FXMLLoader(getClass().getResource("/application/Fxmls/qbitUsers.fxml"));
		anchorPaneMain=usersLoader.load();
	   borderPane.setCenter(anchorPaneMain);
	}	public void handleQuestionsButtonAction() throws Exception
	{	anchorPaneMain.setPrefWidth(borderPane.getWidth());
          questionsloader=new FXMLLoader((getClass().getResource("/application/Fxmls/qbitQuestions.fxml")));
	anchorPaneMain=questionsloader.load();
		borderPane.setCenter(anchorPaneMain);
	}
	public void handlePaperMakerButtonAction() 
	{
		try {
			anchorPaneMain.setPrefWidth(borderPane.getWidth());
		papersLoader=new FXMLLoader(getClass().getResource("/application/Fxmls/qbitPapers.fxml"));
		anchorPaneMain=papersLoader.load();
		borderPane.setCenter(anchorPaneMain);
	}catch (Exception e) {
	alertMaker.showErrorMessage("Not able to load window", "");
	}

	
	}

}
