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
        gc.setStroke(Color.DARKSLATEGRAY);
        gc.setLineWidth(getLineHeight());
        gc.strokeLine(0, 0, getPositionX()-getLocationX(), getPositionY()-getLocationY());
    }

    private double getLineHeight() {
        return (getHeight() > getWidth() ? getWidth() : getHeight()) * 0.25;
    }

    private double getPositionX(){
        switch (orientation){
            case HORIZONTAL:{
                return getLocationX()+getWidth();
            } default:{
                return  getLocationX();
            }
        }
    }

    private double getPositionY(){
        switch (orientation){
            case HORIZONTAL:{
                return getLocationY();
            } default:{
                return getLocationY() + getHeight();
            }
        }
    }


    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}
