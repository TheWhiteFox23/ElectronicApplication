package cz.thewhiterabbit.electronicapp.canvas.layout;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage layout of the components on the screen;
 */
public abstract class CanvasLayout {
    private Canvas canvas; //todo manage width and height different way
    private EventAggregator canvasEventAggregator;
    private List<CanvasObject> canvasObjects;

    public CanvasLayout(Canvas canvas, EventAggregator eventAggregator){
        this.canvas = canvas;
        this.canvasEventAggregator = eventAggregator; //todo get aggregator through injections
        this.canvasObjects = new ArrayList<>();
    }

    /************GETTERS AND SETTERS***********/
    public Canvas getCanvas() {return canvas;}
    public EventAggregator getCanvasEventAggregator() {return canvasEventAggregator;}

    /************METHODS**********************/
    public void add(CanvasObject object){
        canvasObjects.add(object);
    }

    public <T extends  LayoutProperties> void add(CanvasObject object, T layoutProperties){
        layoutProperties.addPropertiesListener(()->{
            updatePaintProperties(object);
        });
        object.setLayoutProperties(layoutProperties);
        updatePaintProperties(object);
        add(object);
    }

    protected abstract void updatePaintProperties(CanvasObject object);

    public List<CanvasObject> getVisible(){
        List<CanvasObject> visibleObjects = new ArrayList<>();
        double canvasHeight = getCanvas().getHeight();
        double canvasWidth = getCanvas().getWidth();
        canvasObjects.forEach(o ->{
            if(o.isVisible(canvasWidth, canvasHeight))visibleObjects.add(o);
        });
        return visibleObjects;
    }

    public List<CanvasObject> getAll(){
        return canvasObjects;
    }

    public void remove(CanvasObject o){
        canvasObjects.remove(o);
    }

    public boolean containsObject(CanvasObject o){
        return canvasObjects.contains(o);
    }

    public CanvasObject getObject(double x, double y){
        List<CanvasObject> visible = getVisible();
        for (int i = 0; i<visible.size(); i++){
            CanvasObject o = visible.get(i);
            if(o.isInBounds(x, y)) return o;
        }
        return null;
    }

    public List<CanvasObject> getInBounds(double locationX, double locationY, double width, double height){
        List<CanvasObject> visibleObjects = new ArrayList<>();
        canvasObjects.forEach(o ->{
            if(o.isInBounds(locationX, locationY, width, height))visibleObjects.add(o);
        });
        return visibleObjects;
    }




}
