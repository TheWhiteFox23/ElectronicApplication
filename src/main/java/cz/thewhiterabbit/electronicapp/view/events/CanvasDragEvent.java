package cz.thewhiterabbit.electronicapp.view.events;

import javafx.event.Event;
import javafx.event.EventType;

public abstract class CanvasDragEvent extends Event {

    private double beginningX;
    private double beginningY;

    private double lastX;
    private double lastY;

    private double x;
    private double y;

    public CanvasDragEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public double getBeginningX() {
        return beginningX;
    }

    public void setBeginningX(double beginningX) {
        this.beginningX = beginningX;
    }

    public double getBeginningY() {
        return beginningY;
    }

    public void setBeginningY(double beginningY) {
        this.beginningY = beginningY;
    }

    public double getLastX() {
        return lastX;
    }

    public void setLastX(double lastX) {
        this.lastX = lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public void setLastY(double lastY) {
        this.lastY = lastY;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
