package cz.thewhiterabbit.electronicapp.events;

import javafx.event.Event;
import javafx.event.EventType;

public class CanvasMoveOrigin extends CanvasDragEvent{
    public static final EventType<CanvasMoveOrigin> START = new EventType<>(Event.ANY, "MOVE_ORIGIN_START");
    public static final EventType<CanvasMoveOrigin> MOVE = new EventType<>(Event.ANY, "MOVE_ORIGIN");
    public static final EventType<CanvasMoveOrigin> FINISH = new EventType<>(Event.ANY, "MOVE_ORIGIN_FINISH");

    public CanvasMoveOrigin(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
