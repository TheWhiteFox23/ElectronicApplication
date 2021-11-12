package cz.thewhiterabbit.electronicapp.canvas;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class EventManager {
    /**
     * Get full access to drawing area and manage its events
     * @param drawingCanvas
     */
    private DrawingCanvas drawingCanvas;
    private CanvasObject hoveredObject;
    private EventAggregator canvasEventAggregator;

    //EVENT LOGIC
    private eventCrate canvasDragEvent;
    private eventCrate objectDragEvent;
    private eventCrate selectionDragEvent;

    private double dragStartX = -1;
    private double dragStartY = -1;
    private double lastDragX = -1;
    private double lastDragY = -1;
    public EventManager(DrawingCanvas drawingCanvas){
        this.drawingCanvas = drawingCanvas;
        canvasEventAggregator = CanvasEventAggregator.getInstance();
        /***** EVENT CRATES ****/
        canvasDragEvent = new eventCrate();
        objectDragEvent = new eventCrate();
        selectionDragEvent = new eventCrate();
        /**** LOGIC ****/
        registerListeners();
    }

    private void registerListeners() {
        Canvas canvas = drawingCanvas.getCanvas();
        registerMouseMovementListeners(canvas);
        registerCanvasDragListeners(canvas);
        registerCanvasScrolledEvent(canvas);
        registerMousePressEvents(canvas);
    }

    private void registerMousePressEvents(Canvas canvas) {
    }

    private void registerCanvasScrolledEvent(Canvas canvas) {
        canvas.addEventHandler(ScrollEvent.SCROLL, e -> {
            canvasEventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.CANVAS_SCROLLED, e.getDeltaY()));
        });

    }

    private void registerCanvasDragListeners(Canvas canvas) { //TODO refactor !!!!
        /************ MOUSE_DRAGGED_EVENT ******************/
        canvas.addEventHandler(MouseEvent.DRAG_DETECTED, e->{
            /****** CANVAS DRAGGING ******/
            if(dragStartX == -1){
                dragStartX = e.getX(); lastDragX =  e.getX();
                dragStartY = e.getY(); lastDragY =  e.getY();
                CanvasMouseEvent canvasMouseEvent;
                if(e.isMiddleButtonDown()){
                    canvasMouseEvent= new CanvasMouseEvent(CanvasMouseEvent.CANVAS_DRAG_DETECTED, dragStartX, dragStartY, lastDragX, lastDragY, e.getX(), e.getY());
                    canvasEventAggregator.fireEvent(canvasMouseEvent);
                }else if(e.isPrimaryButtonDown() && hoveredObject != null){
                    canvasMouseEvent= new CanvasMouseEvent(CanvasMouseEvent.OBJECTS_DRAG_DETECTED, dragStartX, dragStartY, lastDragX, lastDragY, e.getX(), e.getY(), hoveredObject);
                    canvasEventAggregator.fireEvent(canvasMouseEvent);
                }
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->{
            /****** CANVAS DRAGGING ******/
            if(dragStartX!=-1) {
                if(e.isMiddleButtonDown()){
                    CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(CanvasMouseEvent.CANVAS_DRAGGED, dragStartX, dragStartY, lastDragX, lastDragY, e.getX(), e.getY());
                    canvasEventAggregator.fireEvent(canvasMouseEvent);
                }else if(e.isPrimaryButtonDown() && hoveredObject != null){
                    CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(CanvasMouseEvent.OBJECT_DRAGGED, dragStartX, dragStartY, lastDragX, lastDragY, e.getX(), e.getY(), hoveredObject);
                    canvasEventAggregator.fireEvent(canvasMouseEvent);
                }
                lastDragX = e.getX();
                lastDragY = e.getY();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e->{
            /****** CANVAS DRAGGING ******/
            if(dragStartX != -1){
                if(!e.isMiddleButtonDown() && hoveredObject == null){
                    CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(CanvasMouseEvent.CANVAS_DRAG_DROPPED, dragStartX, dragStartY, lastDragX, lastDragY, e.getX(), e.getY());
                    canvasEventAggregator.fireEvent(canvasMouseEvent);
                }else if(!e.isPrimaryButtonDown() && hoveredObject != null){
                    CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(CanvasMouseEvent.OBJECT_DRAG_DROPPED, dragStartX, dragStartY, lastDragX, lastDragY, e.getX(), e.getY(), hoveredObject);
                    canvasEventAggregator.fireEvent(canvasMouseEvent);
                }
                lastDragX = -1;
                lastDragY = -1;
                dragStartY = -1;
                dragStartX = -1;
            }
        });
    }

    private void registerMouseMovementListeners(Canvas canvas) {
        /******* MOUSE MOVED EVENT **********/ //TODO refactor to only fire specific events by event aggregator and object reaction is specified in the object it self
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, e ->{
            MouseEvent event = (MouseEvent)e;
            //getting object on coordinates
            CanvasObject object = drawingCanvas.getCanvasLayout().getObject(event.getX(), event.getY());

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

    class eventCrate{
        double startX = -1;
        double startY = -1;
        double lastX = - 1;
        double lastY = -1;
    }
}
