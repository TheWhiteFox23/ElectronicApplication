module cz.thewhiterabbit.electronicapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.thewhiterabbit.electronicapp to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp;
    exports cz.thewhiterabbit.electronicapp.controllers;
    opens cz.thewhiterabbit.electronicapp.controllers to javafx.fxml;
}