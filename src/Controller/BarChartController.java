// TODO: clean-up class
package Controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import CodeSmells.Report;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;


import Model.BarChartCalc;

public class BarChartController implements Initializable {

    @FXML
    private BarChart<?, ?> smellChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private TextFlow overAllAnalysisText;

    @FXML
    private TextFlow codeExplanationText;

    private BarChartCalc barChart = new BarChartCalc();

    private HashMap<String, Report> reportsHashMap = barChart.runInspection(); //gets the inspection reports

    private XYChart.Series codeSmellBarSeries = new XYChart.Series<>();

    public static NumberFormat formatter = new DecimalFormat("#0.00");

    public void showOverallAnalysisText() {
        overAllAnalysisText.setAccessibleText(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("BarChartControllerCalled");
        ArrayList<String> keysToSmellReports = new ArrayList<>(); // used to store the names of the smells, will be used to set XY names
        keysToSmellReports.add("FeatureEnvy");
        keysToSmellReports.add("LongMethod");
        keysToSmellReports.add("LongParameter");
        keysToSmellReports.add("LazyClass");
        keysToSmellReports.add("PrimitiveObsession");
        keysToSmellReports.add("TooManyLiterals");
        keysToSmellReports.add("LargeClass");
        
        HashMap<String,String> barColour = new HashMap<>();
        barColour.put("FeatureEnvy","green");
        barColour.put("LongMethod","lightcoral");
        barColour.put("LongParameter","orange");
        barColour.put("LazyClass","yellow");
        barColour.put("PrimitiveObsession","blue");
        barColour.put("TooManyLiterals","grey");
        barColour.put("LargeClass","crimson");

        int index = 0;
        for(String keyToSmellReport:keysToSmellReports){
            XYChart.Data<String, Number> data = new Data(keyToSmellReport, reportsHashMap.get(keyToSmellReport).getPercentage());
            //XYChart.Data<String, Number> data = new XYChart.Data(keysToSmellReports.get(index), 100); // use this line to see the bars for every smell regardless of project
            codeSmellBarSeries.getData().add(data);
            
            data.nodeProperty().addListener((ov, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: " +barColour.get(keyToSmellReport));
                }
            });
        }
        
        smellChart.getData().addAll(codeSmellBarSeries);

        //	Text t = new Text("Please click on bar for more information.\n");

        for (Series<?, ?> serie : smellChart.getData()) {
            for (Data<?, ?> item : serie.getData()) {
                item.getNode().setOnMousePressed((MouseEvent event) -> reportDataToGui(item.getXValue().toString()));
            }
        }
        Text setText = new Text("Click on a bar for more information on each code smell.");
        setText.setFill(Color.BLACK);
        setText.setFont(Font.font("Verdana", 12));
        codeExplanationText.getChildren().add(setText);
        setText.setTextAlignment(TextAlignment.CENTER);
        setText.setLineSpacing(20.0f);
    }
    
    private Report getReport(String reportKey){
       return reportsHashMap.get(reportKey);
    }

    private void reportDataToGui(String key){
        Text textOut = new Text("Name: " + key + "\nValue: "  + getReport(key).percentToString() + "\n");
        textOut.setFill(Color.BLACK);
        textOut.setFont(Font.font("Verdana", 12));
        overAllAnalysisText.getChildren().add(textOut);
        textOut.setTextAlignment(TextAlignment.CENTER);
        textOut.setLineSpacing(20.0f);
    }

    // TODO: move to ChangeSceneController -> figure out if scene can have two control classes
    public void goToProjectAnalysis(ActionEvent event) throws IOException {

        Parent root2 = FXMLLoader.load(getClass().getResource("/Model/ProjectAnalysis.fxml")); // Access scene information
        Scene scene = new Scene(root2);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); // allows us to get scene and window
        window.setScene(scene);
        window.show();
    }

    public void goToProjectAnalysis2(ActionEvent event) throws IOException {

        Parent root2 = FXMLLoader.load(getClass().getResource("/Model/ProjectAnalysis2.fxml")); // Access scene information
        Scene scene = new Scene(root2);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); // allows us to get scene and window
        window.setScene(scene);
        window.show();
    }

    public void goBackToProjectUploadScreen(ActionEvent event) throws IOException {

        // dont have access to stage information
        Parent root2 = FXMLLoader.load(getClass().getResource("/Model/ProjectUploadScreen3.fxml"));
        Scene scene = new Scene(root2);
        // This line gets the stage informations
        // Make the object of node type to be returned by getSource which allows us to get scene and window
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    public void goToInDepthAnalysis(ActionEvent event) throws IOException {

        Parent root2 = FXMLLoader.load(getClass().getResource("/Model/InDepthAnalysis.fxml")); // Access scene information
        Scene scene = new Scene(root2);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); // allows us to get scene and window
        window.setScene(scene);
        window.show();
    }


} 

