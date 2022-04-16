module cz.thewhiterabbit.electronicapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires commons.math3;
    requires json.simple;


    opens cz.thewhiterabbit.electronicapp to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp;
    exports cz.thewhiterabbit.electronicapp.view.controllers;
    opens cz.thewhiterabbit.electronicapp.view.controllers to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp.view.components;
    opens cz.thewhiterabbit.electronicapp.view.components to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp.view.canvas;
    opens cz.thewhiterabbit.electronicapp.view.canvas to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp.view.canvas.model;
    opens cz.thewhiterabbit.electronicapp.view.canvas.model to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp.model.objects;
    opens cz.thewhiterabbit.electronicapp.model.objects to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp.utilities;
    opens cz.thewhiterabbit.electronicapp.utilities to javafx.fxml;
    exports  cz.thewhiterabbit.electronicapp.model.components;
    opens cz.thewhiterabbit.electronicapp.model.components to javafx.fxml;
}