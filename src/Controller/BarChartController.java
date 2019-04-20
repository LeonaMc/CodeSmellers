// TODO: clean-up class
package Controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import CodeSmells.Report;
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

    private Series codeSmellBarSeries = new Series<>();

    public static NumberFormat formatter = new DecimalFormat("#0.00");

    public BarChartController() throws IOException {
    }

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
            Data<String, Number> data = new Data(keyToSmellReport +" "+ reportsHashMap.get(keyToSmellReport).percentToString(), reportsHashMap.get(keyToSmellReport).getPercentage());
            codeSmellBarSeries.getData().add(data);
            
            data.nodeProperty().addListener((ov, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: " +barColour.get(keyToSmellReport));
                }
            });
        }
        
        smellChart.getData().addAll(codeSmellBarSeries);
    }

    public void goBackToProjectUploadScreen(ActionEvent event) throws IOException {

        // dont have access to stage information
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/ProjectUploadScreen3.fxml"));
        root.setStyle("-fx-background-color: white");
        Scene scene = new Scene(root);
        // This line gets the stage informations
        // Make the object of node type to be returned by getSource which allows us to get scene and window
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    public void goToInDepthAnalysis(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/InDepthAnalysis.fxml")); // Access scene information
        root.setStyle("-fx-background-color: white");
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); // allows us to get scene and window
        window.setScene(scene);
        window.show();
    }


} 

