package cz.thewhiterabbit.electronicapp.canvas.objects;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.model.RelativeModel;

import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasPaintEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class RelativePointBackground extends CanvasObject{
    private Canvas canvas; //todo get width and height by another way
    private double distance = 10;
    private double pointRadius = 2;
    private double zoomAspect = 1.0;
    private Paint pointColor = Color.GREENYELLOW;

    public RelativePointBackground(Canvas canvas) {
        this.canvas = canvas;
        setRotationStrategy(RotationStrategy.DO_NOT_ROTATE);
    }

    @Override
    protected void registerListeners(EventAggregator eventAggregator) {
        super.registerListeners(eventAggregator);
        getEventAggregator().addEventHandler(CanvasMouseEvent.CANVAS_SCROLLED, e->{
            if(getParentModel() instanceof RelativeModel){
                RelativeModel model = (RelativeModel) getParentModel();
                this.zoomAspect = model.getZoomAspect();
                this.getEventAggregator().fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));
            }
        });
    }

    @Override
    public void doPaint(GraphicsContext gc) {
        gc.restore();
        double gridSize = distance * zoomAspect;
        if(gridSize < 7.0) return;

        double firstPointX = getLocationX()%gridSize;
        double firstPointY = getLocationY()%gridSize;

        int stepsX = (int) (canvas.getHeight()/gridSize) +1;
        int stepsY = (int) (canvas.getWidth()/gridSize) +1;

        for(int i = 0; i< stepsY ;i++){
            for(int j = 0; j< stepsX; j++){
                drawGridPoint(gc, firstPointX+ i*gridSize, firstPointY +j*gridSize);
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

    @Override
    public boolean isInBounds(double locationX, double locationY, double width, double height) {
        return false;
    }
}
