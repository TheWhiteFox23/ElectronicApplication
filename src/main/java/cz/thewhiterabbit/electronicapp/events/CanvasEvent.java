package cz.thewhiterabbit.electronicapp.events;

import javafx.event.Event;
import javafx.event.EventType;

public class CanvasEvent extends Event {
    public static final EventType<CanvasEvent> MOVE_ORIGIN_START = new EventType<>(Event.ANY, "MOVE_ORIGIN_START");
    public static final EventType<CanvasEvent> MOVE_ORIGIN = new EventType<>(Event.ANY, "MOVE_ORIGIN");
    public static final EventType<CanvasEvent> MOVE_ORIGIN_FINISH = new EventType<>(Event.ANY, "MOVE_ORIGIN_FINISH");

    public static final EventType<CanvasEvent> SELECTION_START = new EventType<>(Event.ANY, "SELECTION_START");
    public static final EventType<CanvasEvent> SELECTION_MOVE = new EventType<>(Event.ANY, "SELECTION_MOVE");
    public static final EventType<CanvasEvent> SELECTION_FINISH = new EventType<>(Event.ANY, "SELECTION_FINISH");

    private double x;
    private double y;

    public CanvasEvent(EventType<? extends Event> eventType, double x, double y) {
        super(eventType);
        this.x = x;
        this.y = y;
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
