package application.controllers;

import application.model.*;
import java.net.URL;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import application.alert.alertMaker;
import application.database.databaseHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
//TODO make the generation algo efficient
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class generatePaperController implements Initializable {
	private static databaseHelper handler = null;
	private static int totalEnteredQuestions = 0;
	private static ArrayList<Integer> questionPaperQids = new ArrayList<Integer>();
    @FXML
    private JFXSpinner progressIndicator;
	@FXML
	private JFXButton generateButton;
	@FXML
	private TableColumn<paperModel, String> difficultyColumn;
	@FXML
	private TableView<paperModel> currentStatusTable;
	@FXML
	private TableColumn<paperModel, String> categoryColumn;

	@FXML
	private TableColumn<paperModel, String> subcategoryColumn;

	@FXML
	private MenuItem deleteQuestionMenuItem;
	@FXML
	private TableColumn<paperModel, String> noOfQuestionColumn;

	@FXML
	private ComboBox<String> categoryComboBox;

	@FXML
	private ComboBox<String> subcategoryComboBox;

	@FXML
	private JFXTextField subcategoryQuestionNumberText;

	@FXML
	private JFXButton addButton;
	@FXML
	private ComboBox<String> difficultyComboBox;
	@FXML
	private Label errorLabelConfirmButton;
	@FXML
	private TableColumn<paperModel, String> typeColumn;
	@FXML
	private ComboBox<String> typeComboBox;
	@FXML
	private Label errorLabelCategory;
	@FXML
	private Label notEnoughQuestionsLabel;
	@FXML
	private JFXTextField paperNameTextField;

	@FXML
	private JFXTextField paperPurposeTextField;

	@FXML
	private JFXTextField paperTargetCollegeTextField;

	@FXML
	private JFXDatePicker paperExamDateTextField;

	@FXML
	private JFXTextField paperTargetCourseTextField;
	@FXML
	private JFXTextField totalQuestionNumberText;
	public static HashSet<paperModel> questionSet = new HashSet<paperModel>();
	@FXML
	private JFXButton confirmButton;
	public static ObservableList<String> categoriesList = FXCollections.observableArrayList();
	public static ObservableList<String> subcategoriesList = FXCollections.observableArrayList();
	public static ObservableList<String> difficultyLevelsList = FXCollections.observableArrayList();
	private ObservableList<paperModel> paperData = FXCollections.observableArrayList();
	public static ObservableList<String> typeList = FXCollections.observableArrayList();

	private void initTypeList() throws Exception {
		typeList.clear();
		typeList.addAll(handler.getInstance().getAllTypes());
		typeComboBox.setItems(typeList);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		paperData.clear();
		questionSet.clear();
		categoryColumn.setCellValueFactory(new PropertyValueFactory<paperModel, String>("category"));
		subcategoryColumn.setCellValueFactory(new PropertyValueFactory<paperModel, String>("subcategory"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<paperModel, String>("type"));
		noOfQuestionColumn.setCellValueFactory(new PropertyValueFactory<paperModel, String>("noOfQuestions"));
		difficultyColumn.setCellValueFactory(new PropertyValueFactory<paperModel, String>("difficulty"));

		Platform.runLater(() -> {
			errorLabelCategory.setVisible(false);
		});
		initCategories();
		try {
			initDifficultyLevels();
			initTypeList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initDifficultyLevels() throws Exception {
		difficultyLevelsList.clear();
			difficultyLevelsList.addAll(handler.getInstance().getAllDifficultyLevels());
		difficultyComboBox.setItems(difficultyLevelsList);

	}

	public void clearSelection() {
		currentStatusTable.getSelectionModel().clearSelection();
		errorLabelCategory.setVisible(false);
	}

	public void handleDeleteAction(ActionEvent event) {
		paperModel rowToDelete = currentStatusTable.getSelectionModel().getSelectedItem();
		System.out.println("BEFORE deletion total are" + totalEnteredQuestions);
		if (rowToDelete != null) {
			Platform.runLater(() -> {
				currentStatusTable.getItems().removeAll(rowToDelete);
			});
			questionSet.remove(rowToDelete);
			totalEnteredQuestions = totalEnteredQuestions - Integer.valueOf(rowToDelete.getNoOfQuestions());
			if (currentStatusTable.getItems().size() == 0)
				totalEnteredQuestions = 0;
			System.out.println("after deletion total are" + totalEnteredQuestions);
		}
	}

private void initCategories()  {
			categoriesList.clear();
			try {
				categoriesList.addAll( handler.getInstance().getAllCategories());
				categoryComboBox.setItems(categoriesList);
			} catch (Exception e) {
alertMaker.showErrorMessage("error loading category List","");
			}
		
		}

	private void initSubcategoriesList(int id) throws Exception {
		subcategoriesList.clear();
		subcategoriesList.addAll(handler.getInstance().getSubcategoriesByCategoryId(id));
		subcategoryComboBox.setItems(subcategoriesList);

	}

	public void handleCategorySelectionAction() {

		if (categoryComboBox.getValue() != null) {
			try {
				Platform.runLater(() -> {
					errorLabelCategory.setVisible(false);
				});
				initSubcategoriesList(
						handler.getInstance().getCategoryId(categoryComboBox.getValue().toString().toLowerCase()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	public void handleAddButtonAction() throws Exception {
		Platform.runLater(() -> {
			notEnoughQuestionsLabel.setVisible(false);
		});
		int totalSubcategoryQuestion;
		try {
			totalSubcategoryQuestion = Integer.parseInt(subcategoryQuestionNumberText.getText());
			System.out.println("sub category questions are" + totalSubcategoryQuestion);
		} catch (NumberFormatException e) {
			System.out.println("integer only");
			return;
		}
		Platform.runLater(() -> {
			notEnoughQuestionsLabel.setVisible(false);
			errorLabelCategory.setVisible(false);
		});
		int questionInBank = 0;
		if (subcategoryComboBox.getValue() != null && difficultyComboBox.getValue() != null
				&& typeComboBox.getValue() != null)
			questionInBank = handler.getInstance().countBySubcategoryId(
					handler.getInstance().getSubcategoryId(subcategoryComboBox.getValue().toLowerCase()),
					handler.getInstance().getDifficultyId(difficultyComboBox.getValue().toLowerCase()),
					handler.getInstance().getTypeId(typeComboBox.getValue().toLowerCase()));
		else {
			return;
		}
		if (questionInBank < totalSubcategoryQuestion) {
			Platform.runLater(() -> {
				notEnoughQuestionsLabel.setVisible(true);
			});
			return;
		}
		if (questionInBank >= totalSubcategoryQuestion) {
			Platform.runLater(() -> {
				notEnoughQuestionsLabel.setVisible(false);
			});
		}
		paperModel newObj = new paperModel(categoryComboBox.getValue().toLowerCase(),
				subcategoryComboBox.getValue().toLowerCase(), String.valueOf(totalSubcategoryQuestion),
				difficultyComboBox.getValue().toLowerCase(), typeComboBox.getValue().toLowerCase());
		if (alreadyInTable(newObj) == false || questionSet.isEmpty()) {
			paperData.add(newObj);
			questionSet.add(newObj);
			totalEnteredQuestions += Integer.valueOf(subcategoryQuestionNumberText.getText());

		} else {
			System.out.println("repition!!!!!!!!!!!'");
			alertMaker.showErrorMessage("error", "alerady in table , please select some other combination");
			return;
		}
		currentStatusTable.setItems(paperData);
		clearAllEnteries();

	}

	private boolean alreadyInTable(paperModel newObj) {
		boolean ans = false;
		System.out.println("question set oi s:- " + questionSet.isEmpty());
		for (paperModel oldObj : questionSet) {
			System.out.println("objext" + oldObj.getCategory() + oldObj.getDifficulty());

			System.out.println(oldObj.getCategory());
			System.out.println(newObj.getCategory());
			System.out.println(oldObj.getSubcategory());
			System.out.println(newObj.getSubcategory());
			System.out.println(oldObj.getDifficulty());
			System.out.println(newObj.getDifficulty());

			if ((oldObj.getCategory().equalsIgnoreCase(newObj.getCategory()))
					&& (oldObj.getSubcategory().equalsIgnoreCase(newObj.getSubcategory()))
					&& (oldObj.getDifficulty().equalsIgnoreCase(newObj.getDifficulty())
							&& (oldObj.getType().equalsIgnoreCase(newObj.getType())))) {
				ans = true;
				return true;
			}

		}
		System.out.println(ans);
		return ans;
	}
	private String listOfQuestions = null;
	public void handleGenerateButtonAction(MouseEvent event) throws Exception 
	{
 ExecutorService e= Executors.newFixedThreadPool(2);
 generateTask task=new generateTask();
 progressIndicator.visibleProperty().bind(task.runningProperty());
 e.submit(task);
 e.shutdown();
 task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

	@Override
	public void handle(WorkerStateEvent arg0) {
		String listOfquestions = listOfQuestions;
		handler.getInstance().insertPaperInformationInBank(paperNameTextField.getText(), listOfquestions,
				totalEnteredQuestions, paperTargetCourseTextField.getText().toLowerCase(),
				paperTargetCollegeTextField.getText().toLowerCase(), paperPurposeTextField.getText().toLowerCase(),
				Date.valueOf(LocalDate.now()), Date.valueOf(paperExamDateTextField.getValue()),
				loginController.loggedUser);
		Platform.runLater(()->	alertMaker.showInfoMessage("Success", "Succesfully generated Paper ", ""));
		questionPaperQids.clear();
		generatePaperController.totalEnteredQuestions = 0;
		listOfquestions = null;
		Platform.runLater(()->	questionSet.clear());
		Platform.runLater(()->{totalQuestionNumberText.setText(null);
		totalQuestionNumberText.setPromptText("Enter total no. of questions here ");});
		Platform.runLater(()->paperInfoclear());
		papersController c = adminDashboardController.papersLoader.getController();
         c.initialize(null, null);
		Stage current = (Stage) generateButton.getScene().getWindow();
		current.close();
	Platform.runLater(()-> {progressIndicator.visibleProperty().unbind(); progressIndicator.setVisible(false);});
		
	}
});
 /*task.setOnFailed(new EventHandler<Event>() {

	@Override
	public void handle(Event arg0) {
		Platform.runLater(()-> {progressIndicator.visibleProperty().unbind(); progressIndicator.setVisible(false);
		alertMaker.showErrorMessage("Error generating paper", "Failure");});
 System.out.println("some error");
 
	}*/
//});
		task.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				String listOfquestions = listOfQuestions;
				handler.getInstance().insertPaperInformationInBank(paperNameTextField.getText(), listOfquestions,
						totalEnteredQuestions, paperTargetCourseTextField.getText().toLowerCase(),
						paperTargetCollegeTextField.getText().toLowerCase(), paperPurposeTextField.getText().toLowerCase(),
						Date.valueOf(LocalDate.now()), Date.valueOf(paperExamDateTextField.getValue()),
						loginController.loggedUser);
				Platform.runLater(()->	alertMaker.showInfoMessage("Success", "Succesfully generated Paper ", ""));
				questionPaperQids.clear();
				generatePaperController.totalEnteredQuestions = 0;
				listOfquestions = null;
				Platform.runLater(()->	questionSet.clear());
				Platform.runLater(()->{totalQuestionNumberText.setText(null);
					totalQuestionNumberText.setPromptText("Enter total no. of questions here ");});
				Platform.runLater(()->paperInfoclear());
				papersController c = adminDashboardController.papersLoader.getController();
				c.initialize(null, null);
				Stage current = (Stage) generateButton.getScene().getWindow();
				current.close();
				Platform.runLater(()-> {progressIndicator.visibleProperty().unbind(); progressIndicator.setVisible(false);});

			}
		});
	}
	private class generateTask extends Task{

		@Override
		protected Object call() throws Exception {
           generatePaperAction();
			return null;
		}
		
	
	public void generatePaperAction() throws Exception {
	
		if(totalQuestionNumberText.getText()==null|| totalQuestionNumberText.getText().isEmpty())
		{
		Platform.runLater(()->alertMaker.showErrorMessage("Please enter total number of questions to be generated",""));
			return;
			
		}
		boolean result = checkPaperInfo();
		if (result == false) {
			Platform.runLater(()->		alertMaker.showErrorMessage("Please fill alll fields of paper information",
					" Cant leave the fields of paper detial empty"));
			return;

		}
		System.out.println(generatePaperController.totalEnteredQuestions);
		if (totalQuestionNumberText.getText() != null)
			System.out.println(Integer.valueOf(totalQuestionNumberText.getText().toString()));
		boolean ans = checkSum();
		if (ans == false) {
			Platform.runLater(()->	alertMaker.showErrorMessage("total no of question dont match with subcategories question numbers ",
					"Please correct the total question number"));
			System.out.println("total no of question dont match with subcategories question numbers ");
			return;
		}

		processTable();
		

	}
	}
	

	private void paperInfoclear() {
		Platform.runLater(()->{
			totalQuestionNumberText.clear();
			paperNameTextField.clear();
		paperPurposeTextField.clear();
		paperTargetCollegeTextField.clear();
		paperTargetCourseTextField.clear();
		paperExamDateTextField.setValue(null);
		initialize(null, null);})
		;
	}



	private boolean checkPaperInfo() {
		boolean ans = true;
		if (paperNameTextField.getText() == null || paperNameTextField.getText().isEmpty())
			ans = false;
		if (paperPurposeTextField.getText() == null || paperPurposeTextField.getText().isEmpty())
			ans = false;
		if (paperTargetCollegeTextField.getText() == null || paperTargetCollegeTextField.getText().isEmpty())
			ans = false;
		if (paperTargetCourseTextField.getText() == null || paperTargetCourseTextField.getText().isEmpty())
			ans = false;
		if (paperExamDateTextField.getValue() == null)
			ans = false;
		return ans;
	}

	public void processTable() throws Exception {
		questionPaperQids.clear();
		listOfQuestions = new String();
		while (!paperData.isEmpty()) {
			HashSet<Integer> hashSet = new HashSet<Integer>();
			paperModel obj = paperData.remove(0);
			System.out.println(obj.getNoOfQuestions() + "     " + obj.getSubcategory());
			ArrayList<Integer> qids = handler.getInstance().getQIdBySubcategoryId(
					handler.getInstance().getSubcategoryId(obj.getSubcategory().toLowerCase()),
					handler.getInstance().getDifficultyId(obj.getDifficulty().toLowerCase()),
					handler.getInstance().getTypeId(obj.getType().toLowerCase()));
			System.out.println(qids.toString());
			int range = Integer.valueOf(qids.size());
			System.out.println(range);

			while (hashSet.size() < Integer.valueOf(obj.getNoOfQuestions())) {
				int rand = (int) (Math.random() * range) + 0;
				System.out.println(rand);
				if (hashSet.size() == 0 || !hashSet.contains(qids.get(rand))) {
					questionPaperQids.add(qids.get(rand));
					listOfQuestions = listOfQuestions + String.valueOf(qids.get(rand)) + ",";
					hashSet.add(qids.get(rand));
				}

			}

		}
		System.out.println(" final list is :- " + questionPaperQids.toString());
		makePdfOfQuestions();
	}

	public void makePdfOfQuestions() {

		Font f = new Font();
		f.setStyle(Font.BOLD);
		f.setSize(12);
		Font f1 = new Font();
		f1.setStyle(Font.BOLD);
		f1.setSize(14);
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		File fileSelected = new File("resources/pdfs/" + (handler.getInstance().prevPID() + 1) + ".pdf");
		try {
			if (fileSelected.createNewFile() == false) {
				alertMaker.showErrorMessage("error", "not able to make file");
				Stage current = (Stage) generateButton.getScene().getWindow();
				current.close();
			}
		} catch (IOException e1) {
			alertMaker.showErrorMessage("Some file operation error occured","");
			e1.printStackTrace();
		}
		String path = fileSelected.getPath();
		try {
			System.out.println(path + " read" + fileSelected.canRead() + " " + fileSelected.canWrite() + "	"
					+ fileSelected.getCanonicalPath());
		} catch (IOException e1) {
			alertMaker.showErrorMessage("Some file operation error occured","");
			e1.printStackTrace();
		}
		questionsAllDetials[] obj = new questionsAllDetials[questionPaperQids.size()];
		for (int i = 0; i < questionPaperQids.size(); i++) {
			obj[i] = handler.getInstance().getAllDetialsOfQuestionByQuestionId(questionPaperQids.get(i));

		}
		try {

			PdfWriter.getInstance(document, new FileOutputStream(new File(path)));
			document.open();
			Paragraph p = new Paragraph();
			p.setFont(f1);
			p.add((paperNameTextField.getText()) + "\n"+paperPurposeTextField.getText()+"\n"+paperTargetCollegeTextField.getText()+"\n"+paperTargetCourseTextField.getText()+"\n"+"Date of exam is :-"+ paperExamDateTextField.getValue().toString()+"\n\n"+" Name : _______________" +"  Group____________"+ "  Rno :________________"+"\n");
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
			float[] pointColumnWidths = { 1000F}/*, 250F, 250F, 250F }*/;
			float[] pointColumnWidths1 = { 250F, 250F };
			int size = questionPaperQids.size();
			for (int i = 0; i < size; i++) {
				Paragraph p1 = new Paragraph();
				p1.setFont(f);
				p1.add("\n\n" + (i + 1) + ") " + obj[i].getQuestionText()+"	("+ obj[i].getMarks()+" marks )"+
						" \n\n");
				p1.setAlignment(Element.ALIGN_LEFT);
				document.add(p1);
				if (handler.getInstance().isImagePresent(obj[i].getQid()) == true) {
					try {
						System.out.println("in image tryt");
						Blob imageBlob1 = handler.getInstance().viewImage(obj[i].getQid());
						byte[] imageBytes1 = imageBlob1.getBytes(1, (int) imageBlob1.length());
						System.out.println(imageBytes1.toString());
						com.itextpdf.text.Image image1 = com.itextpdf.text.Image.getInstance(imageBytes1);
						image1.scaleToFit(200f, 200f);
						image1.setAlignment(Element.ALIGN_CENTER);
						document.add(image1);
						document.add(new Paragraph("\n\n"));

					} catch (Exception e) {
						System.out.println(e.getLocalizedMessage());
					 System.out.println("cant load image");
					}

				}

				System.out.println(obj[i].getFirstChoice() + obj[i].getSecondChoice());
				if (obj[i].getFirstChoice() == null && obj[i].getSecondChoice() == null
						&& obj[i].getThirdChoice() == null && obj[i].getFourthChoice() == null) {
					Paragraph p2 = new Paragraph();
					p2.add(" Enter answer to  the subjective question below ");
					p2.setAlignment(Element.ALIGN_LEFT);
					p2.add("\n\n\n\n\n\n\n\n");
					document.add(p2);
					continue;
				}

				if (obj[i].getFirstChoice() != null && obj[i].getSecondChoice() != null
						&& obj[i].getThirdChoice() != null && obj[i].getFourthChoice() != null)

				{
					PdfPTable pdfPTable = new PdfPTable(pointColumnWidths);
					
					PdfPCell pdfPCell1 = new PdfPCell(new Paragraph("a) " + obj[i].getFirstChoice()));
					pdfPCell1.setBorder(Rectangle.NO_BORDER); 
					PdfPCell pdfPCell2 = new PdfPCell(new Paragraph("b) " + obj[i].getSecondChoice()));
					pdfPCell2.setBorder(Rectangle.NO_BORDER); 
					PdfPCell pdfPCell3 = new PdfPCell(new Paragraph("c) " + obj[i].getThirdChoice()));
					pdfPCell3.setBorder(Rectangle.NO_BORDER); 
					PdfPCell pdfPCell4 = new PdfPCell(new Paragraph("d) " + obj[i].getFourthChoice()));
					pdfPCell4.setBorder(Rectangle.NO_BORDER); 

					// Add cells to table
					pdfPTable.addCell(pdfPCell1);
					pdfPTable.addCell(pdfPCell2);
					pdfPTable.addCell(pdfPCell3);
					pdfPTable.addCell(pdfPCell4);
					document.add(pdfPTable);
					continue;
				}

				PdfPTable pdfPTable1 = new PdfPTable(pointColumnWidths1);
				PdfPCell pdfPCell11 = new PdfPCell(new Paragraph("a)   true"));
				pdfPCell11.setBorder(Rectangle.NO_BORDER); 
				PdfPCell pdfPCell21 = new PdfPCell(new Paragraph("b)   false"));
				pdfPCell21.setBorder(Rectangle.NO_BORDER); 
				pdfPTable1.addCell(pdfPCell11);
				pdfPTable1.addCell(pdfPCell21);
				document.add(pdfPTable1);

			}

			document.close();

			System.out.println("Done");

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		makeAnswerKeys();

	}

	public void makeAnswerKeys() {
		Font f = new Font();
		f.setStyle(Font.BOLD);
		f.setSize(12);
		Font f1 = new Font();
		f1.setStyle(Font.BOLD);
		f1.setSize(14);
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		File fileSelected = new File("resources/pdfs/" + (handler.getInstance().prevPID() + 1) + "answerkey.pdf");
		try {
			if (fileSelected.createNewFile() == false) {
				Platform.runLater(()->	alertMaker.showErrorMessage("error", "not able to make answer key  file"));
				Stage current = (Stage) generateButton.getScene().getWindow();
				current.close();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String path = fileSelected.getPath();
		try {
			System.out.println(path + " read" + fileSelected.canRead() + " " + fileSelected.canWrite() + "	"
					+ fileSelected.getCanonicalPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int allAnswers[] = new int[questionPaperQids.size()];
		int allTypes[] = new int[questionPaperQids.size()];

		for (int i = 0; i < questionPaperQids.size(); i++)

		{

			try {
				allAnswers[i] = handler.getInstance().loadCorrectAnswer(questionPaperQids.get(i));
				allTypes[i] = handler.getInstance().loadType(questionPaperQids.get(i));
			} catch (Exception e) {
				Platform.runLater(()->	alertMaker.showErrorMessage("ERROR OCCURED", ""));
				e.printStackTrace();
			}
		}

		try { PdfWriter.getInstance(document, new FileOutputStream(new File(path)));
			document.open();
			Paragraph p = new Paragraph();
			p.setFont(f);
			p.add((paperNameTextField.getText()) + " answer key\n\n");
			p.setAlignment(Element.ALIGN_LEFT);
			document.add(p);
			float[] pointColumnWidths = { 20F, 250F };
			float[] pointColumnWidths1 = { 250F };
			float[] pointColumnWidths2 = { 250F, 250F, 250F, 250F };
			int size = questionPaperQids.size();

			for (int i = 0; i < size; i++) {

				if (allTypes[i] == 2) {

					PdfPTable pdfPTable1 = new PdfPTable(pointColumnWidths);
					PdfPCell pdfPCell11 = new PdfPCell(new Paragraph((i + 1) + ")  "));
					pdfPCell11.setBorder(Rectangle.NO_BORDER); 
					PdfPCell pdfPCell21 = new PdfPCell(new Paragraph("Subjective question",f));
					pdfPCell21.setBorder(Rectangle.NO_BORDER); 
					pdfPTable1.addCell(pdfPCell11);
					pdfPTable1.addCell(pdfPCell21);
					document.add(pdfPTable1);
					continue;
				}

				if (allTypes[i] == 1) {
					PdfPTable pdfPTable1 = new PdfPTable(pointColumnWidths);
					PdfPCell pdfPCell11 = new PdfPCell(new Paragraph((i + 1) + ")  	",f));


					PdfPCell pdfPCell21 = null;

					if (allAnswers[i] == 1)
						pdfPCell21 = new PdfPCell(new Paragraph(" " + (allAnswers[i]) + "st  option"));
					if (allAnswers[i] == 2)
						pdfPCell21 = new PdfPCell(new Paragraph(" " + (allAnswers[i]) + "nd  option"));
					if (allAnswers[i] == 3)
						pdfPCell21 = new PdfPCell(new Paragraph(" " + (allAnswers[i]) + "rd  option"));
					if (allAnswers[i] == 4)
						pdfPCell21 = new PdfPCell(new Paragraph(" " + (allAnswers[i]) + "th  option"));
					pdfPCell21.setBorder(Rectangle.NO_BORDER);
					pdfPCell11.setBorder(Rectangle.NO_BORDER);
					pdfPTable1.addCell(pdfPCell11);
					pdfPTable1.addCell(pdfPCell21);

					document.add(pdfPTable1);
					continue;
				}
				PdfPTable pdfPTable1 = new PdfPTable(pointColumnWidths);
				PdfPCell pdfPCell11 = new PdfPCell(new Paragraph((i + 1) + ")  	",f));
				PdfPCell pdfPCell21 = null;
				if (allAnswers[i] == 1)
					pdfPCell21 = new PdfPCell(new Paragraph(" true"));
				if (allAnswers[i] == 2)
					pdfPCell21 = new PdfPCell(new Paragraph(" false"));
				pdfPCell11.setBorder(Rectangle.NO_BORDER); 
				pdfPCell21.setBorder(Rectangle.NO_BORDER); 
				pdfPTable1.addCell(pdfPCell11);
				pdfPTable1.addCell(pdfPCell21);
				document.add(pdfPTable1);
			}
			document.close();

			System.out.println("Done");

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private boolean checkSum() {
		if (totalQuestionNumberText.getText() != null&& !totalQuestionNumberText.getText().isEmpty()&& !(totalQuestionNumberText.getText()=="")) {
			if (totalEnteredQuestions == Integer.valueOf(totalQuestionNumberText.getText()))
				return true;
		}
		return false;
	}

	private void clearAllEnteries() {
		categoryComboBox.setValue(null);
		subcategoryComboBox.setValue(null);
		difficultyComboBox.setValue(null);
		subcategoryQuestionNumberText.setText(null);
		subcategoryQuestionNumberText.setPromptText("enter no of questions here");
		typeComboBox.setValue(null);
		
	}

	public void handleSubcategorySelectionAction() {
		if (categoryComboBox.getValue() == null) {
			System.out.println("select category");
			Platform.runLater(() -> {
				errorLabelCategory.setVisible(true);
				errorLabelCategory.setText("*Please select a category");
			});

		}

	}

	public void entryCheck(KeyEvent event) {
		Pattern pattern = Pattern.compile(".*[^0-9].*");
		if(event.getCode()==KeyCode.ENTER)
		{	errorLabelConfirmButton.setVisible(false);
		System.out.println("enter!!");
		event.consume();
			return;
		}
		String input = event.getCharacter();
		System.out.println(input);
		if (totalQuestionNumberText.getText() == null) {
			Platform.runLater(() -> {
				errorLabelConfirmButton.setVisible(true);
			});
			return;
		}
		
		else if (pattern.matcher(input).matches() == true) {
			errorLabelConfirmButton.setVisible(true);
			errorLabelConfirmButton.setText(" *Only integer allowed");
			event.consume();
		}
         
		else if (pattern.matcher(input).matches() == false||(totalQuestionNumberText.getText()!=null&&Integer.valueOf(totalQuestionNumberText.getText())>0)) {
			Platform.runLater(() -> {
				errorLabelConfirmButton.setVisible(false);
			});
		}
		else if((totalQuestionNumberText.getText()!=null&&Integer.valueOf(totalQuestionNumberText.getText())==0))
			{
		errorLabelConfirmButton.setVisible(true);
		errorLabelConfirmButton.setText(" *Questions should be greather than zero");
		event.consume();
	}
		else
		{
			Platform.runLater(() -> {
				errorLabelConfirmButton.setVisible(false);
			});
	}
	}

	public void entryCheck2(KeyEvent event) {
		String input = event.getCharacter();
		if (subcategoryQuestionNumberText.getText() == null) {
			Platform.runLater(() -> {
				errorLabelCategory.setVisible(true);
				errorLabelCategory.setText(" *cant leave the field empty allowed");
			});
			return;
		}
		Pattern pattern = Pattern.compile(".*[^0-9].*");
		if (pattern.matcher(input).matches() == true) {
			errorLabelCategory.setVisible(true);
			errorLabelCategory.setText(" *Only integer allowed");
			event.consume();
		}

		if (pattern.matcher(input).matches() == false) {
			Platform.runLater(() -> {
				errorLabelCategory.setVisible(false);
			});
		}

	}
}
