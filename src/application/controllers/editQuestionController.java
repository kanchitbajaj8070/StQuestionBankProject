package application.controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import application.alert.alertMaker;
import application.database.databaseHelper;
import application.model.questionsModel;
import application.model.questionsAllDetials;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class editQuestionController implements Initializable {
	private static databaseHelper handler = null;
	@FXML
	private JFXTextArea editQuestionTextArea;
	@FXML
	private StackPane stackpane;
	@FXML
	private JFXComboBox<String> editQuestionCategory;

	@FXML
	private Label errorLabelCategory;

	@FXML
	private JFXComboBox<String> editQuestionSubcategory;

	@FXML
	private Label errorLabelsubcategory;

	@FXML
	private JFXButton editQuestionImage;

	@FXML
	private JFXComboBox<String> editQuestionType;

	@FXML
	private Label errorLabelType;

	@FXML
	private JFXComboBox<String> editQuestionDifficulty;

	@FXML
	private Label errorLabelDifficulty;

	@FXML
	private JFXTextField editQuestionFirstChoice;

	@FXML
	private Label errorLabelChoice1;

	@FXML
	private JFXTextField editQuestionSecondChoice;

	@FXML
	private Label errorLabelChoice2;

	@FXML
	private JFXTextField editQuestionThirdChoice;

	@FXML
	private Label errorLabelChoice3;

	@FXML
	private JFXTextField editQuestionFourthChoice;

	@FXML
	private Label errorLabelChoice4;

	@FXML
	private ComboBox<String> editQuestionCorrectChoice;

	@FXML
	private Label errorLabelCorrectAnswer;

	@FXML
	private JFXTextField editQuestionMarks;

	@FXML
	private Label errorLabelMarks;
    @FXML
    private CheckBox showDeletedCheckBox;

	@FXML
	private JFXComboBox<String> editQuestionTime;

	@FXML
	private Label errorLabelTimeAssigned;
 private  static int editQid=-1;
	@FXML
	private JFXButton saveButton;
	public static ObservableList<String> categoriesList = FXCollections.observableArrayList();
	public static ObservableList<String> subcategoriesList = FXCollections.observableArrayList();
	public static ObservableList<String> typeList = FXCollections.observableArrayList();
	public static ObservableList<String> correctAnswerList = FXCollections.observableArrayList();
	public static ObservableList<String> timeAssignedList = FXCollections.observableArrayList();

	public void editImageAction(MouseEvent event) {
//not implemented since it is set to disabled 
	}

	public void handleCategorySelectionAction(ActionEvent event) {
		if (editQuestionCategory.getValue() == null) {
			System.out.println("select category");
			Platform.runLater(() -> {
				errorLabelCategory.setVisible(true);
			});

		} else {
			try {
				Platform.runLater(() -> {
					errorLabelCategory.setVisible(false);
				});
				initSubcategoriesList(
						handler.getInstance().getCategoryId(editQuestionCategory.getValue().toString().toLowerCase()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void handleSaveButtonAction(MouseEvent event) {
		try {
			disableAllErrors();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		int m = 0;
		if (editQuestionMarks.getText() != null && !editQuestionMarks.getText().isEmpty())
			m = Integer.valueOf(editQuestionMarks.getText());
		if (m > 99) {
			System.out.println("incorrect Marks");
			alertMaker.showErrorMessage("Marks should be less than 100 ", "Please check and try again");
			return;

		}
		Boolean checkField = checkEntries();
		if (checkField == false) {
			System.out.println("incorrect input");
			alertMaker.showErrorMessage("Incorrect inputs in some fields ", "Please check again");
			return;
		}
		try {
			disableAllErrors();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// variable names corrsp. to datatbase columns
		String question_text = editQuestionTextArea.getText().toLowerCase();
		String category_name = editQuestionCategory.getValue().toLowerCase();
		String difficulty = editQuestionDifficulty.getValue().toLowerCase();
		String subcategory_name = editQuestionSubcategory.getValue().toLowerCase();
		String type = editQuestionType.getValue().toLowerCase();
		String marks = editQuestionMarks.getText().toString();
		String edited_by = loginController.loggedUser.toLowerCase();
		String firstchoice = editQuestionFirstChoice.getText().toLowerCase();
		String secondchoice = editQuestionSecondChoice.getText().toLowerCase();
		String thirdchoice = editQuestionThirdChoice.getText().toLowerCase();
		String fourthchoice = editQuestionFourthChoice.getText().toLowerCase();
		String correct_answer = editQuestionCorrectChoice.getValue();
		if(correct_answer!=null&&editQuestionType.getValue().equalsIgnoreCase("subjective"))
			correct_answer="0";//for subjective
		else	if (!(correct_answer == null) && correct_answer.equalsIgnoreCase("true"))
			correct_answer = "1";
		else if (!(correct_answer == null) && correct_answer.equalsIgnoreCase("false"))
			correct_answer = "2";
		else if(correct_answer!=null&&editQuestionType.getValue().equalsIgnoreCase("objective")&&(!correct_answer.equalsIgnoreCase("no option")))
			 correct_answer=correct_answer.substring(0,correct_answer.indexOf(" "));
		else if(correct_answer!=null&&editQuestionType.getValue().equalsIgnoreCase("objective")&&editQuestionCorrectChoice.getValue().equalsIgnoreCase("no option"))
		{//case of subjective question original and objective afterwards
			alertMaker.showErrorMessage("Please select a choice of correct answer ","");
			return;
		}
		else
		{
			alertMaker.showErrorMessage("Please select a choice of correct answer ","");
			return;
		}

		
	String time_assigned = editQuestionTime.getValue().toString();
		System.out.println("timne is " + time_assigned);

		try {
		Boolean ans=	handler.getInstance().editQuestionDatabaseChangesHandler(question_text, category_name, subcategory_name,handler.getInstance().viewImage(editQid), type, difficulty, marks, loginController.loggedUser, firstchoice, secondchoice, thirdchoice, fourthchoice, correct_answer, time_assigned, editQid);			System.out.println("inserted question" + "	closing edit screenn!!!!!!!!!!!");
		if(ans==true)	{
		alertMaker.showInfoMessage("Success", "Edited question Successfully", "Success");
			questionsController q = adminDashboardController.questionsloader.getController();
			if (q != null)
				q.handleRefreshQuestionsAction();
			Stage current = (Stage) editQuestionCorrectChoice.getScene().getWindow();
			editQid=-1;
			current.close();
		}else
		{
			alertMaker.showErrorMessage("Error occured while saving changes", "Failure");
		}
		} catch (Exception e) {
			alertMaker.showErrorMessage("Failed to insert Question", "Failure");
		}
	}

	private void disableAllErrors() throws Exception {

		Platform.runLater(() -> {
			errorLabelCategory.setVisible(false);
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
		Boolean ans = true;
		if (editQuestionTextArea.getText() == null) {
			ans = false;
		}
		if (editQuestionCategory.getValue() == null) {
			Platform.runLater(() -> errorLabelCategory.setVisible(true));
			ans = false;
		}
		if (editQuestionSubcategory.getValue() == null) {
			Platform.runLater(() -> errorLabelsubcategory.setVisible(true));
			ans = false;
		}
		if (editQuestionType.getValue() == null) {
			Platform.runLater(() -> errorLabelType.setVisible(true));
			ans = false;
		}
		if (editQuestionDifficulty.getValue() == null) {
			Platform.runLater(() -> errorLabelDifficulty.setVisible(true));
			ans = false;
		}
		if (editQuestionMarks.getText().toString() == null || editQuestionMarks.getText().isEmpty()) {
			Platform.runLater(() -> errorLabelMarks.setVisible(true));
			ans = false;
		}
		if (editQuestionTime.getValue() == null || editQuestionTime.getValue().toString().isEmpty()) {
			Platform.runLater(() -> errorLabelTimeAssigned.setVisible(true));
			ans = false;
		}
		if ((editQuestionFirstChoice.getText().toString().isEmpty() || editQuestionFirstChoice.getText() == null)
				&& editQuestionType.getValue() != null && (editQuestionType.getValue().equalsIgnoreCase("objective")
						|| editQuestionType.getValue().equalsIgnoreCase("true/false"))) {
			Platform.runLater(() -> errorLabelChoice1.setVisible(true));
			ans = false;
		}
		if ((editQuestionSecondChoice.getText().toString().isEmpty() || editQuestionSecondChoice.getText() == null)
				&& editQuestionType.getValue() != null && (editQuestionType.getValue().equalsIgnoreCase("objective")
						|| editQuestionType.getValue().equalsIgnoreCase("true/false"))) {
			Platform.runLater(() -> errorLabelChoice2.setVisible(true));
			ans = false;
		}
		if ((editQuestionThirdChoice.getText().toString().isEmpty() || editQuestionThirdChoice.getText() == null)
				&& editQuestionType.getValue() != null && (editQuestionType.getValue().equalsIgnoreCase("objective"))) {
			Platform.runLater(() -> errorLabelChoice3.setVisible(true));
			ans = false;
		}
		if ((editQuestionFourthChoice.getText().toString().isEmpty() || editQuestionFourthChoice.getText() == null)
				&& editQuestionType.getValue() != null && (editQuestionType.getValue().equalsIgnoreCase("objective"))) {
			Platform.runLater(() -> errorLabelChoice4.setVisible(true));
			ans = false;
		}
		if (editQuestionCorrectChoice.getValue() == null && (editQuestionType.getValue() != null
				&& !(editQuestionType.getValue().equalsIgnoreCase("subjective")))) {
			Platform.runLater(() -> errorLabelCorrectAnswer.setVisible(true));
			ans = false;
		}
		if (editQuestionType.getValue() != null
				&& editQuestionType.getValue().toString().equalsIgnoreCase("subjective")) {
			Platform.runLater(() -> {
				errorLabelChoice1.setVisible(false);
				errorLabelChoice2.setVisible(false);
				errorLabelChoice3.setVisible(false);
				errorLabelChoice4.setVisible(false);
			});
		}
		if (editQuestionType.getValue() != null
				&& editQuestionType.getValue().toString().equalsIgnoreCase("true/false")) {
			Platform.runLater(() -> {
				errorLabelChoice1.setVisible(false);
				errorLabelChoice2.setVisible(false);
				errorLabelChoice3.setVisible(false);
				errorLabelChoice4.setVisible(false);
			});
		}
		return ans;

	}

	public void handleSubcategorySelectionAction(MouseEvent event) {
		if (editQuestionCategory.getValue() == null) {
			System.out.println("select category");
			Platform.runLater(() -> {
				errorLabelCategory.setVisible(true);
			});

		} else {
			Platform.runLater(() -> {
				errorLabelCategory.setVisible(false);
			});
		}

	}
//this code was used if type of question was changed , since it is disabled it is not used in app
	public void handleTypeSelectedAction(ActionEvent event) throws Exception {
		if (editQuestionType.getValue() != null) {
			if (editQuestionType.getValue().toString().equalsIgnoreCase(("subjective"))) {
				Platform.runLater(() -> {
					correctAnswerList.clear();
					editQuestionCorrectChoice.setPromptText("No choice available");
					editQuestionFirstChoice.setEditable(true);
					editQuestionFourthChoice.setEditable(true);
					editQuestionSecondChoice.setEditable(true);
					editQuestionThirdChoice.setEditable(true);
					editQuestionFirstChoice.clear();
					editQuestionSecondChoice.clear();
					editQuestionThirdChoice.clear();
					editQuestionFourthChoice.clear();
					editQuestionFirstChoice.setPromptText("no choices in subjective questions allowed");
					editQuestionSecondChoice.setPromptText("no choices in subjective questions allowed");
					editQuestionThirdChoice.setPromptText("no choices in subjective questions allowed");
					editQuestionFourthChoice.setPromptText("no choices in subjective questions allowed");
					editQuestionFirstChoice.setEditable(false);
					editQuestionFourthChoice.setEditable(false);
					editQuestionSecondChoice.setEditable(false);
					editQuestionThirdChoice.setEditable(false);

				});
				return;
			}

			else if (editQuestionType.getValue().toString().equalsIgnoreCase(("true/false"))) {
				Platform.runLater(() -> {
					editQuestionCorrectChoice.setEditable(true);
					editQuestionCorrectChoice.setPromptText("select choice");
					editQuestionCorrectChoice.setEditable(false);
					correctAnswerList.clear();
					correctAnswerList.add("true");
					correctAnswerList.add("false");
					editQuestionCorrectChoice.setItems(correctAnswerList);
					editQuestionFirstChoice.setEditable(true);
					editQuestionFourthChoice.setEditable(true);
					editQuestionSecondChoice.setEditable(true);
					editQuestionThirdChoice.setEditable(true);
					editQuestionFirstChoice.setPromptText("");
					editQuestionSecondChoice.setPromptText("");
					editQuestionThirdChoice.setPromptText("");
					editQuestionFourthChoice.setPromptText("");
					editQuestionFirstChoice.clear();
					editQuestionSecondChoice.clear();
					editQuestionThirdChoice.clear();
					editQuestionFourthChoice.clear();
					editQuestionFirstChoice.setText("true");
					editQuestionSecondChoice.setText("false");
					editQuestionFourthChoice.setEditable(false);
					editQuestionThirdChoice.setEditable(false);
					editQuestionFirstChoice.setEditable(false);
					editQuestionSecondChoice.setEditable(false);
				});

			} else {
				correctAnswerList.clear();
				initCorrectAnswer();
				editQuestionFirstChoice.clear();
				editQuestionSecondChoice.clear();
				editQuestionThirdChoice.clear();
				editQuestionFourthChoice.clear();
				editQuestionFirstChoice.setPromptText("");
				editQuestionSecondChoice.setPromptText("");
				editQuestionThirdChoice.setPromptText("");
				editQuestionFourthChoice.setPromptText("");
				editQuestionFirstChoice.setEditable(true);
				editQuestionFourthChoice.setEditable(true);
				editQuestionSecondChoice.setEditable(true);
				editQuestionThirdChoice.setEditable(true);
			}
		}
	}

	public void marksCheck(KeyEvent event) {
		if (editQuestionMarks.getText().isEmpty())
			return;
		String input = event.getCharacter();
		if (editQuestionMarks.getText() == null) {
			Platform.runLater(() -> {
				errorLabelMarks.setVisible(true);
			});
			return;
		}
		Pattern pattern = Pattern.compile(".*[^0-9].*");
		if (pattern.matcher(input).matches() == true) {
			errorLabelMarks.setVisible(true);
			errorLabelMarks.setText(" *Only integer allowed");
			event.consume();
		}

		if (pattern.matcher(input).matches() == false) {
			Platform.runLater(() -> {
				errorLabelMarks.setVisible(false);
			});
		}

	}

	public questionsAllDetials questionToEdit = null;
	public static ObservableList<String> difficultyLevelsList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 
	
		editQuestionType.setDisable(true);
		editQuestionFirstChoice.setDisable(true);
		editQuestionSecondChoice.setDisable(true);
		editQuestionThirdChoice.setDisable(true);
		editQuestionFourthChoice.setDisable(true);
		editQuestionImage.setDisable(true);
		questionsController c = adminDashboardController.questionsloader.getController();
		application.model.questionsModel obj = (questionsModel) c.getTable().getSelectionModel().getSelectedItem();
		 editQid=obj.getId();
		System.out.println("edit value ois" + obj.getId());
		try {
			initCategoriesList();

		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			initTypeList();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			initDifficultyLevels();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			questionsAllDetials Obj = handler.getInstance().getAllDetialsOfQuestionByQuestionId(obj.getId());
			editQuestionTextArea.setText(Obj.getQuestionText());
			editQuestionCategory.setValue(handler.getInstance().getQuestionCategoryName(Obj.getCategoryid()));
			editQuestionSubcategory.setValue(handler.getInstance().getQuestionSubcategoryName(Obj.getSubcategoryid()));
			initSubcategoriesList(Obj.getCategoryid());

			editQuestionType.setValue(handler.getInstance().getQuestionTypeName(Obj.getType_id()));
			editQuestionDifficulty.setValue(handler.getInstance().getDifficultyLevel(Obj.getDifficulty()));
			String time = String.valueOf(Obj.getTimeAssigned());
			System.out.println(time);
			Platform.runLater(() -> {
				editQuestionTime.getEditor().setEditable(true);
				editQuestionTime.getEditor().setText(time.toString().toUpperCase());
				editQuestionTime.getEditor().setEditable(false);
				editQuestionTime.setValue(time);
			});

			editQuestionMarks.setText(String.valueOf((Obj.getMarks())));

			if (Obj.getFirstChoice() != null)
				editQuestionFirstChoice.setText(Obj.getFirstChoice());
			else
				editQuestionFirstChoice.setText("Choice Not available");
			if (Obj.getSecondChoice() != null)
				editQuestionSecondChoice.setText(Obj.getSecondChoice());
			else
				editQuestionSecondChoice.setText("Choice Not available");
			if (Obj.getThirdChoice() != null)
				editQuestionThirdChoice.setText(Obj.getThirdChoice());
			else
				editQuestionThirdChoice.setText("Choice Not available");
			if (Obj.getFourthChoice() != null)
				editQuestionFourthChoice.setText(Obj.getFourthChoice());
			else
				editQuestionFourthChoice.setText("Choice Not available");

			if (Obj.getType_id() == 2) {
				System.out.println("HERE IT IS SUBJECTIVE");
				Platform.runLater(() -> {
					editQuestionCorrectChoice.getEditor().setEditable(true);
					editQuestionCorrectChoice.getEditor().setText("No Option");
					editQuestionCorrectChoice.setValue(editQuestionCorrectChoice.getEditor().getText());
				});

			}
			if (Obj.getType_id() == 1)

			{
				initCorrectAnswer();
				editQuestionCorrectChoice.setValue(Integer.toString(Obj.getCorrectAnswer()) + " option");

			}
			if (Obj.getType_id() == 3) {
				if (Obj.getCorrectAnswer() == 1)
					editQuestionCorrectChoice.setValue("true");
				else
					editQuestionCorrectChoice.setValue("false");
				correctAnswerList.clear();
				correctAnswerList.add("true");
				correctAnswerList.add("false");
				editQuestionCorrectChoice.setItems(correctAnswerList);

			}
			initTimeAssigned();

		} catch (

		Exception e) {
			e.printStackTrace();
			alertMaker.showErrorMessage("error loading data", "Try again");
			Stage current = (Stage) editQuestionMarks.getScene().getWindow();
			current.close();

		}

	}

	public void initCorrectAnswer() {
		correctAnswerList.clear();
		correctAnswerList.add("1 st option");
		correctAnswerList.add("2 nd option");
		correctAnswerList.add("3 rd option");
		correctAnswerList.add("4 th option");
		editQuestionCorrectChoice.setItems(correctAnswerList);

	}

	public void initSubcategoriesList(int id) throws Exception {
		subcategoriesList.clear();
		subcategoriesList.addAll(handler.getInstance().getSubcategoriesByCategoryId(id));
		editQuestionSubcategory.setItems(subcategoriesList);

	}

	public void initTimeAssigned() {
		timeAssignedList.clear();
		timeAssignedList.add("1");
		timeAssignedList.add("2");
		timeAssignedList.add("3");
		timeAssignedList.add("5");
		timeAssignedList.add("10");
		timeAssignedList.add("30");
		System.out.println(timeAssignedList.toString() + editQuestionTime.getEditor().getText());
		editQuestionTime.setItems(timeAssignedList);
		for (int i = 0; i < timeAssignedList.size(); i++)// temporary fix for edit question problem
		{
			if (timeAssignedList.get(i).toString().equalsIgnoreCase(editQuestionTime.getEditor().getText())) {
				System.out.println("matched");
				editQuestionTime.setValue(timeAssignedList.get(i).toLowerCase());
			}

		} // TODO Give 30 as a disabled input and alert with IT
	}

	public void initTypeList() throws Exception {
		typeList.clear();
		typeList.addAll(handler.getInstance().getAllTypes());
		editQuestionType.setItems(typeList);

	}

	public void initCategoriesList() throws Exception {
		categoriesList.clear();
			categoriesList.addAll(handler.getInstance().getAllCategories());
		editQuestionCategory.setItems(categoriesList);
	}

	public void initDifficultyLevels() throws Exception {
		difficultyLevelsList.clear();
			difficultyLevelsList.addAll(handler.getInstance().getAllDifficultyLevels());
		editQuestionDifficulty.setItems(difficultyLevelsList);

	}

}
