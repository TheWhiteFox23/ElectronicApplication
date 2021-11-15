package cz.thewhiterabbit.electronicapp.canvas.layout;

import java.util.ArrayList;
import java.util.List;

public abstract class LayoutProperties {
    private List<PropertiesListener> propertiesListenerList;
    public LayoutProperties(){
        propertiesListenerList = new ArrayList<>();
    }
    public void addPropertiesListener(PropertiesListener propertiesListener){
        propertiesListenerList.add(propertiesListener);
    }
    public void propertiesChange(){
        propertiesListenerList.forEach(l -> l.onPropertiesChange());
    }
}
