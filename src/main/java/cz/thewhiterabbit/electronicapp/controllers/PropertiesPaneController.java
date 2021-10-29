package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.listeners.PropertiesPaneListener;

import java.util.ArrayList;
import java.util.List;

public class PropertiesPaneController {
    private List<PropertiesPaneListener> listeners = new ArrayList<>();
    public void addPropertiesPaneListener(PropertiesPaneListener listener){
        this.listeners.add(listener);
    }
}
