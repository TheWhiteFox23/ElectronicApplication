package cz.thewhiterabbit.electronicapp.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GeneralCanvasObject extends CanvasObject{
    public GeneralCanvasObject(){
        super();
    }

    public GeneralCanvasObject(double width, double height) {
        super(width, height);
    }

    public GeneralCanvasObject(double locationX, double locationY, double width, double height) {
        super(locationX, locationY, width, height);
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(Color.ALICEBLUE);
        gc.fillRect(getLocationX(), getLocationY(), getWidth(), getHeight());
    }
}
