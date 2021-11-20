package cz.thewhiterabbit.electronicapp.canvas;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.layout.*;
import cz.thewhiterabbit.electronicapp.canvas.objects.*;
import cz.thewhiterabbit.electronicapp.events.CanvasEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;

import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.List;

public class DrawingCanvas extends Region {
    //DRAWING
    private Canvas canvas;
    private GraphicsContext gc;

    private CanvasLayout canvasLayout;
    private EventAggregator eventAggregator = CanvasEventAggregator.getInstance();



    public DrawingCanvas(){
        //TODO Clean up constructor
        getStylesheets().add(App.class.getResource("stylesheets/drawing-area.css").toExternalForm());
        initGraphics();

        RelativeLayout relativeLayout = new RelativeLayout(canvas, eventAggregator);
        relativeLayout.setOriginX(600);
        relativeLayout.setOriginY(400);
        GridLayout gridLayout = new GridLayout(canvas, eventAggregator);
        gridLayout.setGridSize(10);
        gridLayout.setOriginX(600);
        gridLayout.setOriginY(400);
        setCanvasLayout(gridLayout);
        CanvasEventManager manager = new CanvasEventManager(this); //todo this is weird, solve it somehow

        addCanvasObject(new RelativePointBackground(canvas));
        for(int i = 0; i< 100; i+= 4){
            for(int j = 0; j< 100; j+=4){
                GeneralCanvasObject generalCanvasObject = new GeneralCanvasObject();
                generalCanvasObject.getLayoutProperties().set(i,j,2, 2);
                getCanvasLayout().add(generalCanvasObject);
            }
        }
        /*GeneralCanvasObject canvasObject = new GeneralCanvasObject();
        canvasObject.getLayoutProperties().set(-5,-5,2,2);
        addCanvasObject(new ActiveZone(canvasObject, 0, 1));
        addCanvasObject(canvasObject);*/
    }

    private void initGraphics(){
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();

        registerListeners();
        getChildren().add(canvas);
    }

    private void registerListeners() {
        //EVENT AGGREGATOR
        canvas.addEventHandler(Event.ANY, e->eventAggregator.fireEvent(e));
        eventAggregator.registerHandler(CanvasEvent.REPAINT_ALL, e->paint());
        eventAggregator.registerHandler(CanvasEvent.REPAINT_OBJECT,event -> {
            CanvasObject object = ((CanvasEvent)event).getObject();
            gc.clearRect(object.getLocationX(), object.getLocationY(), object.getWidth(), object.getHeight());
            object.paint(gc);
        });
        eventAggregator.registerHandler(CanvasEvent.PAINT_OBJECT, e->{
            CanvasObject object = ((CanvasEvent)e).getObject();
            object.paint(gc);
        });


        /****** SELECTION *******/
        eventAggregator.registerHandler(CanvasMouseEvent.CANVAS_SELECTION_MOVE, event->{
            paint();
            RectangleBounds bounds = getRectangleBounds((CanvasMouseEvent)event);

            //TODO move to paint selection method
            gc.setStroke(Color.GREENYELLOW);
            gc.strokeRect(bounds.locationX, bounds.locationY, bounds.height, bounds.width);
        });

        eventAggregator.registerHandler(CanvasMouseEvent.CANVAS_SELECTION_FINISH, event->{
            RectangleBounds bounds = getRectangleBounds((CanvasMouseEvent)event);
            List<CanvasObject> objects = getCanvasLayout().getInBounds(bounds.locationX, bounds.locationY, bounds.width, bounds.height);

            //TODO Refactor selecting into the command
            objects.forEach(o -> {
                eventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.OBJECT_SELECTED, o));
            });

            paint();
        });


        //TODO move to constructor or somewhere
        widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth((Double) newVal);
            //canvasBackground.setWidth((Double) newVal);
            paint();
        });
        heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight((Double) newVal);
            //canvasBackground.setHeight((Double) newVal);
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
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        getVisible().forEach(o->{o.paint(gc);});
    }


    private void addCanvasObject(CanvasObject canvasObject){
        getCanvasLayout().add(canvasObject);
    }

    public CanvasLayout getCanvasLayout() {
        return canvasLayout;
    }

    public void setCanvasLayout(CanvasLayout canvasLayout) {
        this.canvasLayout = canvasLayout;
    }

    public List<CanvasObject> getVisible(){
        return getCanvasLayout().getVisible();
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
}
