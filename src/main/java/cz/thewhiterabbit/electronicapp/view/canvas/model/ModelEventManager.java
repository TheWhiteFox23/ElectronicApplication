package cz.thewhiterabbit.electronicapp.view.canvas.model;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.view.events.CanvasPaintEvent;
import cz.thewhiterabbit.electronicapp.view.events.EditControlEvent;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

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

    private boolean propertiesDialogVisible = false;

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
        registerPaintSelectionHandler();
        registerDoubleClickListener();
    }

    private void registerDoubleClickListener() {
        eventAggregator.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            GridModel m = (GridModel) parentModel;
            MouseEvent mouseEvent = (MouseEvent) e;
            if (mouseEvent.getClickCount() == 2 && hoveredObject != null && (m.getActiveElement()==null || m.getActiveElement()!=hoveredObject)) {
                eventAggregator.fireEvent(new EditControlEvent(EditControlEvent.ACTIVE_OBJECT_CHANGE, m.getActiveElement(), hoveredObject));
                m.setActiveElement(hoveredObject);
            } else if (mouseEvent.getClickCount() == 1 && hoveredObject == null && m.getActiveElement()!= null) {
                eventAggregator.fireEvent(new EditControlEvent(EditControlEvent.ACTIVE_OBJECT_CHANGE, m.getActiveElement(), null));
                m.setActiveElement(null);
            }
        });
    }

    private void registerModelPropagationListener() {
        eventAggregator.addEventHandler(DrawingAreaEvent.ANY, e -> {
            parentModel.getModelEventAggregator().fireEvent(e);
        });
        eventAggregator.addEventHandler(EditControlEvent.ANY, e -> {
            parentModel.getModelEventAggregator().fireEvent(e);
        });
    }

    private void registerKeyPressListeners() {
        eventAggregator.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            KeyEvent event = (KeyEvent) e;
            //ROTATION
            if (event.getCode() == KeyCode.R && !event.isAltDown()) {
                eventAggregator.fireEvent(new EditControlEvent(EditControlEvent.ROTATE_CLOCKWISE));
            } else if (event.getCode() == KeyCode.R && event.isAltDown()) {
                eventAggregator.fireEvent(new EditControlEvent(EditControlEvent.ROTATE_COUNTER_CLOCKWISE));
            } else if (event.getCode() == KeyCode.DELETE) {
                eventAggregator.fireEvent(new EditControlEvent(EditControlEvent.DELETE));
            } else if (event.getCode() == KeyCode.Z && event.isControlDown() && !event.isAltDown()) {
                eventAggregator.fireEvent(new EditControlEvent(EditControlEvent.UNDO));
            } else if (event.getCode() == KeyCode.Z && event.isControlDown() && event.isAltDown()) {
                eventAggregator.fireEvent(new EditControlEvent(EditControlEvent.REDO));
            } else if(event.getCode() == KeyCode.C && event.isControlDown()){
                eventAggregator.fireEvent(new EditControlEvent(EditControlEvent.COPY));
            }else if(event.getCode() == KeyCode.V && event.isControlDown()){
                eventAggregator.fireEvent(new EditControlEvent(EditControlEvent.PASTE));
            }else if(event.getCode() == KeyCode.A && event.isControlDown() && !event.isAltDown()){
                eventAggregator.fireEvent(new EditControlEvent(EditControlEvent.SELECT_ALL));
            }else if(event.getCode() == KeyCode.A && event.isControlDown() && event.isAltDown()){
                eventAggregator.fireEvent(new EditControlEvent(EditControlEvent.DESELECT_ALL));
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

    private void registerDragEvent(EventType start, EventType move, EventType finish, EventCrate eventCrate) {
        eventAggregator.addEventHandler(MouseEvent.DRAG_DETECTED, event -> {
            //System.out.println("MODEL EVENT MANAGER -> drag detected");
            if (eventCrate.startX != -1) {
                MouseEvent e = (MouseEvent) event;
                eventCrate.dragDetected = true;
                CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(start, eventCrate.startX, eventCrate.startY, eventCrate.lastX, eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                eventAggregator.fireEvent(canvasMouseEvent);
            }
        });

        eventAggregator.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            MouseEvent e = (MouseEvent) event;
            if (eventCrate.startX != -1 && eventCrate.dragDetected) {
                CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(move, eventCrate.startX, eventCrate.startY, eventCrate.lastX, eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                eventAggregator.fireEvent(canvasMouseEvent);
                eventCrate.lastX = e.getX();
                eventCrate.lastY = e.getY();
            }
        });

        eventAggregator.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            if (eventCrate.startX != -1 && eventCrate.dragDetected) {
                MouseEvent e = (MouseEvent) event;
                CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(finish, eventCrate.startX, eventCrate.startY, eventCrate.lastX, eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                eventAggregator.fireEvent(canvasMouseEvent);
                eventCrate.reset();
            }
        });
    }

    private void registerObjectSelection() { //TODO manage selections by commands
        //select object on press
        eventAggregator.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            MouseEvent e = (MouseEvent) event;
            CanvasObject o = parentModel.getObject(e.getX(), e.getY());
            if (e.isPrimaryButtonDown()) {
                lastDragStartX = e.getX();
                if (o != null && !o.isSelected() && !e.isControlDown()) {
                    eventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.DESELECT_ALL));
                    eventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_SELECTED, o));
                    eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
                } else if (o == null && !e.isControlDown()) {
                    eventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.DESELECT_ALL));
                    eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
                } else if (o != null && !o.isSelected() && e.isControlDown()) {
                    eventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_SELECTED, o));
                    eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
                } else if (o != null && e.isControlDown() && o.isSelected()) {
                    eventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_DESELECTED, o));
                    eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
                }
            }
        });
        eventAggregator.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            MouseEvent e = (MouseEvent) event;
            CanvasObject o = parentModel.getObject(e.getX(), e.getY());
            if (e.getX() == lastDragStartX && o != null && !e.isControlDown()) { //TODO refactor selection detection logic
                eventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.DESELECT_ALL));
                eventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_SELECTED, o));
                eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
            }
        });
    }

    private void registerPaintSelectionHandler() {
        eventAggregator.addEventHandler(CanvasMouseEvent.CANVAS_SELECTION_MOVE, event -> {
            if (parentModel.getCanvas() != null) {
                GraphicsContext gc = parentModel.getCanvas().getGraphicsContext2D();
                eventAggregator.fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));

                RectangleBounds bounds = getRectangleBounds((CanvasMouseEvent) event);

                //TODO move to paint selection method
                gc.setStroke(Color.GREENYELLOW);
                gc.strokeRect(bounds.locationX, bounds.locationY, bounds.width, bounds.height);
            }

        });

        eventAggregator.addEventHandler(CanvasMouseEvent.CANVAS_SELECTION_FINISH, event -> {
            RectangleBounds bounds = getRectangleBounds((CanvasMouseEvent) event);
            List<CanvasObject> objects = parentModel.getInBounds(bounds.locationX, bounds.locationY, bounds.width, bounds.height);

            //TODO Refactor selecting into the command
            objects.forEach(o -> eventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.OBJECT_SELECTED, o)));
            eventAggregator.fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
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

    private RectangleBounds getRectangleBounds(CanvasMouseEvent e) {
        double width = e.getX() - e.getStartX();
        double height = e.getY() - e.getStartY();
        double locationX = (width > 0 ? e.getStartX() : e.getX());
        double locationY = (height > 0 ? e.getStartY() : e.getY());
        return new RectangleBounds(locationX, locationY, Math.abs(height), Math.abs(width));
    }

    class RectangleBounds {
        double locationX;
        double locationY;
        double width;
        double height;

        public RectangleBounds(double locationX, double locationY, double height, double width) {
            this.locationX = locationX;
            this.locationY = locationY;
            this.width = width;
            this.height = height;
        }
    }


}
