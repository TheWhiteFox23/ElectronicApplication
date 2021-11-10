package cz.thewhiterabbit.electronicapp.canvas;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import javafx.scene.input.MouseEvent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanvasObjectEventManager {
    private CanvasGridObject object;
    private Map<EventType, List<EventHandler>> handlerMap = new HashMap<>();
    public CanvasObjectEventManager(CanvasGridObject object)
    {
        this.object = object;
    }
    public void handleEvent(Event e)
    {
        /************ MOUSE MOVEMENT **************/
        if(e.getEventType() == MouseEvent.MOUSE_MOVED){
            MouseEvent event = (MouseEvent) e;
            boolean isInside = object.isInside(event.getX(), event.getY());
            if(isInside && !object.isHovered()){
                object.setHovered(true);
                object.fireEvent(copyMouseEvent(event, MouseEvent.MOUSE_ENTERED));
            }else if(isInside && object.isHovered()){
                object.fireEvent(event);
            }else if(object.isHovered()){
                object.setHovered(false);
                object.fireEvent(copyMouseEvent(event, MouseEvent.MOUSE_EXITED));
            }
        }

    }

    public <T extends Event> void fireEvent(T event) {
        if(handlerMap.containsKey(event.getEventType())){
            handlerMap.get(event.getEventType()).forEach(handler -> handler.handle(event));
        }

    }

    public <T extends EventType> void addEventHandler(T eventType, EventHandler handler) {
        if(!handlerMap.containsKey(eventType)) handlerMap.put(eventType, new ArrayList<>());
        handlerMap.get(eventType).add(handler);
    }

    private MouseEvent copyMouseEvent(MouseEvent event, EventType eventType){
        return new MouseEvent(eventType, event.getX(), event.getY(), event.getSceneX(),
                    event.getScreenY(), event.getButton(), event.getClickCount(), event.isShiftDown(),
                    event.isControlDown(),event.isAltDown(),event.isMetaDown(), event.isPrimaryButtonDown(),
                    event.isMiddleButtonDown(), event.isSecondaryButtonDown(), event.isSynthesized(),
                    event.isPopupTrigger(), event.isStillSincePress(),event.getPickResult());
    }
}
