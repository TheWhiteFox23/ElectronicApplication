package cz.thewhiterabbit.electronicapp.view.canvas;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.view.canvas.model.*;
import cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent;

import cz.thewhiterabbit.electronicapp.view.events.CanvasPaintEvent;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DrawingCanvas extends Region {
    //DRAWING
    private Canvas canvas;
    private GraphicsContext gc;

    private CanvasModel canvasModel;


    public DrawingCanvas(){
        //TODO Clean up constructor
        getStylesheets().add(Objects.requireNonNull(App.class.getResource("stylesheets/style.css")).toExternalForm());
        initGraphics();
    }



    private void initGraphics(){
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();

        registerListeners();
        getChildren().add(canvas);
    }

    private void registerListeners() {
        /*** CANVAS EVENT PROPAGATION ***/
        canvas.addEventHandler(CanvasMouseEvent.ANY, e ->{
            if(getCanvasLayout()!= null){
                getCanvasLayout().getInnerEventAggregator().fireEvent(e);
            }
        });
        /*** KEY EVENTS ***/
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> this.requestFocus());
        this.addEventHandler(KeyEvent.ANY, e->{
            if(getCanvasLayout()!= null){
                getCanvasLayout().getInnerEventAggregator().fireEvent(e);
            }
        });


        /*** RESIZING ***/
        widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth((Double) newVal);
            paint();
        });
        heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight((Double) newVal);
            paint();
        });
    }


    public void paint(){
        getVisible().forEach(o-> o.paint(gc));
    }

    public void clear(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public CanvasModel getCanvasLayout() {
        return canvasModel;
    }

    public void setModel(CanvasModel canvasModel) {
        if(this.canvasModel != null){
            removeEventHandlers(this.canvasModel.getInnerEventAggregator());
            this.canvasModel.setCanvas(null);
        }

        this.canvasModel = canvasModel;

        if(this.canvasModel != null){
            this.canvasModel.setCanvas(canvas);
            addModelHandlers(this.canvasModel.getInnerEventAggregator());
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

    /**** HANDLERS ****/ //TODO create handler map and register from map
    private final EventHandler<CanvasPaintEvent> paintHandler = canvasPaintEvent -> paint();
    private final EventHandler<CanvasPaintEvent> paintObjectHandler = new EventHandler<>() {
        @Override
        public void handle(CanvasPaintEvent canvasPaintEvent) {
            CanvasObject o = canvasPaintEvent.getCanvasObject();
            if (o != null && getCanvasLayout().containsObject(o)) {
                o.paint(gc);
            }
        }
    };
    private final EventHandler<CanvasPaintEvent> repaintHandler = new EventHandler<>() {
        @Override
        public void handle(CanvasPaintEvent canvasPaintEvent) {
            clear();
            paint();
        }
    };
    private final EventHandler<CanvasPaintEvent> repaintObjectHandler = new EventHandler<>() {
        @Override
        public void handle(CanvasPaintEvent canvasPaintEvent) {
            CanvasObject o =canvasPaintEvent.getCanvasObject();
            if(o != null && getCanvasLayout().containsObject(o)) {
                o.clean(gc);
                o.paint(gc);
            }
        }
    };
    private final EventHandler<CanvasPaintEvent> clearHandler = new EventHandler<>() {
        @Override
        public void handle(CanvasPaintEvent canvasPaintEvent) {
            clear();
        }
    };
    private final EventHandler<CanvasPaintEvent> clearObjectHandler = new EventHandler<>() {
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
