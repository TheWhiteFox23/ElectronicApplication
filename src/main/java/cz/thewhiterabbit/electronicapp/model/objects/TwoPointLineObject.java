package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.linear.*;

public class TwoPointLineObject extends DocumentObject {
    private IntegerProperty x1;
    private IntegerProperty x2;
    private IntegerProperty y1;
    private IntegerProperty y2;


    public TwoPointLineObject( int x1, int y1, int x2, int y2) {
        this.x1 = new SimpleIntegerProperty(x1);
        this.x2 = new SimpleIntegerProperty(x2);
        this.y1 = new SimpleIntegerProperty(y1);
        this.y2 = new SimpleIntegerProperty(y2);
        mapLineProperties();

        this.locationXProperty().addListener(l -> onGridXChanged());

        this.locationYProperty().addListener(l -> onGridYChanged());
    }

    public TwoPointLineObject(RawObject rawObject) {
        setRawObject(rawObject);

        this.x1 = new SimpleIntegerProperty(this, "X1");
        this.x2 = new SimpleIntegerProperty(this, "X2");
        this.y1 = new SimpleIntegerProperty(this, "Y1");
        this.y2 = new SimpleIntegerProperty(this, "Y2");

        this.locationXProperty().addListener(l -> onGridXChanged());
        this.locationYProperty().addListener(l -> onGridYChanged());
    }

    @Override
    protected void doPaint(GraphicsContext gc) {
        if (getParentModel() != null && getParentModel() instanceof GridModel) {
            GridModel m = (GridModel) getParentModel();

            double locationX1 = m.getGridLocation(getX1(), m.getOriginX()) - getLocationX();
            double locationX2 = m.getGridLocation(getX2(), m.getOriginX()) - getLocationX();
            double locationY1 = m.getGridLocation(getY1(), m.getOriginY()) - getLocationY();
            double locationY2 = m.getGridLocation(getY2(), m.getOriginY()) - getLocationY();

            if (isHovered()) {
                gc.setStroke(Color.RED);
            } else if (isSelected()) {
                gc.setStroke(Color.BLUE);
            } else {
                gc.setStroke(Color.TURQUOISE);
            }

            gc.setLineWidth(getLineWidth());
            gc.strokeLine(locationX1, locationY1, locationX2, locationY2);
        }
    }

    @Override
    public void init() {
        setX1(Integer.parseInt(getRawObject().getProperty("X1").getValue()));
        setY1(Integer.parseInt(getRawObject().getProperty("Y1").getValue()));
        setX2(Integer.parseInt(getRawObject().getProperty("X2").getValue()));
        setY2(Integer.parseInt(getRawObject().getProperty("Y2").getValue()));
        mapProperties();
        mapLineProperties();
    }

    @Override
    public void mapProperties() {
        getRawObject().getProperty("X1").valueProperty().addListener((obs, oldVal, newVal) -> {
            setX1(Integer.parseInt(newVal));
        });
        getRawObject().getProperty("Y1").valueProperty().addListener((obs, oldVal, newVal) -> {
            setY1(Integer.parseInt(newVal));
        });
        getRawObject().getProperty("X2").valueProperty().addListener((obs, oldVal, newVal) -> {
            setX2(Integer.parseInt(newVal));
        });
        getRawObject().getProperty("Y2").valueProperty().addListener((obs, oldVal, newVal) -> {
            setY2(Integer.parseInt(newVal));
        });
    }

    @Override
    public void setLocation(int deltaX, int deltaY){
        getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, this, x1Property(), getX1()-deltaX, getX1()));
        getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, this, x2Property(), getX2()-deltaX, getX2()));
        getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, this, y1Property(), getY1()-deltaY, getY1()));
        getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, this, y2Property(), getY2()-deltaY, getY2()));
        mapLineProperties();
    }

    public void mapLineProperties() {
        this.setGridWidth(Math.abs(getX2() - getX1()));
        this.setGridHeight(Math.abs(getY1() - getY2()));
        this.setGridX((Math.min(getX1(), getX2())));
        this.setGridY((Math.min(getY1(), getY2())));
        if (getParentModel() != null) {
            this.getParentModel().updatePaintProperties(this);
        }
    }

    private void onGridXChanged() {
        if (getParentModel() != null && getParentModel() instanceof GridModel) {
            GridModel m = (GridModel) getParentModel();
            int originalGridX = (Math.min(getX1(), getX2()));
            int deltaX = originalGridX - m.getGridCoordinate(getLocationX(), m.getOriginX());
            setX1(getX1() - deltaX);
            setX2(getX2() - deltaX);
        }
    }

    private void onGridYChanged() {
        if (getParentModel() != null && getParentModel() instanceof GridModel) {
            GridModel m = (GridModel) getParentModel();
            int originalGridY = (Math.min(getY1(), getY2()));
            int deltaY = originalGridY - m.getGridCoordinate(getLocationY(), m.getOriginY());
            setY1(getY1() - deltaY);
            setY2(getY2() - deltaY);
        }
    }


    public int getX1() {
        return x1.get();
    }

    public IntegerProperty x1Property() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1.set(x1);
    }

    public int getX2() {
        return x2.get();
    }

    public IntegerProperty x2Property() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2.set(x2);
    }

    public int getY1() {
        return y1.get();
    }

    public IntegerProperty y1Property() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1.set(y1);
    }

    public int getY2() {
        return y2.get();
    }

    public IntegerProperty y2Property() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2.set(y2);
    }

    public int getLoverX() {
        return (Math.min(getX2(), getX1()));
    }

    public int getHigherX() {
        return (Math.max(getX2(), getX1()));
    }

    public int getLoverY() {
        return (Math.min(getY2(), getY1()));
    }

    public int getHigherY() {
        return (Math.max(getY2(), getY1()));
    }


    @Override
    public boolean isInBounds(double x, double y) {
        GridModel m = (GridModel) getParentModel();
        double tolerance = getLineWidth();
        if (x < getLocationX() - tolerance || x > getLocationX() + getWidth() + tolerance || y < getLocationY() - tolerance || y > getLocationY() + getHeight() + tolerance)
            return false;
        double deltaX = m.getGridLocation(getX2(), m.getOriginX()) - m.getGridLocation(getX1(), m.getOriginX());
        double deltaY = m.getGridLocation(getY2(), m.getOriginY()) - m.getGridLocation(getY1(), m.getOriginY());


        if (deltaX == 0 || deltaY == 0) {
            return true;
        } else {
            double a = deltaY / deltaX;
            double xRelative = x - getLocationX();
            double yRelative = y - getLocationY();
            if (a > 0) {
                double yCalculated = a * xRelative;
                return Math.abs(yRelative - yCalculated) < tolerance;
            } else {
                double yCalculated = Math.abs(a) * xRelative;
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
        if (getParentModel() != null && getParentModel() instanceof GridModel) {
            GridModel m = (GridModel) getParentModel();

            double x1 = m.getGridLocation(getLoverX(), m.getOriginX());
            double y1 = m.getGridLocation(getLoverY(), m.getOriginY());
            double x2 = m.getGridLocation(getHigherX(), m.getOriginX());
            double y2 = m.getGridLocation(getHigherY(), m.getOriginY());

            boolean firstPointInBounds = (x1 >= locationX && x1 <= locationX + width && y1 >= locationY && y1 <= locationY + height);
            boolean secondPointInBounds = (x2 >= locationX && x2 <= locationX + width && y2 >= locationY && y2 <= locationY + height);

            LineCrate line = new LineCrate(x1,y1, x2, y2);
            boolean upperXIntersection = isIntersection(line, new LineCrate(locationX, locationY, locationX+width, locationY));
            boolean lowerXIntersection = isIntersection(line, new LineCrate(locationX, locationY+height, locationX+width, locationY+height));
            boolean leftYIntersection = isIntersection(line, new LineCrate(locationX, locationY, locationX, locationY+height));
            boolean rightYIntersection = isIntersection(line, new LineCrate(locationX+width, locationY, locationX+width, locationY+height));

            boolean result = firstPointInBounds || secondPointInBounds || upperXIntersection || lowerXIntersection || leftYIntersection || rightYIntersection;
            return result;
        }
        return false;
    }

    private double getLineWidth() {
        return 2;
    }

    @Override
    public void clean(GraphicsContext gc) {
        super.clean(gc);
    }


    /**
     * Return if two given lines are intersecting.
     * @param line1 first line of the intersection. Conventionally is corresponding to the TwoPointLineObject.
     * @param line2 second line of the intersection. Conventionally is vertical or horizontal line of the selection rectangle.
     * @return True, if intersection exists and is inside the bounds of the line.
     */
    private boolean isIntersection(LineCrate line1, LineCrate line2) {
        try{
            line1.calculateCoefficients();
            line2.calculateCoefficients();
        }catch (Exception e){
            return isIntersectionExtreme(line1, line2);
        }

        try{
            RealVector solution = getIntersectionSolution(line1, line2);
            return isInLinesBonds(line1, line2,  solution.getEntry(0), solution.getEntry(1));
        } catch (Exception e){
            return false;
        }
    }

    /**
     * Return if given intersection points are with in the bounds of the given lines.
     * @param line1 first line of the intersection. Conventionally is corresponding to the TwoPointLineObject.
     * @param line2 second line of the intersection. Conventionally is vertical or horizontal line of the selection rectangle.
     * @param intersectionX X coordinate of the intersection point
     * @param intersectionY Y coordinate of the intersection point
     * @return True if given point is with in the bounds of the lines. Else false
     */
    private boolean isInLinesBonds(LineCrate line1, LineCrate line2, double intersectionX, double intersectionY) {
        return intersectionX >= Math.min(line1.x1, line1.x2) && intersectionX <= Math.max(line1.x2, line1.x1) &&
                intersectionX >= Math.min(line2.x1, line2.x2) && intersectionX <= Math.max(line2.x2, line2.x1) &&
                intersectionY >= Math.min(line1.y1, line1.y2) && intersectionY <= Math.max(line1.y2, line1.y1) &&
                intersectionY >= Math.min(line2.y1, line2.y2) && intersectionY <= Math.max(line2.y2, line2.y1);
    }

    /**
     * Solve intersection point of two lines
     * @param line1 first line of the intersection. Conventionally is corresponding to the TwoPointLineObject.
     * @param line2 second line of the intersection. Conventionally is vertical or horizontal line of the selection rectangle.
     * @return RealVector solution of the intersection. index 0 -> X, index 1 -> Y
     */
    private RealVector getIntersectionSolution(LineCrate line1, LineCrate line2) {
        RealMatrix coefficients =
                new Array2DRowRealMatrix(new double[][]{{line1.a, -1}, {line2.a, -1}},
                        false);
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
        RealVector constants = new ArrayRealVector(new double[]{-line1.b, -line2.b}, false);
        return solver.solve(constants);
    }

    /**
     * Solve intersection for the extreme cases. One of the lines is perpendicular to the origin
     * @param line1 first line of the intersection. Conventionally is corresponding to the TwoPointLineObject.
     * @param line2 second line of the intersection. Conventionally is vertical or horizontal line of the selection rectangle.
     * @return True if lines has intersection and intersection is with in the bounds of the line
     */
    private boolean isIntersectionExtreme(LineCrate line1, LineCrate line2){
        if((line1.isHorizontal() || line1.isVertical()) && (line2.isVertical() || line1.isHorizontal())){
            return isIntersectionPerpendicular(line1, line2);
        }else if(!line1.isInverted() && !line2.isInverted()) {
            return isIntersection(
                    line1.invert(),
                    line2.invert());
        }
        return false;
    }

    /**
     * Solve intersection of the lines in case each of the two lines is vertical or horizontal
     * @param line1 first line of the intersection. Conventionally is corresponding to the TwoPointLineObject.
     * @param line2 second line of the intersection. Conventionally is vertical or horizontal line of the selection rectangle.
     * @return True if lines has intersection and intersection is with in the bounds of the line
     */
    private boolean isIntersectionPerpendicular(LineCrate line1, LineCrate line2) {
        if(line1.isHorizontal() && line2.isHorizontal()){
            return line1.y1 == line2.y1;
        }else if(line1.isHorizontal() && line2.isVertical()){
            return doIsIntersectingPerpendicular(line1, line2);
        }else if(line1.isVertical() && line1.isVertical()){
            return line1.x1 == line2.x1;
        }else if(line1.isVertical() && line2.isHorizontal()){
            return doIsIntersectingPerpendicular(line2, line1);
        }else {
            return false;
        }
    }

    /**
     * Check if line are intersecting in the bounds of the line, in case that two lines are perpendicular
     * @param line1 first line of the intersection. Conventionally is corresponding to the TwoPointLineObject.
     * @param line2 second line of the intersection. Conventionally is vertical or horizontal line of the selection rectangle.
     * @return True if lines has intersection and intersection is with in the bounds of the line
     */
    private boolean doIsIntersectingPerpendicular(LineCrate line1, LineCrate line2) {
        return (line1.y1 >= Math.min(line2.y1, line2.y2) && line1.y1 <= Math.max(line2.y1, line2.y2)
                && line2.x1 >= Math.min(line1.x1, line1.x2) && line2.x1 <= Math.max(line1.x1, line1.x2));
    }

    /**
     * Helper object for manipulating with the lines and calculations. Represent line as two point object.
     */
    private class LineCrate {
        double x1, y1, x2, y2, a, b;
        private boolean inverted = false;

        public LineCrate(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        /**
         * Calculate coefficients A and B representing the angle and offset of the line. (aX+b = y)
         */
        public void calculateCoefficients() throws Exception {
            RealMatrix coefficients =
                    new Array2DRowRealMatrix(new double[][]{{x1, 1}, {x2, 1}},
                            false);
            DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
            RealVector constants = new ArrayRealVector(new double[]{y1, y2}, false);
            RealVector solution = solver.solve(constants);

            this.a = solution.getEntry(0);
            this.b = solution.getEntry(1);
        }
        
        public boolean isHorizontal(){
            return y1 == y2;
        }
        
        public boolean isVertical(){
            return x1 == x2;
        }

        public LineCrate invert(){
            double temp = x1;
            x1 = y1;
            y1 = temp;
            temp = x2;
            x2 = y2;
            y2 = temp;
            inverted = !inverted;
            return this;
        }

        public boolean isInverted() {
            return inverted;
        }
    }

}
