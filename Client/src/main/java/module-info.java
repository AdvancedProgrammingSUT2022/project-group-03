module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.controlsfx.controls;
    requires jcommander;
    requires javafx.media;


    opens com.example.demo to com.google.gson;
    exports com.example.demo;
    opens com.example.demo.controller;
    exports com.example.demo.controller;
    opens com.example.demo.model;
    exports com.example.demo.model;
    opens com.example.demo.view;
    exports com.example.demo.view;
    opens com.example.demo.view.model;
    exports com.example.demo.view.model;
    exports com.example.demo.view.cheat;
    exports com.example.demo.model.tiles;
    exports com.example.demo.model.building;
    exports com.example.demo.model.Units;
    exports com.example.demo.model.technologies;
    exports com.example.demo.model.resources;
}