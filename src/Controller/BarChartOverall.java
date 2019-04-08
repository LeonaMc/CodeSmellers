
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
			codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(index), reportEntry.getValue().getPercentage()));

			index++;
		}

 		smellChart.getData().addAll(codeSmellBar);
 		
 
 		
 		// CSS
// 		Node n = smellChart.lookup(".data0.chart-bar");
// 	    n.setStyle("-fx-bar-fill: red");
//
// 	    n = smellChart.lookup(".data1.chart-bar");
// 	    n.setStyle("-fx-bar-fill: orange");
//
// 	    n = smellChart.lookup(".data2.chart-bar");
// 	    n.setStyle("-fx-bar-fill: yellow");
 	    
// 	    n = smellChart.lookup(".data3.chart-bar");
// 	    n.setStyle("-fx-bar-fill: green");
//
// 	    n = smellChart.lookup(".data4.chart-bar");
// 	    n.setStyle("-fx-bar-fill: pink");
//
// 	    n = smellChart.lookup(".data5.chart-bar");
// 	    n.setStyle("-fx-bar-fill: purple");
//
// 	    n = smellChart.lookup(".data6.chart-bar");
// 	    n.setStyle("-fx-bar-fill: blue");
//
// 	    n = smellChart.lookup(".data7.chart-bar");
// 	    n.setStyle("-fx-bar-fill: orange");
//
// 	    n = smellChart.lookup(".data8.chart-bar");
// 	    n.setStyle("-fx-bar-fill: yellow");
//
// 	    n = smellChart.lookup(".data9.chart-bar");
// 	    n.setStyle("-fx-bar-fill: green");
//
// 	    n = smellChart.lookup(".data10.chart-bar");
// 	    n.setStyle("-fx-bar-fill: pink");
//
// 	    n = smellChart.lookup(".data11.chart-bar");
// 	    n.setStyle("-fx-bar-fill: purple");
//
// 	    n = smellChart.lookup(".data12.chart-bar");
// 	    n.setStyle("-fx-bar-fill: blue");
 	   
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
	  
