// TODO: clean-up class
package Controller;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Stream;

import CodeSmells.DirectoryReader;
import CodeSmells.FeatureEnvy;
import CodeSmells.LargeClass;
import CodeSmells.LazyClass;
import CodeSmells.LongMethods;
import CodeSmells.LongParamList;
import CodeSmells.PrimitiveObsession;
import CodeSmells.Report;
import CodeSmells.TooManyLiterals;
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

import javafx.application.Application;
import javafx.beans.value.*;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import javafx.scene.Parent;
import javafx.scene.Scene;


import Model.BarChartCalc;
import Model.SplashScreen;
import Testt.TestSmellData;

public class BarChartController implements Initializable{
	
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
   
    BarChartCalc barChart = new BarChartCalc();
    
	HashMap<String, Report> barResults = barChart.getResults(); //gets the inspection reports
	
	XYChart.Series codeSmellBarSeries = new XYChart.Series<>();
    
	public static NumberFormat formatter = new DecimalFormat("#0.00");
	
	TestSmellData testSmellData = new TestSmellData();
	 
    public void showOverallAnalysisText() {
    	overAllAnalysisText.setAccessibleText(null);
    }
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
        
    	

		System.out.println("BarChartControllerCalled");
		ArrayList<String> codeSmells = new ArrayList<>(); // used to store the names of the smells, will be used to set XY names

		int index = 0;
		//Iterates over the reports, gets their name and their percentage and adds to XY Chart
		for(Map.Entry<String,Report> reportEntry : barResults.entrySet()){
			System.out.println("Name: "+reportEntry.getKey()+"  Value: "+ reportEntry.getValue().percentToString());
		/**	Text t = new Text("Name: "+reportEntry.getKey()+"  Value: "+ reportEntry.getValue().percentToString() + "\n");
			t.setFill(Color.BLACK);
			t.setFont(Font.font("Verdana", 12)); 
			overAllAnalysisText.getChildren().add(t); 
            t.setTextAlignment(TextAlignment.CENTER); 
            t.setLineSpacing(20.0f);  **/
		 
			codeSmells.add(reportEntry.getKey());
		    XYChart.Data<String, Number> data = new XYChart.Data(codeSmells.get(index), reportEntry.getValue().getPercentage());
		   // XYChart.Data<String, Number> data = new XYChart.Data(codeSmells.get(index), 100); // use this line to see the bars for every smell regardless of project
			codeSmellBarSeries.getData().add(data);	
			
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
		
	//	Text t = new Text("Please click on bar for more information.\n");
	
		 String newline = "\n";
         String[] packageArray = new String[2];
         DirectoryReader directoryReader = new DirectoryReader();
         String directoryPath = "C:\\Eclipse\\brickBracker3"; // Add path to root of directory here
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
         
         //Bloat Tests
         // Large Class Finished
         System.out.println("==========================TestSmellData Large Class==========================");
         LargeClass findLargeClasses = new LargeClass(javaSource, loadedClasses);
         findLargeClasses.findLargeFiles();
         findLargeClasses.reflectClass();
         Report largeClassReport = findLargeClasses.getReport();
         ArrayList<Class> largeClassAffectedClasses = largeClassReport.getAffectedClasses();
         largeClassReport.setPercentage(loadedClasses.size());
         System.out.println(largeClassReport.printSizeOfAffectedClasses());
         System.out.println(largeClassReport.percentToString() + " of files in project affected by Large Class code smell");
         if(largeClassReport.isClean()){
             System.out.println("Project is clean for Large Class code smell\n");
         }
         else{
             for (Class cls : largeClassAffectedClasses) {
                 System.out.println(largeClassReport.getReportData().get(cls));
             }
         }

	           //Long Methods Finished
	           System.out.println("==========================TestSmellData Long Method==========================");
	           LongMethods findLongMethods = new LongMethods(loadedClasses, javaSource);
	           findLongMethods.reflectClass();
	           Report longMethodReport = findLongMethods.getReport();
	           longMethodReport.setPercentage(loadedClasses.size());
	           ArrayList<Class> longMethodEffectedClasses = longMethodReport.getAffectedClasses();
	           System.out.println(longMethodReport.printSizeOfAffectedClasses());
	           System.out.println(longMethodReport.percentToString() + " of files in project affected by Long Method code smell\n");
	           if (longMethodReport.isClean()){
	               System.out.println("Project is clean for Long Method code smell\n");
	           }
	           else{
	               for (Class cls : longMethodEffectedClasses) {
	                   System.out.println(longMethodReport.getLongMethodData().get(longMethodReport.getReportData().get(cls)));
	                   System.out.print(newline);
	               }
	           }

	           //Primitive Obsession Finished
	           System.out.println("==========================TestSmellData Primitive Obsession==========================");
	           PrimitiveObsession primitiveObsession = new PrimitiveObsession(loadedClasses);
	           primitiveObsession.reflectClass();
	           Report primitiveReport = primitiveObsession.getReport();
	           ArrayList<Class> primitiveAffectedClasses = primitiveReport.getAffectedClasses();
	           System.out.println("Number of affected classes = " + primitiveAffectedClasses.size());
	           primitiveReport.setPercentage(loadedClasses.size());
	           System.out.println(primitiveReport.percentToString() + " of files in project affected by primitive obsession code smell\n");
	           if(primitiveReport.isClean()){
	               System.out.println("Project is clean for Primitive Obsession code smell\n");
	           }
	           else{
	               for (Class cls : primitiveAffectedClasses) {
	                   System.out.println(primitiveReport.getReportData().get(cls) +newline);
	               }
	           }

	           //Long Param List Finished
	           System.out.println("==========================TestSmellData Long Parameter List==========================");
	           LongParamList longParamList = new LongParamList(loadedClasses);
	           longParamList.reflectClass();
	           Report longParamReport = longParamList.getReport();
	           ArrayList<Class> longParamAffectedClasses = longParamReport.getAffectedClasses();
	           System.out.println(longParamReport.printSizeOfAffectedClasses());
	           longParamReport.setPercentage(loadedClasses.size());
	           System.out.println(longParamReport.percentToString() + " of files in project affected by long parameter code smell\n");
	           if (longParamReport.isClean()){
	               System.out.println("Project is clean for Long Parameter List code smell\n");
	           }
	           else{
	               for(Class cls : longParamAffectedClasses){
	                   System.out.print(longParamReport.getReportData().get(cls));
	               }
	           }

	           //Too Many Literals Finished
	           System.out.println("==========================TestSmellData Too Many Literals==========================");
	           TooManyLiterals tooManyLiterals = new TooManyLiterals(loadedClasses);
	           tooManyLiterals.reflectClass();
	           Report literalReport = tooManyLiterals.getReport();
	           System.out.println(literalReport.printSizeOfAffectedClasses());
	           literalReport.setPercentage(loadedClasses.size());
	           ArrayList<Class> literalAffectedClasses = literalReport.getAffectedClasses();
	           System.out.println(literalReport.percentToString() + " of files affected by TooManyLiterals code smell\n");
	           if(literalReport.isClean()){
	               System.out.println("Project is clean for Too Many Literal code smell\n");
	           }
	           else{
	               for (Class cls: literalAffectedClasses){
	                   System.out.println(literalReport.getReportData().get(cls));
	               }
	           }

	           //Lazy Class Finished
	           System.out.println("=============================TestSmellData Lazy Class=============================");
	           LazyClass lazyClass = new LazyClass(loadedClasses, javaSource);
	           lazyClass.reflectClass();
	           Report lazyClassReport = lazyClass.getReport();
	           ArrayList<Class> lazyAffectedClasses = lazyClassReport.getAffectedClasses();
	           System.out.println("Number of affected classes = " + lazyAffectedClasses.size());
	           lazyClassReport.setPercentage(loadedClasses.size());
	           System.out.println(lazyClassReport.percentToString() + " of files in project are affected\n");
	           if(largeClassReport.isClean()){
	               System.out.println("Project is clean for Lazy Class code smell\n");
	           }
	           else{
	               for (Class affectedClass : lazyAffectedClasses) {
	                   System.out.println("Affected class name = " + affectedClass.getSimpleName());
	                   System.out.println(lazyClassReport.getReportData().get(affectedClass));
	               }
	           }

	           //Feature Envy Finished
	           System.out.println("==========================TestSmellData Feature Envy==========================");
	           FeatureEnvy featureEnvy = new FeatureEnvy(javaSource,loadedClasses);
	           featureEnvy.formatData();
	           Report featureReport = featureEnvy.returnReport();
	           featureReport.printSizeOfAffectedClasses();
	           featureReport.setPercentage(loadedClasses.size());
	           System.out.println(featureReport.percentToString() + " of files in project are affected\n");
	           ArrayList<Class> envyClasses = featureReport.getAffectedClasses();
	           if(featureReport.isClean()){
	               System.out.println("Project is clean for Feature Envy code smell\n");
	           }
	           else{
	               for (Class cls: envyClasses){
	                   System.out.println(featureReport.getReportData().get(cls) + newline);
	               }
	           }
		
	       for (Series<?, ?> serie: smellChart.getData()){
	            for (Data<?, ?> item: serie.getData()){
	                item.getNode().setOnMousePressed((MouseEvent event) -> {
	                	
				    	switch(item.getXValue().toString()) {
				    	
				    	  case "FeatureEnvy":
				    		  
			                	Text t = new Text("Name: "+ item.getXValue() +"\nValue: "+ formatter.format(item.getYValue()) + "\n" + featureReport.printSizeOfAffectedClasses() + "\n" + featureReport.percentToString() + " of files in project affected by Large Class code smell\n\n");      		            		
			                	t.setFill(Color.BLACK);
			        			t.setFont(Font.font("Verdana", 12)); 
			        			overAllAnalysisText.getChildren().add(t); 
			                    t.setTextAlignment(TextAlignment.CENTER); 
			                    t.setLineSpacing(20.0f);
			                    
			     	           if(largeClassReport.isClean()){
			     	        	  t = new Text("Project is clean for Lazy Class code smell\n");
			    	           }
			    	           else{
			    	               for (Class affectedClass : envyClasses) {
			    	            	   t = new Text("Affected class name = " + affectedClass.getSimpleName());
			    	            	   t = new Text(featureReport.getReportData().get(affectedClass).toString());
			    	               }
			    	           }
				    		  
				    	    break;
				    	  case "LongMethod":
				    		  
			                	 t = new Text("Name: "+ item.getXValue() +"\nValue: "+ formatter.format(item.getYValue()) + "\n" + longMethodReport.printSizeOfAffectedClasses() + "\n" + longMethodReport.percentToString() + " of files in project affected by Large Class code smell\n\n");       
			            		t.setFill(Color.BLACK);
			        			t.setFont(Font.font("Verdana", 12)); 
			        			overAllAnalysisText.getChildren().add(t); 
			                    t.setTextAlignment(TextAlignment.CENTER); 
			                    t.setLineSpacing(20.0f);
			                    
			     	           if(largeClassReport.isClean()){
			    	               t = new Text("Project is clean for Lazy Class code smell\n");
			    	           }
			    	           else{
			    	               for (Class affectedClass : longMethodEffectedClasses) {
			    	                   t = new Text("Affected class name = " + affectedClass.getSimpleName());
			    	                   t = new Text(longMethodReport.getReportData().get(affectedClass).toString());
			    	               }
			    	           }

				    		  
				    	    break;
				    	  case "LongParameter":
				    		  
			                    t = new Text("Name: "+ item.getXValue() +"\nValue: "+ formatter.format(item.getYValue()) + "\n" + longParamReport.printSizeOfAffectedClasses() + "\n" + longParamReport.percentToString() + " of files in project affected by Large Class code smell\n\n");       
			            		t.setFill(Color.BLACK);
			        			t.setFont(Font.font("Verdana", 12)); 
			        			overAllAnalysisText.getChildren().add(t); 
			                    t.setTextAlignment(TextAlignment.CENTER); 
			                    t.setLineSpacing(20.0f);
			                    
			     	           if(largeClassReport.isClean()){
			    	               t = new Text("Project is clean for Lazy Class code smell\n");
			    	           }
			    	           else{
			    	               for (Class affectedClass : longParamAffectedClasses) {
			    	                   t = new Text("Affected class name = " + affectedClass.getSimpleName());
			    	                   t = new Text(longParamReport.getReportData().get(affectedClass).toString());
			    	               }
			    	           }
			     	           
				    	    break;
				    	  case "LazyClass":
				    		  
			                    t = new Text("Name: "+ item.getXValue() +"\nValue: "+ formatter.format(item.getYValue()) + "\n" + lazyClassReport.printSizeOfAffectedClasses() + "\n" + lazyClassReport.percentToString() + " of files in project affected by Large Class code smell\n\n");       
			            		t.setFill(Color.BLACK);
			        			t.setFont(Font.font("Verdana", 12)); 
			        			overAllAnalysisText.getChildren().add(t); 
			                    t.setTextAlignment(TextAlignment.CENTER); 
			                    t.setLineSpacing(20.0f);
			                    
			     	           if(largeClassReport.isClean()){
			    	               t = new Text("Project is clean for Lazy Class code smell\n");
			    	           }
			    	           else{
			    	               for (Class affectedClass : lazyAffectedClasses) {
			    	                   t = new Text("Affected class name = " + affectedClass.getSimpleName());
			    	                   t = new Text(lazyClassReport.getReportData().get(affectedClass).toString());
			    	               }
			    	           }
			                    
				    	    break;
				    	  case "PrimitiveObsession":
				    		  
			                	 t = new Text("Name: "+ item.getXValue() +"\nValue: "+ formatter.format(item.getYValue()) + "\n\n" + primitiveReport.printSizeOfAffectedClasses() + "\n" + primitiveReport.percentToString() + " of files in project affected by Large Class code smell\n\n");       
			            		t.setFill(Color.BLACK);
			        			t.setFont(Font.font("Verdana", 12)); 
			        			overAllAnalysisText.getChildren().add(t); 
			                    t.setTextAlignment(TextAlignment.CENTER); 
			                    t.setLineSpacing(20.0f);
			                    
			     	           if(largeClassReport.isClean()){
			    	               t = new Text("Project is clean for Lazy Class code smell\n");
			    	           }
			    	           else{
			    	               for (Class affectedClass : primitiveAffectedClasses) {
			    	                   t = new Text("Affected class name = " + affectedClass.getSimpleName());
			    	                   t = new Text(primitiveReport.getReportData().get(affectedClass).toString());
			    	               }
			    	           }
			                    
				    	    break;
				    	  case "TooManyLiterals":
				    		  
			                	t = new Text("Name: "+ item.getXValue() +"\nValue: "+ formatter.format(item.getYValue()) + "\n" + literalReport.printSizeOfAffectedClasses() + "\n" + literalReport.percentToString() + " of files in project affected by Large Class code smell\n\n");       
			            		t.setFill(Color.BLACK);
			        			t.setFont(Font.font("Verdana", 12)); 
			        			overAllAnalysisText.getChildren().add(t); 
			                    t.setTextAlignment(TextAlignment.CENTER); 
			                    t.setLineSpacing(20.0f);
			                    
			     	           if(largeClassReport.isClean()){
			    	               t = new Text("Project is clean for Lazy Class code smell\n");
			    	           }
			    	           else{
			    	               for (Class affectedClass : literalAffectedClasses) {
			    	                   t = new Text("Affected class name = " + affectedClass.getSimpleName());
			    	                   t = new Text(literalReport.getReportData().get(affectedClass).toString());
			    	               }
			    	           }
			                    
				    	    break;
				    	  case "LargeClass":
				    		  
			                    t = new Text("Name: "+ item.getXValue() +"\nValue: "+ formatter.format(item.getYValue()) + "\n" + largeClassReport.printSizeOfAffectedClasses() + "\n" + largeClassReport.percentToString() + " of files in project affected by Large Class code smell\n\n");       
			            		t.setFill(Color.BLACK);
			        			t.setFont(Font.font("Verdana", 12)); 
			        			overAllAnalysisText.getChildren().add(t); 
			                    t.setTextAlignment(TextAlignment.CENTER); 
			                    t.setLineSpacing(20.0f);
			                    
			     	           if(largeClassReport.isClean()){
			    	               t = new Text("Project is clean for Lazy Class code smell\n");
			    	           }
			    	           else{
			    	               for (Class affectedClass : largeClassAffectedClasses) {
			    	                   t = new Text("Affected class name = " + affectedClass.getSimpleName());
			    	                   t = new Text(largeClassReport.getReportData().get(affectedClass).toString());
			    	               }
			    	           }
			                    
				    	    break;
				    	  default:
				    	} 
	                });         
	            }
	        }

	   	
		/**
		 * for making bar chart interactive
        for (Series<?, ?> serie: smellChart.getData()){
            for (Data<?, ?> item: serie.getData()){
                item.getNode().setOnMousePressed((MouseEvent event) -> {
                    System.out.println("you clicked "+item.toString()+serie.toString());
                });
            }
        } **/
		
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
	
	public void goBackToProjectUploadScreen(ActionEvent event) throws IOException {
		
		// dont have access to stage information
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/ProjectUploadScreen3.fxml"));
		Scene scene = new Scene(root2);
		// This line gets the stage informations
		// Make the object of node type to be returned by getSource which allows us to get scene and window
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void goToDeeperAnalysis(ActionEvent event) throws IOException {
		
		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/DeeperAnalysis.fxml"));
		Scene scene = new Scene(root2);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	} 

		
} 
	  
