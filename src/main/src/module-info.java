module com.application.javafxjdbc {
    requires java.datatransfer;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;

    opens com.application /*to javafx.fxml, java.desktop, java.datatransfer*/;
    exports com.application;
    exports gui;
    opens gui to javafx.fxml, java.desktop;
    opens model.entities to javafx.graphics, javafx.fxml, javafx.base;
}