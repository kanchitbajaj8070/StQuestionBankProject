package application.alert;
	import java.io.PrintWriter;
	import java.io.StringWriter;
	import java.util.List;

import javafx.application.Platform;
import javafx.scene.Node;
	import javafx.scene.control.Alert.AlertType;
	import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
	import javafx.scene.control.Label;
	import javafx.scene.control.TextArea;
	import javafx.scene.effect.BoxBlur;
	import javafx.scene.input.MouseEvent;
	import javafx.scene.layout.GridPane;
	import javafx.scene.layout.Priority;
	import javafx.scene.layout.StackPane;
	import javafx.stage.Stage;
	import javax.imageio.ImageIO;

import application.controllers.loginController;
	

	public class alertMaker {

	    public static void showSimpleAlert(String title, String content) {
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(content);

	        alert.showAndWait();
	    }

	    public static void showErrorMessage(String title, String content) {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText(title);
	        alert.setContentText(content);
	        alert.showAndWait();
	    }

	    public static void showErrorMessage(Exception ex) {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error occured");
	        alert.setHeaderText("Error Occured");
	        alert.setContentText(ex.getLocalizedMessage());

	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        ex.printStackTrace(pw);
	        String exceptionText = sw.toString();

	        Label label = new Label("The exception stacktrace was:");

	        TextArea textArea = new TextArea(exceptionText);
	        textArea.setEditable(false);
	        textArea.setWrapText(true);

	        textArea.setMaxWidth(Double.MAX_VALUE);
	        textArea.setMaxHeight(Double.MAX_VALUE);
	        GridPane.setVgrow(textArea, Priority.ALWAYS);
	        GridPane.setHgrow(textArea, Priority.ALWAYS);

	        GridPane expContent = new GridPane();
	        expContent.setMaxWidth(Double.MAX_VALUE);
	        expContent.add(label, 0, 0);
	        expContent.add(textArea, 0, 1);

	        alert.getDialogPane().setExpandableContent(expContent);

	    
	        alert.showAndWait();
	    }

	    public static void showErrorMessage(Exception ex, String title, String content) {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error occured");
	        alert.setHeaderText(title);
	        alert.setContentText(content);

	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        ex.printStackTrace(pw);
	        String exceptionText = sw.toString();

	        Label label = new Label("The exception stacktrace was:");

	        TextArea textArea = new TextArea(exceptionText);
	        textArea.setEditable(false);
	        textArea.setWrapText(true);

	        textArea.setMaxWidth(Double.MAX_VALUE);
	        textArea.setMaxHeight(Double.MAX_VALUE);
	        GridPane.setVgrow(textArea, Priority.ALWAYS);
	        GridPane.setHgrow(textArea, Priority.ALWAYS);

	        GridPane expContent = new GridPane();
	        expContent.setMaxWidth(Double.MAX_VALUE);
	        expContent.add(label, 0, 0);
	        expContent.add(textArea, 0, 1);

	        alert.getDialogPane().setExpandableContent(expContent);
	        alert.showAndWait();
	    }
	    public static void showWarningMessage(String title, String header ,String content) {
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle(title);
	        alert.setHeaderText(header);
	        alert.setContentText(content);
	        alert.showAndWait();
	    }
	    static boolean ans=false;
	    public static boolean showConformationMessage(String title, String header ,String content) {
	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(header);
	        alert.setContentText(content);
		   alert.showAndWait().ifPresent(rs -> { 
			   if(rs == ButtonType.OK) {
		 	   		ans=true;}
			   else
			   {
				   ans=false;
			   }
			   
		   });
		   
		   return ans;
		   }
	    
	    public static void showInfoMessage(String title, String header ,String content) {
	        Platform.runLater(() -> {
	            Alert alert = new Alert(Alert.AlertType.INFORMATION);
	            alert.setTitle(title);
	            alert.setHeaderText(header);
	            alert.setContentText(content);
	            alert.showAndWait();
	        });
	    }

	}

