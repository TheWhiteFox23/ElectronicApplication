package cz.thewhiterabbit.electronicapp.canvas;

import javafx.scene.canvas.GraphicsContext;

public class CanvasContext {
    private GraphicsContext graphicsContext;
    private double originX;
    private double originY;
    private double zoomAspect;
    private double gridSize;

    public CanvasContext(GraphicsContext gc, double originX, double originY, double zoomAspect, double gridSize) {
        this.graphicsContext = gc;
        this.originX = originX;
        this.originY = originY;
        this.zoomAspect = zoomAspect;
        this.gridSize =  gridSize;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    public double getOriginX() {
        return originX;
    }

    public void setOriginX(double originX) {
        this.originX = originX;
    }

    public double getOriginY() {
        return originY;
    }

    public void setOriginY(double originY) {
        this.originY = originY;
    }

    public double getZoomAspect() {
        return zoomAspect;
    }

    public void setZoomAspect(double zoomAspect) {
        this.zoomAspect = zoomAspect;
    }

    public double getGridSize() {
        return gridSize;
    }

    public void setGridSize(double gridSize) {
        this.gridSize = gridSize;
    }
}
