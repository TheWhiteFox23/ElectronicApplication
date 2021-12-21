package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;

import java.util.*;

public class LineOptimizer {

    /**
     * Optimize all the lines in the given gridModel. Optimization include splitting lines in the point of intersections,
     * removing line duplicities and merging continuous lines without branching
     *
     * @param gridModel
     */
    public void optimize(GridModel gridModel) {
        if (gridModel != null) {
            splitIntersectingLines(gridModel);
            removeDuplicities(gridModel);
            mergeContinuous(gridModel);
        }
    }
    /****** LINE SPLITTING ******/


    /**
     * Split all the lines in the point of intersection. (As intersection are count ActivePoints and other lines with
     * different orientation)
     *
     * @param gridModel
     */
    private void splitIntersectingLines(GridModel gridModel) {
        List<TwoPointLineObject> lines = getTwoPointLineObjects(gridModel);
        lines.forEach(l -> {
            splitLine(gridModel, l);
        });
    }

    /**
     * Get all two point line object from the grid model
     *
     * @param gridModel
     * @return
     */
    private List<TwoPointLineObject> getTwoPointLineObjects(GridModel gridModel) {
        List<TwoPointLineObject> lines = new ArrayList<>();
        gridModel.getAll().forEach(o -> {
            if (o instanceof TwoPointLineObject) lines.add((TwoPointLineObject) o);
        });
        return lines;
    }

    /**
     * Remove original line and replace it with new lines based on the splitting points
     *
     * @param gridModel
     * @param l
     */
    private void splitLine(GridModel gridModel, TwoPointLineObject l) {
        //getObject on the line
        List<CanvasObject> object = getIntersectingObjects(gridModel, l);

        //Calculate splitting
        List<PointCrate> splittingPoints = getSplittingPoints(l, object);
        if (splittingPoints.size() != 0) {
            splitLine(l, splittingPoints, gridModel);
        }
    }

    /**
     * Get all object that intersect the line with exception to children of the object and object itself
     *
     * @param gridModel
     * @param line
     * @return
     */
    private List<CanvasObject> getIntersectingObjects(GridModel gridModel, TwoPointLineObject line) {
        List<CanvasObject> object = gridModel.getInBounds(line.getLocationX(), line.getLocationY(),
                Math.max(1, line.getWidth()), Math.max(1, line.getHeight()));


        if (object.contains(line)) object.remove(line);
        line.getChildrenList().forEach(o -> {
            if (object.contains(o)) object.remove(o);
        });

        return object;
    }

    /**** MERGE CONTINUOUS ****/


    /**
     * Merge continuous lines if there is no branching in the nodes
     *
     * @param gridModel
     */
    private void mergeContinuous(GridModel gridModel) {
        List<LineCrate> horizontal = getLineByOrientation(gridModel, true);
        List<LineCrate> vertical = getLineByOrientation(gridModel, false);

        horizontal.sort(horizontalComparator);
        vertical.sort(verticalComparator);

        merge(horizontal, gridModel);
        merge(vertical, gridModel);

    }

    /**
     * Sort lines by Y level and X axis in descending order
     */
    Comparator<LineCrate> horizontalComparator = new Comparator<LineCrate>() {
        @Override
        public int compare(LineCrate o1, LineCrate o2) {
            int result = o1.point1.y - o2.point1.y;
            if (result == 0) return o1.object.getLoverX() - o2.object.getLoverX();
            return result;
        }
    };

    /**
     * Sort lines by X level and Y axis in descending order
     */
    Comparator<LineCrate> verticalComparator = new Comparator<LineCrate>() {
        @Override
        public int compare(LineCrate o1, LineCrate o2) {
            int result = o1.point1.x - o2.point1.x;
            if (result == 0) return o1.object.getLoverY() - o2.object.getLoverY();
            return result;
        }
    };

    /**
     * Return all lines in given model with corresponding orientation
     *
     * @param gridModel
     * @param isHorizontal
     * @return
     */
    private List<LineCrate> getLineByOrientation(GridModel gridModel, boolean isHorizontal) {
        List<LineCrate> lines = new ArrayList<>();
        gridModel.getAll().forEach(o -> {
            if (o instanceof TwoPointLineObject && isHorizontal((TwoPointLineObject) o) == isHorizontal) {
                lines.add(new LineCrate((TwoPointLineObject) o));
            }
        });
        return lines;
    }

    /**
     * Merge lines, expect sorted list of the line as an input
     *
     * @param lines
     * @param gridModel
     */
    private void merge(List<LineCrate> lines, GridModel gridModel) {
        List<LineCrate> toMerge = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            toMerge.add(lines.get(i));
            if (i + 1 < lines.size()) {
                if (!canBeMerged(lines.get(i), lines.get(i + 1), gridModel)) {
                    doMerge(toMerge, gridModel);
                    toMerge.clear();
                }
            }
        }
        doMerge(toMerge, gridModel);
    }

    /**
     * Merge given lines together, expect toMerge array to be sorted
     *
     * @param toMerge
     * @param gridModel
     */
    private void doMerge(List<LineCrate> toMerge, GridModel gridModel) {
        if (toMerge.size() == 1) return;

        PointCrate point1 = toMerge.get(0).point1;
        PointCrate point2 = toMerge.get(toMerge.size() - 1).point2;

        toMerge.forEach(o -> o.object.callForDelete());

        callForAdd(gridModel, point1, point2);
    }

    /**
     * Decide if two lines can be merged together
     *
     * @param line1
     * @param line2
     * @param gridModel
     * @return
     */
    private boolean canBeMerged(LineCrate line1, LineCrate line2, GridModel gridModel) {
        if (!line1.point2.equals(line2.point1)) return false;
        ActivePoint ac = getLowerActionPoint(line1);
        if (gridModel != null && ac != null) {
            List<CanvasObject> objects = gridModel.getInBounds(
                    ac.getLocationX(), ac.getLocationY(), ac.getWidth(), ac.getHeight());
            objects.removeIf(e -> !(e instanceof ActivePoint) || e == ac);
            if (objects.size() != 1) return false;
        }
        return true;
    }

    /**
     * Return action point with lower coordinates
     *
     * @param line1
     * @return
     */
    private ActivePoint getLowerActionPoint(LineCrate line1) {
        for (CanvasObject o : line1.object.getChildrenList()) {
            if (o instanceof ActivePoint && o.getGridY() == line1.point2.y && o.getGridX() == line1.point2.x) {
                return (ActivePoint) o;
            }
        }
        return null;
    }

    /**** REMOVING DUPLICITIES ****/

    /**
     * Remove line duplicities. As duplicities count lines with same point1 and point2
     *
     * @param gridModel
     */
    private void removeDuplicities(GridModel gridModel) {
        List<LineCrate> lineCrateList = getLineCrates(gridModel);

        HashMap<LineCrate, TwoPointLineObject> lineMap = new HashMap<>();
        List<LineCrate> toDelete = new ArrayList<>();

        lineCrateList.forEach(l -> {
            if (!lineMap.containsKey(l)) {
                lineMap.put(l, l.object);
            } else {
                toDelete.add(l);
            }
        });

        toDelete.forEach(l -> {l.object.callForDelete();});
    }

    /**
     * Get all lines from gridModel in form of LineCrate
     * @param gridModel
     * @return
     */
    private List<LineCrate> getLineCrates(GridModel gridModel) {
        List<LineCrate> lineCrateList = new ArrayList<>();
        gridModel.getAll().forEach(o -> {
            if (o instanceof TwoPointLineObject) lineCrateList.add(new LineCrate((TwoPointLineObject) o));
        });
        return lineCrateList;
    }

    /**
     * Get the splitting points and filter them
     * @param line
     * @param splittingObjects
     * @return
     */
    private List<PointCrate> getSplittingPoints(TwoPointLineObject line, List<CanvasObject> splittingObjects) {
        //as splitting point is count every active point and intersecting line with different orientation
        List<PointCrate> pointList = new ArrayList<>();

        doGetSplittingPoints(line, splittingObjects, pointList);

        if (isHorizontal(line)) {
            pointList.removeIf(n -> n.x == line.getX2() || n.x == line.getX1());
        } else {
            pointList.removeIf(n -> n.y == line.getY2() || n.y == line.getY1());
        }
        return pointList;
    }

    /**
     * Do get splitting points
     * @param line
     * @param splittingObjects
     * @param pointList
     */
    private void doGetSplittingPoints(TwoPointLineObject line, List<CanvasObject> splittingObjects, List<PointCrate> pointList) {
        for (CanvasObject o : splittingObjects) {
            if (o instanceof ActivePoint) {
                PointCrate crate = new PointCrate(o.getGridX(), o.getGridY());
                if (!pointList.contains(crate)) pointList.add(crate);
            } else if (o instanceof TwoPointLineObject) {
                splitOnLineIntersection(line, pointList, (TwoPointLineObject) o);
            }
        }
    }

    /**
     * get splitting point on the intersection with another line
     * @param line
     * @param pointList
     * @param o
     */
    private void splitOnLineIntersection(TwoPointLineObject line, List<PointCrate> pointList, TwoPointLineObject o) {
        if (isHorizontal(o) != isHorizontal(line)) {
            TwoPointLineObject line2 = o;
            if (isHorizontal(line)) {
                PointCrate crate = new PointCrate(line2.getX2(), line.getY1());
                if (!pointList.contains(crate)) pointList.add(crate);
            } else {
                PointCrate crate = new PointCrate(line.getX2(), line2.getY1());
                if (!pointList.contains(crate)) pointList.add(crate);
            }
        }
    }

    /**
     * Split line on the given points
     * @param lineObject
     * @param points
     * @param gridModel
     */
    private void splitLine(TwoPointLineObject lineObject, List<PointCrate> points, GridModel gridModel) {
        points.add(new PointCrate(lineObject.getX1(), lineObject.getY1()));
        points.add(new PointCrate(lineObject.getX2(), lineObject.getY2()));

        sortPoints(lineObject, points);

        replaceLineWithSplits(lineObject, points, gridModel);
    }

    /**
     * Replace given line with given splits
     * @param lineObject
     * @param points
     * @param gridModel
     */
    private void replaceLineWithSplits(TwoPointLineObject lineObject, List<PointCrate> points, GridModel gridModel) {
        lineObject.callForDelete();

        for (int i = 0; i < points.size() - 1; i++) {
            PointCrate point1 = points.get(i);
            PointCrate point2 = points.get(i + 1);
            callForAdd(gridModel, point1, point2);
        }
    }

    /**
     * Sort points based on the line orientation
     * @param lineObject
     * @param points
     */
    private void sortPoints(TwoPointLineObject lineObject, List<PointCrate> points) {
        if (isHorizontal(lineObject)) {
            points.sort(new SortByX());
        } else {
            points.sort(new SortByY());
        }
    }

    /**
     * Init line and call for add
     * @param gridModel
     * @param point1
     * @param point2
     */
    private void callForAdd(GridModel gridModel, PointCrate point1, PointCrate point2) {
        TwoPointLineObject lineObject1 = new TwoPointLineObject(point1.x, point1.y, point2.x, point2.y);
        initLineActivePoints(lineObject1);
        EventAggregator eventAggregator = gridModel.getInnerEventAggregator();
        eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, lineObject1));
    }

    /**
     * Initialize line active points
     * @param line
     */
    private void initLineActivePoints(TwoPointLineObject line) {
        ActivePoint activePoint = new ActivePoint();
        activePoint.set(line.getX1(), line.getY1(), 1, 1);
        line.addChildren(activePoint);
        activePoint = new ActivePoint();
        activePoint.set(line.getX2(), line.getY2(), 1, 1);
        line.addChildren(activePoint);
    }


    private boolean isHorizontal(TwoPointLineObject lineObject) {
        return lineObject.getY1() == lineObject.getY2();
    }

    /****** POINT SORTING ******/

    class SortByX implements Comparator<PointCrate> {
        public int compare(PointCrate a, PointCrate b) {
            return a.x - b.x;
        }
    }

    class SortByY implements Comparator<PointCrate> {
        public int compare(PointCrate a, PointCrate b) {
            return a.y - b.y;
        }
    }


    /***** HELPER CLASSES *****/
    class PointCrate {
        int x;
        int y;

        public PointCrate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PointCrate that = (PointCrate) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    class LineCrate {
        PointCrate point1;
        PointCrate point2;
        TwoPointLineObject object;

        public LineCrate(TwoPointLineObject object) {
            this.object = object;
            point1 = new PointCrate(object.getLoverX(), object.getLoverY());
            point2 = new PointCrate(object.getHigherX(), object.getHigherY());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LineCrate lineCrate = (LineCrate) o;
            return Objects.equals(point1, lineCrate.point1) && Objects.equals(point2, lineCrate.point2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(point1, point2);
        }
    }
}
