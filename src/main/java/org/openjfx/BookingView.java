package org.openjfx;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
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

public class BookingView implements java.io.Serializable{

    public Load loadShowing;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label loadName;

    @FXML
    private Button bookThis;

    @FXML
    private TextField delDate;

    @FXML
    private TextField puDate;

    //get further details and add load to LoadsView.java
    @FXML
    void bookCurLoad(ActionEvent event) {
        this.loadShowing.puDate = this.puDate.getText();
        this.loadShowing.delDate = this.delDate.getText();
        LoadsView loadsView = new LoadsView();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadsView.fxml"));
            Parent root = loader.load();
            loadsView = loader.getController();
            loadsView.addNewLoad(this.loadShowing);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoad(Load inLoad) {
        loadName.setText(inLoad.allStops.get(1).getState() + " to " + inLoad.allStops.get(inLoad.allStops.size() - 1).getState());
        this.loadShowing = inLoad;
    }

    @FXML
    void initialize() {

    }

}
