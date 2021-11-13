package cz.thewhiterabbit.electronicapp.events;

import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import javafx.event.Event;
import javafx.event.EventType;

public class CanvasMouseEvent extends Event {

    public static final EventType<CanvasMouseEvent> OBJECT_ENTERED = new EventType<>(Event.ANY, "OBJECT_ENTERED");
    public static final EventType<CanvasMouseEvent> OBJECT_EXITED = new EventType<>(Event.ANY, "OBJECT_EXITED");
    public static final EventType<CanvasMouseEvent> OBJECTS_DRAG_DETECTED = new EventType<>(Event.ANY, "OBJECTS_DRAG_DETECTED");
    public static final EventType<CanvasMouseEvent> OBJECT_DRAGGED = new EventType<>(Event.ANY, "OBJECT_DRAGGED");
    public static final EventType<CanvasMouseEvent> OBJECT_DRAG_DROPPED = new EventType<>(Event.ANY, "OBJECT_DRAG_DROPPED");
    public static final EventType<CanvasMouseEvent> CANVAS_DRAG_DETECTED = new EventType<>(Event.ANY, "CANVAS_DRAG_DETECTED");
    public static final EventType<CanvasMouseEvent> CANVAS_DRAGGED = new EventType<>(Event.ANY, "CANVAS_DRAGGED");
    public static final EventType<CanvasMouseEvent> CANVAS_DRAG_DROPPED = new EventType<>(Event.ANY, "CANVAS_DRAG_DROPPED");
    public static final EventType<CanvasMouseEvent> CANVAS_SCROLLED = new EventType<>(Event.ANY, "CANVAS_SCROLLED");
    public static final EventType<CanvasMouseEvent> CANVAS_SELECTION_DETECTED = new EventType<>(Event.ANY, "CANVAS_SELECTION_DETECTED");
    public static final EventType<CanvasMouseEvent> CANVAS_SELECTION_MOVE = new EventType<>(Event.ANY, "CANVAS_SELECTION_MOVE");
    public static final EventType<CanvasMouseEvent> CANVAS_SELECTION_FINISH = new EventType<>(Event.ANY, "CANVAS_SELECTION_FINISH");
    public static final EventType<CanvasMouseEvent> OBJECT_SELECTED = new EventType<>(Event.ANY, "OBJECT_SELECTED");
    public static final EventType<CanvasMouseEvent> OBJECT_DESELECTED = new EventType<>(Event.ANY, "OBJECT_DESELECTED");
    public static final EventType<CanvasMouseEvent> DESELECT_ALL = new EventType<>(Event.ANY, "DESELECT_ALL");

    private double startX = -1;
    private double startY = -1;
    private double lastX = -1;
    private double lastY = -1;
    private double x = -1;
    private double y = -1;
    private CanvasObject object;
    private double deltaY = 0;

    public CanvasMouseEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public CanvasMouseEvent(EventType<? extends Event> eventType, double startX, double startY, double lastX, double lastY, double x, double y) {
        super(eventType);
        this.startX = startX;
        this.startY = startY;
        this.lastX = lastX;
        this.lastY = lastY;
        this.x = x;
        this.y = y;
    }

    public CanvasMouseEvent(EventType<? extends Event> eventType, double startX, double startY, double lastX, double lastY, double x, double y, CanvasObject object) {
        super(eventType);
        this.startX = startX;
        this.startY = startY;
        this.lastX = lastX;
        this.lastY = lastY;
        this.x = x;
        this.y = y;
        this.object = object;
    }

    public CanvasMouseEvent(EventType<? extends Event> eventType, double x, double y, CanvasObject object) {
        super(eventType);
        this.x = x;
        this.y = y;
        this.object = object;
    }

    public CanvasMouseEvent(EventType<? extends Event> eventType,  CanvasObject object) {
        super(eventType);
        this.x = x;
        this.y = y;
        this.object = object;
    }


    public CanvasMouseEvent(EventType<? extends Event> eventType, double deltaY) {
        super(eventType);
        this.deltaY = deltaY;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
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

    public CanvasObject getObject() {
        return object;
    }

    public void setObject(CanvasObject object) {
        this.object = object;
    }

    public double getDeltaY() {return deltaY;}

    public void setDeltaY(double deltaY) {this.deltaY = deltaY;}
}
