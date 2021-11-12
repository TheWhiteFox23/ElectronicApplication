package cz.thewhiterabbit.electronicapp.canvas;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import cz.thewhiterabbit.electronicapp.events.CanvasEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class CanvasEventManager {
    /**
     * Get full access to drawing area and manage its events
     * @param drawingCanvas
     */
    private DrawingCanvas drawingCanvas;
    private CanvasObject hoveredObject;
    private EventAggregator canvasEventAggregator;

    //EVENT LOGIC
    private EventCrate canvasDragEvent;
    private EventCrate objectDragEvent;
    private EventCrate selectionDragEvent;

    private double dragStartX = -1;
    private double dragStartY = -1;
    private double lastDragX = -1;
    private double lastDragY = -1;
    public CanvasEventManager(DrawingCanvas drawingCanvas){
        this.drawingCanvas = drawingCanvas;
        canvasEventAggregator = CanvasEventAggregator.getInstance();
        /***** EVENT CRATES ****/
        canvasDragEvent = new EventCrate();
        objectDragEvent = new EventCrate();
        selectionDragEvent = new EventCrate();
        /**** LOGIC ****/
        registerListeners();
    }

    private void registerListeners() {
        Canvas canvas = drawingCanvas.getCanvas();
        registerMouseMovementListeners(canvas);
        registerCanvasScrolledEvent(canvas);
        registerMousePressEvents(canvas);
        registerDragEvent(canvas, CanvasMouseEvent.CANVAS_DRAG_DETECTED, CanvasMouseEvent.CANVAS_DRAGGED,
                CanvasMouseEvent.CANVAS_DRAG_DROPPED, canvasDragEvent);
        registerDragEvent(canvas, CanvasMouseEvent.OBJECTS_DRAG_DETECTED, CanvasMouseEvent.OBJECT_DRAGGED,
                CanvasMouseEvent.OBJECT_DRAG_DROPPED, objectDragEvent);
        registerDragEvent(canvas, CanvasMouseEvent.CANVAS_SELECTION_DETECTED, CanvasMouseEvent.CANVAS_SELECTION_MOVE,
                CanvasMouseEvent.CANVAS_SELECTION_FINISH, selectionDragEvent);
    }

    private void registerMousePressEvents(Canvas canvas) {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e->{
            /***** CANVAS DRAG EVENT ****/
            if(e.isMiddleButtonDown()){
                canvasDragEvent.startX =e.getX(); canvasDragEvent.startY = e.getY();
                canvasDragEvent.lastX = e.getX(); canvasDragEvent.lastY = e.getY();
            }
            /***** OBJECT DRAG EVENT ****/
            if(e.isPrimaryButtonDown() && hoveredObject!= null){
                objectDragEvent.startX =e.getX(); objectDragEvent.startY = e.getY();
                objectDragEvent.lastX = e.getX(); objectDragEvent.lastY = e.getY();
            }
            /***** SELECTION DRAG EVENT ****/
            if(e.isPrimaryButtonDown() && hoveredObject == null){
                selectionDragEvent.startX =e.getX(); selectionDragEvent.startY = e.getY();
                selectionDragEvent.lastX = e.getX(); selectionDragEvent.lastY = e.getY();
            }
        });
    }

    private void registerCanvasScrolledEvent(Canvas canvas) {
        canvas.addEventHandler(ScrollEvent.SCROLL, e -> {
            canvasEventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.CANVAS_SCROLLED, e.getDeltaY()));
        });

    }


    private void registerDragEvent(Canvas canvas, EventType start, EventType move, EventType finish, EventCrate eventCrate){
        canvas.addEventHandler(MouseEvent.DRAG_DETECTED, e->{
            if(eventCrate.startX != -1){
                CanvasMouseEvent canvasMouseEvent= new CanvasMouseEvent(start, eventCrate.startX, eventCrate.startY, eventCrate.lastX,  eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                canvasEventAggregator.fireEvent(canvasMouseEvent);
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->{
            if(eventCrate.startX != -1) {
                CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(move, eventCrate.startX, eventCrate.startY, eventCrate.lastX,  eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                canvasEventAggregator.fireEvent(canvasMouseEvent);

                eventCrate.lastX = e.getX();
                eventCrate.lastY = e.getY();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e->{
            if(eventCrate.startX != -1){
                CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(finish, eventCrate.startX, eventCrate.startY, eventCrate.lastX,  eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                canvasEventAggregator.fireEvent(canvasMouseEvent);
                eventCrate.lastX = -1;
                eventCrate.lastY = -1;
                eventCrate.startX = -1;
                eventCrate.startY = -1;
            }
        });
    }

    private void registerMouseMovementListeners(Canvas canvas) {
        /******* MOUSE MOVED EVENT **********/ //TODO refactor to only fire specific events by event aggregator and object reaction is specified in the object it self
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, e ->{
            CanvasObject object = drawingCanvas.getCanvasLayout().getObject(e.getX(), e.getY());

            /********* MOUSE EXITED OBJECT ***********/
            if(hoveredObject != null && hoveredObject!=object){
                CanvasMouseEvent canvasMouseEvents = new CanvasMouseEvent(CanvasMouseEvent.OBJECT_EXITED, e.getX(), e.getY(), hoveredObject);
                canvasEventAggregator.fireEvent(canvasMouseEvents);
                hoveredObject = null;

            }
            /********* MOUSE ENTERED OBJECT *********/
            if(object != null && !object.isHovered()){
                hoveredObject = object;
                CanvasMouseEvent canvasMouseEvents = new CanvasMouseEvent(CanvasMouseEvent.OBJECT_ENTERED, e.getX(), e.getY(), hoveredObject);
                canvasEventAggregator.fireEvent(canvasMouseEvents);
            }
        });
    }

    private MouseEvent copyMouseEvent(EventType eventType, MouseEvent event ){
        return new MouseEvent(eventType, event.getX(), event.getY(), event.getSceneX(),
                event.getScreenY(), event.getButton(), event.getClickCount(), event.isShiftDown(),
                event.isControlDown(),event.isAltDown(),event.isMetaDown(), event.isPrimaryButtonDown(),
                event.isMiddleButtonDown(), event.isSecondaryButtonDown(), event.isSynthesized(),
                event.isPopupTrigger(), event.isStillSincePress(),event.getPickResult());
    }

    class EventCrate {
        double startX = -1;
        double startY = -1;
        double lastX = - 1;
        double lastY = -1;
        CanvasObject object = null;
    }
}
