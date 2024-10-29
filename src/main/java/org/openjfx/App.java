
package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.*;
import javafx.scene.text.Text;
import javafx.scene.*;

import java.io.IOException;

/**
 * JavaFX Application class
 */
public class App extends Application {

    private static Scene scene;

    //Starts the application showing home screen (MainFrame.java)
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainFrame.fxml"));
        Scene scene = new Scene(root,600,600, Color.BEIGE);
        String CSS = this.getClass().getResource("application.css").toExternalForm();
        scene.getStylesheets().add(CSS);
        Text text = new Text();
        text.setText("Origin City");
        text.setX(50);
        text.setY(50);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
 

}