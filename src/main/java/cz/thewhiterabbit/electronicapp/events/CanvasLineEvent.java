package cz.thewhiterabbit.electronicapp.events;

import javafx.event.Event;
import javafx.event.EventType;

public class CanvasLineEvent extends CanvasDragEvent{
    public static final EventType<CanvasLineEvent> PAINT_LINE = new EventType<>(Event.ANY, "PAINT_LINE");
    public static final EventType<CanvasLineEvent> INSERT_LINE = new EventType<>(Event.ANY, "INSERT_LINE");

    public CanvasLineEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
