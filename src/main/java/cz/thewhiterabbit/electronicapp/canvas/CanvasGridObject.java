package cz.thewhiterabbit.electronicapp.canvas;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

public abstract class CanvasGridObject implements ICanvasObject {
    private int gridX;
    private int gridY;
    private int gridHeight;
    private int gridWidth;

    private boolean hovered = false;

    private CanvasContext canvasContext;
    private CanvasObjectEventManager eventManager;

    public CanvasGridObject(CanvasContext canvasContext){
        this.canvasContext = canvasContext;
        eventManager = new CanvasObjectEventManager(this);
    }

    /*****************ABSTRACT METHODS***************/
    @Override
    public abstract void draw();

    /****************GETTERS AND SETTERS ***************/

    public int getGridX() {return gridX;}

    public void setGridX(int gridX) {this.gridX = gridX;}

    public int getGridY() {return gridY;}

    public void setGridY(int gridY) {this.gridY = gridY;}

    public int getGridHeight() {return gridHeight;}

    public void setGridHeight(int gridHeight) {this.gridHeight = gridHeight;}

    public int getGridWidth() {return gridWidth;}

    public void setGridWidth(int gridWidth) {this.gridWidth = gridWidth;}

    public boolean isHovered() {return hovered;}

    public void setHovered(boolean hovered) {this.hovered = hovered;}

    public CanvasContext getCanvasContext() {return canvasContext;}

    public void setCanvasContext(CanvasContext canvasContext) {this.canvasContext = canvasContext;}

    /*************DRAW CALCULATIONS**************/

    public double getPositionX(){return canvasContext.getOriginX() + getGridX() * canvasContext.getGridSize() * canvasContext.getZoomAspect();}

    public double getPositionY(){return canvasContext.getOriginY() + getGridY() * canvasContext.getGridSize() * canvasContext.getZoomAspect();}

    public double getHeight(){return canvasContext.getGridSize() * canvasContext.getZoomAspect() * gridHeight;}

    public double getWidth(){return canvasContext.getGridSize() * canvasContext.getZoomAspect() * gridWidth;}


    /************** EVENT HANDLING *****************/

    public boolean isInside(double positionX, double positionY){
        double actualX = getPositionX();
        double actualY = getPositionY();
        return ((positionX>= actualX && positionX<= actualX+getHeight()) &&
                (positionY >= actualY && positionY <= actualY+getWidth()));
    }

    public <T extends Event> void fireEvent(T event) {
        eventManager.fireEvent(event);

    }

    public <T extends EventType> void addEventHandler(T eventType, EventHandler handler) {
        eventManager.addEventHandler(eventType, handler);
    }

    public void passEvent(MouseEvent e) {
        eventManager.handleEvent(e);
    }
}
