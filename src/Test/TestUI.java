//package Test;

//import Model.SplashScreen;
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;

//public class TestUI {// extends Application
//    // Setting up GUI
//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            Parent root = FXMLLoader.load(this.getClass().getResource("/Model/WelcomeScreen.fxml"));
//            primaryStage.setScene(new Scene(root));
//            primaryStage.show();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static void main(String[] args) throws InterruptedException {
//        // Splash Screen
//        SplashScreen splashScreen = new SplashScreen();
//        splashScreen.setVisible(true);
//        Thread thread = Thread.currentThread();
//        thread.sleep(2500);
//        splashScreen.dispose();

//        // Calling the Welcome Screen for GUI in main
//        launch(args);
//    }
//}