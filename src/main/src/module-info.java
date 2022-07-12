module com.application.javafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.application.javafxjdbc to javafx.fxml;
    exports com.application.javafxjdbc;
}