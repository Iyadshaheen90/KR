module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires poi;
    requires poi.ooxml.schemas;
    opens org.example to javafx.fxml;
    exports org.example;
}