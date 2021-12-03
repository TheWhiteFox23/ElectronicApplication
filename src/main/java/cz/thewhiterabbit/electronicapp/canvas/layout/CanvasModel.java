package cz.thewhiterabbit.electronicapp.canvas.layout;

import cz.thewhiterabbit.electronicapp.Document;
import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Is used as the paint cache for canvas, is specific to given document
 */
public abstract class CanvasModel {
    private Canvas canvas; //todo manage width and height different way
    private EventAggregator canvasEventAggregator;
    private List<CanvasObject> canvasObjects;
    private Document document;

    public CanvasModel(Canvas canvas, EventAggregator eventAggregator){
        this.canvas = canvas;
        this.canvasEventAggregator = eventAggregator; //todo get aggregator through injections
        this.canvasObjects = new ArrayList<>();
    }

    /************GETTERS AND SETTERS***********/
    public Canvas getCanvas() {return canvas;}
    public EventAggregator getCanvasEventAggregator() {return canvasEventAggregator;}
    public void setCanvas(Canvas canvas) {this.canvas = canvas;}
    public void setCanvasEventAggregator(EventAggregator canvasEventAggregator) {this.canvasEventAggregator = canvasEventAggregator;}
    public List<CanvasObject> getCanvasObjects() {return canvasObjects;}
    public void setCanvasObjects(List<CanvasObject> canvasObjects) {this.canvasObjects = canvasObjects;}
    public Document getDocument() {return document;}
    public void setDocument(Document document) {this.document = document;}

    /************METHODS**********************/
    //TODO Modify to add and remove handlers
    //TODO link to the add DocumentComponent
    public void add(CanvasObject object){
        object.getDocumentComponent().addPropertiesListener(()->{
            updatePaintProperties(object);
        });
        updatePaintProperties(object);
        object.getActiveZones().forEach(a -> add(a));
        addObject(object);
    }

    protected abstract void updatePaintProperties(CanvasObject object);

    /**
     * Return list of objects currently visible on the screen
     * @return
     */
    public List<CanvasObject> getVisible(){
        List<CanvasObject> visibleObjects = new ArrayList<>();
        double canvasHeight = getCanvas().getHeight();
        double canvasWidth = getCanvas().getWidth();
        canvasObjects.forEach(o ->{
            if(o.isVisible(canvasWidth, canvasHeight))visibleObjects.add(o);
        });
        return visibleObjects;
    }

    //TODO link to remove DocumentComponent
    public void remove(CanvasObject o){
        canvasObjects.remove(o);
    }

    public boolean containsObject(CanvasObject o){
        return canvasObjects.contains(o);
    }

    /**
     * Look for abject on given scree coordinates and returns it if there is any.
     * @param x X coordinate on the screen
     * @param y Y coordinate on the screen
     * @return Object if there is any or NULL
     */
    public CanvasObject getObject(double x, double y){
        List<CanvasObject> visible = getVisible();
        for (int i = visible.size()-1; i>=0; i--){
            CanvasObject o = visible.get(i);
            if(o.isInBounds(x, y)) return o;
        }
        return null;
    }

    /**
     * Get all object in the given bounds of the screen
     * @param locationX
     * @param locationY
     * @param width
     * @param height
     * @return List of CanvasObjects in given bounds
     */
    public List<CanvasObject> getInBounds(double locationX, double locationY, double width, double height){
        List<CanvasObject> visibleObjects = new ArrayList<>();
        canvasObjects.forEach(o ->{
            if(o.isInBounds(locationX, locationY, width, height))visibleObjects.add(o);
        });
        return visibleObjects;
    }

    /**
     * Add object to the canvasObjects list to the proper index
     * @param canvasObject
     */
    //TODO link do add DocumentComponent
    private void addObject(CanvasObject canvasObject){
        Priority objectPriority = canvasObject.getPriority();
        int index = getIndex(objectPriority);
        canvasObjects.add(index, canvasObject);

    }

    public List<CanvasObject> getAll(){
        return canvasObjects;
    }

    /**
     * Calculate index for inserting the object base on the priority
     * @param priority
     * @return
     */
    private int getIndex(Priority priority){
        for(int i = 0; i< canvasObjects.size(); i++){
            CanvasObject object = canvasObjects.get(i);
            if(object.getPriority().getPriorityIndex()> priority.getPriorityIndex()){
                return i;
            }
        }
        return canvasObjects.size();
    }






}

