package cz.thewhiterabbit.electronicapp.canvas.layout;

public class RelativeLayoutProperties extends LayoutProperties{
    private double height;
    private double width;
    private double relativeLocationX;
    private double relativeLocationY;

    public RelativeLayoutProperties(double height, double width, double relativeLocationX, double relativeLocationY) {
        this.height = height;
        this.width = width;
        this.relativeLocationX = relativeLocationX;
        this.relativeLocationY = relativeLocationY;
    }

    public double getHeight() {return height;}

    public void setHeight(double height) {
        this.height = height;
        super.propertiesChange();
    }
    public double getWidth() {return width;}

    public void setWidth(double width) {
        this.width = width;
        super.propertiesChange();
    }
    public double getRelativeLocationX() {return relativeLocationX;}

    public void setRelativeLocationX(double relativeLocationX) {
        this.relativeLocationX = relativeLocationX;
        super.propertiesChange();
    }

    public double getRelativeLocationY() {return relativeLocationY;}

    public void setRelativeLocationY(double relativeLocationY) {
        this.relativeLocationY = relativeLocationY;
        super.propertiesChange();
    }
}
