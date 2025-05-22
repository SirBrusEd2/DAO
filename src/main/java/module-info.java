module com.example.lab2dao {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.sql;

    opens com.example.lab2dao to javafx.fxml;
    exports com.example.lab2dao;
    exports com.example.lab2dao.controller;
    opens com.example.lab2dao.controller to javafx.fxml;
    exports com.example.lab2dao.dao;
    opens com.example.lab2dao.dao to javafx.fxml;
    exports com.example.lab2dao.model;
    opens com.example.lab2dao.model to javafx.fxml;
}