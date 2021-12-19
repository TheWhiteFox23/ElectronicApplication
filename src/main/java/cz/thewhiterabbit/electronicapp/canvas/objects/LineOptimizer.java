package cz.thewhiterabbit.electronicapp.canvas.objects;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.canvas.model.GridModel;

import java.util.*;

public class LineOptimizer {
    public void optimize(GridModel gridModel){
        //get All lines
        List<TwoPointLineObject> lines = new ArrayList<>();
        if(gridModel != null){
            gridModel.getAll().forEach(o -> {
                if(o instanceof TwoPointLineObject)lines.add((TwoPointLineObject)o);
            });
        }

        //find intersecting lines and split
        lines.forEach(l -> {
            List<CanvasObject> object = gridModel.getInBounds(l.getLocationX(), l.getLocationY(),
                    Math.max(1,l.getWidth()),Math.max(1,l.getHeight()));

            //Remove given object and its children
            if(object.contains(l))object.remove(l);
            l.getChildrenList().forEach(o -> {
                if(object.contains(o))object.remove(o);
            });

            //Calculate splitting
            List<PointCrate> splittingPoints = getSplittingPoints(l, object);
            if(splittingPoints.size()!= 0){
                splitLine(l, splittingPoints, gridModel);
            }
        });

        //remove duplicate lines
        List<LineCrate> lineCrateList = new ArrayList<>();
        if(gridModel != null){
            gridModel.getAll().forEach(o -> {
                if(o instanceof TwoPointLineObject) lineCrateList.add(new LineCrate((TwoPointLineObject) o));
            });
        }
        removeDuplicities(lineCrateList);

        mergeContinuous(gridModel);

    }

    private void mergeContinuous(GridModel gridModel) {
        //get lines in one level
        List<LineCrate> horizontal = new ArrayList<>();
        List<LineCrate> vertical = new ArrayList<>();
        //populate list
        if(gridModel != null){
            gridModel.getAll().forEach(o ->{
                if(o instanceof TwoPointLineObject){
                    if(isHorizontal((TwoPointLineObject)o)){
                        horizontal.add(new LineCrate((TwoPointLineObject) o));
                    }else{
                        vertical.add(new LineCrate((TwoPointLineObject) o));
                    }
                }
            });
        }
        //sort lists
        horizontal.sort(new Comparator<LineCrate>() {
            @Override
            public int compare(LineCrate o1, LineCrate o2) {
                int result =  o1.point1.y - o2.point1.y;
                if(result == 0) return o1.object.getLoverX() - o2.object.getLoverX();
                return result;
            }
        });
        merge(horizontal, gridModel);
        vertical.sort(new Comparator<LineCrate>() {
            @Override
            public int compare(LineCrate o1, LineCrate o2) {
                int result =  o1.point1.x - o2.point1.x;
                if(result == 0) return o1.object.getLoverY() - o2.object.getLoverY();
                return result;
            }
        });
        merge(vertical, gridModel);
        //get continuous lines

        //merge
    }

    private void merge(List<LineCrate> lines, GridModel gridModel){
        List<LineCrate> toMerge = new ArrayList<>();
        for(int i = 0; i< lines.size(); i++){
            toMerge.add(lines.get(i));
            if(i+1<lines.size()){
                if(!canBeMerged(lines.get(i), lines.get(i+1), gridModel)){
                    doMerge(toMerge, gridModel);
                    toMerge.clear();
                }
            }
        }
        doMerge(toMerge, gridModel);
    }

    private void doMerge(List<LineCrate> toMerge, GridModel gridModel) {
        if (gridModel == null || toMerge.size() == 1) return;
        System.out.println("merge: " + toMerge.size());

        PointCrate point1 = toMerge.get(0).point1;
        PointCrate point2 = toMerge.get(toMerge.size()-1).point2;
        toMerge.forEach(o -> o.object.callForDelete());
        TwoPointLineObject lineObject = new TwoPointLineObject(point1.x, point1.y, point2.x, point2.y);
        initLineActivePoints(lineObject);
        gridModel.getInnerEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, lineObject));
    }

    private boolean canBeMerged(LineCrate line1, LineCrate line2, GridModel gridModel) {
        if(!line1.point2.equals(line2.point1))return false;
        ActivePoint ac = getLowerActionPint(line1);
        if(gridModel != null && ac != null) {
            List<CanvasObject> objects = gridModel.getInBounds(
                    ac.getLocationX(), ac.getLocationY(), ac.getWidth(), ac.getHeight());
            objects.removeIf(e-> !(e instanceof ActivePoint) || e == ac);
            if(objects.size() != 1)return false;
        }
        return true;
    }

    private ActivePoint getLowerActionPint(LineCrate line1) {
        for (CanvasObject o : line1.object.getChildrenList()) {
            if (o instanceof ActivePoint && o.getGridY() == line1.point2.y && o.getGridX() == line1.point2.x) {
                return (ActivePoint) o;
            }
        }
        return null;
    }


    private void removeDuplicities(List<LineCrate> lineCrateList) {
        HashMap<LineCrate, TwoPointLineObject> lineMap = new HashMap<>();
        List<LineCrate> toDelete = new ArrayList<>();
        lineCrateList.forEach(l ->{
            if(!lineMap.containsKey(l)){
                lineMap.put(l, l.object);
            }else{
                toDelete.add(l);
            }
        });
        toDelete.forEach(l -> {
            l.object.callForDelete();
        });
    }

    private List<PointCrate> getSplittingPoints(TwoPointLineObject line, List<CanvasObject> splittingObjects) {
        //as splitting point is count every active point and intersecting line with different orientation
        List<PointCrate> pointList = new ArrayList<>();
        for(CanvasObject o : splittingObjects){
            if(o instanceof ActivePoint){
                PointCrate crate = new PointCrate(o.getGridX(), o.getGridY());
                if(!pointList.contains(crate))pointList.add(crate);
            }else if(o instanceof TwoPointLineObject){
                if(isHorizontal((TwoPointLineObject) o) != isHorizontal(line)){
                    TwoPointLineObject line2 = (TwoPointLineObject) o;
                    if(isHorizontal(line)){
                        PointCrate crate = new PointCrate(line2.getX2(), line.getY1());
                        if(!pointList.contains(crate))pointList.add(crate);
                    }else{
                        PointCrate crate = new PointCrate(line.getX2(), line2.getY1());
                        if(!pointList.contains(crate))pointList.add(crate);
                    }
                }
            }
        }
        if(isHorizontal(line)){
            pointList.removeIf(n -> n.x == line.getX2() || n.x == line.getX1());
        }else{
            pointList.removeIf(n -> n.y == line.getY2() || n.y == line.getY1());
        }
        return pointList;
    }

    private void splitLine(TwoPointLineObject lineObject, List<PointCrate> points, GridModel gridModel){
        points.add(new PointCrate( lineObject.getX1(), lineObject.getY1()));
        points.add(new PointCrate(lineObject.getX2(), lineObject.getY2()));
        if(isHorizontal(lineObject)){
            points.sort(new SortByX());
        }else{
            points.sort(new SortByY());
        }
        lineObject.callForDelete();
        for(int i = 0; i< points.size()-1; i++){
            PointCrate point1 = points.get(i);
            PointCrate point2 = points.get(i+1);
            TwoPointLineObject lineObject1 = new TwoPointLineObject(point1.x, point1.y, point2.x, point2.y);
            initLineActivePoints(lineObject1);
            EventAggregator eventAggregator = gridModel.getInnerEventAggregator();
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, lineObject1));
        }

    }

    private void initLineActivePoints(TwoPointLineObject line) {
        ActivePoint activePoint = new ActivePoint();
        activePoint.set(line.getX1(), line.getY1(), 1, 1);
        line.addChildren(activePoint);
        activePoint = new ActivePoint();
        activePoint.set(line.getX2(), line.getY2(), 1, 1);
        line.addChildren(activePoint);
    }


    private boolean isHorizontal(TwoPointLineObject lineObject){
        return lineObject.getY1() == lineObject.getY2();
    }
    /****** POINT SORTING ******/

    class SortByX implements Comparator<PointCrate> {
        public int compare(PointCrate a, PointCrate b)
        {
            return a.x - b.x;
        }
    }

    class SortByY implements Comparator<PointCrate> {
        public int compare(PointCrate a, PointCrate b)
        {
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

    class LineCrate{
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
