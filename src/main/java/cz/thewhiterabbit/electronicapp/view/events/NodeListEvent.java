package cz.thewhiterabbit.electronicapp.view.events;

import cz.thewhiterabbit.electronicapp.view.components.NodeListItem;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class NodeListEvent extends Event {
    public static final EventType<NodeListEvent> ANY = new EventType<>(Event.ANY, "NODE_LIST_ANY");
    public static final EventType<NodeListEvent> NODE_CHECK_CHANGE = new EventType<>(Event.ANY, "NODE_CHECK_CHANGE");
    public static final EventType<NodeListEvent> SHOW_NODE = new EventType<>(Event.ANY, "SHOW_NODE");

    private NodeListItem nodeListItem;


    public NodeListEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public NodeListEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }

    public NodeListEvent(EventType<? extends Event> eventType, NodeListItem nodeListItem) {
        super(eventType);
        this.nodeListItem = nodeListItem;
    }

    public NodeListEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, NodeListItem nodeListItem) {
        super(o, eventTarget, eventType);
        this.nodeListItem = nodeListItem;
    }

    public NodeListItem getNodeListItem() {
        return nodeListItem;
    }
}
