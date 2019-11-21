package application.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.alert.alertMaker;
import application.database.databaseHelper;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class addCategoryController {
	private static databaseHelper handler = null;
	@FXML
	private JFXTextField categoryTextField;

	@FXML
	private JFXButton addCategoryButton;

	public void handleAddCategoryAction() throws Exception {
		if (categoryTextField.getText() != null && (categoryTextField.getText().length() > 1)) {
			handler.getInstance().addCategory(categoryTextField.getText().toLowerCase());
			categoryController c = adminDashboardController.categoryLoader.getController();
			c.initialize(null, null);
			alertMaker.showInfoMessage("Information", "Successfully added category ", "Success");
			Stage current = (Stage) categoryTextField.getScene().getWindow();
			current.close();
		} else {
			alertMaker.showErrorMessage("Cant leave Category name empty", "");
			return;
		}
	}

	public void handleEnterAction(KeyEvent e) throws Exception {

		if (e.getCode() == KeyCode.ENTER) {
			if (categoryTextField.getText() != null || categoryTextField.getText().length() > 1) {
				handler.getInstance().addCategory(categoryTextField.getText().toLowerCase());
				categoryController c = adminDashboardController.categoryLoader.getController();
				c.initialize(null, null);
				alertMaker.showInfoMessage("Information", "Successfully added category ", "Success");
				Stage current = (Stage) categoryTextField.getScene().getWindow();
				current.close();
			} else {
				alertMaker.showErrorMessage("Cant leave category name empty", "");
				return;
			}
		}
	}
}
