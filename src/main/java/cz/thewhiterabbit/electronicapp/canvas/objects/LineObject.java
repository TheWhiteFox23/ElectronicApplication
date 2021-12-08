package cz.thewhiterabbit.electronicapp.canvas.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LineObject extends CanvasObject {
    private double X2 = 0;
    private double Y2 = 0;

    public enum Orientation {HORIZONTAL, VERTICAL}

    Orientation orientation = Orientation.HORIZONTAL;

    public LineObject() {
        setRotationStrategy(RotationStrategy.DO_NOT_ROTATE);
        initPropertyListeners();
    }

    private void initPropertyListeners() {
        locationXProperty().addListener(l -> {
            calculateSecondPoint();
        });
        locationYProperty().addListener(l -> {
            calculateSecondPoint();
        });
        heightProperty().addListener(l -> {
            calculateSecondPoint();
        });
        widthProperty().addListener(l ->{
            calculateSecondPoint();
        });
    }

    private void calculateSecondPoint() {
        X2 = (getPositionX() + getLineHeight());
        Y2 = (getPositionY() + getLineHeight());
    }

    @Override
    public void doPaint(GraphicsContext gc) {
        //paint bounds
        double X2 = this.X2 - getLocationX();
        double Y2 = this.Y2 - getLocationY();


        if(isHovered()){
            gc.setFill(Color.WHEAT);
        }else if(isSelected()){
            gc.setFill(Color.TURQUOISE);
        }else{
            gc.setFill(Color.DARKSLATEGRAY);
        }

        gc.fillRoundRect(-getLineHeight()/2, -getLineHeight()/2, X2, Y2, getLineHeight(), getLineHeight());
    }

    private double getLineHeight() {
        return (getHeight() > getWidth() ? getWidth() : getHeight()) * 0.25;
    }

    private double getPositionX() {
        switch (orientation) {
            case HORIZONTAL: {
                return getLocationX() + getWidth();
            }
            default: {
                return getLocationX();
            }
        }
    }

    private double getPositionY() {
        switch (orientation) {
            case HORIZONTAL: {
                return getLocationY();
            }
            default: {
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

    @Override
    public boolean isInBounds(double x, double y) {
        return (x> getLocationX()-getLineHeight()/2 && x<X2 && y> getLocationY()-getLineHeight()/2 && y< Y2);
    }

    @Override
    public boolean isVisible(double canvasWidth, double canvasHeight) {
        return super.isVisible(canvasWidth, canvasHeight);
    }

    @Override
    public boolean isInBounds(double locationX, double locationY, double width, double height) {
        return super.isInBounds(locationX, locationY, width, height);
    }
}
