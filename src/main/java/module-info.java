module cz.thewhiterabbit.electronicapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.thewhiterabbit.electronicapp to javafx.fxml;
    exports cz.thewhiterabbit.electronicapp;
}