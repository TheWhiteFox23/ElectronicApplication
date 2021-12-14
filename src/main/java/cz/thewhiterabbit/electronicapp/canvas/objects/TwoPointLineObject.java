package cz.thewhiterabbit.electronicapp.canvas.objects;

import cz.thewhiterabbit.electronicapp.canvas.model.GridModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TwoPointLineObject extends CanvasObject{
    private IntegerProperty x1;
    private IntegerProperty x2;
    private IntegerProperty y1;
    private IntegerProperty y2;

    public TwoPointLineObject(int x1, int y1, int x2, int y2) {
        this.x1 = new SimpleIntegerProperty(x1);
        this.x2 = new SimpleIntegerProperty(x2);
        this.y1 = new SimpleIntegerProperty(y1);
        this.y2 = new SimpleIntegerProperty(y2);
        mapProperties();

        this.locationXProperty().addListener(l -> onGridXChanged());

        this.locationYProperty().addListener(l -> onGridYChanged());
    }

    @Override
    protected void doPaint(GraphicsContext gc) {
        if(getParentModel() != null && getParentModel() instanceof GridModel){
            GridModel m = (GridModel)getParentModel();

            double locationX1 = m.getGridLocation(getX1(), m.getOriginX()) - getLocationX();
            double locationX2 = m.getGridLocation(getX2(), m.getOriginX()) - getLocationX();
            double locationY1 = m.getGridLocation(getY1(), m.getOriginY()) - getLocationY();
            double locationY2 = m.getGridLocation(getY2(), m.getOriginY()) - getLocationY();

            if(isHovered()){
                gc.setStroke(Color.RED);
            }else if(isSelected()){
                gc.setStroke(Color.BLUE);
            }else{
                gc.setStroke(Color.TURQUOISE);
            }

            gc.setLineWidth(getLineWidth());
            gc.strokeLine(locationX1, locationY1,locationX2, locationY2);
        }
    }

    public void mapProperties(){
        this.setGridWidth(Math.abs(getX2() - getX1()));
        this.setGridHeight(Math.abs(getY1() - getY2()));
        this.setGridX((Math.min(getX1(), getX2())));
        this.setGridY((Math.min(getY1(), getY2())));
        if(getParentModel() != null){
            this.getParentModel().updatePaintProperties(this);
        }
    }

    private void onGridXChanged(){
        if(getParentModel() != null && getParentModel() instanceof GridModel){
            GridModel m = (GridModel) getParentModel();
            int originalGridX = (Math.min(getX1(), getX2()));
            int deltaX = originalGridX - m.getGridCoordinate(getLocationX(), m.getOriginX());
            setX1(getX1() - deltaX);
            setX2(getX2() - deltaX);
        }
    }

    private void onGridYChanged(){
        if(getParentModel() != null && getParentModel() instanceof GridModel){
            GridModel m = (GridModel) getParentModel();
            int originalGridY = (Math.min(getY1(), getY2()));
            int deltaY = originalGridY - m.getGridCoordinate(getLocationY(), m.getOriginY());
            setY1(getY1() - deltaY);
            setY2(getY2() - deltaY);
        }
    }


    public int getX1() {return x1.get();}

    public IntegerProperty x1Property() {return x1;}

    public void setX1(int x1) {this.x1.set(x1);}

    public int getX2() {return x2.get();}

    public IntegerProperty x2Property() {return x2;}

    public void setX2(int x2) {this.x2.set(x2);}

    public int getY1() {return y1.get();}

    public IntegerProperty y1Property() {return y1;}

    public void setY1(int y1) {this.y1.set(y1);}

    public int getY2() {return y2.get();}

    public IntegerProperty y2Property() {return y2;}

    public void setY2(int y2) {this.y2.set(y2);}

    public int getLoverX(){
        return (Math.min(getX2(), getX1()));
    }

    public int getHigherX(){
        return (Math.max(getX2(), getX1()));
    }

    public int getLoverY(){
        return (Math.min(getY2(), getY1()));
    }

    public int getHigherY(){
        return (Math.max(getY2(), getY1()));
    }


    @Override
    public boolean isInBounds(double x, double y) {
        GridModel m = (GridModel) getParentModel();
        double tolerance = getLineWidth();
        if(x<getLocationX()-tolerance || x> getLocationX() + getWidth() + tolerance || y<getLocationY() -tolerance || y>getLocationY() + getHeight() + tolerance)return false;
        double deltaX = m.getGridLocation(getX2(), m.getOriginX()) - m.getGridLocation(getX1(), m.getOriginX());
        double deltaY = m.getGridLocation(getY2(), m.getOriginY()) - m.getGridLocation(getY1(), m.getOriginY());


        if(deltaX == 0 || deltaY == 0){
            return true;
        }else{
            double a = deltaY/deltaX;
            double xRelative = x -getLocationX();
            double yRelative = y -getLocationY();
            if(a > 0){
                double yCalculated = a*xRelative;
                return Math.abs(yRelative - yCalculated) < tolerance;
            }else{
                double yCalculated = Math.abs(a)*xRelative;
                return Math.abs(yRelative - (getHeight() - yCalculated)) < tolerance;
            }

        }

    }

    @Override
    public boolean isVisible(double canvasWidth, double canvasHeight) {
        return super.isVisible(canvasWidth, canvasHeight);
    }

    @Override
    public boolean isInBounds(double locationX, double locationY, double width, double height) {
        if(getParentModel() != null && getParentModel() instanceof  GridModel){
            GridModel m = (GridModel) getParentModel();

            System.out.println("X: " + locationX + " Y: " + locationY + " W: " + width + " H: " + height);
            double x1 = m.getGridLocation(getLoverX(), m.getOriginX());
            double y1 = m.getGridLocation(getLoverY(), m.getOriginY());
            double x2 = m.getGridLocation(getHigherX(), m.getOriginX());
            double y2 = m.getGridLocation(getHigherY(), m.getOriginY());

            //first point in bounds
            boolean firstPointInBounds = (x1 >= locationX && x1 <= locationX + width && y1 >= locationY && y1 <= locationY + height);
            //second point in bounds
            boolean secondPointInBounds = (x2 >= locationX && x2 <= locationX + width && y2 >= locationY && y2 <= locationY + height);
            //location x, location y, locationX +height, location y;

            System.out.println("First point in bounds: " + firstPointInBounds);
            System.out.println("Second point in bounds: " + secondPointInBounds);
        }
        return false;
    }

    private double getLineWidth(){
        return 2;
    }

    @Override
    public void clean(GraphicsContext gc) {
        super.clean(gc);
    }

}
