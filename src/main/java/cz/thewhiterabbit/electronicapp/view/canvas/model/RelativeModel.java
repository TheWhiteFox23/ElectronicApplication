package cz.thewhiterabbit.electronicapp.view.canvas.model;

import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.events.CanvasEvent;
import cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.view.events.CanvasPaintEvent;

public abstract class RelativeModel extends CanvasModel {
    private double originX;
    private double originY;
    private double zoomAspect;
    private final double[] zoomAspects = new double[]{0.1,0.2, 0.3, 0.5, 0.8, 1.0, 1.5, 2.0, 3.0, 5.0, 7.0,8.0,9.0,10.0};
    //TODO while zoom Aspect is set to 0.7 weird behavior while moving items

    public RelativeModel() {
        super();
        originX = 0;
        originY = 0;
        zoomAspect = 2.0;
        registerListeners();
    }

    /********* GETTERS AND SETTERS ***********/
    public double getOriginX() {
        return originX;
    }
    public void setOriginX(double originX) {this.originX = originX;}
    public double getOriginY() {return originY;}
    public void setOriginY(double originY) {this.originY = originY;}
    public double getZoomAspect() {return zoomAspect;}

    private void setZoomAspect(double zoomAspect){
        this.zoomAspect = zoomAspect;
        getAll().forEach(o -> updatePaintProperties(o));
    }

    /********* METHODS ***********************/
    protected void registerListeners() {
        getInnerEventAggregator().addEventHandler(CanvasMouseEvent.CANVAS_DRAGGED, e ->{
            onCanvasDragged((CanvasMouseEvent) e);
        });

        getInnerEventAggregator().addEventHandler(CanvasMouseEvent.OBJECT_DRAGGED, e->{
            onObjectDragged((CanvasMouseEvent) e);
        });

        getInnerEventAggregator().addEventHandler(CanvasMouseEvent.OBJECT_DRAG_DROPPED, e->{
            onObjectDragDropped((CanvasMouseEvent) e);
        });

        getInnerEventAggregator().addEventHandler(CanvasMouseEvent.CANVAS_SCROLLED, e->{
            onCanvasScrolled((CanvasMouseEvent) e);
        });

        getInnerEventAggregator().addEventHandler(CanvasMouseEvent.OBJECT_MOVING, e->{
            onObjectMoving((CanvasMouseEvent) e);
        });

        getInnerEventAggregator().addEventHandler(CanvasMouseEvent.OBJECT_MOVED, e->{
            onObjectMoved((CanvasMouseEvent) e);
        });
    }

    private void onCanvasDragged(CanvasMouseEvent e) {
        CanvasMouseEvent event = e;
        double deltaX = event.getX() - event.getLastX();
        double deltaY = event.getY() - event.getLastY();
        moveOriginBy(deltaX, deltaY);
        getInnerEventAggregator().fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));
    }

    protected abstract void onObjectDragged(CanvasMouseEvent e);

    protected abstract void onObjectDragDropped(CanvasMouseEvent e);

    protected abstract void onObjectMoved(CanvasMouseEvent e);

    protected abstract void onObjectMoving(CanvasMouseEvent e);

    private void onCanvasScrolled(CanvasMouseEvent e) {
        //get currant zoom aspect index
        int index = 0;
        for(int i =  0; i< zoomAspects.length; i++){
            if (zoomAspects[i] == zoomAspect) {
                index = i;
                break;
            }
        }
        if(e.getDeltaY() > 0 && index <zoomAspects.length -1){
            index++;
            //moveOriginBy(-50, -50);
        }else if(e.getDeltaY() < 0 && index >0){
            index--;
            //moveOriginBy(50, 50);
        }

        setZoomAspect(zoomAspects[index]);
        getInnerEventAggregator().fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
    }

    @Override
    public void add(CanvasObject object) {
        if(getAll().contains(object))return;
        updatePaintProperties(object);
        super.add(object);
    }


    protected void moveOriginBy(double deltaX, double deltaY){
        originX += Math.round(deltaX);
        originY += Math.round(deltaY);
        getAll().forEach(o ->{
            o.setLocationX(o.getLocationX() + Math.round(deltaX));
            o.setLocationY(o.getLocationY() + Math.round(deltaY));
        });
    }

    public void center(){
        double deltaX = getCanvas().getWidth()/2-originX;
        double deltaY = getCanvas().getHeight()/2 - originY;
        moveOriginBy(deltaX, deltaY);
    }

}
