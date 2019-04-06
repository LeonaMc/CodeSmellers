
package CodeSmellers.Controller;
 

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

import CodeSmellers.*;
import CodeSmellers.Model.BarChartCalc;

public class BarChartOverall implements Initializable{
	
    @FXML
    private BarChart<?, ?> SmellChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;
    
    BarChartCalc bc = new BarChartCalc();

    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub


        XYChart.Series smell1 = new XYChart.Series<>();
        XYChart.Series smell2 = new XYChart.Series<>();
		XYChart.Series smell3 = new XYChart.Series<>();
		XYChart.Series smell4 = new XYChart.Series<>();
		XYChart.Series smell5 = new XYChart.Series<>();
		XYChart.Series smell6 = new XYChart.Series<>();
		XYChart.Series smell7 = new XYChart.Series<>();
		XYChart.Series smell8 = new XYChart.Series<>();
		XYChart.Series smell9 = new XYChart.Series<>();
		XYChart.Series smell10 = new XYChart.Series<>();
		XYChart.Series smell11 = new XYChart.Series<>();
		
		smell1.setName("Duplicate Code");
		smell2.setName("Bloated Code");
		smell3.setName("Feature Envy");
		smell4.setName("Inappropriate Intimacy");
		smell5.setName("Too Many Literals");
		smell6.setName("Primitive Obsessions");
		smell7.setName("God Complex");
		smell8.setName("Lazy Class/Freeloader");
		smell9.setName("Comments");
		smell10.setName("Switch Statements");
		smell11.setName("Data Class");

        
		smell1.getData().add(new XYChart.Data("Duplicate Code", bc.result()));
		smell2.getData().add(new XYChart.Data("Bloated Code", 20.00));
		smell3.getData().add(new XYChart.Data("Feature Envy", 30.00));
		smell4.getData().add(new XYChart.Data("Inappropriate Intimacy", 10.00));
		smell5.getData().add(new XYChart.Data("Too Many Literals", 20.00));
		smell6.getData().add(new XYChart.Data("Primitive Obsessions", 50.00));
		smell7.getData().add(new XYChart.Data("God Complex", 60.00));
		smell8.getData().add(new XYChart.Data("Lazy Class/Freeloader", 30.00));
		smell9.getData().add(new XYChart.Data("Comments", 10.00));
		smell10.getData().add(new XYChart.Data("Switch Statements", 2.00));
		smell11.getData().add(new XYChart.Data("Data", 1.00));
		
		SmellChart.getData().addAll(smell1, smell2, smell3, smell4, smell5, smell6, smell7, smell8, smell9, smell10, smell11);
		
}
		
		
	} 
	  
