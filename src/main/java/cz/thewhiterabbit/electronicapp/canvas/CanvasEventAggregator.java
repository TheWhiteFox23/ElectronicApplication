package cz.thewhiterabbit.electronicapp.canvas;

import cz.thewhiterabbit.electronicapp.EventAggregator;

public class CanvasEventAggregator extends EventAggregator {
    public static final CanvasEventAggregator canvasEventAggregator= new CanvasEventAggregator();
    private CanvasEventAggregator(){}
    public static CanvasEventAggregator getInstance(){return canvasEventAggregator;}
}
