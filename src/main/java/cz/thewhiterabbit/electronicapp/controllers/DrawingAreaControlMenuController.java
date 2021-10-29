package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.listeners.DrawingAreaControlMenuListener;

import java.util.ArrayList;
import java.util.List;

public class DrawingAreaControlMenuController {
    private List<DrawingAreaControlMenuListener> listeners = new ArrayList<>();

    public void addCanvasControlMenuListener(DrawingAreaControlMenuListener listener){
        this.listeners.add(listener);
    }


}
