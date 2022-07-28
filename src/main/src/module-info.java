module com.application.javafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires java.sql;

    opens com.application to javafx.fxml;
    exports com.application;
    exports gui;
    opens gui to javafx.fxml;
    opens model.entities to javafx.graphics, javafx.fxml, javafx.base;
}