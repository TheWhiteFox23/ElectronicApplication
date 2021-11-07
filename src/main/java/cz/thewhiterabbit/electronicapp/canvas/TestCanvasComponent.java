package cz.thewhiterabbit.electronicapp.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.security.PrivateKey;

public class TestCanvasComponent extends CanvasGridObject{

    @Override
    public void draw(CanvasContext canvasContext) {
        GraphicsContext gc = canvasContext.getGraphicsContext();
        double originX = canvasContext.getOriginX();
        double originY = canvasContext.getOriginY();
        double height = canvasContext.getGridSize() * canvasContext.getZoomAspect();
        gc.setFill(Color.RED);

        double coordinateX = originX + getGridX() * canvasContext.getGridSize() * canvasContext.getZoomAspect();
        double coordinateY = originY + getGridY() * canvasContext.getGridSize() * canvasContext.getZoomAspect();
        gc.fillRect(coordinateX, coordinateY, height, height);
        //gc.fillText("TEST",coordinateX +height/2, coordinateY +height/2 );
    }
}
