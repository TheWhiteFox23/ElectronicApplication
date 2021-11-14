package cz.thewhiterabbit.electronicapp.canvas.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GeneralCanvasObject extends CanvasObject{
    public GeneralCanvasObject(){
        super();
    }

    public GeneralCanvasObject(double width, double height) {
        super(width, height);
    }

    public GeneralCanvasObject(double locationX, double locationY, double width, double height) {
        super(locationX, locationY, width, height);
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(getColor());
        gc.fillRect(getLocationX(), getLocationY(), getWidth(), getHeight());
    }

    private Paint getColor(){
        if(isHovered()){
            return Color.RED;
        }else if (isSelected()){
            return Color.BLUEVIOLET;
        }else{
            return Color.GREENYELLOW;
        }
    }

}
