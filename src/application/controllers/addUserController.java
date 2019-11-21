package application.controllers;


import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.alert.alertMaker;
import application.database.databaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class addUserController implements Initializable {
 private static databaseHelper handler=null;
    @FXML
    private JFXTextField usernameText;

    @FXML
    private ComboBox<String> selectCategoryComboBox;

    @FXML
    private JFXButton addUserButton;
    
    public static ObservableList<String> categoriesList=FXCollections.observableArrayList();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
              initCategoriesList();
		
	}
	private void initCategoriesList() {
		categoriesList.clear();
			try {
				categoriesList.addAll(handler.getInstance().getAllCategories());
		selectCategoryComboBox.setItems(categoriesList);
			} catch (Exception e) {
	alertMaker.showErrorMessage("Error loading Data","");

	}
	}
	public void handleAddUserAction() throws Exception
	{
		if(usernameText.getText()==null ||selectCategoryComboBox.getValue()==null)//TODO
		{
			System.out.println("show alert");
			return;
			
		}
		
		String username=usernameText.getText().toLowerCase();
		handler.getInstance().addUser(username,selectCategoryComboBox.getValue().toLowerCase());
		alertMaker.showInfoMessage("success", "added user", "");
		usersController u= adminDashboardController.usersLoader.getController();
		u.initialize(null, null);
		Stage cur=(Stage) addUserButton.getScene().getWindow();
		cur.close();
	}

}
