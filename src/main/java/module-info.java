module com.apollor.spycer {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.joda.time;


    opens com.apollor.spycer to javafx.fxml;
    exports com.apollor.spycer;
    exports com.apollor.spycer.controllers;
    exports com.apollor.spycer.utils;
    opens com.apollor.spycer.controllers to javafx.fxml;
}