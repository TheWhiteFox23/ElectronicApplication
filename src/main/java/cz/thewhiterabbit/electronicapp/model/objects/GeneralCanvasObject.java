package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.components.Component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GeneralCanvasObject extends GeneralComponent {
    private final Component component = Component.TEST_COMPONENT;

    public GeneralCanvasObject(){
        setGridHeight(2);
        setGridWidth(2);
    }


    @Override
    public void doPaint(GraphicsContext gc) {
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0, 0, getHeight(), getHeight());
        gc.setFill(getColor());
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

    @Override
    public Component getComponent() {
        return component;
    }
}
