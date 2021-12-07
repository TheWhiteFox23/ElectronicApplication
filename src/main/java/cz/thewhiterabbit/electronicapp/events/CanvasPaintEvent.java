package cz.thewhiterabbit.electronicapp.events;

import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class CanvasPaintEvent extends Event {
    public static final EventType<CanvasPaintEvent> ANY = new EventType<>(Event.ANY, "CANVAS_PAINT_ANY");
    public static final EventType<CanvasPaintEvent> REPAINT = new EventType<>(ANY, "REPAINT");
    public static final EventType<CanvasPaintEvent> REPAINT_OBJECT = new EventType<>(ANY, "REPAINT_OBJECT");
    public static final EventType<CanvasPaintEvent> PAINT = new EventType<>(ANY, "PAINT");
    public static final EventType<CanvasPaintEvent> PAINT_OBJECT = new EventType<>(ANY, "PAINT_OBJECT");
    public static final EventType<CanvasPaintEvent> CLEAN = new EventType<>(ANY, "CLEAN");
    public static final EventType<CanvasPaintEvent> CLEAN_OBJECT = new EventType<>(ANY, "CLEAN_OBJECT");

    /**** INFORMATION ****/
    private CanvasObject canvasObject;
    private boolean force = false;

    /**** CONSTRUCTORS ****/
    public CanvasPaintEvent(EventType<? extends Event> eventType, CanvasObject canvasObject) {
        super(eventType);
        this.canvasObject = canvasObject;
    }

    public CanvasPaintEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, CanvasObject canvasObject) {
        super(o, eventTarget, eventType);
        this.canvasObject = canvasObject;
    }

    public CanvasPaintEvent(EventType<? extends Event> eventType, CanvasObject canvasObject, boolean force) {
        super(eventType);
        this.canvasObject = canvasObject;
        this.force = force;
    }

    public CanvasPaintEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, CanvasObject canvasObject, boolean force) {
        super(o, eventTarget, eventType);
        this.canvasObject = canvasObject;
        this.force = force;
    }

    public CanvasPaintEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public CanvasPaintEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }

    /**** GETTERS AND SETTERS ****/

    public CanvasObject getCanvasObject() {
        return canvasObject;
    }

    public void setCanvasObject(CanvasObject canvasObject) {
        this.canvasObject = canvasObject;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }
}
