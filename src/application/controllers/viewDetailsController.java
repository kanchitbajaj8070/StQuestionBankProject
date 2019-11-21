package application.controllers;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.util.ResourceBundle;

import application.alert.alertMaker;
import application.database.databaseHelper;
import application.model.questionsAllDetials;
import application.model.questionsModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
	public class viewDetailsController implements Initializable {
 private static databaseHelper handler=null;
	    @FXML
	    private TextArea questionTextArea;

	    @FXML
	    private TextField questionCategory;

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
	    private TextField questionTimeAssigned;
	     private static  int  qidRowToView=-1;
	 	@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
	 		 qidRowToView=-1;
		viewRowFromQuestionTable();
			
		}
  
	    public void viewImageAction() throws Exception {
			System.out.println(qidRowToView);
			ImageView imgView = new ImageView();
			questionsAllDetials obj = handler.getInstance().getAllDetialsOfQuestionByQuestionId(qidRowToView);
			Blob blob = obj.getImage();
			if (blob == null) {

				alertMaker.showInfoMessage("", "No image for this question", "");
				return;
			}
			InputStream input = new ByteArrayInputStream(blob.getBytes(1, (int) blob.length()));
			Image imge = new Image(input);
			imgView.setImage(imge);
			StackPane pane = new StackPane();
			pane.getChildren().add(imgView);
			Scene s = new Scene(pane, imge.getWidth(), imge.getHeight());
			Stage s1 = new Stage();
			s1.setScene(s);
			s1.setResizable(true);
			s1.initStyle(StageStyle.UNIFIED);
			s1.initModality(Modality.APPLICATION_MODAL);
			s1.show();

		}
	    public void viewRowFromQuestionTable() {
	        questionsController c= adminDashboardController.questionsloader.getController();
			questionsModel rowToView = (questionsModel) c.getTable().getSelectionModel().getSelectedItem();
			int id = rowToView.getId();
			qidRowToView=id;
			try
			{
				application.model.questionsAllDetials obj = handler.getInstance().getAllDetialsOfQuestionByQuestionId(id);
				if (obj == null)
					System.out.println("0null value of obj");
				questionTextArea.setText(obj.getQuestionText());

				questionCategory.setText(handler.getInstance().getQuestionCategoryName(obj.getCategoryid()));

				questionSubcategory.setText(handler.getInstance().getQuestionSubcategoryName(obj.getSubcategoryid()));

				questionType.setText(handler.getInstance().getQuestionTypeName(obj.getType_id()));

				questionDifficulty.setText(handler.getInstance().getDifficultyLevel(Integer.valueOf(obj.getDifficulty())));
				questionMarks.setText(String.valueOf(obj.getMarks()));
				questionAddedby.setText(obj.getAddedBy());
				if (obj.getFirstChoice() != null)
					questionChoice1.setText(obj.getFirstChoice());
				else
					questionChoice1.setText("Choice Not available");
				if (obj.getSecondChoice() != null)
					questionChoice2.setText(obj.getSecondChoice());
				else
					questionChoice2.setText("Choice Not available");
				if (obj.getThirdChoice() != null)
					questionChoice3.setText(obj.getThirdChoice());
				else
					questionChoice3.setText("Choice Not available");
				if (obj.getFourthChoice() != null)
					questionChoice4.setText(obj.getFourthChoice());
				else
					questionChoice4.setText("Choice Not available");
				if (obj.getType_id() == 1) {
					if (obj.getCorrectAnswer() == 1)
						questionCorrectAnswer.setText("First option");
					if (obj.getCorrectAnswer() == 2)
						questionCorrectAnswer.setText("Second option");
					if (obj.getCorrectAnswer() == 3)
						questionCorrectAnswer.setText("Third Option");
					if (obj.getCorrectAnswer() == 4)
						questionCorrectAnswer.setText("Fourth option");

				}
				if (obj.getType_id() == 2)
					questionCorrectAnswer.setText("No answer for subjective questions ");
				if (obj.getType_id() == 3) {
					if (obj.getCorrectAnswer() == 1)
						questionCorrectAnswer.setText("true");
					else
						questionCorrectAnswer.setText("false");

				}
				questionTimeAssigned.setText(String.valueOf(obj.getTimeAssigned()) + " minutes");

			} catch (Exception e) {
				e.printStackTrace();

			}

		}
	

	}

