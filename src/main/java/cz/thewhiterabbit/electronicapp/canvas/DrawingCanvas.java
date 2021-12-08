package cz.thewhiterabbit.electronicapp.canvas;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.model.*;
import cz.thewhiterabbit.electronicapp.canvas.objects.*;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;

import cz.thewhiterabbit.electronicapp.events.CanvasPaintEvent;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class DrawingCanvas extends Region {
    //DRAWING
    private Canvas canvas;
    private GraphicsContext gc;

    private CanvasModel canvasModel;


    public DrawingCanvas(){
        //TODO Clean up constructor
        getStylesheets().add(App.class.getResource("stylesheets/drawing-area.css").toExternalForm());
        initGraphics();
    }



    private void initGraphics(){
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();

        registerListeners();
        getChildren().add(canvas);
    }

    private void registerListeners() {
        /*** CANVAS EVENT PROPAGATION ***/ //TODO probably can be refactored
        canvas.addEventHandler(CanvasMouseEvent.ANY, e ->{
            if(getCanvasLayout()!= null){
                getCanvasLayout().getInnerEventAggregator().fireEvent(e);
            }
        });
        /*** KEY EVENTS ***/
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            this.requestFocus();
        });
        this.addEventHandler(KeyEvent.ANY, e->{
            if(getCanvasLayout()!= null){
                getCanvasLayout().getInnerEventAggregator().fireEvent(e);
            }
        });


        //TODO move to constructor or somewhere
        widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth((Double) newVal);
            paint();
        });
        heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight((Double) newVal);
            paint();
        });
    }


    private RectangleBounds getRectangleBounds(CanvasMouseEvent e) {
        double height = e.getX() - e.getStartX();
        double width = e.getY()- e.getStartY();
        double locationX = (height>0 ? e.getStartX() : e.getX());
        double locationY = (width>0 ? e.getStartY() : e.getY());
        RectangleBounds bounds = new RectangleBounds(locationX, locationY, Math.abs(height), Math.abs(width));
        return bounds;
    }

    private void paint(){
        getVisible().forEach(o->{o.paint(gc);});
    }

    private void clear(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public CanvasModel getCanvasLayout() {
        return canvasModel;
    }

    public void setModel(CanvasModel canvasModel) {
        //TODO deregister handles
        if(this.canvasModel != null){
            removeEventHandlers(this.canvasModel.getInnerEventAggregator());
            this.canvasModel.setCanvas(null);
        }

        this.canvasModel = canvasModel;


        if(this.canvasModel != null){
            this.canvasModel.setCanvas(canvas);
            addModelHandlers(this.canvasModel.getInnerEventAggregator());
            //TODO MOVE TO SOMEWHERE MORE APPROPRIATE

            /****** SELECTION *******/
            canvasModel.getInnerEventAggregator().addEventHandler(CanvasMouseEvent.CANVAS_SELECTION_MOVE, event->{
                clear();
                paint();
                RectangleBounds bounds = getRectangleBounds((CanvasMouseEvent)event);

                //TODO move to paint selection method
                gc.setStroke(Color.GREENYELLOW);
                gc.strokeRect(bounds.locationX, bounds.locationY, bounds.height, bounds.width);
            });

            canvasModel.getInnerEventAggregator().addEventHandler(CanvasMouseEvent.CANVAS_SELECTION_FINISH, event->{
                RectangleBounds bounds = getRectangleBounds((CanvasMouseEvent)event);
                List<CanvasObject> objects = getCanvasLayout().getInBounds(bounds.locationX, bounds.locationY, bounds.width, bounds.height);

                //TODO Refactor selecting into the command
                objects.forEach(o -> {
                    canvasModel.getInnerEventAggregator().fireEvent(new CanvasMouseEvent(CanvasMouseEvent.OBJECT_SELECTED, o));
                });

                clear();
                paint();
            });
        }
    }

    public List<CanvasObject> getVisible(){
        if(getCanvasLayout() != null){
            return getCanvasLayout().getVisible();
        }else{
            return new ArrayList<>();
        }

    }

    public Canvas getCanvas() {
        return canvas;
    }

    class RectangleBounds{
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

    /**** HANDLERS ****/
    private final EventHandler<CanvasPaintEvent> paintHandler = new EventHandler<CanvasPaintEvent>() {
        @Override
        public void handle(CanvasPaintEvent canvasPaintEvent) {
            paint();
        }
    };
    private final EventHandler<CanvasPaintEvent> paintObjectHandler = new EventHandler<CanvasPaintEvent>() {
        @Override
        public void handle(CanvasPaintEvent canvasPaintEvent) {
            CanvasObject o =canvasPaintEvent.getCanvasObject();
            if(o != null && getCanvasLayout().containsObject(o)) {
               o.paint(gc);
            }
        }
    };
    private final EventHandler<CanvasPaintEvent> repaintHandler = new EventHandler<CanvasPaintEvent>() {
        @Override
        public void handle(CanvasPaintEvent canvasPaintEvent) {
            clear();
            paint();
        }
    };
    private final EventHandler<CanvasPaintEvent> repaintObjectHandler = new EventHandler<CanvasPaintEvent>() {
        @Override
        public void handle(CanvasPaintEvent canvasPaintEvent) {
            CanvasObject o =canvasPaintEvent.getCanvasObject();
            if(o != null && getCanvasLayout().containsObject(o)) {
                o.clean(gc);
                o.paint(gc);
            }
        }
    };
    private final EventHandler<CanvasPaintEvent> clearHandler = new EventHandler<CanvasPaintEvent>() {
        @Override
        public void handle(CanvasPaintEvent canvasPaintEvent) {
            clear();
        }
    };
    private final EventHandler<CanvasPaintEvent> clearObjectHandler = new EventHandler<CanvasPaintEvent>() {
        @Override
        public void handle(CanvasPaintEvent canvasPaintEvent) {
            CanvasObject o =canvasPaintEvent.getCanvasObject();
            if(o != null && getCanvasLayout().containsObject(o)) {
                o.clean(gc);
            }
        }
    };

    private void addModelHandlers(EventAggregator aggregator){
        aggregator.addEventHandler(CanvasPaintEvent.PAINT, paintHandler);
        aggregator.addEventHandler(CanvasPaintEvent.PAINT_OBJECT, paintObjectHandler);
        aggregator.addEventHandler(CanvasPaintEvent.REPAINT, repaintHandler);
        aggregator.addEventHandler(CanvasPaintEvent.REPAINT_OBJECT, repaintObjectHandler);
        aggregator.addEventHandler(CanvasPaintEvent.CLEAN, clearHandler);
        aggregator.addEventHandler(CanvasPaintEvent.CLEAN_OBJECT, clearObjectHandler);
    }

    private void removeEventHandlers(EventAggregator aggregator){
        aggregator.removeEventHandler(CanvasPaintEvent.PAINT, paintHandler);
        aggregator.removeEventHandler(CanvasPaintEvent.PAINT_OBJECT, paintObjectHandler);
        aggregator.removeEventHandler(CanvasPaintEvent.REPAINT, repaintHandler);
        aggregator.removeEventHandler(CanvasPaintEvent.REPAINT_OBJECT, repaintObjectHandler);
        aggregator.removeEventHandler(CanvasPaintEvent.CLEAN, clearHandler);
        aggregator.removeEventHandler(CanvasPaintEvent.CLEAN_OBJECT, clearObjectHandler);
    }

}
