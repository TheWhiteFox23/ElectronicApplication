package cz.thewhiterabbit.electronicapp.canvas.layout;

import java.util.ArrayList;
import java.util.List;

public class LayoutProperties {
    private List<PropertiesListener> propertiesListenerList;

    /***** GRID LAYOUT *****/
    private int gridX;
    private int gridY;
    private int gridHeight;
    private int gridWidth;

    /***** RELATIVE LAYOUT *****/
    private double height;
    private double width;
    private double relativeLocationX;
    private double relativeLocationY;

    public LayoutProperties() {
        propertiesListenerList = new ArrayList<>();
        this.gridX = 0;
        this.gridY = 0;
        this.gridHeight = 0;
        this.gridWidth = 0;
        this.height = 0;
        this.width = 0;
        this.relativeLocationX = 0;
        this.relativeLocationY = 0;
    }

    public void set(int gridX, int gridY, int gridHeight, int gridWidth) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        propertiesChange();
    }

    public void set(double height, double width, double relativeLocationX, double relativeLocationY) {
        this.height = height;
        this.width = width;
        this.relativeLocationX = relativeLocationX;
        this.relativeLocationY = relativeLocationY;
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

    /***** RELATIVE LAYOUT ******/
    public double getHeight() {return height;}

    public void setHeight(double height) {
        this.height = height;
        propertiesChange();
    }
    public double getWidth() {return width;}

    public void setWidth(double width) {
        this.width = width;
        propertiesChange();
    }
    public double getRelativeLocationX() {return relativeLocationX;}

    public void setRelativeLocationX(double relativeLocationX) {
        this.relativeLocationX = relativeLocationX;
        propertiesChange();
    }

    public double getRelativeLocationY() {return relativeLocationY;}

    public void setRelativeLocationY(double relativeLocationY) {
        this.relativeLocationY = relativeLocationY;
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
