package cz.thewhiterabbit.electronicapp.utilities;

import org.apache.commons.math3.linear.*;

public class LineCrate {
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
