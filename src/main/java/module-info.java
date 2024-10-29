module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.openjfx to javafx.fxml;


    //needed for HTTP
    requires unirest.java;

    //needed for JSON
    requires org.json;
    requires json.simple;
    requires gson;
    requires java.sql;

    //needed for JavaFX
 

    //needed for JSON

    exports org.openjfx;
}
