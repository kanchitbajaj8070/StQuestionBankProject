package application.controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class addSubcategoryController implements Initializable{
   private static databaseHelper handler=null;
    @FXML
    private JFXTextField subcategoryTextField;

    @FXML
    private JFXButton addSubcategoryButton;

    @FXML
    private ComboBox<String> categoryComboBox;
    public static ObservableList<String> categoriesList=FXCollections.observableArrayList();
 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		categoriesList.clear();
		try
		{
			categoriesList.addAll(handler.getInstance().getAllCategories());
		categoryComboBox.setItems(categoriesList);
		}catch (Exception e) {
			alertMaker.showErrorMessage("error loading data", "");
		}
		}
    public void handleAddSubcategoryAction() throws Exception
    {
    	if(categoryComboBox.getValue()==null)
    	{ alertMaker.showErrorMessage("Select a category from drop down box", "");
    	    return;}
    	if(subcategoryTextField.getText()!=null)
    	{
    		handler.getInstance().addSubcategory(subcategoryTextField.getText().toLowerCase(), categoryComboBox.getValue().toLowerCase());
    		categoryController c= adminDashboardController.categoryLoader.getController();
       	 c.initialize(null,null);
       	alertMaker.showInfoMessage("Information","Successfully added Subcategory " ,"Success");
    		Stage current=(Stage) subcategoryTextField.getScene().getWindow();
        	current.close();                                           
    	
    	
    }}
    public void handleEnterAction(KeyEvent e) throws Exception
    {
    	
    	if(e.getCode()==KeyCode.ENTER)
    	{
    		if(categoryComboBox.getValue()==null)
        	{
    			alertMaker.showErrorMessage("Select a category from drop down box", "");
    			return;
        	}
        	if(subcategoryTextField.getText()!=null)
        	{
        		handler.getInstance().addSubcategory(subcategoryTextField.getText().toLowerCase(), categoryComboBox.getValue().toLowerCase());
        		categoryController c= adminDashboardController.categoryLoader.getController();
              	 c.initialize(null,null);
              	alertMaker.showInfoMessage("Information","Successfully added Subcategory " ,"Success");
           		Stage current=(Stage) subcategoryTextField.getScene().getWindow();
               	current.close();  
        	
        }
    	}
    }
	
	}


