package cz.thewhiterabbit.electronicapp.canvas;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.events.CanvasEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawingCanvas extends Region {
    //Drag Event handling
    private double dragBeginX;
    private double dragBeginY;

    private double lastDragX;
    private double lastDragY;


    private double originX = 600;
    private double originY = 400;
    private double gridSize = 30;
    private final double[] zoomMultiplayer = new double[]{0.1,0.2, 0.3, 0.5, 0.7, 1.0, 1.5, 2.0, 3.0, 5.0, 7.0};
    private int multiplierIndex = 5;

    private int selectedX = 0;
    private int selectedY = 0;

    private Canvas canvas;
    private GraphicsContext gc;

    private CanvasBackground canvasBackground;
    private CanvasOrigin canvasOrigin;

    private Map<String, List<CanvasObject>> objectMap;

    public DrawingCanvas(){
        getStylesheets().add(App.class.getResource("stylesheets/drawing-area.css").toExternalForm());
        objectMap = new HashMap<>();
        initGraphics();

        //add test object
        for(int i = 0; i< 10; i++){
            for(int j = 0; j< 10; j++){
                addObject(new TestCanvasComponent(), i ,j);
            }
        }

    }

    private void initGraphics(){
        canvas = new Canvas();
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);

        gc = canvas.getGraphicsContext2D();

        canvasBackground = new CanvasBackground();
        canvasOrigin = new CanvasOrigin();

        registerListeners();


        getChildren().add(canvas);
        drawBackground();
    }

    private void registerListeners() {
        //TODO add canvasEvent firing logic to separate class

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e->{
            if(e.isMiddleButtonDown()){
                canvas.fireEvent(new CanvasEvent(CanvasEvent.MOVE_ORIGIN_START, e.getX(), e.getY()));
            }else if(e.isPrimaryButtonDown()){
                canvas.fireEvent(new CanvasEvent(CanvasEvent.SELECTION_START, e.getX(), e.getY()));
            }

        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->{
            if(e.isMiddleButtonDown()){
                canvas.fireEvent(new CanvasEvent(CanvasEvent.MOVE_ORIGIN, e.getX(), e.getY()));
            }else if(e.isPrimaryButtonDown()){
                canvas.fireEvent(new CanvasEvent(CanvasEvent.SELECTION_MOVE, e.getX(), e.getY()));
            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e->{
            //todo add logic to decide the right event based on drag event origin
                canvas.fireEvent(new CanvasEvent(CanvasEvent.MOVE_ORIGIN_FINISH, e.getX(), e.getY()));
                canvas.fireEvent(new CanvasEvent(CanvasEvent.SELECTION_FINISH, e.getX(), e.getY()));
        });

        canvas.addEventHandler(CanvasEvent.MOVE_ORIGIN_START, e->{
            onDragBegin(e);
        });

        canvas.addEventHandler(CanvasEvent.MOVE_ORIGIN, e ->{
                originY-= (lastDragY - e.getY());
                originX-= (lastDragX - e.getX());
                lastDragX = e.getX();
                lastDragY = e.getY();
                paint();
        });

        canvas.addEventHandler(CanvasEvent.SELECTION_START, e->{
            onDragBegin(e);
        });

        canvas.addEventHandler(CanvasEvent.SELECTION_MOVE, e->{
            //todo draw selection rectangle
            paint();

            double height = e.getX() - dragBeginX;
            double width = e.getY()- dragBeginY;
            double locationX = (height>0 ? dragBeginX : e.getX());
            double locationY = (width>0 ? dragBeginY : e.getY());

            gc.setStroke(Color.GREENYELLOW);
            gc.strokeRect(locationX, locationY, Math.abs(height), Math.abs(width));
        });

        canvas.addEventHandler(CanvasEvent.SELECTION_FINISH,e->{
            //todo select object in the rectangle
            paint();
        });

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

    private void onDragBegin(CanvasEvent e) {
        dragBeginX = e.getX();
        dragBeginY = e.getY();
        lastDragX = e.getX();
        lastDragY = e.getY();
    }

    private void setSelected(int gridX, int gridY) {
        this.selectedX = gridX;
        this.selectedY = gridY;
    }

    private void paint(){
        CanvasContext ca= new CanvasContext(gc, originX, originY, getZoomMultiplier(), gridSize);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvasBackground.draw(ca);
        canvasOrigin.draw(ca);
        objectMap.forEach((key, list) ->{
            list.forEach(object -> object.draw(ca));
        });
    }


    private void drawBackground(){
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        gc.clearRect(0, 0, width, height);
        gc.beginPath();
        gc.moveTo(0.5 * width, 0 * height);
        gc.lineTo(1 * width, 1 * height);
        gc.lineTo(0 * width, 1 * height);
        gc.lineTo(0.5 * width, 0 * height);
        gc.closePath();
        gc.setFill(Color.web("#BD003D"));
        gc.fill();
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

    private void addObject(CanvasGridObject object, int gridX, int gridY){
        object.setGridX(gridX);
        object.setGridY(gridY);
        String key = getCoordinatesKey(gridX, gridY);
        if(objectMap.containsKey(key)){
            objectMap.get(key).add(object);
        }else{
            List<CanvasObject> list = new ArrayList<>();
            list.add(object);
            objectMap.put(key, list);
        }

    }

}
