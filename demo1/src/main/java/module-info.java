module demo1 {
    requires javafx.graphics;
    requires  javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires  de.jensd.fx.glyphs.fontawesome;
    exports com.example.demo1 to javafx.graphics;

    opens com.example.demo1.Controllers;
}