package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.similation.NetlistNode;
import cz.thewhiterabbit.electronicapp.model.similation.SimulationComponent;
import cz.thewhiterabbit.electronicapp.utilities.LineCrate;
import cz.thewhiterabbit.electronicapp.utilities.LineUtilities;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

@ComponentType
public class TwoPointLineObject extends DocumentObject implements SimulationComponent {
    private final LineUtilities lineUtilities = new LineUtilities();

    private final String _X1 = "X1";
    private final String _X2 = "X2";
    private final String _Y1 = "Y1";
    private final String _Y2 = "Y2";
    private final String _TYPE = "LINE";

    private boolean blockUpdate = false;

    private NetlistNode in;
    private NetlistNode out;
    private ActivePoint activePointIn;
    private ActivePoint activePointOut;

    private String name;




    @RawPropertyMapping
    @PropertyDialogField(name = "x1", type = ComponentPropertyType.LABEL)
    private final IntegerProperty x1 = new SimpleIntegerProperty(this, _X1);
    @RawPropertyMapping
    @PropertyDialogField(name = "x2", type = ComponentPropertyType.LABEL)
    private final IntegerProperty x2 = new SimpleIntegerProperty(this, _X2);
    @RawPropertyMapping
    @PropertyDialogField(name = "y1", type = ComponentPropertyType.LABEL)
    private final IntegerProperty y1 = new SimpleIntegerProperty(this, _Y1);
    @RawPropertyMapping
    @PropertyDialogField(name = "y1", type = ComponentPropertyType.LABEL)
    private final IntegerProperty y2 = new SimpleIntegerProperty(this, _Y2);


    @PropertyDialogField(name = "locationX", type = ComponentPropertyType.LABEL)
    private final DoubleProperty _locationX = locationXProperty();

    @PropertyDialogField(name = "locationY", type = ComponentPropertyType.LABEL)
    private final DoubleProperty _locationY = locationYProperty();


    public TwoPointLineObject(int x1, int y1, int x2, int y2) {
        setX1(x1);
        setX2(x2);
        setY1(y1);
        setY2(y2);
        mapLineProperties();
        initListeners();
    }


    public TwoPointLineObject() {
        initListeners();
    }

    private void initListeners() {
        this.locationXProperty().addListener(l->{
            if(!blockUpdate) onGridXChanged();
        });
        this.locationYProperty().addListener(l->{
            if(!blockUpdate) onGridYChanged();
        });
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
                gc.setStroke(Color.BLUEVIOLET);
            } else if (isSelected()) {
                gc.setStroke(Color.GREENYELLOW);
            } else {
                gc.setStroke(Color.BLACK);
            }

            gc.setLineWidth(getLineWidth());
            gc.strokeLine(locationX1, locationY1, locationX2, locationY2);
        }
    }

    @Override
    public void init() {
        setX1(Integer.parseInt(getRawObject().getProperty(_X1).getValue()));
        setY1(Integer.parseInt(getRawObject().getProperty(_Y1).getValue()));
        setX2(Integer.parseInt(getRawObject().getProperty(_X2).getValue()));
        setY2(Integer.parseInt(getRawObject().getProperty(_Y2).getValue()));
        mapProperties();
        mapLineProperties();
    }

    @Override
    public void mapProperties() {
        getRawObject().getProperty(_X1).valueProperty().addListener((obs, oldVal, newVal) -> {
            setX1(Integer.parseInt(newVal));
            //System.out.println("X1 changed");
            forceChangeLineProperties();
        });
        getRawObject().getProperty(_Y1).valueProperty().addListener((obs, oldVal, newVal) -> {
            setY1(Integer.parseInt(newVal));
            forceChangeLineProperties();
        });
        getRawObject().getProperty(_X2).valueProperty().addListener((obs, oldVal, newVal) -> {
            setX2(Integer.parseInt(newVal));
            forceChangeLineProperties();
        });
        getRawObject().getProperty(_Y2).valueProperty().addListener((obs, oldVal, newVal) -> {
            setY2(Integer.parseInt(newVal));
            forceChangeLineProperties();
        });
    }

    @Override
    public RawObject toRawObject() {
        RawObject rawObject = new RawObject(_TYPE);
        rawObject.addProperty(new RawProperty(_X1, String.valueOf(getX1())));
        rawObject.addProperty(new RawProperty(_X2, String.valueOf(getX2())));
        rawObject.addProperty(new RawProperty(_Y1, String.valueOf(getY1())));
        rawObject.addProperty(new RawProperty(_Y2, String.valueOf(getY2())));
        getChildrenList().forEach(l -> {
            rawObject.getChildren().add(((DocumentObject) l).toRawObject());
        });
        return rawObject;
    }

    @Override
    public String getType() {
        return _TYPE;
    }

    @Override
    public void setPosition(int deltaX, int deltaY) {
        getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, this, x1Property(), getX1() - deltaX, getX1()));
        getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, this, x2Property(), getX2() - deltaX, getX2()));
        getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, this, y1Property(), getY1() - deltaY, getY1()));
        getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, this, y2Property(), getY2() - deltaY, getY2()));
        //getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));//TODO: debugging
        mapLineProperties();
    }

    //TODO -> better way to manage this
    public void mapLineProperties() {
        this.setGridWidth(Math.abs(getX2() - getX1()));
        this.setGridHeight(Math.abs(getY1() - getY2()));
        this.setGridX((Math.min(getX1(), getX2())));
        this.setGridY((Math.min(getY1(), getY2())));
        if (getParentModel() != null) {
            this.getParentModel().updatePaintProperties(this);
        }
    }

    private void forceChangeLineProperties(){
        blockUpdate=true;
        mapLineProperties();
        blockUpdate=false;
    }

    //TODO -> better way to manage this
    private void onGridXChanged() {
        if (getParentModel() != null && getParentModel() instanceof GridModel) {
            GridModel m = (GridModel) getParentModel();
            int originalGridX = (Math.min(getX1(), getX2()));
            int deltaX = originalGridX - m.getGridCoordinate(getLocationX(), m.getOriginX());
            setX1(getX1() - deltaX);
            setX2(getX2() - deltaX);
            //System.out.println(getX1() +" : " + getX2());
        }
    }

    //TODO -> better way to manage this
    private void onGridYChanged() {
        if (getParentModel() != null && getParentModel() instanceof GridModel) {
            GridModel m = (GridModel) getParentModel();
            int originalGridY = (Math.min(getY1(), getY2()));
            int deltaY = originalGridY - m.getGridCoordinate(getLocationY(), m.getOriginY());
            setY1(getY1() - deltaY);
            setY2(getY2() - deltaY);
            //System.out.println(getY1() +" : " + getY2());
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

            LineCrate line = new LineCrate(x1, y1, x2, y2);
            boolean upperXIntersection = lineUtilities.isIntersection(line, new LineCrate(locationX, locationY, locationX + width, locationY));
            boolean lowerXIntersection = lineUtilities.isIntersection(line, new LineCrate(locationX, locationY + height, locationX + width, locationY + height));
            boolean leftYIntersection = lineUtilities.isIntersection(line, new LineCrate(locationX, locationY, locationX, locationY + height));
            boolean rightYIntersection = lineUtilities.isIntersection(line, new LineCrate(locationX + width, locationY, locationX + width, locationY + height));

            boolean result = firstPointInBounds || secondPointInBounds || upperXIntersection || lowerXIntersection || leftYIntersection || rightYIntersection;
            return result;
        }
        return false;
    }

    private double getLineWidth() {
        GridModel model = (GridModel) getParentModel();
        return (model.getGridSize() * model.getZoomAspect())*0.05;
        //return 2;
    }

    @Override
    public void clean(GraphicsContext gc) {
        super.clean(gc);
    }

    @Override
    public void setNode(ActivePoint activePoint, NetlistNode node) {
        if(activePointIn == null && getChildrenList().contains(activePoint)){
            activePointIn = activePoint;
        }else if(activePointOut == null && getChildrenList().contains(activePoint)){
            activePointOut = activePoint;
        }
        if(activePoint == activePointIn){
            in = node;
        }else if(activePoint == activePointOut){
            out = node;
        }
    }

    @Override
    public NetlistNode getNode(ActivePoint activePoint) {
        if(activePoint == activePointIn)return in;
        if(activePoint == activePointOut)return out;
        return null;
    }

    @Override
    public String getSimulationComponent() {
        return "R"+getName() +" "+ in.getName() + " " + out.getName() + " 0";
    }

    @Override
    public List<NetlistNode> getNodes() {
        return new ArrayList<>(){{add(in);add(out);}};
    }

    @Override
    public void setNode(NetlistNode oldNode, NetlistNode newNode) {
        if(oldNode == in){
            in=newNode;
        }else if(oldNode == out){
            out = newNode;
        }
    }

    @Override
    public void removeNode(NetlistNode oldNode) {
        if(oldNode == in){
            in=null;
        }else if(oldNode == out){
            out = null;
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Boolean isProbeActive() {
        return false;
    }

    @Override
    public String getProbeName() {
        return null;
    }

    @Override
    public void setProbeName(String name) {

    }

    @Override
    public String getComponentName() {
        return null;
    }

    public ActivePoint getActivePointIn() {
        return activePointIn;
    }

    public void setActivePointIn(ActivePoint activePointIn) {
        this.activePointIn = activePointIn;
    }

    public ActivePoint getActivePointOut() {
        return activePointOut;
    }

    public void setActivePointOut(ActivePoint activePointOut) {
        this.activePointOut = activePointOut;
    }

    public NetlistNode getIn() {
        return in;
    }

    public void setIn(NetlistNode in) {
        this.in = in;
    }

    public NetlistNode getOut() {
        return out;
    }

    public void setOut(NetlistNode out) {
        this.out = out;
    }
}
