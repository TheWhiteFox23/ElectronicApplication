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
        //this.x1.addListener(l -> mapProperties());

        this.x2 = new SimpleIntegerProperty(x2);
        //this.x2.addListener(l -> mapProperties());

        this.y1 = new SimpleIntegerProperty(y1);
        //this.y1.addListener(l -> mapProperties());

        this.y2 = new SimpleIntegerProperty(y2);
        //this.y2.addListener(l -> mapProperties());
        mapProperties();

        /*gridXProperty().addListener( l->{
            onObjectMoved();
        });

        gridYProperty().addListener( l->{
            onObjectMoved();
        });*/

        locationXProperty().addListener(l ->{
            onObjectMoving();
        });
        locationYProperty().addListener(l-> {
            onObjectMoving();
        });


    }

    @Override
    protected void doPaint(GraphicsContext gc) {
        GridModel m = (GridModel)getParentModel();

        double locationX1 = m.getGridLocation(getX1(), m.getOriginX()) - getLocationX();
        double locationX2 = m.getGridLocation(getX2(), m.getOriginX()) - getLocationX();
        double locationY1 = m.getGridLocation(getY1(), m.getOriginY()) - getLocationY();
        double locationY2 = m.getGridLocation(getY2(), m.getOriginY()) - getLocationY();

        if(isHovered()){
            gc.setStroke(Color.RED);
        }else{
            gc.setStroke(Color.TURQUOISE);
        }

        gc.setLineWidth(getLineWidth());
        gc.strokeLine(locationX1, locationY1,locationX2, locationY2);
    }

    private void mapProperties(){
        this.setGridWidth(Math.abs(getX2() - getX1()));
        this.setGridHeight(Math.abs(getY1() - getY2()));
        this.setGridX((getX1() > getX2()? getX2():getX1()));
        this.setGridY((getY1() > getY2()? getY2():getY1()));
    }

    private void onObjectMoved(){
        int originalGridX = (getX1() > getX2()? getX2():getX1());
        int originalGridY = (getY1() > getY2()? getY2():getY1());
        int deltaX = originalGridX - getGridX();
        int deltaY = originalGridY - getGridY();
        setX1(getX1() - deltaX);
        setX2(getX2() - deltaX);
        setY2(getY2() - deltaY);
        setY1(getY1() - deltaY);
    }

    private void onObjectMoving(){
        if(getParentModel() != null && getParentModel() instanceof GridModel){
            GridModel m = (GridModel) getParentModel();
            int originalGridX = (getX1() > getX2()? getX2():getX1());
            int originalGridY = (getY1() > getY2()? getY2():getY1());
            int deltaX = originalGridX - m.getGridCoordinate(getLocationX(), m.getOriginX());
            int deltaY = originalGridY - m.getGridCoordinate(getLocationY(), m.getOriginY());
            setX1(getX1() - deltaX);
            setX2(getX2() - deltaX);
            setY2(getY2() - deltaY);
            setY1(getY1() - deltaY);
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

    @Override
    public boolean isInBounds(double x, double y) {
        double tolerance = getLineWidth();
        if(x<getLocationX()-tolerance || x> getLocationX() + getWidth() + tolerance || y<getLocationY() -tolerance || y>getLocationY() + getHeight() + tolerance)return false;
        int deltaX = getX2() - getX1();
        int deltaY = getY2() - getY1();


        if(deltaX == 0 || deltaY == 0){
            return true;
        }else{
            double a = getHeight()/getWidth();
            double xRelative = x -getLocationX();
            double yRelative = y -getLocationY();
            double yCalculated = a*xRelative;
            if(Math.abs(yRelative -yCalculated)<tolerance){
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean isVisible(double canvasWidth, double canvasHeight) {
        return super.isVisible(canvasWidth, canvasHeight);
    }

    @Override
    public boolean isInBounds(double locationX, double locationY, double width, double height) {
        return super.isInBounds(locationX, locationY, width, height);
    }

    private double getLineWidth(){
        return 2;
    }
}
