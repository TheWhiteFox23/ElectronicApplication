package cz.thewhiterabbit.electronicapp.view.events;

import cz.thewhiterabbit.electronicapp.model.components.Component;
import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.io.File;

public class MenuEvent extends Event {
    public static final EventType<MenuEvent> ANY = new EventType<>(Event.ANY, "MENU_ANY");
    public static final EventType<MenuEvent> NEW_DOCUMENT = new EventType<>(ANY, "NEW_DOCUMENT");
    public static final EventType<MenuEvent> CLOSE_DOCUMENT = new EventType<>(ANY, "CLOSE_DOCUMENT");
    public static final EventType<MenuEvent> CHANGE_ACTIVE_DOCUMENT = new EventType<>(ANY, "CHANGE_ACTIVE_DOCUMENT");
    public static final EventType<MenuEvent> OPEN_FILE = new EventType<>(ANY, "OPEN_FILE");
    public static final EventType<MenuEvent> DO_OPEN_FILE = new EventType<>(ANY, "DO_OPEN_FILE");
    public static final EventType<MenuEvent> SAVE_FILE = new EventType<>(ANY, "SAVE_FILE");
    public static final EventType<MenuEvent> SAVE_FILE_AS = new EventType<>(ANY, "SAVE_FILE_AS");
    public static final EventType<MenuEvent> CLEAN_CANVAS = new EventType<>(ANY, "CLEAN_CANVAS");
    public static final EventType<MenuEvent> SHOW_INFO_DIALOG = new EventType<>(ANY, "SHOW_INFO_DIALOG");
    public static final EventType<MenuEvent> HIDE_INFO_DIALOG = new EventType<>(ANY, "HIDE_INFO_DIALOG");
    public static final EventType<MenuEvent> SWITCH_MODE_SIMULATION = new EventType<>(ANY, "SWITCH_MODE_SIMULATION");
    public static final EventType<MenuEvent> SWITCH_MODE_SCHEMATIC = new EventType<>(ANY, "SWITCH_MODE_SCHEMATIC");
    public static final EventType<MenuEvent> MODE_CHANGED = new EventType<>(ANY, "MODE_CHANGED");
    public static final EventType<MenuEvent> CLEAN_WORKPLACE = new EventType<>(ANY, "CLEAN_WORKPLACE");


    private Document document;
    private Component component;
    private File file;

    public MenuEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public MenuEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }

    public MenuEvent(EventType<? extends Event> eventType, Document document) {
        super(eventType);
        this.document = document;
    }

    public MenuEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, Document document) {
        super(o, eventTarget, eventType);
        this.document = document;
    }

    public MenuEvent(EventType<? extends Event> eventType, Component component) {
        super(eventType);
        this.component = component;
    }

    public MenuEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, Component component) {
        super(o, eventTarget, eventType);
        this.component = component;
    }

    public MenuEvent(EventType<? extends Event> eventType, File file) {
        super(eventType);
        this.file = file;
    }

    public MenuEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, File file) {
        super(o, eventTarget, eventType);
        this.file = file;
    }

    public Component getComponent() {
        return component;
    }

    public Document getDocument() {
        return document;
    }

    public File getFile() {
        return file;
    }
}
