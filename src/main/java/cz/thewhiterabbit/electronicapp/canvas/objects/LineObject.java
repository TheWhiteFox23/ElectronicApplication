package cz.thewhiterabbit.electronicapp.canvas.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LineObject extends CanvasObject{
    public enum Rotation {LEFT_TO_RIGHT, LEFT_TO_BOTTOM}
    Rotation rotation = Rotation.LEFT_TO_RIGHT;

    @Override
    public void paint(GraphicsContext gc) {
        double height = (getHeight()>getWidth()?getWidth():getHeight())*0.25;
        double positionX;
        double positionY;
        switch (rotation){
            case LEFT_TO_RIGHT:{
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

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public void finalize() throws Throwable{
        //System.out.println("Object is destroyed by the Garbage Collector");
    }
}
