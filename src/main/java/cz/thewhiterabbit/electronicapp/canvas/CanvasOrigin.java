package cz.thewhiterabbit.electronicapp.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasOrigin implements ICanvasObject {
    private double height = 3;
    private double width = 30;
    private double pointRadius = 10;
    private CanvasContext canvasContext;

    public CanvasOrigin(CanvasContext canvasContext){
        this.canvasContext = canvasContext;
    }


    @Override
    public void draw() {
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
