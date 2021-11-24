package cz.thewhiterabbit.electronicapp.canvas;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.layout.GridModel;
import cz.thewhiterabbit.electronicapp.canvas.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import cz.thewhiterabbit.electronicapp.canvas.objects.LineObject;
import cz.thewhiterabbit.electronicapp.events.CanvasEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import static cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent.OBJECT_DESELECTED;
import static cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent.OBJECT_SELECTED;

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

    //LINE DRAWING
    LineObject firstLine;
    LineObject secondLine;

    private double lastDragStartX;
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
        /*** OBJECT HOVERING ***/
        registerMouseMovementListeners(canvas);
        /*** CANVAS SCROLLING ***/
        registerCanvasScrolledEvent(canvas);
        /*** INITIALIZE DRAGGING ***/
        registerMousePressEvents(canvas);
        /*** CANVAS DRAGGING ***/
        registerDragEvent(canvas, CanvasMouseEvent.CANVAS_DRAG_DETECTED, CanvasMouseEvent.CANVAS_DRAGGED,
                CanvasMouseEvent.CANVAS_DRAG_DROPPED, canvasDragEvent);
        /*** OBJECT DRAGGING ***/
        registerDragEvent(canvas, CanvasMouseEvent.OBJECTS_DRAG_DETECTED, CanvasMouseEvent.OBJECT_DRAGGED,
                CanvasMouseEvent.OBJECT_DRAG_DROPPED, objectDragEvent);
        /*** SELECTIONS ***/
        registerDragEvent(canvas, CanvasMouseEvent.CANVAS_SELECTION_DETECTED, CanvasMouseEvent.CANVAS_SELECTION_MOVE,
                CanvasMouseEvent.CANVAS_SELECTION_FINISH, selectionDragEvent);
        registerObjectSelection(canvas);
        /*** LINE DRAWING ***/
        registerLineDrawingListeners();
    }

    private void registerObjectSelection(Canvas canvas) {
        //select object on press
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e->{
            CanvasObject o = drawingCanvas.getCanvasLayout().getObject(e.getX(), e.getY());
            if(e.isPrimaryButtonDown()){
                lastDragStartX = e.getX();
                if(o != null && !o.isSelected() && !e.isControlDown()){
                    canvasEventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.DESELECT_ALL));
                    canvasEventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_SELECTED, o));
                }else if(o == null && !e.isControlDown()){
                    canvasEventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.DESELECT_ALL));
                }else if(o != null && !o.isSelected() && e.isControlDown()){
                    canvasEventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_SELECTED, o));
                }else if(o!= null && e.isControlDown() && o.isSelected()){
                    canvasEventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_DESELECTED,o));
                }
            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            CanvasObject o = drawingCanvas.getCanvasLayout().getObject(e.getX(), e.getY());
            if(e.getX() == lastDragStartX && o!= null && !e.isControlDown()){ //TODO refactor selection detection logic
                canvasEventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.DESELECT_ALL));
                canvasEventAggregator.fireEvent(new CanvasMouseEvent(OBJECT_SELECTED, o));
            }
        });
    }

    private void registerMousePressEvents(Canvas canvas) {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e->{
            /***** CANVAS DRAG EVENT ****/
            if(e.isMiddleButtonDown() && !e.isPrimaryButtonDown()){
                canvasDragEvent.startX =e.getX(); canvasDragEvent.startY = e.getY();
                canvasDragEvent.lastX = e.getX(); canvasDragEvent.lastY = e.getY();
                objectDragEvent.reset();
                selectionDragEvent.reset();
            }else
            /***** OBJECT DRAG EVENT ****/
            if(e.isPrimaryButtonDown() && hoveredObject!= null && !e.isMiddleButtonDown()){
                objectDragEvent.startX =e.getX(); objectDragEvent.startY = e.getY();
                objectDragEvent.lastX = e.getX(); objectDragEvent.lastY = e.getY();
                canvasDragEvent.reset();
                selectionDragEvent.reset();
            }else
            /***** SELECTION DRAG EVENT ****/
            if(e.isPrimaryButtonDown() && hoveredObject == null && !e.isMiddleButtonDown()){
                selectionDragEvent.startX =e.getX(); selectionDragEvent.startY = e.getY();
                selectionDragEvent.lastX = e.getX(); selectionDragEvent.lastY = e.getY();
                objectDragEvent.reset();
                canvasDragEvent.reset();
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
                eventCrate.dragDetected = true;
                CanvasMouseEvent canvasMouseEvent= new CanvasMouseEvent(start, eventCrate.startX, eventCrate.startY, eventCrate.lastX,  eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                canvasEventAggregator.fireEvent(canvasMouseEvent);
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->{
            if(eventCrate.startX != -1 && eventCrate.dragDetected) {
                CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(move, eventCrate.startX, eventCrate.startY, eventCrate.lastX,  eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                canvasEventAggregator.fireEvent(canvasMouseEvent);
                eventCrate.lastX = e.getX();
                eventCrate.lastY = e.getY();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e->{
            if(eventCrate.startX != -1 && eventCrate.dragDetected){
                CanvasMouseEvent canvasMouseEvent = new CanvasMouseEvent(finish, eventCrate.startX, eventCrate.startY, eventCrate.lastX,  eventCrate.lastY, e.getX(), e.getY(), hoveredObject);
                canvasEventAggregator.fireEvent(canvasMouseEvent);
                eventCrate.reset();
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

    private void registerLineDrawingListeners(){
        //TODO REFACTOR
        canvasEventAggregator.registerHandler(CanvasMouseEvent.ACTIVE_POINT_DRAGGED, h->{
            ActivePoint activePoint = (ActivePoint) ((CanvasMouseEvent)h).getObject();
            if(drawingCanvas.getCanvasLayout() instanceof GridModel){
                int coordinateStartX = activePoint.getLayoutProperties().getGridX();
                int coordinateStartY = activePoint.getLayoutProperties().getGridY();
                int coordinateFinishX = ((GridModel)drawingCanvas.getCanvasLayout()).getGridCoordinate(((CanvasMouseEvent) h).getX(),
                        ((GridModel) drawingCanvas.getCanvasLayout()).getOriginX());
                int coordinateFinishY = ((GridModel)drawingCanvas.getCanvasLayout()).getGridCoordinate(((CanvasMouseEvent) h).getY(),
                        ((GridModel) drawingCanvas.getCanvasLayout()).getOriginY());

                setLines(coordinateStartX, coordinateStartY, coordinateFinishX, coordinateFinishY);

                canvasEventAggregator.fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
                if(firstLine != null){
                    ((GridModel) drawingCanvas.getCanvasLayout()).updatePaintProperties(firstLine);
                    firstLine.paint(drawingCanvas.getCanvas().getGraphicsContext2D());
                }
                if(secondLine != null){
                    ((GridModel) drawingCanvas.getCanvasLayout()).updatePaintProperties(secondLine);
                    secondLine.paint(drawingCanvas.getCanvas().getGraphicsContext2D());
                }
            }

        });

        canvasEventAggregator.registerHandler(CanvasMouseEvent.ACTIVE_POINT_DRAG_DROPPED, e->{
            if(firstLine != null) {
                drawingCanvas.getCanvasLayout().add(firstLine);
                tryFinalizeLine(firstLine);
                firstLine = null;
            }
            if(secondLine != null) {
                drawingCanvas.getCanvasLayout().add(secondLine);
                tryFinalizeLine(secondLine);
                secondLine = null;
            }


        });
    }

    private void setLines(int activePointX, int activePointY, int cursorX, int cursorY){
        if(activePointX != cursorX){
            createAndSetLine(activePointX, cursorX, activePointY, cursorY, LineObject.Orientation.HORIZONTAL);
        }else{
            deleteAndAdjustOrder(LineObject.Orientation.HORIZONTAL);
        }

        if(activePointY != cursorY){
            createAndSetLine(activePointY, cursorY,activePointX, cursorX, LineObject.Orientation.VERTICAL);
        }else{
            deleteAndAdjustOrder(LineObject.Orientation.VERTICAL);
        }
    }

    private void createAndSetLine(int firstLinePoint, int secondLinePoint,int firsLineLevel, int secondLineLevel, LineObject.Orientation orientation) {
        if (firstLine == null){
            firstLine = new LineObject();
            firstLine.setOrientation(orientation);
            adjustLine(firstLine, firstLinePoint, secondLinePoint, firsLineLevel);
        }else if (firstLine.getOrientation() == orientation) {
            adjustLine(firstLine, firstLinePoint, secondLinePoint, firsLineLevel);
        }else if (secondLine == null)  {
            secondLine = new LineObject();
            secondLine.setOrientation(orientation);
            adjustLine(secondLine, firstLinePoint, secondLinePoint, secondLineLevel);
        }else if (secondLine.getOrientation() == orientation) {
            adjustLine(secondLine, firstLinePoint, secondLinePoint, secondLineLevel);
        }

    }

    private void deleteAndAdjustOrder(LineObject.Orientation orientation){
        if(firstLine != null && firstLine.getOrientation() == orientation){
            tryFinalizeLine(firstLine);
            firstLine = secondLine;
            secondLine = null;
        }else if (secondLine != null && secondLine.getOrientation() == orientation){
            tryFinalizeLine(secondLine);
            secondLine = null;
        }
    }

    private void tryFinalizeLine(LineObject lineObject) {
        try {
            this.secondLine.finalize();
        }catch (Throwable throwable){

        }
    }

    private void adjustLine(LineObject lineObject, int firstPoint, int secondPoint, int level) {
        int length = secondPoint - firstPoint;
        if(lineObject.getOrientation() == LineObject.Orientation.HORIZONTAL){
            lineObject.getLayoutProperties().set((length<0? secondPoint:firstPoint), level,1, Math.abs(length));
        }else{
            lineObject.getLayoutProperties().set(level,(length<0? secondPoint:firstPoint), Math.abs(length), 1);
        }
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
        boolean dragDetected = false;

        public void reset(){
            startX = -1;
            startY = -1;
            lastX = - 1;
            lastY = -1;
            dragDetected = false;
        }
    }
}
