package application.controllers;

import application.alert.alertMaker;
import application.database.*;
import application.model.questionsModel;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.fxml.Initializable;

public class categoryController implements Initializable {
	private databaseHelper handler = null;

	@FXML
	private MenuItem treeViewDeleteMenuItem;
	@FXML
	private Button deleteCategoryButton;
	@FXML
	private MenuItem treeViewEditMenuItem;
	@FXML
	private Button addCategoryButton;
	@FXML
	private CheckBox showDeletedCheckBox;
	@FXML
	private MenuItem treeViewUndeleteMenuItem;
	@FXML
	private Button addSubcategoryButton;
	private ObservableList<String> categoriesListTreeView = FXCollections.observableArrayList();
	@FXML
	private TreeView treeViewCategories;
	private TreeItem rootItem;
	@FXML
	private AnchorPane anchorPaneCatgeories;

	@FXML
	private Label categoryManagerLabel;

	@FXML
	private VBox vboxTools;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//resizeFunctionality();
		treeViewCategories.setPrefHeight(anchorPaneCatgeories.getPrefHeight()*.9);
		System.out.println("loading categroires>>>>>>>>>>>>>>");
		handler = databaseHelper.getInstance();
		rootItem = new TreeItem("Categories");// setting root of tree view
		treeViewCategories.setShowRoot(false);
		treeViewCategories.setRoot(rootItem);
		ArrayList<String> categories = new ArrayList<String>();
		getCategories getCategoriesTask = new getCategories();// used to get categories from database
		ExecutorService executorService = Executors.newFixedThreadPool(4);
		executorService.execute(getCategoriesTask);
		executorService.shutdown();// task gets executed
		getCategoriesTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				categories.addAll((getCategoriesTask.getValue()));
				System.out.println("printing array list  " + categories.toString());

			}
		});
		
		EventHandler<javafx.scene.input.MouseEvent> mouseEventHandle = (javafx.scene.input.MouseEvent event) -> {
		    handleMouseClicked(event);
		};
		treeViewCategories.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, mouseEventHandle);
	}

	private void resizeFunctionality() {
		double width1 =loginController.adminStage.getWidth();

		anchorPaneCatgeories.setPrefWidth(width1);
		anchorPaneCatgeories.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double width=(double) newValue;
				treeViewCategories.setPrefWidth(width*.75);
				vboxTools.setLayoutX(width*.78);
				vboxTools.setPrefWidth(width*.18);
				showDeletedCheckBox.setPrefWidth(width*.2);
				showDeletedCheckBox.setLayoutX(width*.55);
				categoryManagerLabel.setPrefWidth(width*.2);
				addCategoryButton.setPrefWidth(width*.18);
				addSubcategoryButton.setPrefWidth(width*.18);
				deleteCategoryButton.setPrefWidth(width*.18);
				categoryManagerLabel.setLayoutX(width*.3);

			}
		});
	}

	private void handleMouseClicked(javafx.scene.input.MouseEvent event) {
         TreeItem i= (TreeItem) treeViewCategories.getSelectionModel().getSelectedItem();
         
         if(i==null)
         return;
         System.out.println(i.getValue().toString());
         String selected =i.getValue().toString();
          if(selected!=null&&selected.length()>11&&selected.substring(selected.length()-10,selected.length()-1).equalsIgnoreCase("(deleted)"))
          {
        	  System.out.println("in delete");
    
       	   Platform.runLater(()->{
       		   deleteCategoryButton.setDisable(true);
       treeViewDeleteMenuItem.setVisible(false);
       treeViewEditMenuItem.setVisible(false);
       treeViewUndeleteMenuItem.setVisible(true);
       	  });
       	  
          }
          else
          {  Platform.runLater(()->{  deleteCategoryButton.setDisable(false);
          treeViewUndeleteMenuItem.setVisible(false);
          treeViewEditMenuItem.setVisible(false);
        	    treeViewDeleteMenuItem.setVisible(true);
        	    
       	   });
		
	}
	}

	private class getCategories extends Task<ArrayList<String>> {

		@Override
		protected ArrayList<String> call() throws Exception {

			ArrayList<String> allCategories = handler.getInstance().getCategories();
			for (int i = 0; i < allCategories.size(); i++) {
				String newCat = allCategories.get(i);
				System.out.println(newCat);
				TreeItem newitem = new TreeItem(newCat.toUpperCase());
				ArrayList<String> subCategoryList = handler.getInstance()
						.getSubcategoriesByCategoryId(handler.getInstance().getCategoryId(newCat));
				Platform.runLater(() -> {
					for (String string : subCategoryList) {
						newitem.getChildren().add(new TreeItem(string));
					}

					rootItem.getChildren().add(newitem);

				});
			}
			return allCategories;

		}
	}

	public void clearSelection() {
		treeViewCategories.getSelectionModel().clearSelection();
	}

	public void deleteEntryFromTreeView() {
		TreeItem entryToDelete = (TreeItem) treeViewCategories.getSelectionModel().getSelectedItem();
		if (entryToDelete == null) {
			alertMaker.showErrorMessage("Please select an item from the list", "");
			return;
		}
		if (alertMaker.showConformationMessage("Confirmation", "Are you sure you want to delete the item",
				"Press OK to continue") == false) {
			return;
		}

		System.out.println("entry to delete" + entryToDelete.isLeaf() + entryToDelete.toString()
				+ entryToDelete.getParent().getValue());
		if (!entryToDelete.isLeaf()
				|| entryToDelete.getParent().getValue().toString().equalsIgnoreCase(rootItem.getValue().toString())) {
			// for category
			try {
				handler.getInstance().deleteCategoryById(
						handler.getInstance().getCategoryId(entryToDelete.getValue().toString().toLowerCase()));
				handler.getInstance().deleteCategoryQuestions(
						handler.getInstance().getCategoryId(entryToDelete.getValue().toString().toLowerCase()));

			} catch (Exception e) {
				alertMaker.showErrorMessage("Can not delete your selected entry", "Failure");
				return;
			}
		} 
		else // for subcategory
		{

			try {
				handler.getInstance().deleteSubcategoryById(
						handler.getInstance().getSubcategoryId(entryToDelete.getValue().toString().toLowerCase()),
						handler.getInstance()
								.getCategoryId(entryToDelete.getParent().getValue().toString().toLowerCase()));
				handler.getInstance().deleteSubcategoryQuestion(
						handler.getInstance().getSubcategoryId(entryToDelete.getValue().toString().toLowerCase()));
			} catch (Exception e) {
				alertMaker.showErrorMessage("Can not delete your selected entry", "Failure");
				return;
			}

		}
		boolean remove = entryToDelete.getParent().getChildren().remove(entryToDelete);
		alertMaker.showInfoMessage("", "Succesfully deleted the selected entry", "");
		initialize(null,null);

	}

	public void handleAddCategoryAction(javafx.scene.input.MouseEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/application/Fxmls/addCategory.fxml"));
		primaryStage.setTitle("Question bank Internship Test System");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public void handleAddSubcategoryAction(javafx.scene.input.MouseEvent event) throws Exception {
		System.out.println("inside handle subcat");
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/application/Fxmls/addSubcategory.fxml"));
		primaryStage.setTitle("Question bank Internship Test System");
		primaryStage.setScene(new Scene(root));
		System.out.println("complete subcat");
		primaryStage.show();

	}

	public void handleShowDeletedAction() throws Exception {
		rootItem.getChildren().removeAll();
		treeViewCategories.getRoot().getChildren().clear();
		if (showDeletedCheckBox.isSelected()) {
			ArrayList<String> list = handler.getInstance().getAllDeletedCategories();
			for (int i = 0; i < list.size(); i++) {

				if (list.get(i)!=null&&handler.getInstance().isCategoryDeleted(handler.getCategoryId(list.get(i).toLowerCase()))) {
					String s = list.get(i);
					Platform.runLater(() -> {
						TreeItem t = new TreeItem(s.toUpperCase().concat(" (deleted) "));
						ArrayList<String> subCategoryList = handler.getInstance()
								.getDeletedOrUndeletedSubcategoriesByCategoryId(handler.getInstance().getCategoryId(s));
						for (String string : subCategoryList) {
							if (handler.isSubcategoryDeleted(handler.getSubcategoryId(string.toLowerCase())))
								t.getChildren().add(new TreeItem(string.concat(" (deleted) ")));
							else
								t.getChildren().add(new TreeItem(string));
						}

						rootItem.getChildren().add(t);
					});
				}

				else {
					String s = list.get(i);
					if(s!=null)
					{
					Platform.runLater(() -> {
						TreeItem t = new TreeItem(s.toUpperCase());
						ArrayList<String> subCategoryList = handler.getInstance()
								.getDeletedOrUndeletedSubcategoriesByCategoryId(
										handler.getInstance().getCategoryId(s.toLowerCase()));
						for (String string : subCategoryList) {
							if (handler.isSubcategoryDeleted(handler.getSubcategoryId(string.toLowerCase())))
								t.getChildren().add(new TreeItem(string.concat(" (deleted) ")));
							else {
								t.getChildren().add(new TreeItem(string));
							}
						}
						rootItem.getChildren().add(t);
					});
				}
			}
		}}
		if (!showDeletedCheckBox.isSelected()) {
			categoryController c = adminDashboardController.categoryLoader.getController();
			c.initialize(null, null);
		}

	}
	public void undeleteEntryFromTreeView() {
		TreeItem entryToUndelete = (TreeItem) treeViewCategories.getSelectionModel().getSelectedItem();
		if (entryToUndelete == null) {
			alertMaker.showErrorMessage("Please select an item from the list", "");
			return;
		}
		if (alertMaker.showConformationMessage("Confirmation", "Are you sure you want to undelete the item",
				"Press OK to continue") == false) {
			return;
		}

		System.out.println("entry to delete" + entryToUndelete.isLeaf() + entryToUndelete.toString()
				+ entryToUndelete.getParent().getValue());
		if (!entryToUndelete.isLeaf()
				|| entryToUndelete.getParent().getValue().toString().equalsIgnoreCase(rootItem.getValue().toString())) {
			// for category
			try {
				String s=(entryToUndelete.getValue().toString().toLowerCase());
				handler.getInstance().undeleteCategoryById(
						handler.getInstance().getCategoryId(s.substring(0,s.length()-11)));
				handler.getInstance().undeleteCategoryQuestions(
						handler.getInstance().getCategoryId(s.substring(0,s.length()-11)));
                  alertMaker.showInfoMessage("Success", "Succesfully undeleted ", "");
			} catch (Exception e) {
				e.printStackTrace();
				alertMaker.showErrorMessage("Can not undelete your selected entry", "Failure");
				return;
			}
		} 
		else // for subcategory
		{
                  
			try {
				String s=entryToUndelete.getValue().toString().toLowerCase();
				handler.getInstance().undeleteSubcategoryById(handler.getInstance().getSubcategoryId(s.substring(0,s.length()-11)),
						handler.getInstance()
								.getCategoryId(entryToUndelete.getParent().getValue().toString().toLowerCase()));
				handler.getInstance().undeleteSubcategoryQuestion(
						handler.getInstance().getSubcategoryId((s.substring(0,s.length()-11))));
			     alertMaker.showInfoMessage("Success", "Succesfully undeleted ", "");
			} catch (Exception e) {
				alertMaker.showErrorMessage("Can not Undelete your selected entry", "Failure");
				e.printStackTrace();
				return;
			}

		}
		showDeletedCheckBox.setSelected(false);
  categoryController c = adminDashboardController.categoryLoader.getController();
 c.initialize(null, null);
	}


	/*
	 * if(!(showDeletedCheckBox.isSelected())) { ArrayList<TreeItem> list = new
	 * ArrayList<TreeItem>(); list.addAll(rootItem.getChildren()); for(int i=0;i<
	 * list.size() ;i++) { TreeItem j=list.get(i); String s=
	 * j.getValue().toString(); System.out.println(j);
	 * if(s.lastIndexOf(" ")>0&&s.indexOf(" ")>0&&(s.substring(s.indexOf(" ")+1,
	 * s.lastIndexOf(" ")).equalsIgnoreCase("(deleted)"))) {
	 * Platform.runLater(()->j.getParent().getChildren().remove(j));
	 * System.out.println(s); } }
	 * 
	 * }
	 * 
	 * }
	 */

	/*
	 * private class getSubCategories extends Task <Void>{ private String
	 * categoryName; TreeItem item; public getSubCategories(String categoryname,
	 * TreeItem newitem) { categoryName=categoryname; item=newitem; }
	 * 
	 * @Override protected Void call() throws Exception { ArrayList<String>
	 * subCategoriesByCategory=
	 * handler.getInstance().getSubCategories(categoryName.toLowerCase());
	 * System.out.println("in  sub category task class");
	 * 
	 * Platform.runLater(() -> {
	 * System.out.println(subCategoriesByCategory.toString());
	 * item.getChildren().addAll(subCategoriesByCategory); });
	 * 
	 * return null; } }
	 */

}
