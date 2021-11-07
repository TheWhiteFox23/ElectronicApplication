package cz.thewhiterabbit.electronicapp;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class DrawingRegion extends Region {
    private double initialX = 0;
    private double initialY = 0;
    
    public DrawingRegion(){
        getStylesheets().add(App.class.getResource("stylesheets/drawing-area.css").toExternalForm());
        initGraphics();
    }

    private void initGraphics() {
        //performance test
        for(int i = 0; i< 200; i++){
            for(int j = 0; j< 200; j++){
                Circle circle = new Circle();
                circle.setCenterX(i * 10);
                circle.setCenterY(j*10);
                circle.setRadius(2);
                circle.setFill(Color.RED);
                getChildren().add(circle);
            }
        }
        addEventHandler(MouseEvent.MOUSE_PRESSED, e->{
            initialX = e.getX();
            initialY = e.getY();
        });
        addEventHandler(MouseEvent.MOUSE_DRAGGED, e->{
            getChildren().forEach(ch ->{
                ch.setLayoutX(ch.getLayoutX() - (initialX - e.getX()));
                ch.setLayoutY(ch.getLayoutY() - (initialY - e.getY()));
            });
            initialY = e.getY();
            initialX = e.getX();
        });
    }

    private void drawBackground(){

    }
}
