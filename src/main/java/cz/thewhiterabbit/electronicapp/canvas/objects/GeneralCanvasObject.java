package cz.thewhiterabbit.electronicapp.canvas.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class GeneralCanvasObject extends CanvasObject{
    private List<ActiveZone> activeZones;

    public GeneralCanvasObject(){
        super();
        activeZones = new ArrayList<>();
        ActiveZone activeZone = new ActiveZone(this, 0, 0);
        addActiveZone(activeZone);
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(getColor());
        gc.fillRect(getLocationX(), getLocationY(), getWidth(), getHeight());
    }

    @Override
    public List<ActiveZone> getActiveZones() {
        return activeZones;
    }

    @Override
    public void addActiveZone(ActiveZone activeZone) {
        activeZones.add(activeZone);
    }

    @Override
    public void removeActiveZone(ActiveZone activeZone) {
        activeZones.remove(activeZone);
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
