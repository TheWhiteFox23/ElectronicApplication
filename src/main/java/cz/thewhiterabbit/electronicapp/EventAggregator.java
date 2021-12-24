package cz.thewhiterabbit.electronicapp;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventAggregator{
    //logic
    private Map<EventType, List<EventHandler>> handlerMap = new HashMap<>();
    /**
     * Fire event -> pass the event to interested handlers
     * @param event
     * @param <T>
     */
    public <T extends Event> void fireEvent(T event) {
        fireEvent(event.getEventType(), event);
    }

    public <T extends Event> void fireEvent(EventType eventType, T event) {
        if(handlerMap.containsKey(eventType)){
            handlerMap.get(eventType).size();
            for(int i = 0; i< handlerMap.get(eventType).size(); i++){
                EventHandler handler = handlerMap.get(eventType).get(i);
                handler.handle(event);
            }
        }
        if(eventType.getSuperType() != null){
            fireEvent(eventType.getSuperType(), event);
        }
    }

    /**
     * Register event handler
     * @param eventType
     * @param handler
     * @param <T>
     */
    public <T extends EventType> void addEventHandler(T eventType, EventHandler handler) {
        if(!handlerMap.containsKey(eventType)) {
            handlerMap.put(eventType, new ArrayList<>());
        }
        handlerMap.get(eventType).add(handler);

    }

    public void removeEventHandler(EventType eventType, EventHandler eventHandler) {
        if(handlerMap.containsKey(eventType)){
            ListIterator iterator = handlerMap.get(eventType).listIterator();
            while (iterator.hasNext()){
                if(iterator.next() == eventHandler){
                    iterator.remove();
                }
            }
        }
    }

    public boolean contains(EventHandler eventHandler){
        for(List<EventHandler> l:handlerMap.values()){
            if (l.contains(eventHandler)) return true;
        }
        return false;
    }
}
