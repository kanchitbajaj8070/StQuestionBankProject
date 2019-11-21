package application.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;

import application.alert.alertMaker;
import application.database.databaseHelper;
import application.model.questionsAllDetials;
import application.model.questionsModel;
import com.sun.org.glassfish.external.statistics.annotations.Reset;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class questionsController implements Initializable {
	private static databaseHelper handler = null;

    @FXML
    private Label errorLabelDeletedQuestion;
    
	private static int qidRowToView = 0;
	
	@FXML
	private Label errorLabelCategory;
	@FXML
	private TextArea questionTextArea;
    @FXML
    private JFXButton viewQuestionButton;

	@FXML
	private TextField questionCategory;

    @FXML
    private MenuItem questionTableMenuItemUndelete;

	@FXML
	private TextField questionSubcategory;

	@FXML
	private Button questionImage;

	@FXML
	private TextField questionType;

	@FXML
	private TextField questionDifficulty;

	@FXML
	private TextField questionMarks;

	@FXML
	private TextField questionAddedby;

	@FXML
	private TextField questionChoice1;

	@FXML
	private TextField questionChoice2;

	@FXML
	private TextField questionChoice3;

	@FXML
	private TextField questionChoice4;

	@FXML
	private TextField questionCorrectAnswer;
	@FXML
	private HBox hboxSearch;

	@FXML
	private HBox hboxTools;

	@FXML
	private VBox vbox1Tools;

	@FXML
	private VBox vbox2Tools;
	@FXML
	private TextField questionTimeAssigned;

	@FXML
	private JFXButton refreshQuestionsButton;

	@FXML
	private TableView<questionsModel> questionsTable;

	@FXML
	private TableColumn<questionsModel, String> questionText;

	@FXML
	private TableColumn<questionsModel, String> type;

	@FXML
	private TableColumn<questionsModel, String> subcategory;

    @FXML
    private MenuItem editQuestionMenuItem;
	@FXML
	private ContextMenu questionsTableContextMenu;

	@FXML
	private MenuItem questionTableMenuItemDelete;

	@FXML
	private MenuItem questionTableMenuItemView;

	@FXML
	private Button addNewQuestionButton;

	@FXML
	private ComboBox<String> selectTypeComboBox;
	@FXML
	private JFXButton editQuestionButton;

	@FXML
	private JFXButton saveQuestionButton;
	@FXML
	private ComboBox<String> selectCategoryComboBox;

	@FXML
	private ComboBox<String> selectSubcategoryComboBox;

	@FXML
	private JFXSpinner progressIndicator;

	@FXML
	private Label progressLabel;

	@FXML
	private ComboBox<String> selectUserComboBox;
	private String tempCategory = null;
	@FXML
	private Button searchButton;
	@FXML
	private Button Reset;
    @FXML
    private CheckBox showDeletedCheckBox;

	public FXMLLoader addQuestionLoader = null;
	public static ObservableList<String> categoriesList = FXCollections.observableArrayList();
	public static ObservableList<String> subcategoriesList = FXCollections.observableArrayList();
	public static ObservableList<String> typeList = FXCollections.observableArrayList();
	public static ObservableList<String> userList = FXCollections.observableArrayList();

	@FXML
	private ScrollPane detialsScrollPane;
	private ObservableList<questionsModel> questionsData = FXCollections.observableArrayList();

	public ObservableList<questionsModel> getQuestionsData() {
		return questionsData;
	}

	public void handleSortTable()// TODO make option for showing deleted item and options to reintriduce thgem
	{

	}

	public TableView getTable() {
		return questionsTable;
	}

	private class initializeTasks extends Task<Boolean> {
		Boolean result = false;

		@Override
		protected Boolean call() throws Exception {
			try {
				initquestionsList();
				initCategoriesList();
				initTypeList();
				initUsersList();
				result = true;
			} catch (Exception e) {
				alertMaker.showErrorMessage(e, "Error", "Error in loading Data");
				result = false;
			}

			return result;
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{ //resizeFuntionality();
		
		questionsData.clear();
		 questionText.setCellValueFactory(new PropertyValueFactory<questionsModel, String>("questionText"));
		type.setCellValueFactory(new PropertyValueFactory<questionsModel, String>("type"));
		subcategory.setCellValueFactory(new PropertyValueFactory<questionsModel, String>("subcategory"));
		questionsTable.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

			@Override
			public void handle(javafx.scene.input.MouseEvent mouseEvent) {

				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 2) {
						System.out.println("Double clicked");

						try {
							viewRowFromQuestionTable();
						} catch (Exception e1) {
							System.out.println(e1.getLocalizedMessage());
						}
					}
				}
			}
		});
		questionTableMenuItemDelete.setVisible(false);
	questionsTable.setRowFactory (tv -> 
	      {
	         TableRow <questionsModel> oRow = new TableRow<>();
	         oRow.setOnMouseClicked (event -> 
	         {
	            if (event.getClickCount() == 1 && (!oRow.isEmpty()))
	            {
	               int wNroIdxRow = questionsTable.getSelectionModel().getSelectedIndex();
	 
	               String selected = questionsTable.getItems().get(wNroIdxRow).getQuestionText().getValue();
	              System.out.println(selected);
	               if(selected!=null&&selected.length()>11&&selected.substring(selected.length()-10,selected.length()-1).equalsIgnoreCase("(deleted)"))
	               {
	            	   Platform.runLater(()->{
	            		   errorLabelDeletedQuestion.setVisible(true);
	            	     questionTableMenuItemView.setVisible(false);
 	            		   questionTableMenuItemDelete.setVisible(false);
	            	   editQuestionMenuItem.setVisible(false);
	            	   editQuestionButton.setDisable(true);
	            	   questionTableMenuItemUndelete.setVisible(true);
	            	    questionTableMenuItemView.setVisible(true);
	            	  });
	            	  
	            	 
	               }
	               else
	               {
	            	   Platform.runLater(()->{errorLabelDeletedQuestion.setVisible(false);
	            	   questionTableMenuItemUndelete.setVisible(false);
	            	   editQuestionMenuItem.setVisible(true);
	            	   editQuestionButton.setDisable(false);
	            	   questionTableMenuItemDelete.setVisible(true);
	            	  
	            	   });
	               }
	            }
	         });
	            return oRow;
	      });
		initializeTasks initTasks = new initializeTasks();
		// Platform.runLater(() -> {
		// progressIndicator.setVisible(true);
		// progressLabel.setVisible(true);
		// });
		ExecutorService e = Executors.newFixedThreadPool(4);
		e.submit(initTasks);
		e.shutdown();
		// progressIndicator.visibleProperty().bind(initTasks.runningProperty());
		// progressLabel.visibleProperty().bind(initTasks.runningProperty());
		initTasks.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
			}

		});
		initTasks.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {

				alertMaker.showErrorMessage("Error", "Not able to Load data in table \n Trying to refresh");

			}

		});

	}
	@FXML
	private StackPane stackPaneQuestionsTable;
	@FXML
	private AnchorPane anchorPaneQuestions;

	private void resizeFuntionality() {
		double width =anchorPaneQuestions.getWidth();
		width=width*.9;
		anchorPaneQuestions.setPrefWidth(width);
		anchorPaneQuestions.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double width=(double) newValue;
				System.out.println(width+" 	 in questions");
				stackPaneQuestionsTable.setPrefWidth(width);
				hboxSearch.setPrefWidth(width*.6);
				editQuestionButton.setLayoutX(width*.6);
				editQuestionButton.setPrefWidth(width*.1);
				viewQuestionButton.setLayoutX(width*.7);
				viewQuestionButton.setPrefWidth(width*.1);
				addNewQuestionButton.setLayoutX(width*.8);
				addNewQuestionButton.setPrefWidth(width*.1);
				refreshQuestionsButton.setLayoutX(width*.9);
				refreshQuestionsButton.setPrefWidth(width*.1);
				searchButton.setPrefWidth(width*.1);
				selectCategoryComboBox.setPrefWidth(width *.1);
				selectSubcategoryComboBox.setPrefWidth(width *.1);
			   selectTypeComboBox.setPrefWidth(width *.1);
				selectUserComboBox.setPrefWidth(width *.1);
				Reset.setPrefWidth(width*.1);
				errorLabelCategory.setPrefWidth(width*.1);
				questionsTable.setPrefWidth(width);
				questionText.setPrefWidth(width*.4);
				subcategory.setPrefWidth(width*.3);
				type.setPrefWidth(width*.3);
				showDeletedCheckBox.setLayoutX(width*.8);
				showDeletedCheckBox.setPrefWidth(width*.2);
				errorLabelDeletedQuestion.setPrefWidth(width*.3);
				progressIndicator.setLayoutX(width*.72);

			}
		});
	}

	public void handleRefreshQuestionsAction() throws Exception {
		questionsData.clear();
		initquestionsList();

	}

	public void handleSubcategorySelectionAction(javafx.scene.input.MouseEvent event) {
		if (selectCategoryComboBox.getValue() == null) {
			System.out.println("select category");
			Platform.runLater(() -> {
				errorLabelCategory.setVisible(true);
			});

		}

	}

	public void handleCategorySelectionAction() {
		if (selectCategoryComboBox.getValue() != null) {
			try {
				Platform.runLater(() -> {
					errorLabelCategory.setVisible(false);
				});
				initSubcategoriesList(handler.getInstance()
						.getCategoryId(selectCategoryComboBox.getValue().toString().toLowerCase()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
public void handleResetButtonAction()
{
	clearAll();
	errorLabelCategory.setVisible(false);
}
	public void handleSearchButtonAction() {

		if ((selectCategoryComboBox.getValue() == null) && (selectSubcategoryComboBox.getValue() == null)
				&& (selectTypeComboBox.getValue() == null) && (selectUserComboBox.getValue() == null)) {

			alertMaker.showErrorMessage("Select filters", "Please select some filters to search questions");

			return;

		}

		questionsData.clear();
		String type = selectTypeComboBox.getValue();
		if (type != null)
			type = type.toLowerCase();
		String category = selectCategoryComboBox.getValue();
		if (category != null)
			category = category.toLowerCase();
		String subcategory = selectSubcategoryComboBox.getValue();
		if (subcategory != null)
			subcategory = subcategory.toLowerCase();

		String user = selectUserComboBox.getValue();
		if (user != null)
			user = user.toLowerCase();
		try
		{questionsData.addAll(handler.getInstance().getSearchResults(type, category, subcategory, user));
		questionsTable.setItems(questionsData);
		clearAll();
		errorLabelCategory.setVisible(false);
		}
		catch (Exception e) {
		alertMaker.showErrorMessage("Error loading search data", "");
		}


	}

	private void clearAll() {
		selectCategoryComboBox.setValue(null);
		selectSubcategoryComboBox.setValue(null);
		selectTypeComboBox.setValue(null);
		selectUserComboBox.setValue(null);

	}

	private void initSubcategoriesList(int id) throws Exception {
		subcategoriesList.clear();
		subcategoriesList.addAll(handler.getInstance().getSubcategoriesByCategoryId(id));
	Platform.runLater(()->	selectSubcategoryComboBox.setItems(subcategoriesList));

	}

	private void initTypeList() throws Exception {
		typeList.clear();
		typeList.addAll(handler.getInstance().getAllTypes());
		Platform.runLater(()->selectTypeComboBox.setItems(typeList));

	}

	private void initUsersList() throws Exception {
		userList.clear();
			userList.addAll( handler.getInstance().getAllUsers());
			Platform.runLater(()->selectUserComboBox.setItems(userList));
	}

	/*
	 * public void viewRowFromQuestionTable() { questionsModel rowToView =
	 * questionsTable.getSelectionModel().getSelectedItem(); int id =
	 * rowToView.getId(); qidRowToView = id; try {
	 * application.model.questionsAllDetials obj =
	 * handler.getInstance().getAllDetialsOfQuestionByQuestionId(id); if (obj ==
	 * null) System.out.println("0null value of obj"); print(obj);
	 * questionTextArea.setText(obj.getQuestionText());
	 * 
	 * questionCategory.setText(handler.getInstance().getQuestionCategoryName(obj.
	 * getCategoryid()));
	 * 
	 * questionSubcategory.setText(handler.getInstance().getQuestionSubcategoryName(
	 * obj.getSubcategoryid()));
	 * 
	 * questionType.setText(handler.getInstance().getQuestionTypeName(obj.getType_id
	 * ()));
	 * 
	 * questionDifficulty.setText(handler.getInstance().getDifficultyLevel(Integer.
	 * valueOf(obj.getDifficulty())));
	 * questionMarks.setText(String.valueOf(obj.getMarks()));
	 * questionAddedby.setText(obj.getAddedBy()); if (obj.getFirstChoice() != null)
	 * questionChoice1.setText(obj.getFirstChoice()); else
	 * questionChoice1.setText("Choice Not available"); if (obj.getSecondChoice() !=
	 * null) questionChoice2.setText(obj.getSecondChoice()); else
	 * questionChoice2.setText("Choice Not available"); if (obj.getThirdChoice() !=
	 * null) questionChoice3.setText(obj.getThirdChoice()); else
	 * questionChoice3.setText("Choice Not available"); if (obj.getFourthChoice() !=
	 * null) questionChoice4.setText(obj.getFourthChoice()); else
	 * questionChoice4.setText("Choice Not available"); if (obj.getType_id() == 1) {
	 * if (obj.getCorrectAnswer() == 1)
	 * questionCorrectAnswer.setText("First option"); if (obj.getCorrectAnswer() ==
	 * 2) questionCorrectAnswer.setText("Second option"); if (obj.getCorrectAnswer()
	 * == 3) questionCorrectAnswer.setText("Third Option"); if
	 * (obj.getCorrectAnswer() == 4) questionCorrectAnswer.setText("Fourth option");
	 * 
	 * } if (obj.getType_id() == 2)
	 * questionCorrectAnswer.setText("No answer for subjective questions "); if
	 * (obj.getType_id() == 3) { if (obj.getCorrectAnswer() == 1)
	 * questionCorrectAnswer.setText("true"); else
	 * questionCorrectAnswer.setText("false");
	 * 
	 * } questionTimeAssigned.setText(String.valueOf(obj.getTimeAssigned()) +
	 * " minutes");
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * 
	 * }
	 * 
	 * }
	 */
	public void viewRowFromQuestionTable()
	{ 
		questionsModel obj =questionsTable.getSelectionModel().getSelectedItem();
		if(obj==null)
		{
			alertMaker.showErrorMessage("Please select a question to view", "");
			return;
		}
		try
		{
			Stage viewStage = new Stage();
			FXMLLoader viewQuestionLoader = new FXMLLoader(
					getClass().getResource("/application/Fxmls/viewDetialsQuestions.fxml"));
			Parent root = viewQuestionLoader.load();
			Scene scene = new Scene(root);
			viewStage.setScene(scene);
			viewStage.setResizable(false);
			viewStage.initOwner(viewQuestionButton.getScene().getWindow());
			viewStage.initModality(Modality.APPLICATION_MODAL);
			viewStage.show();
			System.out.println("closed view tab");
		}catch (Exception e) {
			e.printStackTrace();
			alertMaker.showErrorMessage("error opening view window","");
		}
		
	}
	private void print(questionsAllDetials obj) {
		System.out.println(obj.getAddedBy());
		System.out.println(obj.getCategoryid());
		System.out.println(obj.getCorrectAnswer());
		System.out.println(obj.getMarks());
		System.out.println(obj.getType_id());

	}

	private void initCategoriesList() throws Exception {
		categoriesList.clear();
	Platform.runLater(()->	{try {
		categoriesList.addAll(handler.getInstance().getAllCategories());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}});
		selectCategoryComboBox.setItems(categoriesList);
}

	/*
	 * public void viewImageAction() throws Exception {
	 * System.out.println(qidRowToView); ImageView imgView = new ImageView();
	 * questionsAllDetials obj =
	 * handler.getInstance().getAllDetialsOfQuestionByQuestionId(qidRowToView); Blob
	 * blob = obj.getImage(); if (blob == null) {
	 * 
	 * alertMaker.showInfoMessage("", "No image for this question", ""); return; }
	 * InputStream input = new ByteArrayInputStream(blob.getBytes(1, (int)
	 * blob.length())); Image imge = new Image(input); imgView.setImage(imge);
	 * StackPane pane = new StackPane(); pane.getChildren().add(imgView); Scene s =
	 * new Scene(pane, imge.getWidth(), imge.getHeight()); Stage s1 = new Stage();
	 * s1.setScene(s); s1.setResizable(true); s1.initStyle(StageStyle.UNIFIED);
	 * s1.initModality(Modality.APPLICATION_MODAL); s1.show();
	 * 
	 * }
	 */
	public void clearSelection() {
		questionsTable.getSelectionModel().clearSelection();
		errorLabelCategory.setVisible(false);
		errorLabelDeletedQuestion.setVisible(false);
	}

	public void deleteRowFromQuestionTable(ActionEvent event) {
		boolean ans = alertMaker.showConformationMessage("Delete Question",
				"Are you sure you want to delete the question", "Press OK to continue");
		if (ans == false)
			return;

		questionsModel rowToDelete = questionsTable.getSelectionModel().getSelectedItem();
		if (rowToDelete == null)
			System.out.println("select entry");

		if (rowToDelete != null) {

			try {
				int res = handler.getInstance().deleteQuestion(rowToDelete);
				if (res == 1) {
					Platform.runLater(() -> {
						questionsTable.getItems().removeAll(rowToDelete);
					});
					questionsController c= adminDashboardController.questionsloader.getController();
					c.initialize(null, null);
					alertMaker.showInfoMessage("Info","Deleted question succesfully","");

				}
				if (res == 0)
					alertMaker.showErrorMessage("Failed","Failed to delete question");

			} catch (Exception e) {
				e.printStackTrace();
				alertMaker.showErrorMessage("Failed","Failed to delete question");
			}

		}
	}
	public void undeleteRowFromQuestionTable(ActionEvent event) {
		boolean ans = alertMaker.showConformationMessage("Undo Delete operation",
				"Are you sure you want to Undelete the question", "Press OK to continue");
		if (ans == false)
			return;
		questionsModel rowToUndelete = questionsTable.getSelectionModel().getSelectedItem();
		if (rowToUndelete == null)
			System.out.println("select entry");

		if (rowToUndelete != null) {

			try {

				int res = handler.getInstance().undeleteQuestion(rowToUndelete);
				if (res == 1) {
					Platform.runLater(() -> {
						String s=rowToUndelete.getQuestionText().getValue().toLowerCase();
						System.out.println(s+"	"+s.substring(0,s.length()-11));
						questionsTable.getItems().removeAll(rowToUndelete);

					});
					showDeletedCheckBox.setSelected(false);
					questionsController c= adminDashboardController.questionsloader.getController();
					c.initialize(null, null);
					Platform.runLater(()->{errorLabelDeletedQuestion.setVisible(false); editQuestionButton.setDisable(false);});
					alertMaker.showInfoMessage("Info","Undeleted question succesfully","");
					return;
				}
				if (res == 0)
				alertMaker.showErrorMessage("Failed","Failed to undelete question");
		


			} catch (Exception e) {
				e.printStackTrace();
				alertMaker.showErrorMessage("Failed","Failed to undelete question");

			}

		}
	}
	private void initquestionsList() throws Exception {
		try {
		questionsData.addAll( handler.getInstance().loadQuestionData());
		Platform.runLater(() -> {
			questionsTable.setItems(questionsData);
		});
		System.out.println(" added questions in tables");
			
			}
		catch (Exception e) {
			alertMaker.showErrorMessage("Cant load data", "");
		}
}

	public void loadAddQuestionWindow() throws Exception {
		Stage primaryStage = new Stage();
		addQuestionLoader = new FXMLLoader();
		Parent root = addQuestionLoader.load(getClass().getResource("/application/Fxmls/addQuestion.fxml"));
		primaryStage.setTitle("Question bank Internship Test System");
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		primaryStage.show();

	}

	// TODO make a new table for storing previous history of the questions
	public void handleEditQuestionButton() {

		if (questionsTable.getSelectionModel().getSelectedItems().size() == 0) {
			alertMaker.showErrorMessage("select an entry from questions table", "");
			return;
		} else {
            FXMLLoader editQuestionLoader = new FXMLLoader(
                    getClass().getResource("/application/Fxmls/editQuestion.fxml"));
            Parent root = null;
            try {
                root = editQuestionLoader.load();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Stage papersStage = new Stage();
            Scene scene = new Scene(root);
            papersStage.setScene(scene);
            papersStage.setResizable(false);
            papersStage.initOwner(editQuestionButton.getScene().getWindow());
            papersStage.initModality(Modality.APPLICATION_MODAL);
            papersStage.show();
            System.out.println("closed edit tab");
        }
	}
   
   public void handleShowDeletedAction() {
	   if (!showDeletedCheckBox.isSelected()) {
		  initialize(null, null);
	   } else {
		   questionsData.clear();
		   try {
			   questionsData.addAll(handler.getInstance().loadDeletedAndUndeletedQuestionData());
		   } catch (Exception e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }

		   for (int i = 0; i < questionsData.size(); i++) {
			   if (handler.getInstance().isQuestionDeleted(questionsData.get(i).getId()) && questionsData.get(i).getQuestionText().getValue() != null) {
				   questionsData.get(i).setQuestionText(questionsData.get(i).getQuestionText().getValue().concat(" (deleted) "));
			   }
		   }
		   System.out.println(questionsData.toString());
		   Platform.runLater(() -> {
			   questionsTable.setItems(questionsData);
		   });
	   }
   }
	/*
	 * private void viewRowFromQuestionTableForEdit(int id) throws Exception { //
	 * TODO Auto-generated method stub questionsModel rowToView =
	 * questionsTable.getSelectionModel().getSelectedItem(); try
	 * 
	 * {questionsAllDetials obj= handler.getAllDetialsOfQuestionByQuestionId(id);
	 * questionTextArea.setText(obj.getQuestionText());
	 * 
	 * questionCategory.setText(handler.getInstance().getQuestionCategoryName(obj.
	 * getCategoryid()));
	 * 
	 * questionSubcategory.setText(handler.getInstance().getQuestionSubcategoryName(
	 * obj.getSubcategoryid()));
	 * 
	 * questionType.setText(handler.getInstance().getQuestionTypeName(obj.getType_id
	 * ()));
	 * 
	 * questionDifficulty.setText(handler.getInstance().getDifficultyLevel(Integer.
	 * valueOf(obj.getDifficulty())));
	 * 
	 * questionMarks.setText(String.valueOf(obj.getMarks()));
	 * 
	 * questionAddedby.setText(obj.getAddedBy());
	 * 
	 * questionChoice1.setText(obj.getFirstChoice());
	 * 
	 * questionChoice2.setText(obj.getSecondChoice());
	 * questionChoice3.setText(obj.getThirdChoice());
	 * questionChoice4.setText(obj.getFourthChoice());
	 * questionCorrectAnswer.setText(obj.getCorrectAnswer());
	 * questionTimeAssigned.setText(String.valueOf(obj.getTimeAssigned()));
	 * 
	 * }catch (Exception e) { alertMaker.showErrorMessage(e); } }
	 * 
	 * public void handleSaveQuestionButton(javafx.scene.input.MouseEvent event) {
	 * questionTextArea.setEditable(false);
	 * 
	 * questionCategory.setEditable(false); questionSubcategory.setEditable(false);
	 * 
	 * questionType.setEditable(false);
	 * 
	 * questionDifficulty.setEditable(false); questionMarks.setEditable(false);
	 * questionAddedby.setEditable(false);
	 * 
	 * questionChoice1.setEditable(false);
	 * 
	 * questionChoice2.setEditable(false);
	 * 
	 * questionChoice3.setEditable(false);
	 * 
	 * questionChoice4.setEditable(false);
	 * 
	 * questionCorrectAnswer.setEditable(false);
	 * questionTimeAssigned.setEditable(false);
	 * 
	 * }
	 */
}
