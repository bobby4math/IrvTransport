package org.openjfx;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

//import javax.naming.ServiceUnavailableException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import java.time.LocalDate;
//Home screen class
public class MainFrame implements java.io.Serializable{

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String lastDateUpd = "";


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void initialize() {

    }
    //pick up city and state
    @FXML
    private TextField city1;
    @FXML
    private TextField state1;
    @FXML
    void getCity(ActionEvent event) {
        System.out.println(city1.getText());
    }
    @FXML
    private Button submit;
    //Rate of the potential load
    @FXML
    private TextField rateT;
    //Weight of the potential load
    @FXML
    private TextField weightT;

    @FXML
    private Label prof;
    //text fields for additional stops and buttons to make them available
    @FXML
    private TextField city4;

    @FXML
    private TextField state4;

    @FXML
    private Button plus4;

    @FXML
    private Label stop4;

    @FXML
    private TextField city5;

    @FXML
    private Button plus5;

    @FXML
    private TextField state5;

    @FXML
    private Label stop5;

    
    @FXML
    private TextField city6;

    @FXML
    private TextField state6;

    @FXML
    private Label stop6;

    
    @FXML
    void fifthPlus(ActionEvent event) {
        plus5.setVisible(false);
        state6.setVisible(true);
        city6.setVisible(true);
        stop6.setVisible(true);
    }

    @FXML
    void fourthPlus(ActionEvent event) {
        plus4.setVisible(false);
        plus5.setVisible(true);
        state5.setVisible(true);
        city5.setVisible(true);
        stop5.setVisible(true);
    }

    @FXML
    void plusThird(ActionEvent event) {
        plus3.setVisible(false);
        plus4.setVisible(true);
        state4.setVisible(true);
        city4.setVisible(true);
        stop4.setVisible(true);
    }
    //Get load details after submit
    @FXML
    void getDet(ActionEvent event) {
        try {
            FileInputStream file = new FileInputStream("date.txt");
            ObjectInputStream in = new ObjectInputStream(file);
            this.lastDateUpd = (String)in.readObject();
            in.close();
            file.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        //Load that will be evaluated
        Load testLoad = new Load();
        //Add necessary stops to load
        testLoad.addStop(city1.getText(), state1.getText());
        testLoad.addStop(city2.getText(), state2.getText());
        testLoad.addStop(city3.getText(), state3.getText());
        if(!city4.getText().equals("")){
            testLoad.addStop(city4.getText(), state4.getText());
        }
        if(!city5.getText().equals("")){
            testLoad.addStop(city5.getText(), state5.getText());
        }
        if(!city6.getText().equals("")){
            testLoad.addStop(city6.getText(), state6.getText());
        }
        testLoad.weight = Integer.parseInt(weightT.getText());
        testLoad.rate = Integer.parseInt(rateT.getText());
        //Evaluate the rate to get profit for the load
        prof.setText("Profit: $" + String.valueOf(testLoad.evalRate(false)));
        boolean updateGas = false;
        LocalDate testDate = LocalDate.now();
        //If the current date != the date gas prices were last updated then get gas prices and update them. 
        if(!this.lastDateUpd.equals(testDate.toString())){
            updateGas =true;
            this.lastDateUpd = testDate.toString();
            try {
                FileOutputStream file = new FileOutputStream("date.txt");
                ObjectOutputStream out = new ObjectOutputStream(file);
                out.writeObject(this.lastDateUpd);
                out.close();
                file.close();
            } catch (Exception e) {
                System.out.print("serialize out failed");
            }
        }
        int profs = testLoad.evalRate(updateGas);
        prof.setVisible(true);
        //update scene to show profitability details (LoadEvalScene)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadEvalScene.fxml"));
            root = loader.load();
            LoadEvalSceneController loadEvalSceneController = loader.getController();
            loadEvalSceneController.diaplayLoad(testLoad);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //profit.setText("Profit: $" + String.valueOf(profs));


    }
    @FXML
    private Label stop2;


    @FXML
    private Button plus1;

    @FXML
    private Button plus2;

    @FXML
    private TextField city2;

    @FXML
    private TextField state2;

    @FXML
    private Label stop3;

    @FXML
    private TextField city3;

    @FXML
    private TextField state3;

    @FXML
    private Button plus3;

    @FXML
    void plusFirst(ActionEvent event) {
        plus1.setVisible(false);
        stop2.setVisible(true);
        plus2.setVisible(true);
        city2.setVisible(true);
        state2.setVisible(true);
    }
    @FXML
    void plusSecond(ActionEvent event) {
        plus2.setVisible(false);
        stop3.setVisible(true);
        plus3.setVisible(true);
        city3.setVisible(true);
        state3.setVisible(true);
    } 

}
