
package Controller;
 

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

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


 		// 12 code smells
 	    List<String> codeSmells = Arrays.asList("Feature Envy", "Large Class", "Long Method", "Long Parametre",
 	    										"Duplicate Code", "Inappropriate Intimacy", "Too Many Literals", 
 	    										"Primitive Obsessions", "God Complex", "Lazy Class/Freeloader", 
 	    										"Comments", "Switch Statements", "Data Clump");
 	    
 	   codeSmellBar.setName(codeSmells.get(0));
 	   codeSmellBar.setName(codeSmells.get(1));
 	   codeSmellBar.setName(codeSmells.get(2));

		System.out.println("BarCharOverAllCalled");
 	   
 	    // Set name and percentage to bar chart
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(0), barChart.getResults().get(0)));
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(1), barChart.getResults().get(1)));
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(2), barChart.getResults().get(2)));
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(3), barChart.getResults().get(1)));
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(4), barChart.getResults().get(1)));
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(5), barChart.getResults().get(2)));
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(6), barChart.getResults().get(2)));
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(7), barChart.getResults().get(1)));
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(8), barChart.getResults().get(2)));
		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(9), barChart.getResults().get(2)));
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(10), barChart.getResults().get(2)));
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(11), barChart.getResults().get(1)));
 		codeSmellBar.getData().add(new XYChart.Data(codeSmells.get(12), barChart.getResults().get(1)));
 		
 		// Add data to bar chart
 		smellChart.getData().addAll(codeSmellBar);
 		
 
 		
 		// CSS
 		Node n = smellChart.lookup(".data0.chart-bar");
 	    n.setStyle("-fx-bar-fill: red");
 	    
 	    n = smellChart.lookup(".data1.chart-bar");
 	    n.setStyle("-fx-bar-fill: orange");
 	    
 	    n = smellChart.lookup(".data2.chart-bar");
 	    n.setStyle("-fx-bar-fill: yellow");
 	    
 	    n = smellChart.lookup(".data3.chart-bar");
 	    n.setStyle("-fx-bar-fill: green");
 	    
 	    n = smellChart.lookup(".data4.chart-bar");
 	    n.setStyle("-fx-bar-fill: pink");
 	    
 	    n = smellChart.lookup(".data5.chart-bar");
 	    n.setStyle("-fx-bar-fill: purple");
 	    
 	    n = smellChart.lookup(".data6.chart-bar");
 	    n.setStyle("-fx-bar-fill: blue");
 	    
 	    n = smellChart.lookup(".data7.chart-bar");
 	    n.setStyle("-fx-bar-fill: orange");
 	    
 	    n = smellChart.lookup(".data8.chart-bar");
 	    n.setStyle("-fx-bar-fill: yellow");
 	    
 	    n = smellChart.lookup(".data9.chart-bar");
 	    n.setStyle("-fx-bar-fill: green");
 	    
 	    n = smellChart.lookup(".data10.chart-bar");
 	    n.setStyle("-fx-bar-fill: pink");
 	    
 	    n = smellChart.lookup(".data11.chart-bar");
 	    n.setStyle("-fx-bar-fill: purple");
 	    
 	    n = smellChart.lookup(".data12.chart-bar");
 	    n.setStyle("-fx-bar-fill: blue");
 	   
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
	  
