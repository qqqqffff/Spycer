module com.apollor.spycer {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.sql;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.slf4j;
    requires ipgeolocation;

    opens com.apollor.spycer to javafx.fxml;
    exports com.apollor.spycer;
    exports com.apollor.spycer.controllers;
    exports com.apollor.spycer.utils;
    exports com.apollor.spycer.database;
    opens com.apollor.spycer.controllers to javafx.fxml;
}