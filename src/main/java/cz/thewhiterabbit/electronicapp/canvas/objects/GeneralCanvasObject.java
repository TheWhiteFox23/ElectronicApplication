package cz.thewhiterabbit.electronicapp.canvas.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class GeneralCanvasObject extends CanvasObject{
    private List<ActivePoint> activePoints;

    public GeneralCanvasObject(){
        super();
        activePoints = new ArrayList<>();
        ActivePoint activePoint = new ActivePoint(this, 0, 0);
        addActiveZone(activePoint);
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(getColor());
        gc.fillRect(getLocationX(), getLocationY(), getWidth(), getHeight());
    }

    @Override
    public List<ActivePoint> getActiveZones() {
        return activePoints;
    }

    @Override
    public void addActiveZone(ActivePoint activePoint) {
        activePoints.add(activePoint);
    }

    @Override
    public void removeActiveZone(ActivePoint activePoint) {
        activePoints.remove(activePoint);
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
