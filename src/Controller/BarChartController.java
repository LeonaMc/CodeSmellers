
package Controller;
 
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

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
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.beans.value.*;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.Parent;
import javafx.scene.Scene;


import Model.BarChartCalc;
import Model.SplashScreen;

public class BarChartController implements Initializable{
	
    @FXML
    private BarChart<?, ?> smellChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;
   
    BarChartCalc barChart = new BarChartCalc();
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
        
    	 XYChart.Series codeSmellBarSeries = new XYChart.Series<>();

		System.out.println("BarChartControllerCalled");

		HashMap<String, Report> barResults = barChart.getResults(); //gets the inspection reports
		ArrayList<String> codeSmells = new ArrayList<>(); // used to store the names of the smells, will be used to set XY names

		int index = 0;
		//Iterates over the reports, gets their name and their percentage and adds to XY Chart
		for(Map.Entry<String,Report> reportEntry : barResults.entrySet()){
			System.out.println("Name: "+reportEntry.getKey()+"  Value: "+ reportEntry.getValue().percentToString());
			codeSmells.add(reportEntry.getKey());
		    XYChart.Data<String, Number> data = new XYChart.Data(codeSmells.get(index), reportEntry.getValue().getPercentage());
			codeSmellBarSeries.getData().add(data);	
		//	codeSmellBar.setName(reportEntry.getKey());
			
			// For changing individual colours 
			data.nodeProperty().addListener(new ChangeListener<Node>() {
				  @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
				    if (newNode != null) {
				    	switch(reportEntry.getKey()) {
				    	
				    	  case "FeatureEnvy":
				    		  newNode.setStyle("-fx-bar-fill: green;");
				    	    break;
				    	  case "LongMethod":
				    		  newNode.setStyle("-fx-bar-fill: lightcoral;");
				    	    break;
				    	  case "LongParameter":
				    		  newNode.setStyle("-fx-bar-fill: orange;");
				    	    break;
				    	  case "LazyClass":
				    		  newNode.setStyle("-fx-bar-fill: yellow;");
				    	    break;
				    	  case "PrimitiveObsession":
				    		  newNode.setStyle("-fx-bar-fill: blue;");
				    	    break;
				    	  case "TooManyLiterals":
				    		  newNode.setStyle("-fx-bar-fill: grey;");
				    	    break;
				    	  case "LargeClass":
				    		  newNode.setStyle("-fx-bar-fill: crimson;");
				    	    break;
				    	  default:
				    		  newNode.setStyle("-fx-bar-fill: navy;");
				    	} 
				    }
				  }
				}); 
			index++;
		}		
		
		smellChart.getData().addAll(codeSmellBarSeries);
		/**
		 * Attempt to give different label to different code smell, generate series for each label
		int size = 15;
		XYChart.Series<String, Number>[] seriesArray = Stream.<XYChart.Series<String, Number>>generate(XYChart.Series::new).limit(size).toArray(XYChart.Series[]::new);
		seriesArray[0] = codeSmellBarSeries;
		XYChart.Series codeSmellBarSeriesNaming = new XYChart.Series<>();
		for(int i = 1; i < seriesArray.length ; i++) {
			codeSmellBarSeriesNaming = new XYChart.Series<>();
			seriesArray[i] = codeSmellBarSeriesNaming;
		} 
		
		seriesArray[0].setName("Code Smell Name Tag");
		seriesArray[1].setName("Code Smell Name Tag");
		seriesArray[2].setName("Code Smell Name Tag"); **/
		
}
// TODO: move to ChangeSceneController -> figure out if scene can have two control classes
	public void goToProjectAnalysis(ActionEvent event) throws IOException {
		
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/ProjectAnalysis.fxml")); // Access scene information 
		Scene scene = new Scene(root2); 		
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow(); // allows us to get scene and window
		window.setScene(scene);
		window.show();
	} 
	
	public void goToProjectAnalysis2(ActionEvent event) throws IOException {
		
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/ProjectAnalysis2.fxml")); // Access scene information
		Scene scene = new Scene(root2);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow(); // allows us to get scene and window
		window.setScene(scene);
		window.show();
	} 
		
} 
	  
