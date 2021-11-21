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

    //DRAWING CONNECTION



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
        LineObject  lineObject = new LineObject();
        lineObject.getLayoutProperties().set(-10,-10,1,10);
        lineObject.setRotation(LineObject.Rotation.LEFT_TO_RIGHT);
        getCanvasLayout().add(lineObject);

        LineObject  lineObject2 = new LineObject();
        lineObject2.getLayoutProperties().set(-10,-10,10,1);
        lineObject2.setRotation(LineObject.Rotation.LEFT_TO_BOTTOM);
        getCanvasLayout().add(lineObject2);
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
        eventAggregator.registerHandler(CanvasEvent.PAINT_OBJECT,e->{
            CanvasObject object = ((CanvasEvent)e).getObject();
            if(getCanvasLayout().containsObject(object)) object.paint(gc);
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

        eventAggregator.registerHandler(CanvasMouseEvent.ACTIVE_POINT_DRAGGED, h->{
            ActivePoint activePoint = (ActivePoint) ((CanvasMouseEvent)h).getObject();
            if(getCanvasLayout() instanceof GridLayout){
                int coordinateStartX = activePoint.getLayoutProperties().getGridX();
                int coordinateStartY = activePoint.getLayoutProperties().getGridY();
                int coordinateFinishX = ((GridLayout)getCanvasLayout()).getGridCoordinate(((CanvasMouseEvent) h).getX(),
                        ((GridLayout) getCanvasLayout()).getOriginX());
                int coordinateFinishY = ((GridLayout)getCanvasLayout()).getGridCoordinate(((CanvasMouseEvent) h).getY(),
                        ((GridLayout) getCanvasLayout()).getOriginY());

                LineObject lineObject = new LineObject();

                paint();
                /****** XCoordinate ******/
                int gridHeight = coordinateFinishX - coordinateStartX;
                if(gridHeight<0){
                    lineObject.getLayoutProperties().set(coordinateFinishX, coordinateStartY,1 , Math.abs(gridHeight));
                }else{
                    lineObject.getLayoutProperties().set(coordinateStartX, coordinateStartY, 1, Math.abs(gridHeight));
                }
                ((GridLayout) getCanvasLayout()).updatePaintProperties(lineObject);
                lineObject.paint(gc);

                int gridWidth = coordinateFinishY - coordinateStartY;
                if(gridWidth<0){
                    lineObject.getLayoutProperties().set(coordinateFinishX, coordinateFinishY,Math.abs(gridWidth) , 1);
                }else{
                    lineObject.getLayoutProperties().set(coordinateFinishX, coordinateStartY, Math.abs(gridWidth), 1);
                }
                ((GridLayout) getCanvasLayout()).updatePaintProperties(lineObject);
                lineObject.setRotation(LineObject.Rotation.LEFT_TO_BOTTOM);
                lineObject.paint(gc);

                try {
                    lineObject.finalize();
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }

        }); //todo draw line
        //eventAggregator.registerHandler(CanvasMouseEvent.ACTIVE_POINT_DRAG_DROPPED, h-> System.out.println("Active point dropped")); //todo insert lineObject


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
