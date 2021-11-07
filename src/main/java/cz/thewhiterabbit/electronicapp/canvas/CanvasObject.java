package cz.thewhiterabbit.electronicapp.canvas;

import javafx.scene.canvas.GraphicsContext;

public interface CanvasObject {
    public void draw(GraphicsContext gc, double originX, double originY, double zoomAspect, double gridSize);
    public void draw(CanvasContext canvasContext);
}
