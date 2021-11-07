package cz.thewhiterabbit.electronicapp;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

/**
 * Collect events from the components and notify subscribers
 */
public interface IEventAggregator {
    <T extends Event> void fireEvent(T event);
    <T extends EventType>void registerHandler(T eventType, EventHandler handler);
}
