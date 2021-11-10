package cz.thewhiterabbit.electronicapp.events;

import javafx.event.Event;
import javafx.event.EventType;

public class CanvasSelection extends CanvasDragEvent{
    public static final EventType<CanvasSelection> START = new EventType<>(Event.ANY, "SELECTION_START");
    public static final EventType<CanvasSelection> MOVE = new EventType<>(Event.ANY, "SELECTION_MOVE");
    public static final EventType<CanvasSelection> FINISH = new EventType<>(Event.ANY, "SELECTION_FINISH");

    public CanvasSelection(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
