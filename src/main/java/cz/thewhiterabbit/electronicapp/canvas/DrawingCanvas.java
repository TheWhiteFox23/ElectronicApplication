package cz.thewhiterabbit.electronicapp.canvas;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.events.CanvasMoveOrigin;
import cz.thewhiterabbit.electronicapp.events.CanvasSelection;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class DrawingCanvas extends Region {
    //BOUNDS Used for origin movement and resizing -> move to some higher level class???
    private double originX = 600;
    private double originY = 400;
    private double gridSize = 10;
    private final double[] zoomMultiplayer = new double[]{0.1,0.2, 0.3, 0.5, 0.7, 1.0, 1.5, 2.0, 3.0, 5.0, 7.0};
    private int multiplierIndex = 5;
    //BOUNDS CRATE
    private CanvasContext canvasContext; //TODO separate graphical context from canvas context
    //DEPENDS ON THE BOUNDS
    private CanvasBackground canvasBackground;
    private CanvasOrigin canvasOrigin;
    //DRAWING
    private Canvas canvas;
    private GraphicsContext gc;
    //HANDLING EVENTS
    private CanvasEventManager eventManager;
    //OBJECTS
    private List<CanvasGridObject> objects;
    private List<CanvasObject> canvasObjects;



    public DrawingCanvas(){
        //TODO Clean up constructor
        getStylesheets().add(App.class.getResource("stylesheets/drawing-area.css").toExternalForm());

        canvasContext = new CanvasContext(gc, originX, originY, getZoomMultiplier(), gridSize);
        initGraphics();

        eventManager = new CanvasEventManager(canvas);
        objects = new ArrayList<>();
        canvasObjects = new ArrayList<>();

        for(int i = 0; i< 100; i+= 2){
            for(int j = 0; j< 100; j+=2){
                double positionX = originX + i*gridSize;
                double positionY = originY + j*gridSize;
                GeneralCanvasObject generalCanvasObject = new GeneralCanvasObject(positionX, positionY, gridSize, gridSize);
                addCanvasObject(generalCanvasObject);
            }
        }
    }

    private void initGraphics(){
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();

        /************* DRAWING BACKGROUND AND ORIGIN*****/ //todo move to upper layer of the app
        canvasBackground = new CanvasBackground(canvasContext);
        canvasOrigin = new CanvasOrigin(canvasContext);

        registerListeners();

        getChildren().add(canvas);
    }

    private void registerListeners() {
        /************ LOW LEVEL EVENT DETECTION ******/

        //todo move mouse move listening logic to event manager
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, e->{
            boolean objetFound = false;
            for(int i = 0; i< objects.size(); i++){
                CanvasGridObject object = objects.get(i);
                boolean isInside = object.isInside(e.getX(), e.getY());
                if(isInside && !objetFound){
                    objetFound = true;
                    object.passEvent(e);
                }else if(!isInside){
                    object.passEvent(e);
                }
            }
        });

        /******* HIGHER LEVEL EVENTS *********/
        canvas.addEventHandler(CanvasMoveOrigin.MOVE, e ->{
                originY-= (e.getLastY() - e.getY());
                originX-= (e.getLastX() - e.getX());
                //todo adjust simple canvas objects
                paint();
        });

        canvas.addEventHandler(CanvasSelection.MOVE, e->{
            paint();

            double height = e.getX() - e.getBeginningX();
            double width = e.getY()- e.getBeginningY();
            double locationX = (height>0 ? e.getBeginningX() : e.getX());
            double locationY = (width>0 ? e.getBeginningY() : e.getY());

            gc.setStroke(Color.GREENYELLOW);
            gc.strokeRect(locationX, locationY, Math.abs(height), Math.abs(width));
        });

        canvas.addEventHandler(CanvasSelection.FINISH, e->{
            //todo select object in the rectangle
            paint();
        });

        //TODO: transform into zoom event
        canvas.addEventHandler(ScrollEvent.SCROLL, e->{
            if (e.getDeltaY() > 0){
                if(multiplierIndex < zoomMultiplayer.length-1){
                    multiplierIndex++;
                }
            }else{
                if(multiplierIndex > 0){
                    multiplierIndex--;
                }
            }
            paint();
        });

        //TODO move to constructor or somewhere
        widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth((Double) newVal);
            canvasBackground.setWidth((Double) newVal);
            paint();
        });
        heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight((Double) newVal);
            canvasBackground.setHeight((Double) newVal);
            paint();
        });
    }

    private void paint(){
        //updating canvas context
        canvasContext.setValue(gc, originX, originY, getZoomMultiplier(), gridSize);
        //cleaning canvas //todo add clear only object that are visible
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //drawing elements //todo draw only object that are visible
        canvasBackground.draw();
        canvasOrigin.draw();
        objects.forEach(o -> o.draw());
        canvasObjects.forEach(o ->  o.paint(gc));
    }


    private double getZoomMultiplier(){
        return zoomMultiplayer[multiplierIndex];
    }

    private int getGridX(double X){
        int gridX = (int)((X-originX)/(gridSize * getZoomMultiplier()));
        System.out.println("X: " + X + " gridX:" + gridX);
        if (X<= originX)gridX--;
        return gridX;
    }

    private int getGridY(double Y){
        int gridY = (int)((Y-originY)/(gridSize * getZoomMultiplier()));
        System.out.println("Y: "+ Y + " gridY: " + gridY);
        if (Y<= originY)gridY--;
        return gridY;
    }

    private String getCoordinatesKey(int X, int Y){
        return X + ":" +Y;
    }

    private void addObject(CanvasGridObject object, int x, int y){
        object.setGridY(y);
        object.setGridX(x);
        objects.add(object);
    }

    private void addCanvasObject(CanvasObject canvasObject){
        canvasObjects.add(canvasObject);
    }


}
