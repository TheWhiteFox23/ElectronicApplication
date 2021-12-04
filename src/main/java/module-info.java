module cz.thewhiterabbit.electronicapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.thewhiterabbit.electronicapp to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp;
    exports cz.thewhiterabbit.electronicapp.controllers;
    opens cz.thewhiterabbit.electronicapp.controllers to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp.guicomponents;
    opens cz.thewhiterabbit.electronicapp.guicomponents to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp.canvas;
    opens cz.thewhiterabbit.electronicapp.canvas to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp.canvas.model;
    opens cz.thewhiterabbit.electronicapp.canvas.model to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp.canvas.objects;
    opens cz.thewhiterabbit.electronicapp.canvas.objects to javafx.fxml;
}