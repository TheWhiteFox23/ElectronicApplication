package cz.thewhiterabbit.electronicapp.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasOrigin implements CanvasObject{
    private double height = 3;
    private double width = 30;
    private double pointRadius = 10;

    @Override
    public void draw(GraphicsContext gc, double originX, double originY, double zoomAspect, double gridSize) {
        this.draw(new CanvasContext(gc, originX, originY, zoomAspect, gridSize));
    }

    @Override
    public void draw(CanvasContext canvasContext) {
        //getting variables
        double zoomAspect = canvasContext.getZoomAspect();
        GraphicsContext gc = canvasContext.getGraphicsContext();
        double originX = canvasContext.getOriginX();
        double originY = canvasContext.getOriginY();


        double height = this.height * zoomAspect;
        double width = this.width * zoomAspect;
        double radius = this.pointRadius * zoomAspect;

        gc.setFill(Color.GREEN);
        gc.fillRect(originX-height/2, originY-height/2, width,height);

        gc.setFill(Color.RED);
        gc.fillRect(originX-height/2, originY-height/2, height,width);

        gc.setFill(Color.BLACK);
        gc.fillOval(originX-radius/2, originY-radius/2, radius,radius);
    }
}
