package cz.thewhiterabbit.electronicapp.canvas;

import javafx.scene.canvas.GraphicsContext;

public abstract class CanvasGridObject implements CanvasObject {
    protected int gridX;
    protected int gridY;
    protected int gridHeight = 1;
    protected int gridWidth = 1;

    @Override
    public void draw(GraphicsContext gc, double originX, double originY, double zoomAspect, double gridSize) {
        this.draw(new CanvasContext(gc, originX, originY, zoomAspect, gridSize));
    }

    @Override
    public abstract void draw(CanvasContext canvasContext);

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }
}
