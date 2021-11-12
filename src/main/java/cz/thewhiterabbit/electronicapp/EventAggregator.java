package cz.thewhiterabbit.electronicapp;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAggregator implements IEventAggregator{
    //logic
    private Map<EventType, List<EventHandler>> handlerMap = new HashMap<>();
    /**
     * Fire event -> pass the event to interested handlers
     * @param event
     * @param <T>
     */
    @Override
    public <T extends Event> void fireEvent(T event) {
        if(handlerMap.containsKey(event.getEventType())){
            handlerMap.get(event.getEventType()).forEach(handler -> handler.handle(event));
        }

    }

    /**
     * Register event handler
     * @param eventType
     * @param handler
     * @param <T>
     */
    @Override
    public <T extends EventType> void registerHandler(T eventType, EventHandler handler) {
        if(!handlerMap.containsKey(eventType)) handlerMap.put(eventType, new ArrayList<>());
        handlerMap.get(eventType).add(handler);
    }

}
