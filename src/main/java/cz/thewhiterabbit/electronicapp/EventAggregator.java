package cz.thewhiterabbit.electronicapp;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.*;
import java.util.concurrent.Semaphore;

public class EventAggregator implements IEventAggregator{
    //logic
    private Map<EventType, List<EventHandler>> handlerMap = new HashMap<>();
    Semaphore semaphore = new Semaphore(10);
    /**
     * Fire event -> pass the event to interested handlers
     * @param event
     * @param <T>
     */
    @Override
    public <T extends Event> void fireEvent(T event) {
        if(handlerMap.containsKey(event.getEventType())){
            //handlerMap.get(event.getEventType()).forEach(handler -> handler.handle(event));
            handlerMap.get(event.getEventType()).size();
            for(int i = 0; i< handlerMap.get(event.getEventType()).size(); i++){
                EventHandler handler = handlerMap.get(event.getEventType()).get(i);
                handler.handle(event);
                //System.out.println(event.getEventType());
            }
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
        if(!handlerMap.containsKey(eventType)) {
            handlerMap.put(eventType, new ArrayList<>());
        }
        handlerMap.get(eventType).add(handler);

    }

}
