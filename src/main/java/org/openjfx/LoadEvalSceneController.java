package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class LoadEvalSceneController implements java.io.Serializable{

    Load thisLoad;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label profit;

    @FXML
    private Label profitPerMile;

    @FXML
    private Label ratePerMile;

    @FXML
    private Label totalRate;

    @FXML
    private Button bookButton;

    @FXML
    private Button cancelButton;


    @FXML
    void initialize() {
        assert profit != null : "fx:id=\"profit\" was not injected: check your FXML file 'Untitled'.";

    }
    //shows all profitability details of the load
    public void diaplayLoad(Load inLoad){
        thisLoad = inLoad;
        profit.setText("Profit: $" + String.valueOf(inLoad.loadProfit));
        profitPerMile.setText("Profit/Mile: $" + String.valueOf(inLoad.loadProfit/inLoad.miles));
        ratePerMile.setText("Rate/Mile: $" + String.valueOf(inLoad.rate/inLoad.miles));
        totalRate.setText("Total Rate: $" + String.valueOf(inLoad.rate));
    }
    //option to book the load goes to BookingView.java
    @FXML
    void bookLoad(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BookingView.fxml"));
        Parent root = loader.load();
        BookingView bookingView = loader.getController();
        bookingView.showLoad(this.thisLoad);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }

    }
    //If load is not booked return to MainFrame.java
    @FXML
    void cancelLoad(ActionEvent event) {
        try {
           
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}