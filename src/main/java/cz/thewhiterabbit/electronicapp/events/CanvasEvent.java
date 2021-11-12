package cz.thewhiterabbit.electronicapp.events;

import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import javafx.event.Event;
import javafx.event.EventType;

public class CanvasEvent extends Event {
    private double x = 0;
    private double y = 0;
    private double width = 0;
    private double height = 0;
    private CanvasObject object;
    private double zoomAspect = 1.0;

    public static final EventType<CanvasEvent> REPAINT_ALL = new EventType<>(Event.ANY, "REPAINT_ALL");
    public static final EventType<CanvasEvent> REPAINT_RECTANGLE = new EventType<>(Event.ANY, "REPAINT_RECTANGLE");
    public static final EventType<CanvasEvent> REPAINT_OBJECT = new EventType<>(Event.ANY, "REPAINT_OBJECT");
    public static final EventType<CanvasEvent> PAINT_OBJECT = new EventType<>(Event.ANY, "PAINT_OBJECT");
    public static final EventType<CanvasEvent> ZOOM_ASPECT_CHANGED = new EventType<>(Event.ANY, "ZOOM_ASPECT_CHANGED");

    public CanvasEvent(EventType<? extends Event> eventType, double x, double y, double width, double height) {
        super(eventType);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public CanvasEvent(EventType<? extends Event> eventType, CanvasObject object) {
        super(eventType);
        this.object = object;
    }

    public CanvasEvent(EventType<? extends Event> eventType, double zoomAspect) {
        super(eventType);
        this.zoomAspect = zoomAspect;
    }

    public CanvasEvent(EventType<? extends Event> eventType) {
        super(eventType);
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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {this.width = width;}

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public CanvasObject getObject() {
        return object;
    }

    public void setObject(CanvasObject object) {
        this.object = object;
    }
}
