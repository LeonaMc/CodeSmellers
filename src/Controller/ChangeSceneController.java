package Controller;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.*;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ChangeSceneController  { // implements Initializable
	 
	@FXML 
	private Label lbl;
	
	@FXML 
	private  TextField textField;
	private static String textPath; //used to store string from text field

	public void goToWelcomeScreen2(ActionEvent event) throws IOException {

		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/WelcomeScreen2.fxml"));
		Scene scene = new Scene(root2);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void goBackToWelcomeScreen(ActionEvent event) throws IOException {
		
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/WelcomeScreen.fxml"));
		Scene scene = new Scene(root2);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void goBackToWelcomeScreen2(ActionEvent event) throws IOException {
		
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/WelcomeScreen2.fxml"));
		Scene scene = new Scene(root2);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	} 
	
	public void goToProjectUploadScreen(ActionEvent event) throws IOException {
		
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/ProjectUploadScreen3.fxml"));
		Scene scene = new Scene(root2);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void goToProjectBarChartOverall(ActionEvent event) throws IOException {
		// if the text path is null then the user won't be able to click next, an alert will pop-up
		if(textPath == null) {
			String selection = null;
			Alert alert = new Alert(AlertType.ERROR, "You cannot continue until you select a folder.", ButtonType.OK);
			alert.showAndWait();
				if (alert.getResult() == ButtonType.OK) {
				    alert.close();				}		
		}else {
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/BarChartOverall.fxml"));
		Scene scene = new Scene(root2);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
		}
	} 

	public void openDirectoryChooser(ActionEvent event) {
		
        try { 
        	// set title for the stage 
    		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        	stage.setTitle("DirectoryChooser"); 
            // create a Directory chooser 
            DirectoryChooser dir_chooser = new DirectoryChooser(); 
            // get the file selected  
             File directory = dir_chooser.showDialog(stage);
     		if (directory != null) { 
                textField.setAccessibleText(directory.getAbsolutePath()); // set user directory path in text field
            // dir = directory.getAbsolutePath(); // TODO: use for directory in main
            // displays directory path that user has selected
			textField.setText(textField.getAccessibleText());
			textPath = textField.getAccessibleText();
     		}
             stage.show(); 
        }  
        catch (Exception e) { 
        	  textField.setText(e.getMessage()); 
        }
	}	
	
	// Methods to deal with hyperlinks for definitions 
	
	public void defineDuplicatedCode() throws IOException{
		String url_open ="https://refactoring.guru/smells/duplicate-code";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
	}
	
	public void defineBloatedCode() throws IOException{
		String url_open ="https://sourcemaking.com/refactoring/smells/bloaters";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
	}
	
	public void defineFeatureEnvy() throws IOException{
		String url_open ="https://refactoring.guru/smells/feature-envy";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
	}
	
	public void defineInappropriateIntimacy() throws IOException{
		String url_open ="https://refactoring.guru/smells/inappropriate-intimacy";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
	}
	
	public void defineTooManyLiterals() throws IOException{
		String url_open ="https://refactoring.guru/replace-magic-number-with-symbolic-constant";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
	}
	
	public void definePrimitiveObsessions() throws IOException{
		String url_open ="https://refactoring.guru/smells/primitive-obsession";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
	}
	
	public void defineGodComplex() throws IOException{
		String url_open ="https://sourcemaking.com/antipatterns/the-blob";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
	}
	
	public void defineLazyClass() throws IOException{
		String url_open ="https://refactoring.guru/smells/lazy-class";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
	}
	
	public void defineSwitchStatements() throws IOException{
		String url_open ="https://refactoring.guru/smells/switch-statements";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
	}
	
	public void defineComments() throws IOException{
		String url_open ="https://refactoring.guru/smells/comments";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
	}
	
	public void defineDataClumps() throws IOException{
		String url_open ="https://refactoring.guru/smells/data-clumps";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
	}
	
	// For returning the path of the folder
	
	public static String getTextPath(){
		return textPath;
	}
}
	

