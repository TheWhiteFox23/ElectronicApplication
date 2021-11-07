package cz.thewhiterabbit.electronicapp;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAggregator implements IEventAggregator{
    //instance
    private static EventAggregator eventAggregator = new EventAggregator();
    //logic
    private Map<EventType, List<EventHandler>> handlerMap = new HashMap<>();

    private EventAggregator(){}

    /**
     * Fire event -> pass the event to interested handlers
     * @param event
     * @param <T>
     */
    @Override
    public <T extends Event> void fireEvent(T event) {
        //notify handlers with corresponding types
        if(handlerMap.containsKey(event.getEventType())){ //TODO handling
            handlerMap.get(event.getEventType()).forEach(handler -> handler.handle(event));
        }else{
            System.out.println("Aggregator do not contain: " + event.getEventType());
        }

    }

    @Override
    public <T extends EventType> void registerHandler(T eventType, EventHandler handler) {
        if(!handlerMap.containsKey(eventType)) handlerMap.put(eventType, new ArrayList<>());
        handlerMap.get(eventType).add(handler);
    }

    /**
     * Return singleton instance of the eventAggregator
     * @return
     */
    public static EventAggregator getInstance(){
        return eventAggregator;
    }
}
