package cz.thewhiterabbit.electronicapp.canvas.layout;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import cz.thewhiterabbit.electronicapp.events.CanvasEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import javafx.scene.canvas.Canvas;

public class RelativeLayout extends CanvasLayout{
    private double originX;
    private double originY;
    private double zoomAspect;
    private final double[] zoomAspects = new double[]{0.1,0.2, 0.3, 0.5, 0.7, 1.0, 1.5, 2.0, 3.0, 5.0, 7.0};

    public RelativeLayout(Canvas canvas, EventAggregator eventAggregator) {
        super(canvas, eventAggregator);
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
        getCanvasEventAggregator().registerHandler(CanvasMouseEvent.CANVAS_DRAGGED, e ->{
            onCanvasDragged((CanvasMouseEvent) e);
        });

        getCanvasEventAggregator().registerHandler(CanvasMouseEvent.OBJECT_DRAGGED, e->{
            onObjectDragged((CanvasMouseEvent) e);
        });

        getCanvasEventAggregator().registerHandler(CanvasMouseEvent.OBJECT_DRAG_DROPPED, e->{
            onObjectDragDropped((CanvasMouseEvent) e);
        });

        getCanvasEventAggregator().registerHandler(CanvasMouseEvent.CANVAS_SCROLLED, e->{
            onCanvasScrolled((CanvasMouseEvent) e);
        });
    }

    private void onCanvasDragged(CanvasMouseEvent e) {
        CanvasMouseEvent event = e;
        double deltaX = event.getX() - event.getLastX();
        double deltaY = event.getY() - event.getLastY();
        moveOriginBy(deltaX, deltaY);
        getCanvasEventAggregator().fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
    }

    protected void onObjectDragged(CanvasMouseEvent e) {
        CanvasObject canvasObject = e.getObject();
        if(canvasObject != null && containsObject(canvasObject) && canvasObject.isSelected()){
            CanvasMouseEvent event = e;
            double deltaX = event.getX() - event.getLastX();
            double deltaY = event.getY() - event.getLastY();
            getAll().forEach(o -> {
                if(o.isSelected()){
                    o.setLocationY(o.getLocationY() + deltaY);
                    o.setLocationX(o.getLocationX() + deltaX);
                }
            });
            getCanvasEventAggregator().fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
        }
    }

    protected void onObjectDragDropped(CanvasMouseEvent e) {
        CanvasObject canvasObject = e.getObject();
        if(canvasObject != null && containsObject(canvasObject) && canvasObject.isSelected()){
            CanvasMouseEvent event = e;
            double deltaX = (event.getX() - event.getStartX())/zoomAspect;
            double deltaY = (event.getY() - event.getStartY())/zoomAspect;
            getAll().forEach(o -> {
                if(o.isSelected()){
                    LayoutProperties properties = o.getLayoutProperties();
                    properties.setRelativeLocationY(properties.getRelativeLocationY() + deltaY);
                    properties.setRelativeLocationX(properties.getRelativeLocationX() + deltaX);
                }
            });

            getCanvasEventAggregator().fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
        }
    }

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
        getCanvasEventAggregator().fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
    }

    @Override
    public void add(CanvasObject object) {
        //set relative coordinates
        updatePaintProperties(object);
        super.add(object);
    }

    @Override
    protected void updatePaintProperties(CanvasObject object) {
        LayoutProperties properties = object.getLayoutProperties();
        object.setLocationX(originX + properties.getRelativeLocationX() * zoomAspect);
        object.setLocationY(originY + properties.getRelativeLocationY() * zoomAspect);
        object.setWidth(properties.getWidth() * zoomAspect);
        object.setHeight(properties.getHeight() * zoomAspect);
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
