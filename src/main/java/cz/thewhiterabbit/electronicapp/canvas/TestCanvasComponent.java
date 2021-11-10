package cz.thewhiterabbit.electronicapp.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TestCanvasComponent extends CanvasGridObject{

    public TestCanvasComponent(CanvasContext canvasContext){
        super(canvasContext);
        setGridHeight(2);
        setGridWidth(2);
    }

    @Override
    public void draw() {
        GraphicsContext gc = getCanvasContext().getGraphicsContext();
        double height = getHeight();
        if(isHovered()){
            gc.setFill(Color.RED);
        }else{
            gc.setFill(Color.GREENYELLOW);
        }

        double coordinateX = getPositionX();
        double coordinateY = getPositionY();
        gc.fillRect(coordinateX, coordinateY, height, height);
    }

}
