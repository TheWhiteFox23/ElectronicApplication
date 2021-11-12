package cz.thewhiterabbit.electronicapp;

public class GUIEventAggregator extends EventAggregator{
    private static GUIEventAggregator guiEventAggregator = new GUIEventAggregator();
    private GUIEventAggregator(){};
    public static GUIEventAggregator getInstance(){
        return guiEventAggregator;
    }
}
