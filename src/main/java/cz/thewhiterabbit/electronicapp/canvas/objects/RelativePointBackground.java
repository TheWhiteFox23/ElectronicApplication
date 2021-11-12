package cz.thewhiterabbit.electronicapp.canvas.objects;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class RelativePointBackground extends CanvasObject{
    private Canvas canvas; //todo get width and height by another way
    private double distance = 10;
    private double pointRadius = 2;
    private Paint pointColor = Color.GREENYELLOW;

    public RelativePointBackground(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void paint(GraphicsContext gc) {
        double gridSize = distance;

        double firstPointX = getLocationX()%gridSize;
        double firstPointY = getLocationY()%gridSize;

        int stepsX = (int) (canvas.getHeight()/gridSize) +1;
        int stepsY = (int) (canvas.getWidth()/gridSize) +1;

        for(int i = 0; i< stepsY ;i++){
            for(int j = 0; j< stepsX; j++){
                drawGridPoint(gc, firstPointX+ i*distance, firstPointY +j*distance);
            }
        }
    }

    private void drawGridPoint(GraphicsContext gc, double X, double Y) {
        gc.setFill(pointColor);
        gc.fillOval( X-pointRadius/2, Y -pointRadius/2, pointRadius,pointRadius);
    }

    @Override
    public boolean isInBounds(double x, double y) {
        return false;
    }

    @Override
    public boolean isVisible(double canvasWidth, double canvasHeight) {
        return true;
    }
}
