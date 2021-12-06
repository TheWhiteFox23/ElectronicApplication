package cz.thewhiterabbit.electronicapp.canvas.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LineObject extends CanvasObject{
    public enum Orientation {HORIZONTAL, VERTICAL}
    Orientation orientation = Orientation.HORIZONTAL;

    public LineObject() {
        setRotationStrategy(RotationStrategy.DO_NOT_ROTATE);
    }

    @Override
    public void doPaint(GraphicsContext gc) {
        double height = (getHeight()>getWidth()?getWidth():getHeight())*0.25;
        double positionX;
        double positionY;
        switch (orientation){
            case HORIZONTAL:{
                positionX = getWidth();
                positionY = 0;
                break;
            } default:{
                positionX = 0;
                positionY = getHeight();
                break;
            }
        }
        gc.setStroke(Color.DARKSLATEGRAY);
        gc.setLineWidth(height);
        gc.strokeLine(0, 0, positionX, positionY);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}
