package cz.thewhiterabbit.electronicapp.canvas.model;

import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import cz.thewhiterabbit.electronicapp.events.CanvasEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasPaintEvent;

public abstract class RelativeModel extends CanvasModel {
    private double originX;
    private double originY;
    private double zoomAspect;
    private final double[] zoomAspects = new double[]{0.1,0.2, 0.3, 0.5, 0.7, 1.0, 1.5, 2.0, 3.0, 5.0, 7.0};

    public RelativeModel() {
        super();
        originX = 0;
        originY = 0;
        zoomAspect = 1.0;
        registerListeners();
    }

    /********* GETTERS AND SETTERS ***********/
    public double getOriginX() {return originX;}
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
        }else if(e.getDeltaY() < 0 && index >0){
            index--;
        }
        setZoomAspect(zoomAspects[index]);
        getInnerEventAggregator().fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
    }

    @Override
    public void add(CanvasObject object) {
        updatePaintProperties(object);
        super.add(object);
    }


    private void moveOriginBy(double deltaX, double deltaY){
        originX += deltaX;
        originY += deltaY;
        getAll().forEach(o ->{
            o.setLocationX(o.getLocationX() + deltaX);
            o.setLocationY(o.getLocationY() + deltaY);
        });
    }

}