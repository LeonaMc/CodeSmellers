
package Controller;
 

import java.io.IOException;
import java.net.URL;
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

import Model.BarChartCalc;

public class BarChartOverall implements Initializable{
	
    @FXML
    private BarChart<?, ?> smellChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;
   
    BarChartCalc barChart = new BarChartCalc();
    
    

    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
    	
    	 XYChart.Series codeSmellBar = new XYChart.Series<>();

		System.out.println("BarCharOverAllCalled");


		HashMap<String, Report> barResults = barChart.getResults(); //gets the inspection reports
		ArrayList<String> codeSmells = new ArrayList<>(); // used to store the names of the smells, will be used to set XY names

		int index = 0;
		//Iterates over the reports, gets their name and their percentage and adds to XY Chart
		for(Map.Entry<String,Report> reportEntry : barResults.entrySet()){
			System.out.println("Name: "+reportEntry.getKey()+"  Value: "+ reportEntry.getValue().percentToString());
			codeSmells.add(reportEntry.getKey());
		    XYChart.Data<String, Number> data = new XYChart.Data(codeSmells.get(index), reportEntry.getValue().getPercentage());
			codeSmellBar.getData().add(data);	
		//	codeSmellBar.setName(reportEntry.getKey());
			
			data.nodeProperty().addListener(new ChangeListener<Node>() {
				  @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
				    if (newNode != null) {
				    	switch(reportEntry.getKey()) {
				    	  case "FeatureEnvy":
				    		  newNode.setStyle("-fx-bar-fill: red;");
				    	    break;
				    	  case "LongMethod":
				    		  newNode.setStyle("-fx-bar-fill: navy;");
				    	    break;
				    	  case "LongParameter":
				    		  newNode.setStyle("-fx-bar-fill: yellow;");
				    	    break;
				    	  case "LazyClass":
				    		  newNode.setStyle("-fx-bar-fill: pink;");
				    	    break;
				    	  case "PrimitiveObsession":
				    		  newNode.setStyle("-fx-bar-fill: green;");
				    	    break;
				    	  case "TooManyLiterals":
				    		  newNode.setStyle("-fx-bar-fill: purple;");
				    	    break;
				    	  case "LargeClass":
				    		  newNode.setStyle("-fx-bar-fill: firebrick;");
				    	    break;
				    	  default:
				    		  newNode.setStyle("-fx-bar-fill: orange;");
				    	} 
				    }
				  }
				}); 
			
			index++;
		
		}
		
		
		smellChart.getData().addAll(codeSmellBar); 
		
		

 		
 	   
}
    
	public void goToProjectAnalysis(ActionEvent event) throws IOException {
		
		// dont have access to stage information
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/ProjectAnalysis.fxml"));
		Scene scene = new Scene(root2);
		// This line gets the stage informations
		// Make the object of node type to be returned by getSource which allows us to get scene and window
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	} 
	
	public void goToProjectAnalysis2(ActionEvent event) throws IOException {
		
		// dont have access to stage information
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/ProjectAnalysis2.fxml"));
		Scene scene = new Scene(root2);
		// This line gets the stage informations
		// Make the object of node type to be returned by getSource which allows us to get scene and window
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	} 
		
} 
	  
