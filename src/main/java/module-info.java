module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
//    requires javafx.media;
    requires com.google.gson;


    opens com.example.demo;
    exports com.example.demo;
    exports com.example.demo.controller;
    exports com.example.demo.model;
    opens com.example.demo.controller;
    opens com.example.demo.model;

    opens com.example.demo.view;
    exports com.example.demo.view;
}