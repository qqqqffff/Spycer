module com.apollor.respicy {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.apollor.respicy to javafx.fxml;
    exports com.apollor.respicy;
    exports com.apollor.respicy.controllers;
    exports com.apollor.respicy.utils;
    opens com.apollor.respicy.controllers to javafx.fxml;
}