package application.controllers;
import application.alert.alertMaker;
import application.database.*;
import application.model.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;

public class usersController implements Initializable {
	
    @FXML
    private ContextMenu contextMenuUsersTable;

    @FXML
    private MenuItem contextMenuItemDeleteUsersTable;

    @FXML
    private MenuItem contextMenuItemEditUsersTable;
	   @FXML
	    private TableView<usersModel> tableViewUsers;

	    @FXML
	    private TableColumn<usersModel,String> username;

	    @FXML
	    private TableColumn<usersModel, String> role;

	    @FXML
	    private TableColumn<usersModel, String> categoryAssigned;

	    @FXML
	    private Button addUserButton;

	    @FXML
	    private Button editUserButton;
	    @FXML
	    private ProgressBar progressBar;
	
	    @FXML
	    private Button deleteUserButton;

	@FXML
	private AnchorPane anchorPaneUsers;

	@FXML
	private StackPane stackPaneUsersTable;





	@FXML
	private Label usersManagerLabel;

	@FXML
	private VBox vboxTools;


	    private static databaseHelper handler=null;
	    private ObservableList<application.model.usersModel> usersData = FXCollections.observableArrayList();
	    public ObservableList<application.model.usersModel> getPersonData() {
	        return usersData;
	    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    	//resizeFunctionality();
	              usersData.clear();
	          username.setCellValueFactory(new PropertyValueFactory<usersModel, String>("username"));
	          role.setCellValueFactory(new PropertyValueFactory<usersModel, String>("role"));
	          categoryAssigned.setCellValueFactory(new PropertyValueFactory<usersModel, String>("categoryAssigned"));
	          initUsersList();

		
	}

	private void resizeFunctionality() {
		double width =loginController.adminStage.getWidth();
		anchorPaneUsers.setPrefWidth(width);
		anchorPaneUsers.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double width=(double) newValue;
				stackPaneUsersTable.setPrefWidth(width*.96);
				tableViewUsers.setPrefWidth(width*.94);
				addUserButton.setPrefWidth(width*.15);
				deleteUserButton.setPrefWidth(width*.15);
				vboxTools.setLayoutX(width*.80);
				vboxTools.setPrefWidth(width*.16);
				usersManagerLabel.setPrefWidth(width*.4);
				usersManagerLabel.setLayoutX(width*.2);
				progressBar.setPrefWidth(width*.75);
				progressBar.setPrefWidth(width*.05);
				username.setPrefWidth(width*.32);
				role.setPrefWidth(width*.34);
				categoryAssigned.setPrefWidth(width*.31);

			}
		});
	}

	private void initUsersList() {
		usersData.clear();
		loadUsersData loadTask = new loadUsersData();
		  progressBar.setVisible(true);
        progressBar.progressProperty().bind(loadTask.progressProperty());
		   ExecutorService executorService= Executors.newFixedThreadPool(1);
           executorService.execute(loadTask);
  	     	executorService.shutdown();//task gets executed
  	     	loadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
  		    @Override
  		    public void handle(WorkerStateEvent t) {
  		    	progressBar.progressProperty().unbind();
  		    progressBar.setVisible(false);
  		       Platform.runLater(()-> tableViewUsers.setItems(getPersonData()));
  		      System.out.println("printing array list of users "+usersData.toString());
  	
  		    }
  		});
	    
	
		
	}
	public void editCategoryAssignedAction()throws Exception
	{
		usersModel rowToEdit=tableViewUsers.getSelectionModel().getSelectedItem();
		
		
		
	}
	public void clearSelection()
	{
	   tableViewUsers.getSelectionModel().clearSelection();
	}

	public class loadUsersData extends Task<Void>
	{
     
		@Override
		protected Void call() throws Exception {
			usersData.addAll(handler.getInstance().loadUsersData());
			return null;
		}
		
		
	}
	public void deleteRowFromUsersTable()
	{ Boolean ans=false;
	ans=alertMaker.showConformationMessage("Confirm Deletion", "Are you sure you want to delete this user", "");
		
	  if(ans==false)
	  {
		  clearSelection();
		  return;
	  }
	 usersModel rowToDelete=tableViewUsers.getSelectionModel().getSelectedItem();
	    if(rowToDelete==null)
		       System.out.println("select entry");// Alert_handler.showErrorMessage("No Booking Selected"," Please  select a entry from table","");
		    if (rowToDelete != null) {
		    	
		    	try
		    	{
		    		int res=handler.getInstance().deleteUser(rowToDelete);
		    		if(res==1)
		    			{
		    			 Platform.runLater(()->{ tableViewUsers.getItems().removeAll(rowToDelete);}); 
		    			System.out.println("deletion succesful");
		    			}
		    		if(res==0)
		    			System.out.println("failed deletion");
		    			
		    	}
		    	catch (Exception e) {
				e.printStackTrace();
				}
		    }
	}
	public void handleAddUserButtonAction() throws Exception
	{
		
        Stage stage = new Stage();
       
       Parent root=FXMLLoader.load(getClass().getResource("/application/Fxmls/addUser.fxml" ));
		 	stage.setTitle("Question bank Internship Test System");
		    stage.setScene(new Scene(root));
		    stage.setResizable(false);
		    stage.show();
		
		
	}
	
	
}

