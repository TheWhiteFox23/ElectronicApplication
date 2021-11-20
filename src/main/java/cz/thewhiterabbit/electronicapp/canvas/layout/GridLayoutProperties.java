package cz.thewhiterabbit.electronicapp.canvas.layout;

public class GridLayoutProperties extends LayoutProperties{
    private int gridX;
    private int gridY;
    private int gridHeight;
    private int gridWidth;

    public GridLayoutProperties(int gridX, int gridY, int gridHeight, int gridWidth) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
    }

    public int getGridX() {return gridX;}
    public void setGridX(int gridX) {
        this.gridX = gridX;
        super.propertiesChange();
    }
    public int getGridY() {return gridY;}
    public void setGridY(int gridY) {
        this.gridY = gridY;
        super.propertiesChange();
    }
    public int getGridHeight() {return gridHeight;}
    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
        super.propertiesChange();
    }
    public int getGridWidth() {return gridWidth;}
    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
        super.propertiesChange();
    }
}
