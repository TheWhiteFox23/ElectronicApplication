package cz.thewhiterabbit.electronicapp.canvas.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LineObject extends CanvasObject{
    public static int count;
    public enum Orientation {HORIZONTAL, VERTICAL}
    Orientation orientation = Orientation.HORIZONTAL;

    public LineObject() {
        count++;
    }

    @Override
    public void paint(GraphicsContext gc) {
        System.out.println("Count: " + count);
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

    public void finalize() throws Throwable{
        count--;
        //System.out.println("Object is destroyed by the Garbage Collector");
    }
}
