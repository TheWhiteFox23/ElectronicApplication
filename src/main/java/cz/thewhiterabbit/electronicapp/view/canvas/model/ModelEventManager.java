package cz.thewhiterabbit.electronicapp.view.canvas.model;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.List;

import static cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent.OBJECT_DESELECTED;
import static cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent.OBJECT_SELECTED;

/**
 * Manages events for the CanvasModel.
 * Process all events from canvas and fire specific model events.
 */
public class ModelEventManager {
    private final CanvasModel parentModel;
    private final EventAggregator eventAggregator;

    /**** LOGIC ****/
    private CanvasObject hoveredObject;

    private EventCrate canvasDragEvent;
    private EventCrate objectDragEvent;
    private EventCrate selectionDragEvent;

    private double lastDragStartX;

    public ModelEventManager(CanvasModel parentModel, EventAggregator eventAggregator) {
        this.parentModel = parentModel;
        this.eventAggregator = eventAggregator;

        canvasDragEvent = new EventCrate();
        objectDragEvent = new EventCrate();
        selectionDragEvent = new EventCrate();

        registerHandlers();
    }

    private void registerHandlers() {
        registerMouseMovementListeners();
        registerCanvasScrolledEvent();
        registerMousePressEvents();
        registerDragEvent(CanvasMouseEvent.CANVAS_DRAG_DETECTED, CanvasMouseEvent.CANVAS_DRAGGED,
                CanvasMouseEvent.CANVAS_DRAG_DROPPED, canvasDragEvent);
        registerDragEvent(CanvasMouseEvent.OBJECTS_DRAG_DETECTED, CanvasMouseEvent.OBJECT_DRAGGED,
                CanvasMouseEvent.OBJECT_DRAG_DROPPED, objectDragEvent);
        registerDragEvent(CanvasMouseEvent.CANVAS_SELECTION_DETECTED, CanvasMouseEvent.CANVAS_SELECTION_MOVE,
                CanvasMouseEvent.CANVAS_SELECTION_FINISH, selectionDragEvent);
        registerObjectSelection();
        registerKeyPressListeners();
        registerModelPropagationListener();
    }

    private void registerModelPropagationListener() {
        eventAggregator.addEventHandler(DrawingAreaEvent.ANY, e->{
            parentModel.getModelEventAggregator().fireEvent(e);
        });
    }

    private void registerKeyPressListeners() {
        eventAggregator.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            KeyEvent event = (KeyEvent) e;
            //ROTATION
            if(event.getCode() == KeyCode.R && !event.isAltDown()){
                List<CanvasObject> selected = parentModel.getSelectedObject();
                selected.forEach(o -> {
                    o.setRotation((o.getRotation() + 1)%4);
                    o.paint(parentModel.getCanvas().getGraphicsContext2D());
                });
                //TODO: manage rotation as property
                //System.out.println("Rotate selection clockwise");
            }else if(event.getCode() == KeyCode.R && event.isAltDown()){
                System.out.println("Rotate selection counter clockwise");
            }else if(event.getCode() == KeyCode.DELETE){
                parentModel.getSelectedObject().forEach(o -> {
                    o.callForDelete();
                });
                eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
            }
        });
    }

    private void registerMouseMovementListeners() {
        /******* MOUSE MOVED EVENT **********/ //TODO refactor to only fire specific events by event aggregator and object reaction is specified in the object it self
        eventAggregator.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            MouseEvent e = (MouseEvent) event;
            CanvasObject object = parentModel.getObject(e.getX(), e.getY());

            /********* MOUSE EXITED OBJECT ***********/
            if (hoveredObject != null && hoveredObject != object) {
                CanvasMouseEvent canvasMouseEvents = new CanvasMouseEvent(CanvasMouseEvent.OBJECT_EXITED, e.getX(), e.getY(), hoveredObject);
                eventAggregator.fireEvent(canvasMouseEvents);
                hoveredObject = null;

            }
            /********* MOUSE ENTERED OBJECT *********/
            if (object != null && !object.isHovered()) {
                hoveredObject = object;
                CanvasMouseEvent canvasMouseEvents = new CanvasMouseEvent(CanvasMouseEvent.OBJECT_ENTERED, e.getX(), e.getY(), hoveredObject);
                eventAggregator.fireEvent(canvasMouseEvents);
            }
        });
    }

    private void registerCanvasScrolledEvent() {
        eventAggregator.addEventHandler(ScrollEvent.SCROLL, event -> {
            ScrollEvent e = (ScrollEvent) event;
            eventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.CANVAS_SCROLLED, e.getDeltaY()));
        });
    }

    private void registerMousePressEvents() {
        eventAggregator.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            MouseEvent e = (MouseEvent) event;

            /***** CANVAS DRAG EVENT ****/
            if (e.isMiddleButtonDown() && !e.isPrimaryButtonDown()) {
                canvasDragEvent.startX = e.getX();
                canvasDragEvent.startY = e.getY();
                canvasDragEvent.lastX = e.getX();
                canvasDragEvent.lastY = e.getY();
                objectDragEvent.reset();
                selectionDragEvent.reset();
            } else if (e.isPrimaryButtonDown() && hoveredObject != null && !e.isMiddleButtonDown()) {
                objectDragEvent.startX = e.getX();
                objectDragEvent.startY = e.getY();
                objectDragEvent.lastX = e.getX();
                objectDragEvent.lastY = e.getY();
                canvasDragEvent.reset();
                selectionDragEvent.reset();
            } else if (e.isPrimaryButtonDown() && hoveredObject == null && !e.isMiddleButtonDown()) {
                selectionDragEvent.startX = e.getX();
                selectionDragEvent.startY = e.getY();
                selectionDragEvent.lastX = e.getX();
                selectionDragEvent.lastY = e.getY();
                objectDragEvent.reset();
                canvasDragEvent.reset();
            }
        });

    }

    private void registerDragEvent(EventType start, EventType move, EventType finish, EventCrate eventCrate){
        eventAggregator.addEventHandler(MouseEvent.DRAG_DETECTED, event->{
            //System.out.println("MODEL EVENT MANAGER -> drag detected");
            if(eventCrate.startX != -1){
                MouseEvent e = (MouseEvent) event;
                eventCrate.dragDetected = true;
                CanvasMouseEvent canvasMouseEvent= new CanvasMouseEvent(start, eventCrate.startX, eventCrate.startY, eventCrate.lastX,  eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                eventAggregator.fireEvent(canvasMouseEvent);
            }
        });

        eventAggregator.addEventHandler(MouseEvent.MOUSE_DRAGGED, event->{
            MouseEvent e = (MouseEvent) event;
            if(eventCrate.startX != -1 && eventCrate.dragDetected) {
                CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(move, eventCrate.startX, eventCrate.startY, eventCrate.lastX,  eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                eventAggregator.fireEvent(canvasMouseEvent);
                eventCrate.lastX = e.getX();
                eventCrate.lastY = e.getY();
            }
        });

        eventAggregator.addEventHandler(MouseEvent.MOUSE_RELEASED, event->{
            if(eventCrate.startX != -1 && eventCrate.dragDetected){
                MouseEvent e = (MouseEvent) event;
                CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(finish, eventCrate.startX, eventCrate.startY, eventCrate.lastX,  eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                eventAggregator.fireEvent(canvasMouseEvent);
                eventCrate.reset();
            }
        });
    }

    private void registerObjectSelection() {
        //select object on press
        eventAggregator.addEventHandler(MouseEvent.MOUSE_PRESSED, event->{
            MouseEvent e = (MouseEvent)event;
            CanvasObject o = parentModel.getObject(e.getX(), e.getY());
            if(e.isPrimaryButtonDown()){
                lastDragStartX = e.getX();
                if(o != null && !o.isSelected() && !e.isControlDown()){
                    eventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.DESELECT_ALL));
                    eventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_SELECTED, o));
                }else if(o == null && !e.isControlDown()){
                    eventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.DESELECT_ALL));
                }else if(o != null && !o.isSelected() && e.isControlDown()){
                    eventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_SELECTED, o));
                }else if(o!= null && e.isControlDown() && o.isSelected()){
                    eventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_DESELECTED,o));
                }
            }
        });
        eventAggregator.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            MouseEvent e = (MouseEvent)event;
            CanvasObject o = parentModel.getObject(e.getX(), e.getY());
            if(e.getX() == lastDragStartX && o!= null && !e.isControlDown()){ //TODO refactor selection detection logic
                eventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.DESELECT_ALL));
                eventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_SELECTED, o));
            }
        });
    }

    class EventCrate {
        double startX = -1;
        double startY = -1;
        double lastX = -1;
        double lastY = -1;
        boolean dragDetected = false;

        public void reset() {
            startX = -1;
            startY = -1;
            lastX = -1;
            lastY = -1;
            dragDetected = false;
        }
    }


}
