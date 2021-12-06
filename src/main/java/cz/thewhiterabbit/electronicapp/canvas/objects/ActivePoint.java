package cz.thewhiterabbit.electronicapp.canvas.objects;


import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ActivePoint extends CanvasObject{

    /***** OVERRIDES *****/
    @Override
    public void paint(GraphicsContext gc) {
        double height = getHeight()*0.4;
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

    @Override
    protected void onObjectDropped(Event e) {
        e.consume();
        System.out.println("INSERT LINE");
    }

    @Override
    protected void onObjectDragged(Event e) {
        e.consume();
        System.out.println("DRAW LINE");
    }
}
