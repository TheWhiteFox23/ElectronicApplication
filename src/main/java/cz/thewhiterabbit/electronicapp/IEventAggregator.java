package cz.thewhiterabbit.electronicapp;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

public interface IEventAggregator {
    <T extends Event> void fireEvent(T event);

    <T extends EventType> void addEventHandler(T eventType, EventHandler handler);

    void removeEventHandler(EventType eventType, EventHandler eventHandler);

    boolean contains(EventHandler eventHandler);
}
