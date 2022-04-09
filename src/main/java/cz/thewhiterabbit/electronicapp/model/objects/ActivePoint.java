package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.components.Component;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import cz.thewhiterabbit.electronicapp.utilities.LineDrawingUtilities;
import cz.thewhiterabbit.electronicapp.view.canvas.model.CanvasModel;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;

import cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent;
import javafx.beans.property.*;
import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

@ComponentType
public class ActivePoint extends GeneralMappingComponent {
    private final LineDrawingUtilities lineDrawingUtilities = new LineDrawingUtilities(); //TODO to many instances, make static utility class
    private final Component component = Component.ACTIVE_POINT;

    private int relativeLocationX = 0;
    private int relativeLocationY = 0;

    private String name;

    private final String VOLTAGE_PROBE = "voltage_probe";
    private final String VOLTAGE_PROBE_NAME = "voltage_probe_name";

    @PropertyDialogField(name = "locationX", type = ComponentPropertyType.LABEL)
    private final DoubleProperty _locationX = locationXProperty();

    @PropertyDialogField(name = "locationY", type = ComponentPropertyType.LABEL)
    private final DoubleProperty _locationY = locationYProperty();

    @PropertyDialogField(name = "_gridX", type = ComponentPropertyType.LABEL)
    private final IntegerProperty _gridX = gridXProperty();

    @PropertyDialogField(name = "_gridY", type = ComponentPropertyType.LABEL)
    private final IntegerProperty _gridY = gridYProperty();

    @RawPropertyMapping
    @PropertyDialogField(name = "Set voltage probe", type = ComponentPropertyType.CHECK_BOX, unit = "Check to activate probe")
    private final BooleanProperty _probeActive = new SimpleBooleanProperty(this, VOLTAGE_PROBE, false);

    @RawPropertyMapping
    @PropertyDialogField(name = "Voltage probe name", type = ComponentPropertyType.TEXT_FIELD)
    private final StringProperty _probeName = new SimpleStringProperty(this, VOLTAGE_PROBE_NAME, "probe");


    public ActivePoint() {
        initPoint();
    }

    public ActivePoint(String name) {
        this.name = name;
        initPoint();
    }


    public ActivePoint(int relativeLocationX, int relativeLocationY) {
        this.relativeLocationX = relativeLocationX;
        this.relativeLocationY = relativeLocationY;
        initPoint();
        //testing
       /* gridXProperty().addListener(l->{
            if(getParentModel() != null){
                getParentModel().updatePaintProperties(this);
            }
        });
        gridYProperty().addListener(l->{
            if(getParentModel() != null){
                getParentModel().updatePaintProperties(this);
            }
        });*/
    }

    private void initPoint() {
        setGridHeight(1);
        setGridWidth(1);
        setPriority(CanvasModel.Priority.ALWAYS_ON_TOP);
        setRotationStrategy(RotationStrategy.MOVE_WITH_PARENT_ROTATION);
        gridXProperty().addListener(l->{
            if(getParentModel() != null){
                getParentModel().updatePaintProperties(this);
            }
        });
        gridYProperty().addListener(l->{
            if(getParentModel() != null){
                getParentModel().updatePaintProperties(this);
            }
        });
    }

    /***** OVERRIDES *****/
    @Override
    public void doPaint(GraphicsContext gc) {
        double height = getHeight() * 0.125;
        double locationX = -height / 2;
        double locationY = -height / 2;

        if (isHovered()) {
            gc.setFill(Color.GREENYELLOW);
        } else {
            gc.setFill(Color.BLACK);
        }

        gc.fillOval(locationX, locationY, height, height);
    }

    @Override
    public boolean isInBounds(double x, double y) {
        double height = getHeight() * 0.5;
        double locationX = getLocationX() - height / 2;
        double locationY = getLocationY() - height / 2;
        return ((x >= locationX && x <= locationX + height) && (y >= locationY && y <= locationY + height));
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    protected void onObjectDropped(Event e) {
        e.consume();
        lineDrawingUtilities.onActivePointDropped(getParentModel(), getParent(), getEventAggregator());
    }

    @Override
    protected void onObjectDragged(Event e) {
        e.consume();
        if (getParentModel() != null && getParentModel() instanceof GridModel) {
            lineDrawingUtilities.onActivePointDragged((GridModel)getParentModel(), getParent(),this, (CanvasMouseEvent) e);
        }
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

    @Override
    public void clean(GraphicsContext gc) {}

    @Override
    public Component getComponent() {
        return component;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean get_probeActive() {
        return _probeActive.get();
    }

    public BooleanProperty _probeActiveProperty() {
        return _probeActive;
    }

    public String get_probeName() {
        return _probeName.get();
    }

    public StringProperty _probeNameProperty() {
        return _probeName;
    }
}
