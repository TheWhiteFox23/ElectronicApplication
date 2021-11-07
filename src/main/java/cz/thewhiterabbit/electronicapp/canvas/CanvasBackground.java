package cz.thewhiterabbit.electronicapp.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasBackground implements CanvasObject {
    private double width;
    private double height;
    private double pointRadius = 2;

    @Override
    public void draw(GraphicsContext gc, double originX, double originY, double zoomAspect, double gridSize) {
        this.draw(new CanvasContext(gc, originX, originY, zoomAspect,gridSize));
    }

    @Override
    public void draw(CanvasContext canvasContext) {
        double gridSize = canvasContext.getGridSize() * canvasContext.getZoomAspect();

        double firstPointX = canvasContext.getOriginX()%gridSize;
        double firstPointY = canvasContext.getOriginY()%gridSize;

        int stepsX = (int) (height/gridSize) +1;
        int stepsY = (int) (width/gridSize) +1;

        for(int i = 0; i< stepsY ;i++){
            for(int j = 0; j< stepsX; j++){
                drawGridPoint(canvasContext, firstPointX + i*gridSize, firstPointY + j*gridSize);
            }
        }
    }

    private void drawGridPoint(CanvasContext con, double X, double Y) {
        double radius = pointRadius* con.getZoomAspect();
        con.getGraphicsContext().setFill(Color.GREENYELLOW);
        con.getGraphicsContext().fillOval( X-radius/pointRadius, Y -radius/pointRadius, radius,radius);
    }


    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
