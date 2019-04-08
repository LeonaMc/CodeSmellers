package Controller;

// TODO: create CodeSmellers.Controller package and place this class in it
// To change to the next scene 

//import gui.model;
//import gui.view;
//import gui.controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.*;
import javafx.scene.control.TextField;

public class ChangeSceneController  { // implements Initializable
	 
	@FXML 
	private Label lbl;
	
	@FXML 
	private  TextArea textArea;



	@FXML 
	private  TextField textField;
	private static String textPath; //used to store string from text field,



	public ChangeSceneController(){

	}

    // directory
	
//	private static String dir;
	//DirectoryReader directoryReader = new DirectoryReader();
	
	// Changes welcome screen to screen 2 
	// Want to be able to read from button to get scene, once read
	// from scene we can get the stage 
	public void goToWelcomeScreen(ActionEvent event) throws IOException {
		
		// dont have access to stage information
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/WelcomeScreen2.fxml"));
		Scene scene = new Scene(root2);
		// This line gets the stage informations
		// Make the object of node type to be returned by getSource which allows us to get scene and window
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void goToProjectBarChartOverall(ActionEvent event) throws IOException {
		
		// dont have access to stage information
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/BarChartOverall.fxml"));
		Scene scene = new Scene(root2);
		// This line gets the stage informations
		// Make the object of node type to be returned by getSource which allows us to get scene and window
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	} 
	
	public void goBackToWelomeScreen(ActionEvent event) throws IOException {
		
		// dont have access to stage information
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/WelcomeScreen.fxml"));
		Scene scene = new Scene(root2);
		// This line gets the stage informations
		// Make the object of node type to be returned by getSource which allows us to get scene and window
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void goToProjectUploadScreen(ActionEvent event) throws IOException {
		
		// dont have access to stage information
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/ProjectUploadScreen3.fxml"));
		Scene scene = new Scene(root2);
		// This line gets the stage informations
		// Make the object of node type to be returned by getSource which allows us to get scene and window
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
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
          //      dir = directory.getAbsolutePath(); // TODO: use for directory in main




//				displays directory path that user has selected
			textField.setText(textField.getAccessibleText());
			textPath = textField.getAccessibleText();





     		}
             stage.show(); 
        }  
        catch (Exception e) { 
        	  textField.setText(e.getMessage()); 
        }
	}
	
	public void getDirectoryPath() {
  //      String[] packageArray = new String[2];
   //     DirectoryReader directoryReader = new DirectoryReader();
    //    String directoryPath = null; // Add path to root of directory here
   //     String directoryPath = "C:\\Eclipse\\SoftwareEngineering3\\src"; 
       // String directoryPath = openDirectoryChooser(event);
        /**
        String dir = textField.getText();
        
        if(textField != null) {

        
        System.out.println(dir);
        
        }{
        	System.out.println("Nope");
        } **/
        
     
        /**

        directoryReader.getFiles(directoryPath);
        
        if(directoryReader.getDirectoryLevel() > 0){
            packageArray = directoryReader.getClasspath(directoryReader.getClassArrayList().get(0).getPath());
        }
        else{
            packageArray[0] = null;
            packageArray[1] = directoryPath;
        }

        try {
            directoryReader.loadClasses(packageArray);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Class> loadedClasses = new ArrayList<>(directoryReader.getLoadedClasses()); // classes ready for reflection
        ArrayList<File> javaSource = new ArrayList<>(directoryReader.getJavaSourceArrayList()); // can read java files as text

        textArea.setText("List of classes: \n");
        // prints list of loaded classes
        for(Class cls : loadedClasses){
            System.out.println(cls.getName());
       //     textArea.setText(cls.getName());
            textArea.appendText(cls.getName() + "\n");
        }
        
        textArea.appendText("List of java sources: \n");
        // prints list of java files
        for (File file : javaSource){
            System.out.println(file.getName());
          //  textArea.setText(file.getName());
            textArea.selectNextWord();
            textArea.appendText(file.getName() + "\n"); 
        } **/
	}
	
	public void lazyImageClicked(ActionEvent event) throws IOException {
		
		// dont have access to stage information
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/WelcomeScreen2.fxml"));
		Scene scene = new Scene(root2);
		// This line gets the stage informations
		// Make the object of node type to be returned by getSource which allows us to get scene and window
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	} 
		
	
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



	public static String getTextPath(){
		return textPath;
	}


}
	

