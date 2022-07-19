module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.controlsfx.controls;
    requires jcommander;


    opens com.example.demo;
    exports com.example.demo;
    opens com.example.demo.controller;
    exports com.example.demo.controller;
    opens com.example.demo.model;
//    opens com.example.demo.font;
    exports com.example.demo.model;
    opens com.example.demo.view;
    exports com.example.demo.view;
    exports com.example.demo.view.model;
    opens com.example.demo.view.model;
}