package cz.thewhiterabbit.electronicapp.canvas.layout;

public class AbsolutLayoutProperties extends LayoutProperties{
    private double width;
    private double height;
    private double locationX;
    private double locationY;

    public AbsolutLayoutProperties(double width, double height, double locationX, double locationY) {
        this.width = width;
        this.height = height;
        this.locationX = locationX;
        this.locationY = locationY;
    }
    public double getWidth() {return width;}
    public void setWidth(double width) {this.width = width;}
    public double getHeight() {return height;}
    public void setHeight(double height) {this.height = height;}
    public double getLocationX() {return locationX;}
    public void setLocationX(double locationX) {this.locationX = locationX;}
    public double getLocationY() {return locationY;}
    public void setLocationY(double locationY) {this.locationY = locationY;}
}
