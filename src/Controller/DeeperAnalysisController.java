// TODO: when String leads to an invalid path, produce error message
package Controller;

import java.io.*;

import CodeSmells.Inspection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;

public class DeeperAnalysisController { // implements Initializable

    @FXML
    private TextFlow overAllAnalysisText;

    public DeeperAnalysisController() throws IOException {

    }

    //TODO: generate for all smells 
    @FXML
    void reportDataToGui(ActionEvent event) throws IOException {
        Text textOut = new Text();

        textOut.setFill(Color.BLACK);
        textOut.setFont(Font.font("Verdana", 12));
        textOut.setTextAlignment(TextAlignment.CENTER);
        textOut.setLineSpacing(20.0f);

        File file = new File(Inspection.getDesktopPath());
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader input = new BufferedReader(new InputStreamReader(fileInputStream));
        String line;

        while ((line = input.readLine()) != null) {
            textOut = new Text(line+"\n");
            overAllAnalysisText.getChildren().add(textOut);
        }
    }

    public void goBackToBarChartOverall(ActionEvent event) throws IOException {

        Parent root2 = FXMLLoader.load(getClass().getResource("/res/BarChartOverall.fxml"));
        Scene scene = new Scene(root2);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

//    public void goToExitScreen(ActionEvent event) throws IOException {

//        Parent root2 = FXMLLoader.load(getClass().getResource("/ExitScreen.fxml"));
//        Scene scene = new Scene(root2);
//        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        window.setScene(scene);
//        window.show();
//    }

}


