package cz.thewhiterabbit.electronicapp.canvas.objects;

import cz.thewhiterabbit.electronicapp.canvas.layout.PropertiesListener;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ActiveZone extends CanvasObject{
    private CanvasObject owner;
    private int relativeLocationX;
    private int relativeLocationY;

    public ActiveZone(CanvasObject owner, int relativeLocationX, int relativeLocationY) {
        setOwner(owner);
        this.relativeLocationX = relativeLocationX;
        this.relativeLocationY = relativeLocationY;
        getLayoutProperties().set(0,0,1,1);
    }

    public ActiveZone() {
        this.relativeLocationX = 0;
        this.relativeLocationY = 0;
    }

    @Override
    public void paint(GraphicsContext gc) {
        //System.out.println("paint");
        double height = getHeight()*0.5;
        double locationX = getLocationX()-height/2;
        double locationY = getLocationY()-height/2;
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillOval(locationX, locationY, height, height);
    }

    @Override
    public boolean isInBounds(double x, double y) {
        double height = getHeight()*0.5;
        double locationX = getLocationX()-height/2;
        double locationY = getLocationY()-height/2;
        return ((x>=locationX && x<=locationX+height)&&(y>=locationY && y<= locationY+height));
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    public CanvasObject getOwner() {
        return owner;
    }

    public void setOwner(CanvasObject owner) {
        this.owner = owner;
        owner.getLayoutProperties().addPropertiesListener(new PropertiesListener() {
            @Override
            public void onPropertiesChange() {
                initLocation(owner);
            }
        });
    }

    private void initLocation(CanvasObject owner) {
        getLayoutProperties().setGridX(owner.getLayoutProperties().getGridX()+relativeLocationX);
        getLayoutProperties().setGridY(owner.getLayoutProperties().getGridY()+relativeLocationY);
    }

    public int getRelativeLocationX() {
        return relativeLocationX;
    }

    public void setRelativeLocationX(int relativeLocationX) {
        this.relativeLocationX = relativeLocationX;
    }

    public int getRelativeLocationY() {
        return relativeLocationY;
    }

    public void setRelativeLocationY(int relativeLocationY) {
        this.relativeLocationY = relativeLocationY;
    }
}
