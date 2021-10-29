package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.listeners.ComponentInfoPaneListener;

import java.util.ArrayList;
import java.util.List;

public class ComponentInfoPaneController {
    private List<ComponentInfoPaneListener> listeners = new ArrayList<>();
    public void addComponentInfoPaneListener(ComponentInfoPaneListener listener){
        this.listeners.add(listener);
    }
}
