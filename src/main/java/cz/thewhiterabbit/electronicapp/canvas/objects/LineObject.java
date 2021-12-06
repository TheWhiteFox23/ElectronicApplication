package cz.thewhiterabbit.electronicapp.canvas.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LineObject extends CanvasObject{
    public enum Orientation {HORIZONTAL, VERTICAL}
    Orientation orientation = Orientation.HORIZONTAL;


    @Override
    public void paint(GraphicsContext gc) {
        double height = (getHeight()>getWidth()?getWidth():getHeight())*0.25;
        double positionX;
        double positionY;
        switch (orientation){
            case HORIZONTAL:{
                positionX = getLocationX()+getWidth();
                positionY = getLocationY();
                break;
            } default:{
                positionX = getLocationX();
                positionY = getLocationY() + getHeight();
                break;
            }
        }
        gc.setStroke(Color.DARKSLATEGRAY);
        gc.setLineWidth(height);
        gc.strokeLine(getLocationX(), getLocationY(), positionX, positionY);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}
