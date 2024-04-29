module demo1 {
    requires javafx.graphics;
    requires  javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires  de.jensd.fx.glyphs.fontawesome;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires com.google.protobuf;
    requires javafx.media;
    requires freetts;
    exports com.example.demo1 to javafx.graphics;
    opens com.example.demo1.Entities to javafx.base, javafx.fxml;

    opens com.example.demo1.Controllers;
}