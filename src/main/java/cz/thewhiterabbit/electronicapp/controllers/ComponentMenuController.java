package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.listeners.ComponentMenuListener;

import java.util.ArrayList;
import java.util.List;

public class ComponentMenuController {
    private List<ComponentMenuListener> listeners = new ArrayList<>();
    public void addComponentMenuListener(ComponentMenuListener listener){
        this.listeners.add(listener);
    }
}
