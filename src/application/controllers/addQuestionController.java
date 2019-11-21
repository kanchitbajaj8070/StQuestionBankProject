package application.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import application.alert.alertMaker;
import application.controllers.usersController.loadUsersData;
import application.database.*;
	import application.model.*;
	import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.ResourceBundle;
	import java.util.concurrent.ExecutorService;
	import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javafx.scene.input.*;
	import javafx.scene.layout.BorderPane;
	import javafx.scene.layout.VBox;
	import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.concurrent.Task;
	import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.Cursor;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
	import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
	import javafx.scene.control.TableView;
	import javafx.scene.control.TextArea;
	import javafx.scene.control.TextField;
	import javafx.scene.control.cell.PropertyValueFactory;
	import javafx.fxml.Initializable;  
public class addQuestionController implements Initializable {
	private static databaseHelper handler=null;
	    @FXML
	    private Label errorLabelsubcategory;


	    @FXML
	    private Label errorLabelType;

	    @FXML
	    private Label errorLabelDifficulty;

	

	    @FXML
	    private Label errorLabelChoice1;

	    @FXML
	    private Label errorLabelChoice2;

	    @FXML
	    private Label errorLabelChoice3;

	    @FXML
	    private Label errorLabelChoice4;
	    @FXML
	    private Label errorLabelCorrectAnswer;

	    @FXML
	    private Label errorLabelMarks;
	    @FXML
	    private Label errorLabelTimeAssigned;


    @FXML
    private JFXTextArea addQuestionTextArea;

    @FXML
    private JFXComboBox<String> addQuestionCategory;

    @FXML
    private JFXComboBox<String> addQuestionSubcategory;

    @FXML
    private JFXButton addQuestionImage;
    

    @FXML
    private JFXComboBox<String> addQuestionType;

    @FXML
    private JFXComboBox<String> addQuestionDifficulty;

    @FXML
    private JFXTextField addQuestionFirstChoice;

    @FXML
    private JFXTextField addQuestionSecondChoice;

    @FXML
    private JFXTextField addQuestionThirdChoice;

    @FXML
    private JFXTextField addQuestionFourthChoice;

    @FXML
    private JFXComboBox<String> addQuestionCorrectChoice;

    @FXML
    private JFXTextField addQuestionMarks;

    @FXML
    private JFXComboBox<String> addQuestionTime;
    @FXML
    private Label errorLabelCategory;
    @FXML
    private JFXButton addButton;
    public static ObservableList<String> categoriesList=FXCollections.observableArrayList();
    public static ObservableList<String> subcategoriesList=FXCollections.observableArrayList();
    public static ObservableList<String> typeList=FXCollections.observableArrayList();
    public static ObservableList<String> difficultyLevelsList=FXCollections.observableArrayList();
    public static ObservableList<String> correctAnswerList=FXCollections.observableArrayList();
    public static ObservableList<String> timeAssignedList=FXCollections.observableArrayList();
   
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	      try {
			initCategories();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      try {
			initTypeList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      try {
			initDifficultyLevels();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      initCorrectAnswer();
	      initTimeAssigned();
	      addQuestionMarks.textProperty().addListener((obs, oldText, newText) -> {
      	    System.out.println("MARKS changed from "+oldText+" to "+newText);
      	    if(addQuestionMarks.getText()!=null)
      	    	errorLabelMarks.setVisible(false);
      	    else
      	  	errorLabelMarks.setVisible(true);
      	    // ...
      	});
       
	      
		
	} 
	private String imagePath=null;
	public void addImageAction()

	{
		 FileChooser fileChooser= new FileChooser();
		    File fileSelected= fileChooser.showOpenDialog(addQuestionCorrectChoice.getScene().getWindow());
		    if(fileSelected==null)
		    {
		        alertMaker.showErrorMessage("Error","Please select a path of image ");
		        return;

		    }
		    String path= fileSelected.getPath();
		    
		    if(path==null)
		    {
		        alertMaker.showErrorMessage("Error","Please select a path of image ");
		return;

		    }
		    imagePath=path;
		
		   
	}
	private void initTimeAssigned() {
		timeAssignedList.clear();
		timeAssignedList.add("1 minute");
		timeAssignedList.add("2 minute");
		timeAssignedList.add("3 minute");
		timeAssignedList.add("5 minute");
		timeAssignedList.add("10 minute");
		timeAssignedList.add("30 minute");
		addQuestionTime.setItems(timeAssignedList);
		// TODO Give 30 as a disabled input and alert with IT
		
	}
	public void handleKeyPressAction(KeyEvent event) throws Exception
	{
		if(event.getCode()==KeyCode.ENTER)
			handleAddButtonAction();
	}
	private void initCorrectAnswer() {
		correctAnswerList.clear();
	
			correctAnswerList.add("1");
			correctAnswerList.add("2");
			correctAnswerList.add("3");
			correctAnswerList.add("4");
		addQuestionCorrectChoice.setItems(correctAnswerList);
		
	}
	private void initSubcategoriesList(int id) throws Exception {
		subcategoriesList.clear();
	subcategoriesList.addAll( handler.getInstance().getSubcategoriesByCategoryId(id));
		addQuestionSubcategory.setItems(subcategoriesList);
		
	}
	private void initTypeList() throws Exception{
		typeList.clear();
		typeList.addAll(handler.getInstance().getAllTypes());
		addQuestionType.setItems(typeList);
		
	}
	private void initDifficultyLevels() throws Exception{
		difficultyLevelsList.clear();
			difficultyLevelsList.addAll( handler.getInstance().getAllDifficultyLevels());
		addQuestionDifficulty.setItems(difficultyLevelsList);
		
	}
	private void initCategories() throws Exception {
		categoriesList.clear();
			categoriesList.addAll(handler.getInstance().getAllCategories());
		addQuestionCategory.setItems(categoriesList);
		
	}
	public void handleSubcategorySelectionAction()
	{   	if(addQuestionSubcategory.getValue()==null)
	{  
		System.out.println("select Subcategory");
		Platform.runLater(()->{ errorLabelsubcategory.setVisible(true);});
		
	}else
	{Platform.runLater(()->{ errorLabelsubcategory.setVisible(false);});}
	
		if(addQuestionCategory.getValue()==null)
		{  
			System.out.println("select category");
			Platform.runLater(()->{ errorLabelCategory.setVisible(true);});
			
		}else
		{Platform.runLater(()->{ errorLabelCategory.setVisible(false);});}
		
		if(addQuestionSubcategory.getValue()==null)
		{  
			System.out.println("select Subcategory");
			Platform.runLater(()->{ errorLabelsubcategory.setVisible(true);});
			
		}else
		{Platform.runLater(()->{ errorLabelsubcategory.setVisible(false);});}
	}
	public void handleTypeSelectedAction() throws Exception{
		
		Platform.runLater(()->{errorLabelType.setVisible(false); });
		
		if(addQuestionType.getValue()!=null&&addQuestionType.getValue().toString().equalsIgnoreCase("subjective"))
		{Platform.runLater(()->{
			errorLabelChoice1.setVisible(false);
			errorLabelChoice2.setVisible(false);
			errorLabelChoice3.setVisible(false);
			errorLabelChoice4.setVisible(false);
		});
		}
		if(addQuestionType.getValue()!=null&&addQuestionType.getValue().toString().equalsIgnoreCase("true/false"))
		{Platform.runLater(()->{
			errorLabelChoice1.setVisible(false);
			errorLabelChoice2.setVisible(false);
			errorLabelChoice3.setVisible(false);
			errorLabelChoice4.setVisible(false);
		});
		}
		if(addQuestionType.getValue()!=null) {
		if(addQuestionType.getValue().toString().equalsIgnoreCase(("subjective")))
		{
			Platform.runLater(()->{
				correctAnswerList.clear();
				addQuestionCorrectChoice.setPromptText("No choice available");
				addQuestionCorrectChoice.setEditable(false);
				addQuestionFirstChoice.setEditable(true);
				addQuestionFourthChoice.setEditable(true);
				addQuestionSecondChoice.setEditable(true);
				addQuestionThirdChoice.setEditable(true);
				addQuestionFirstChoice.clear();
				addQuestionSecondChoice.clear();
				addQuestionThirdChoice.clear();
				addQuestionFourthChoice.clear();
			addQuestionFirstChoice.setPromptText("no choices in subjective questions allowed");
			addQuestionSecondChoice.setPromptText("no choices in subjective questions allowed");
			addQuestionThirdChoice.setPromptText("no choices in subjective questions allowed");
			addQuestionFourthChoice.setPromptText("no choices in subjective questions allowed");
			addQuestionFirstChoice.setEditable(false);
			addQuestionFourthChoice.setEditable(false);
			addQuestionSecondChoice.setEditable(false);
			addQuestionThirdChoice.setEditable(false);
			
		});
			return;}
		
		else if(addQuestionType.getValue().toString().equalsIgnoreCase(("true/false")))
		{
		Platform.runLater(()->{	addQuestionCorrectChoice.setEditable(true);
			addQuestionCorrectChoice.setPromptText("select choice");
			addQuestionCorrectChoice.setEditable(false);
			correctAnswerList.clear();
			correctAnswerList.add("true");
		   correctAnswerList.add("false");
			addQuestionCorrectChoice.setItems(correctAnswerList);
			addQuestionFirstChoice.setEditable(true);
			addQuestionFourthChoice.setEditable(true);
			addQuestionSecondChoice.setEditable(true);
			addQuestionThirdChoice.setEditable(true);
			addQuestionFirstChoice.setPromptText("");
			addQuestionSecondChoice.setPromptText("");
			addQuestionThirdChoice.setPromptText("");
			addQuestionFourthChoice.setPromptText("");
			addQuestionFirstChoice.clear();
			addQuestionSecondChoice.clear();
			addQuestionThirdChoice.clear();
			addQuestionFourthChoice.clear();
			addQuestionFirstChoice.setText("true");
			addQuestionSecondChoice.setText("false");
			addQuestionFourthChoice.setEditable(false);
			addQuestionThirdChoice.setEditable(false);
			addQuestionFirstChoice.setEditable(false);
			addQuestionSecondChoice.setEditable(false);
		});
		
		}
		else
		{
		correctAnswerList.clear();
		initCorrectAnswer();
		addQuestionFirstChoice.clear();
		addQuestionSecondChoice.clear();
		addQuestionThirdChoice.clear();
		addQuestionFourthChoice.clear();
		addQuestionFirstChoice.setPromptText("");
		addQuestionSecondChoice.setPromptText("");
		addQuestionThirdChoice.setPromptText("");
		addQuestionFourthChoice.setPromptText("");
			addQuestionFirstChoice.setEditable(true);
		addQuestionFourthChoice.setEditable(true);
		addQuestionSecondChoice.setEditable(true);
		addQuestionThirdChoice.setEditable(true);
		}
		}
		}
	public void handleAddButtonAction()throws Exception {
		disableAllErrors();
       int m=0;
		if(addQuestionMarks.getText()!=null&&!addQuestionMarks.getText().isEmpty())
		m= Integer.valueOf(addQuestionMarks.getText());
		   if(m>99)
		   {  System.out.println("incorrect Marks");
	    	  alertMaker.showErrorMessage("Marks should be less than 100 ", "Please check and try again");
	    	  return;
			   
		   }
		      Boolean checkField=checkEntries();
		      if(checkField==false)
		      {
		    	  System.out.println("incorrect input");
		    	  alertMaker.showErrorMessage("Incorrect inputs in some fields ", "Please check again");
		    	  return;
		      }
		      disableAllErrors();
		//variable names corrsp. to datatbase columns
		String question_text=addQuestionTextArea.getText().toLowerCase();
		String category_name=addQuestionCategory.getValue().toLowerCase();
		String difficulty=addQuestionDifficulty.getValue().toLowerCase();
		String subcategory_name=addQuestionSubcategory.getValue().toLowerCase();
		String type=addQuestionType.getValue().toLowerCase();
	    String marks=addQuestionMarks.getText().toString();
	    String added_by=loginController.loggedUser.toLowerCase();
	    String firstchoice=addQuestionFirstChoice.getText().toLowerCase();
	    String secondchoice=addQuestionSecondChoice.getText().toLowerCase();
	    String thirdchoice=addQuestionThirdChoice.getText().toLowerCase();
	    String fourthchoice=addQuestionFourthChoice.getText().toLowerCase();
	    String correct_answer=addQuestionCorrectChoice.getValue();
	    
	    if(!(correct_answer==null)&&correct_answer.equalsIgnoreCase("true"))
	    {     correct_answer="1";
	    }if(!(correct_answer==null)&&correct_answer.equalsIgnoreCase("false"))
	    	correct_answer="2";
	    	String time_assigned=addQuestionTime.getValue().toString();
	    	time_assigned=time_assigned.substring(0, time_assigned.indexOf(" "));
		System.out.println("timne is "+ time_assigned);
         
		try
		{
			handler.getInstance().insertQuestion(question_text,category_name,subcategory_name,imagePath,type,difficulty,marks,added_by, firstchoice,secondchoice,thirdchoice,fourthchoice,correct_answer,0,time_assigned);		
		
         System.out.println("inserted question"+"	closing add screenn!!!!!!!!!!!");
		alertMaker.showInfoMessage("Success","Inserted question into question bank", "Success");
		questionsController q= adminDashboardController.questionsloader.getController();
		if(q!=null)
		q.handleRefreshQuestionsAction();
		imagePath=null;
		Stage current=(Stage)addQuestionCorrectChoice.getScene().getWindow();
		 current.close();
		 
	}
		catch (Exception e) {
		alertMaker.showErrorMessage("Failed to insert Question","Failure");
		}
	}
	private void disableAllErrors() throws Exception{
		
		Platform.runLater(()->{errorLabelCategory.setVisible(false); 
		errorLabelsubcategory.setVisible(false); 
	errorLabelType.setVisible(false);
	errorLabelDifficulty.setVisible(false);
	errorLabelMarks.setVisible(false);
errorLabelTimeAssigned.setVisible(false);
errorLabelChoice1.setVisible(false);
errorLabelChoice2.setVisible(false);
errorLabelChoice3.setVisible(false);
errorLabelChoice4.setVisible(false);	
errorLabelCorrectAnswer.setVisible(false);
		});
	}
	private Boolean checkEntries() {
		Boolean ans=true;
		if(addQuestionTextArea.getText()==null)
		{ans= false;}
		if(addQuestionCategory.getValue()==null)
		{Platform.runLater(()->errorLabelCategory.setVisible(true)); ans= false;}
		if(addQuestionSubcategory.getValue()==null)
			{Platform.runLater(()->errorLabelsubcategory.setVisible(true)); ans= false;}
		if(addQuestionType.getValue()==null)
			{Platform.runLater(()->errorLabelType.setVisible(true));ans=false;}
		if(addQuestionDifficulty.getValue()==null)
			{Platform.runLater(()->errorLabelDifficulty.setVisible(true));ans= false;}
		if(addQuestionMarks.getText().toString()==null||addQuestionMarks.getText().isEmpty())
			{Platform.runLater(()->errorLabelMarks.setVisible(true));ans= false;}
		if(addQuestionTime.getValue()==null||addQuestionTime.getValue().toString().isEmpty())
			{Platform.runLater(()->errorLabelTimeAssigned.setVisible(true));ans= false;}
		if((addQuestionFirstChoice.getText().toString().isEmpty()||addQuestionFirstChoice.getText()==null)&&addQuestionType.getValue()!=null&&(addQuestionType.getValue().equalsIgnoreCase("objective")|| addQuestionType.getValue().equalsIgnoreCase("true/false")))
			{Platform.runLater(()->errorLabelChoice1.setVisible(true));ans= false;}
		if((addQuestionSecondChoice.getText().toString().isEmpty()||addQuestionSecondChoice.getText()==null)&&addQuestionType.getValue()!=null&&(addQuestionType.getValue().equalsIgnoreCase("objective")|| addQuestionType.getValue().equalsIgnoreCase("true/false")))
			{Platform.runLater(()->errorLabelChoice2.setVisible(true));ans= false;}
		if((addQuestionThirdChoice.getText().toString().isEmpty()||addQuestionThirdChoice.getText()==null)&&addQuestionType.getValue()!=null&&(addQuestionType.getValue().equalsIgnoreCase("objective")))
			{Platform.runLater(()->errorLabelChoice3.setVisible(true));ans= false;}
		if((addQuestionFourthChoice.getText().toString().isEmpty()||addQuestionFourthChoice.getText()==null)&&addQuestionType.getValue()!=null&&(addQuestionType.getValue().equalsIgnoreCase("objective")))
		{Platform.runLater(()->errorLabelChoice4.setVisible(true));ans= false;}
		if(addQuestionCorrectChoice.getValue()==null&&(addQuestionType.getValue()!=null&&!(addQuestionType.getValue().equalsIgnoreCase("subjective"))))
			{Platform.runLater(()->errorLabelCorrectAnswer.setVisible(true));ans= false;}
		if(addQuestionType.getValue()!=null&&addQuestionType.getValue().toString().equalsIgnoreCase("subjective"))
		{Platform.runLater(()->{
			errorLabelChoice1.setVisible(false);
			errorLabelChoice2.setVisible(false);
			errorLabelChoice3.setVisible(false);
			errorLabelChoice4.setVisible(false);
		});
		}
		if(addQuestionType.getValue()!=null&&addQuestionType.getValue().toString().equalsIgnoreCase("true/false"))
		{Platform.runLater(()->{
			errorLabelChoice1.setVisible(false);
			errorLabelChoice2.setVisible(false);
			errorLabelChoice3.setVisible(false);
			errorLabelChoice4.setVisible(false);
		});
		}
		return ans;
		
	}
	public void handleCategorySelectionAction()
	{     
		if(addQuestionCategory.getValue()==null)
		{  
			System.out.println("select category");
			Platform.runLater(()->{ errorLabelCategory.setVisible(true);});
			
		}else
		{
			try {
				Platform.runLater(()->{ errorLabelCategory.setVisible(false);});
				initSubcategoriesList(handler.getInstance().getCategoryId(addQuestionCategory.getValue().toString().toLowerCase()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	  public void marksCheck(KeyEvent event)
	  {    if(addQuestionMarks.getText().isEmpty())
		          return;
	  String input=event.getCharacter();
		  if(addQuestionMarks.getText()==null)
		     {  Platform.runLater(()->{	 errorLabelMarks.setVisible(true);
			    });
		     return;
		}  
		   Pattern pattern = Pattern.compile(".*[^0-9].*");
	      if(pattern.matcher(input).matches()==true)
	      {    errorLabelMarks.setVisible(true);
		  errorLabelMarks.setText(" *Only integer allowed")    ; 
	    	  event.consume();       
			 }
            
	         if(pattern.matcher(input).matches()==false) {	 Platform.runLater(()->{	 errorLabelMarks.setVisible(false);
		 	    });} 

	  }
		
	  public void handleCorrectAnswerSelectedAction()
	  {    if(addQuestionCorrectChoice.getValue()==null)
		  Platform.runLater(()->{errorLabelCorrectAnswer.setVisible(false); });
	  }
	  public void handleTimeSelectedAction()
	  {    if(addQuestionCorrectChoice.getValue()==null)
		  Platform.runLater(()->{errorLabelTimeAssigned.setVisible(false); });
	  }
	  public void handleDifficultySelectedAction()
	  {    if(addQuestionCorrectChoice.getValue()==null)
		  Platform.runLater(()->{errorLabelDifficulty.setVisible(false); });
	  }
		 
	  }
	  
