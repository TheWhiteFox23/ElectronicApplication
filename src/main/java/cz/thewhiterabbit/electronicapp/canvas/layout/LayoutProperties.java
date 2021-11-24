package cz.thewhiterabbit.electronicapp.canvas.layout;

import java.util.ArrayList;
import java.util.List;

public class LayoutProperties {
    private List<PropertiesListener> propertiesListenerList; //TODO move to document object

    /***** GRID LAYOUT *****/
    private int gridX;
    private int gridY;
    private int gridHeight;
    private int gridWidth;

    public LayoutProperties() {
        propertiesListenerList = new ArrayList<>();
        this.gridX = 0;
        this.gridY = 0;
        this.gridHeight = 0;
        this.gridWidth = 0;
    }

    public void set(int gridX, int gridY, int gridHeight, int gridWidth) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        propertiesChange();
    }

    /**** GRID LAYOUT *****/

    public int getGridX() {return gridX;}
    public void setGridX(int gridX) {
        this.gridX = gridX;
        propertiesChange();
    }
    public int getGridY() {return gridY;}
    public void setGridY(int gridY) {
        this.gridY = gridY;
        propertiesChange();
    }
    public int getGridHeight() {return gridHeight;}
    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
        propertiesChange();
    }
    public int getGridWidth() {return gridWidth;}
    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
        propertiesChange();
    }

    /****** PROPERTIES CHANGE ******/
    public void addPropertiesListener(PropertiesListener propertiesListener){
        propertiesListenerList.add(propertiesListener);
    }
    public void propertiesChange(){
        propertiesListenerList.forEach(l -> l.onPropertiesChange());
    }

}
