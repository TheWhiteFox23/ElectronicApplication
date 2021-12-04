package cz.thewhiterabbit.electronicapp.canvas.objects;


import cz.thewhiterabbit.electronicapp.canvas.model.Priority;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ActivePoint extends CanvasObject{
    private CanvasObject owner;
    private int relativeLocationX;
    private int relativeLocationY;

    /**** CONSTRUCTORS ****/
    public ActivePoint(CanvasObject owner, int relativeLocationX, int relativeLocationY) {
        setOwner(owner);
        this.relativeLocationX = relativeLocationX;
        this.relativeLocationY = relativeLocationY;
        //getDocumentComponent().set(0,0,1,1);
        setPriority(Priority.ALWAYS_ON_TOP);
    }

    public ActivePoint() {
        this.relativeLocationX = 0;
        this.relativeLocationY = 0;
    }

    /***** OVERRIDES *****/
    @Override
    public void paint(GraphicsContext gc) {
        double height = getHeight()*0.5;
        double locationX = getLocationX()-height/2;
        double locationY = getLocationY()-height/2;
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillOval(locationX, locationY, height, height);
    }

    @Override
    public boolean isInBounds(double x, double y) {
        double height = getHeight()*0.5;
        double locationX = getLocationX()-height/2;
        double locationY = getLocationY()-height/2;
        return ((x>=locationX && x<=locationX+height)&&(y>=locationY && y<= locationY+height));
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    protected void onObjectEntered(Event e) {
        super.onObjectEntered(e);
        CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(CanvasMouseEvent.ACTIVE_POINT_ENTERED, this);
        getEventAggregator().fireEvent(canvasMouseEvent);
    }

    @Override
    protected void onObjectExited(Event e) {
        super.onObjectExited(e);
        CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(CanvasMouseEvent.ACTIVE_POINT_EXITED, this);
        getEventAggregator().fireEvent(canvasMouseEvent);
    }

    @Override
    protected void onObjectDropped(Event e) {
        super.onObjectDropped(e);
        e.consume();
        fireDragEvent((CanvasMouseEvent) e, CanvasMouseEvent.ACTIVE_POINT_DRAG_DROPPED);
    }

    @Override
    protected void onObjectDragged(Event e) {
        super.onObjectDragged(e);
        e.consume();
        fireDragEvent((CanvasMouseEvent) e, CanvasMouseEvent.ACTIVE_POINT_DRAGGED);
    }

    @Override
    protected void onDragDetected(Event e) {
        super.onDragDetected(e);
        e.consume();
        fireDragEvent((CanvasMouseEvent) e, CanvasMouseEvent.ACTIVE_POINT_DRAG_DETECT);
    }

    private void fireDragEvent(CanvasMouseEvent e, EventType<CanvasMouseEvent> activePointDragDetect) {
        CanvasMouseEvent oe = e; //ORIGINAL EVENT
        CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(activePointDragDetect,
                oe.getStartX(), oe.getStartY(), oe.getLastX(), oe.getLastY(), oe.getX(), oe.getY(), this);
        getEventAggregator().fireEvent(canvasMouseEvent);
    }

    /****** METHODS *******/

    private void initLocation(CanvasObject owner) {
        int gridX = owner.getGridX()+relativeLocationX;
        int gridY = owner.getGridY()+relativeLocationY;

        /*EventAggregator eventAggregator = GUIEventAggregator.getInstance();
        DocumentObjectCommand command = new DocumentObjectCommand(DocumentObjectCommand.CHANGE_PROPERTY,getDocumentComponent(), getDocumentComponent().gridX(), getDocumentComponent().gridX().getValue(), gridX);
        eventAggregator.fireEvent(command);
        command = new DocumentObjectCommand(DocumentObjectCommand.CHANGE_PROPERTY,getDocumentComponent(), getDocumentComponent().gridY(), getDocumentComponent().gridY().getValue(), gridY);
        eventAggregator.fireEvent(command);*/
        //TODO fire properties change event
    }

    /****** GETTERS AND SETTERS ******/
    public CanvasObject getOwner() {
        return owner;
    }

    public void setOwner(CanvasObject owner) {
        this.owner = owner;
        /*owner.getDocumentComponent().addPropertiesListener(new PropertiesListener() { //TODO MOVE TO AGGREGATOR
            @Override
            public void onPropertiesChange() {
                initLocation(owner);
            }
        });*/
    }

    public int getRelativeLocationX() {return relativeLocationX;}

    public void setRelativeLocationX(int relativeLocationX) {this.relativeLocationX = relativeLocationX;}

    public int getRelativeLocationY() {return relativeLocationY;}

    public void setRelativeLocationY(int relativeLocationY) {this.relativeLocationY = relativeLocationY;}
}
