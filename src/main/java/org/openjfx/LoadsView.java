package org.openjfx;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

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
import javafx.scene.Node;

public class LoadsView implements java.io.Serializable{

    public Vector<Load> bookedLoads = new Vector<Load>();
    public int current;
    private boolean typing = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToCalc;

    @FXML
    private Label delDate;

    @FXML
    private Label delLocation;

    @FXML
    private Button deleteLoad;

    @FXML
    private Label loadRate;

    @FXML
    private Label loadStatus;

    @FXML
    private Button nextLoad;

    @FXML
    private Button prevLoad;

    @FXML
    private TextField puDateSearch;

    @FXML
    private Label puLocation;

    @FXML
    private Button searchButton;

    @FXML
    private Button statusChanger;

    @FXML
    private TextField statusField;

    //Change current status of load
    @FXML
    void changeStatus(ActionEvent event) {
        if(!this.typing){
            loadStatus.setVisible(false);
            statusField.setVisible(true);
            statusChanger.setText("Confirm");
            this.typing = true;
        }
        else{
            this.bookedLoads.get(current).status = statusField.getText();
            loadStatus.setVisible(true);
            statusField.setVisible(false);
            statusChanger.setText("Edit Status");
            this.typing = false; 
            this.displayLoad();
        }
    }
    //Add new load to vector of all loads
    public void addNewLoad(Load newLoad) {
        //File ser = new File("savedLoads.txt");
        //if(ser.length() != 0){
            try {
                FileInputStream file = new FileInputStream("savedLoads.txt");
                ObjectInputStream in = new ObjectInputStream(file);
                this.bookedLoads = (Vector<Load>)in.readObject();
                in.close();
                file.close();
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Fail");
            }  
        //}
        this.bookedLoads.add(newLoad);
        this.current = this.bookedLoads.size() - 1;
        this.displayLoad();
    }

    public void displayLoad() {
        if(this.current == -1){
            this.delDate.setText("No Loads");
            puLocation.setText("No Loads");
            delLocation.setText("No Loads");
            loadRate.setText("No Loads");
            loadStatus.setText("No Loads");
        }
        else{
            Load loadDisplaying = this.bookedLoads.get(current);
                this.delDate.setText("Delivery Date: " + loadDisplaying.delDate);
            puLocation.setText("From: " + loadDisplaying.allStops.get(0).getCity() + ", " + loadDisplaying.allStops.get(0).getState());
            delLocation.setText("To: " + loadDisplaying.allStops.get(loadDisplaying.allStops.size()-2).getCity() + ", " + loadDisplaying.allStops.get(loadDisplaying.allStops.size()-2).getState());
            loadRate.setText("Rate: $" + loadDisplaying.rate);
            loadStatus.setText(loadDisplaying.status);
        }
    }
    //Delete load
    @FXML
    void delLoad(ActionEvent event) {
        if(this.current >= -1){
            this.bookedLoads.remove(current);
            if(current >= this.bookedLoads.size()){
                current -= 1;
            }
        }
        this.displayLoad();
    }
    //Go to next load
    @FXML
    void goNext(ActionEvent event) {
        if(current < this.bookedLoads.size() - 1){
            this.current += 1;
            this.displayLoad();
        }
        
    }
    //Go to previous load
    @FXML
    void goPrev(ActionEvent event) {
        if(this.current > 0){
            this.current -= 1;
            this.displayLoad();
        }
    }
    //Return to MainFrame
    @FXML
    void goToCalc(ActionEvent event) {
        //Serialize the vector of all loads
        try {
            FileOutputStream file = new FileOutputStream("savedLoads.txt");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this.bookedLoads);
            out.close();
            file.close();
        } catch (Exception e) {
            System.out.print("serialize out failed");
        }
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
    //option to search for a load on a specific date
    @FXML
    void searchLoad(ActionEvent event) {
        int index = 0;
        boolean keepGoing = true;
        while(keepGoing){
            if(this.bookedLoads.get(index).puDate.equals(puDateSearch.getText())){
                current = index;
                this.displayLoad();
                keepGoing = false;
            }
            else {
                index += 1;
            }

        }

    }

    @FXML
    void initialize() {
        assert backToCalc != null : "fx:id=\"backToCalc\" was not injected: check your FXML file 'LoadsView.fxml'.";
        assert delDate != null : "fx:id=\"delDate\" was not injected: check your FXML file 'LoadsView.fxml'.";
        assert delLocation != null : "fx:id=\"delLocation\" was not injected: check your FXML file 'LoadsView.fxml'.";
        assert deleteLoad != null : "fx:id=\"deleteLoad\" was not injected: check your FXML file 'LoadsView.fxml'.";
        assert loadRate != null : "fx:id=\"loadRate\" was not injected: check your FXML file 'LoadsView.fxml'.";
        assert loadStatus != null : "fx:id=\"loadStatus\" was not injected: check your FXML file 'LoadsView.fxml'.";
        assert nextLoad != null : "fx:id=\"nextLoad\" was not injected: check your FXML file 'LoadsView.fxml'.";
        assert prevLoad != null : "fx:id=\"prevLoad\" was not injected: check your FXML file 'LoadsView.fxml'.";
        assert puDateSearch != null : "fx:id=\"puDateSearch\" was not injected: check your FXML file 'LoadsView.fxml'.";
        assert puLocation != null : "fx:id=\"puLocation\" was not injected: check your FXML file 'LoadsView.fxml'.";
        assert searchButton != null : "fx:id=\"searchButton\" was not injected: check your FXML file 'LoadsView.fxml'.";

    }

}
