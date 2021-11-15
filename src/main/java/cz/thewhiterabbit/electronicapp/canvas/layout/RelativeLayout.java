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


    /********* METHODS ***********************/
    private void registerListeners() {
        getCanvasEventAggregator().registerHandler(CanvasMouseEvent.CANVAS_DRAGGED, e ->{
            CanvasMouseEvent event = ((CanvasMouseEvent) e);
            double deltaX = event.getX() - event.getLastX();
            double deltaY = event.getY() - event.getLastY();
            moveOriginBy(deltaX, deltaY);
            getCanvasEventAggregator().fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
        });

        getCanvasEventAggregator().registerHandler(CanvasMouseEvent.OBJECT_DRAGGED, e->{
            CanvasObject canvasObject = ((CanvasMouseEvent)e).getObject();
            if(canvasObject != null && containsObject(canvasObject) && canvasObject.isSelected()){
                CanvasMouseEvent event = ((CanvasMouseEvent) e);
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
        });

        getCanvasEventAggregator().registerHandler(CanvasMouseEvent.OBJECT_DRAG_DROPPED, e->{
            CanvasObject canvasObject = ((CanvasMouseEvent)e).getObject();
            if(canvasObject != null && containsObject(canvasObject) && canvasObject.isSelected()){
                CanvasMouseEvent event = ((CanvasMouseEvent) e);
                double deltaX = (event.getX() - event.getStartX())/zoomAspect;
                double deltaY = (event.getY() - event.getStartY())/zoomAspect;
                getAll().forEach(o -> {
                    if(o.isSelected()){
                        RelativeLayoutProperties properties = (RelativeLayoutProperties)o.getLayoutProperties();
                        properties.setRelativeLocationY(properties.getRelativeLocationY() + deltaY);
                        properties.setRelativeLocationX(properties.getRelativeLocationX() + deltaX);
                    }
                });

                getCanvasEventAggregator().fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
            }
        });




        getCanvasEventAggregator().registerHandler(CanvasMouseEvent.CANVAS_SCROLLED, e->{
            //get currant zoom aspect index
            int index = 0;
            for(int i =  0; i< zoomAspects.length; i++){
                if (zoomAspects[i] == zoomAspect) {
                    index = i;
                    break;
                }
            }
            if(((CanvasMouseEvent)e).getDeltaY() > 0 && index <zoomAspects.length -1){
                index++;
            }else if(((CanvasMouseEvent)e).getDeltaY() < 0 && index >0){
                index--;
            }
            setZoomAspect(zoomAspects[index]);
            getCanvasEventAggregator().fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
        });
    }

    @Override
    public void add(CanvasObject object) {
        //set relative coordinates
        updatePaintProperties(object);
        super.add(object);
    }

    @Override
    public <T extends LayoutProperties> void add(CanvasObject object, T layoutProperties) {
        super.add(object, layoutProperties);
    }

    @Override
    protected void updatePaintProperties(CanvasObject object) {
        RelativeLayoutProperties properties = (RelativeLayoutProperties)object.getLayoutProperties();
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

    private void setZoomAspect(double zoomAspect){
        this.zoomAspect = zoomAspect;
        getAll().forEach(o -> updatePaintProperties(o));
    }

}
