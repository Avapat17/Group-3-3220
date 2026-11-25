module com.groupproject.gpgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;

    opens com.groupproject.gpgui to javafx,fxml, com.fasterxml.jackson.databind;
    exports com.groupproject.gpgui;
}