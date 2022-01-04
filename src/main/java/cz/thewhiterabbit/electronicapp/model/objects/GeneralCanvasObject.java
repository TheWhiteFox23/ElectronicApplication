package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.components.Component;

import cz.thewhiterabbit.electronicapp.model.components.ComponentType;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.controllers.PropertiesPaneController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

@PropertiesPaneController.Component
@ComponentType
public class GeneralCanvasObject extends GeneralComponent {
    private final Component component = Component.TEST_COMPONENT;

    @PropertiesPaneController.Property(propertyType = PropertiesPaneController.PropertyType.INTEGER)
    private final IntegerProperty _gridX = gridXProperty();
    @PropertiesPaneController.Property(propertyType = PropertiesPaneController.PropertyType.INTEGER)
    private final IntegerProperty _gridY = gridYProperty();
    @PropertiesPaneController.Property(propertyType = PropertiesPaneController.PropertyType.INTEGER, editable = false)
    private final IntegerProperty _gridHeight = gridHeightProperty();
    @PropertiesPaneController.Property(propertyType = PropertiesPaneController.PropertyType.INTEGER, editable = false)
    private final IntegerProperty _gridWidth = gridWidthProperty();

    public GeneralCanvasObject(){
        setGridHeight(2);
        setGridWidth(2);
        initChildren();
    }


    @Override
    public void doPaint(GraphicsContext gc) {
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0, 0, getHeight(), getHeight());
        gc.setFill(getColor());
        gc.fillRect(0, 0, getWidth()/2, getHeight()/2);
        gc.fillRect(getWidth()/2, getHeight()/2, getWidth()/2, getHeight()/2);
        gc.restore();

    }

    private Paint getColor(){
        if(isHovered()){
            return Color.RED;
        }else if (isSelected()){
            return Color.BLUEVIOLET;
        }else{
            return Color.GREENYELLOW;
        }
    }

    @Override
    public Component getComponent() {
        return component;
    }

    private void initChildren(){
        ActivePoint activePoint = new ActivePoint();
        ActivePoint activePoint2= new ActivePoint();
        activePoint2.setGridX(getGridX()+1);
        activePoint2.setGridY(getGridY()+2);
        activePoint.setGridX(getGridX()+1);
        activePoint.setGridY(getGridY());
        addChildren(activePoint);
        addChildren(activePoint2);
    }

    @Override
    public void addChildren(CanvasObject children) {
        super.addChildren(children);
        gridXProperty().addListener((obs, oldVal, newVal) -> {
            if(getParentModel() == null){
                int delta = (int)newVal - (int)oldVal;
                children.setGridX(children.getGridX() + delta);
            }
        });
        gridYProperty().addListener((obs, oldVal, newVal) -> {
            if(getParentModel() == null){
                int delta = (int)newVal - (int)oldVal;
                children.setGridY(children.getGridY() + delta);
            }
        });
    }
}
