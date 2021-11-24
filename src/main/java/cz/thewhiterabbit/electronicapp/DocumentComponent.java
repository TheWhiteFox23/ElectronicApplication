package cz.thewhiterabbit.electronicapp;

import java.util.ArrayList;

/**
 * Basic document object, represents component or some part of the circuit (conductor, node)
 */
public abstract class DocumentComponent {
    //LAYOUT PROPERTIES
    /***** GRID LAYOUT *****/
    private int gridX;
    private int gridY;
    private int gridHeight;
    private int gridWidth;

    public DocumentComponent() {
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

    /**
     * Fire properties change event into the event aggregator
     */
    public void propertiesChange(){

    }
}
