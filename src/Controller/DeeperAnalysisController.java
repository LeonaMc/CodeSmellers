// TODO: when String leads to an invalid path, produce error message
package Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import CodeSmells.Report;
import Model.BarChartCalc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

public class DeeperAnalysisController  { // implements Initializable

    @FXML
    private TextFlow overAllAnalysisText;
    
    private BarChartCalc barChart = new BarChartCalc();

    private HashMap<String, Report> reportsHashMap = barChart.runInspection(); //gets the inspection reports
    
    private Report getReport(String reportKey){
        return reportsHashMap.get(reportKey);
     }

    @FXML
      void reportDataToGui(ActionEvent event) throws IOException{
    	 String key = "LazyClass";
         Text textOut = new Text("Name: " + key + "\nValue: " + "formatter.format(item.getYValue())" + "\n" + getReport(key).printSizeOfAffectedClasses() + "\n" +
                 getReport(key).percentToString() + " of files in project affected by "+ key +" Class code smell\n\n");
         textOut.setFill(Color.BLACK);
         textOut.setFont(Font.font("Verdana", 12));
         overAllAnalysisText.getChildren().add(textOut);
         textOut.setTextAlignment(TextAlignment.CENTER);
         textOut.setLineSpacing(20.0f);

         if (getReport(key).isClean()) {
             textOut = new Text("Project is clean for "+ key +" code smell\n");
         } else {
         ArrayList<Class> affectedClasses = new ArrayList<>(getReport(key).getAffectedClasses());

         for (Class cls : affectedClasses) {
             textOut.setText("Affected class name = " + cls.getSimpleName());
             textOut.setText(getReport(key).getReportData().get(cls).toString());
             }
         }
     }
     
	public void goBackToBarChartOverall(ActionEvent event) throws IOException {

		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/BarChartOverall.fxml"));
		Scene scene = new Scene(root2);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void goToExitScreen(ActionEvent event) throws IOException {

		Parent root2 = FXMLLoader.load(getClass().getResource("/Model/ExitScreen.fxml"));
		Scene scene = new Scene(root2);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

}
	

