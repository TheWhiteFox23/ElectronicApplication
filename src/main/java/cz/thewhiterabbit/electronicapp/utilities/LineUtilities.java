package cz.thewhiterabbit.electronicapp.utilities;

import org.apache.commons.math3.linear.*;

public class LineUtilities {

    /**
     * Return if two given lines are intersecting.
     * @param line1 first line of the intersection. Conventionally is corresponding to the TwoPointLineObject.
     * @param line2 second line of the intersection. Conventionally is vertical or horizontal line of the selection rectangle.
     * @return True, if intersection exists and is inside the bounds of the line.
     */
    public boolean isIntersection(LineCrate line1, LineCrate line2) {
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
            return isIntersection(line1.invert(), line2.invert());
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

}
