package cz.thewhiterabbit.electronicapp.canvas.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class GeneralCanvasObject extends CanvasObject{

    public GeneralCanvasObject(){
        super();
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.save();
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(getLocationX(), getLocationY(), getHeight(), getHeight());
        gc.setFill(getColor());
        gc.translate(getLocationX() + getWidth(), getLocationY() );
        gc.rotate(90);
        gc.fillRect(0, 0, getWidth()/2, getHeight()/2);
        gc.fillRect(getWidth()/2, getHeight()/2, getWidth()/2, getHeight()/2);
        gc.restore();

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
