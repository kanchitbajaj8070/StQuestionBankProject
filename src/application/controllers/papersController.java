package application.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.alert.alertMaker;
import application.database.databaseHelper;
import application.model.generatedPapersModel;
import application.model.questionsModel;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

public class papersController implements Initializable {
	public static FXMLLoader papersLoader = null;
	public static databaseHelper handler = null;
	public static ObservableList<generatedPapersModel> papersList = FXCollections.observableArrayList();
	@FXML
	private TableView<generatedPapersModel> generatedPaperTable;
	@FXML
	private AnchorPane anchorPanePapers;

	@FXML
	private StackPane stackPanePapers;

	@FXML
	private Label paperManagerLabel;
	@FXML
	private Label generatedPaperLabel;
	@FXML
	private TableColumn<generatedPapersModel, String> paperNameColumn;

	@FXML
	private TableColumn<generatedPapersModel, java.sql.Date> createdDateColumn;

	@FXML
	private TableColumn<generatedPapersModel, java.sql.Date> examDateColumn;

	@FXML
	private TableColumn<generatedPapersModel, String> collegeColumn;

	@FXML
	private TableColumn<generatedPapersModel, String> purposeColumn;

	@FXML
	private TableColumn<generatedPapersModel, String> createdByColumn;
	@FXML
	private TableColumn<generatedPapersModel, String> courseColumn;
	@FXML
	private JFXButton viewPaperButton;

	@FXML
	private JFXButton downloadPaperButton;

	@FXML
	private JFXButton downloadAnswerKeyButton;

	@FXML
	private JFXButton newPaperButton;

	@FXML
	void handleDownloadAnswerKeyAction() {
		generatedPapersModel rowToView = generatedPaperTable.getSelectionModel().getSelectedItem();

		if (rowToView == null) {
			alertMaker.showErrorMessage("Select a file please", "Try Again");
			return;
		}

		FileChooser fileChooser = new FileChooser();
		File fileSelected = fileChooser.showSaveDialog(newPaperButton.getScene().getWindow());

		if (fileSelected == null) {
			alertMaker.showErrorMessage("Error", "Please select a path to store the Pdf");
			return;

		}
		String path = fileSelected.getPath();
		if (path == null) {
			alertMaker.showErrorMessage("Error", "Please select a path to store the Pdf");
			return;

		}
		if (path.length() >= 4 && path.substring(path.length() - 3, path.length()).equalsIgnoreCase("pdf"))
			System.out.println("correct");
		else {
			path = path + ".pdf";
		}
		File file = new File("resources/pdfs/" + (rowToView.getPaperId()) + "answerkey.pdf");
		try {
			FileUtils.copyFile(file, new File(path));
			alertMaker.showInfoMessage("Success", "Succesfully downloaded your answer key  file", "");
		} catch (IOException e) {
			alertMaker.showErrorMessage("Error in downloading answer key file", "Please try again");
		}
	}

	public void clearSelection() {
		generatedPaperTable.getSelectionModel().clearSelection();

	}

	@FXML
	void handleDownloadPaperAction() {
		generatedPapersModel rowToView = generatedPaperTable.getSelectionModel().getSelectedItem();

		if (rowToView == null) {
			alertMaker.showErrorMessage("Select a file please", "Try Again");
			return;
		}

		FileChooser fileChooser = new FileChooser();
		File fileSelected = fileChooser.showSaveDialog(newPaperButton.getScene().getWindow());

		if (fileSelected == null) {
			alertMaker.showErrorMessage("Error", "Please select a path to store the Pdf");
			return;

		}
		String path = fileSelected.getPath();
		if (path == null) {
			alertMaker.showErrorMessage("Error", "Please select a path to store the Pdf");
			return;

		}
		if (path.length() >= 4 && path.substring(path.length() - 3, path.length()).equalsIgnoreCase("pdf"))
			System.out.println("correct");
		else {
			path = path + ".pdf";
		}
		File file = new File("resources/pdfs/" + (rowToView.getPaperId()) + ".pdf");
		try {
			FileUtils.copyFile(file, new File(path));
			alertMaker.showInfoMessage("Success", "Succesfully download your file", "");
		} catch (IOException e) {
			alertMaker.showErrorMessage("Error in downloading file", "Please try again");
		}

	}

	@FXML
	void handleMakePaperAction() {
		papersLoader = new FXMLLoader(getClass().getResource("/application/Fxmls/generatePaper.fxml"));
		Parent root = null;
		try {
			root = papersLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Stage papersStage = new Stage();
		Scene scene = new Scene(root);
		papersStage.setScene(scene);
		papersStage.setResizable(false);
		papersStage.initOwner(viewPaperButton.getScene().getWindow());
		papersStage.initModality(Modality.APPLICATION_MODAL);
		papersStage.show();
		System.out.println("closed papes");
		initialize(null, null);
	}

	@FXML
	void handleViewPaperAction() {

		generatedPapersModel rowToView = generatedPaperTable.getSelectionModel().getSelectedItem();
		if (rowToView == null) {
			alertMaker.showErrorMessage("Select a file please", "Try Again");
			return;
		}

		File file = new File("resources/pdfs/" + (rowToView.getPaperId()) + ".pdf");
		if (!Desktop.isDesktopSupported()) {
			System.out.println("Desktop is not supported");
			return;
		}

		Desktop desktop = Desktop.getDesktop();
		if (file.exists())
			try {
				desktop.open(file);
			} catch (IOException e) {
				alertMaker.showErrorMessage("File does not exist in the system", "");
			}
		else
		{
		
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//resizeFunctionality();
		double width =loginController.adminStage.getWidth();
		anchorPanePapers.setPrefWidth(width-10);
		stackPanePapers.setPrefWidth(width-10);
		double height =loginController.adminStage.getHeight();
		anchorPanePapers.setPrefHeight(height-10);
		stackPanePapers.setPrefHeight(height-10);
		newPaperButton.setLayoutX(width*.8);
		papersList.clear();
		paperNameColumn.setCellValueFactory(new PropertyValueFactory<generatedPapersModel, String>("paperName"));
		collegeColumn.setCellValueFactory(new PropertyValueFactory<generatedPapersModel, String>("college"));
		purposeColumn.setCellValueFactory(new PropertyValueFactory<generatedPapersModel, String>("purpose"));
		createdByColumn.setCellValueFactory(new PropertyValueFactory<generatedPapersModel, String>("createdBy"));
		createdDateColumn
				.setCellValueFactory(new PropertyValueFactory<generatedPapersModel, java.sql.Date>("createdDate"));
		examDateColumn.setCellValueFactory(new PropertyValueFactory<generatedPapersModel, java.sql.Date>("examDate"));
		courseColumn.setCellValueFactory(new PropertyValueFactory<generatedPapersModel, String>("course"));
		loadInitData();

		generatedPaperTable.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

			@Override
			public void handle(javafx.scene.input.MouseEvent mouseEvent) {

				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 2) {
						System.out.println("Double clicked");
						ActionEvent e = new ActionEvent();
						e.consume();
						try {
							handleViewPaperAction();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
	}
	@FXML
	private HBox hboxTools;

	private void resizeFunctionality() {
		double width =loginController.adminStage.getWidth();
		anchorPanePapers.setPrefWidth(width-10);
		anchorPanePapers.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double width=(double) newValue;
				paperManagerLabel.setPrefWidth(width*.3);
				paperManagerLabel.setLayoutX(width*.1);
				stackPanePapers.setPrefWidth(width*.95);
				generatedPaperTable.setPrefWidth(width*.95);
				generatedPaperLabel.setPrefWidth(width*.15);
			viewPaperButton.setPrefWidth(width*.17);
			downloadPaperButton.setPrefWidth(width*.17);
				downloadAnswerKeyButton.setPrefWidth(width*.17);
				newPaperButton.setPrefWidth(width*.17);
				hboxTools.setLayoutX(width*.35);
				hboxTools.setPrefWidth(width*.60);
				double widthTableColumns=width/7;
				collegeColumn.setPrefWidth(widthTableColumns);
				courseColumn.setPrefWidth(widthTableColumns);
				paperNameColumn.setPrefWidth(widthTableColumns);
				createdByColumn.setPrefWidth(widthTableColumns);
				createdDateColumn.setPrefWidth(widthTableColumns);
				examDateColumn.setPrefWidth(widthTableColumns);
				purposeColumn.setPrefWidth(width/8);

			}
		});
	}

	private void loadInitData()  {
			try {
			papersList.addAll( handler.getInstance().loadPapersData());
			generatedPaperTable.setItems(papersList);
			System.out.println("succesful loaded papers");
			}catch(Exception e)
	{
		alertMaker.showErrorMessage("error in loading papers data", "Failure ");

	}
}


}