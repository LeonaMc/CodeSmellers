package CodeSmells;

import java.io.IOException;

import Model.SplashScreen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import java.io.IOException; 
import org.apache.pdfbox.pdmodel.PDDocument;

public class Main extends Application{ //  

    // Setting up GUI
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/Model/WelcomeScreen.fxml"));
           // root.setStyle("-fx-background-color: white");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    } 

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
    	
    	
        // Splash Screen
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.setVisible(true);
        Thread thread = Thread.currentThread();
        Thread.sleep(2500);
        splashScreen.dispose();
    	
        
       // Calling the Welcome Screen for GUI in main
        launch(args);
        
        

    }
}