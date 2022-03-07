package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.components.Component;

import cz.thewhiterabbit.electronicapp.model.components.GeneralComponent;
import cz.thewhiterabbit.electronicapp.model.property.*;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import javafx.beans.property.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


@ComponentType
public class GeneralCanvasObject extends GeneralComponent {
    private final Component component = Component.TEST_COMPONENT;

    private final String TEXT_PROPERTY = "text_property";
    private final String LONG_TEXT_PROPERTY = "long_text_property";
    private final String INTEGER_PROPERTY = "integer_property";
    private final String DOUBLE_PROPERTY = "double_property";
    private final String FLOAT_PROPERTY = "float_property";
    private final String CHECK_BOX_PROPERTY = "check_box_property";
    private final String COMBO_BOX_PROPERTY = "combo_box_property";

    @RawPropertyMapping
    @PropertyDialogField(name = "Object ID", type = ComponentPropertyType.TEXT_AREA)
    private final StringProperty objectID = new SimpleStringProperty(this, "object_id", this.toString());

    @RawPropertyMapping
    @PropertyDialogField(name = "Text property", type = ComponentPropertyType.TEXT_FIELD)
    private final StringProperty textTest = new SimpleStringProperty(this, TEXT_PROPERTY, "Test of the text property");

    @RawPropertyMapping
    @PropertyDialogField(name = "Long text property", type = ComponentPropertyType.TEXT_AREA)
    private final StringProperty longTextTest = new SimpleStringProperty(this, LONG_TEXT_PROPERTY, "This is the example of the long style text property\n There should be multiple lines of text");

    @RawPropertyMapping
    @PropertyDialogField(name = "Integer number property", type = ComponentPropertyType.TEXT_FIELD)
    private final IntegerProperty integerNumberProperty = new SimpleIntegerProperty(this,INTEGER_PROPERTY, 10);

    @RawPropertyMapping
    @PropertyDialogField(name = "Double number property", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty doubleNumberProperty = new SimpleDoubleProperty(this, DOUBLE_PROPERTY, 10.21);

    @RawPropertyMapping
    @PropertyDialogField(name = "Float number property", type = ComponentPropertyType.TEXT_FIELD)
    private final FloatProperty floatNumberProperty = new SimpleFloatProperty(this, FLOAT_PROPERTY, 10.21f);

    @RawPropertyMapping
    @PropertyDialogField(name = "Check box property", type = ComponentPropertyType.CHECK_BOX)
    private final BooleanProperty checkBoxProperty = new SimpleBooleanProperty(this, CHECK_BOX_PROPERTY, true);

    @RawPropertyMapping
    @PropertyDialogField(name = "Combo box property", type = ComponentPropertyType.COMBO_BOX, values = {"value1", "value2", "value3", "value4"})
    private final StringProperty comboBoxProperty = new SimpleStringProperty(this, COMBO_BOX_PROPERTY, "value1");

    @PropertyDialogField(name = "locationX", type = ComponentPropertyType.LABEL)
    private final DoubleProperty _locationX = locationXProperty();

    @PropertyDialogField(name = "locationY", type = ComponentPropertyType.LABEL)
    private final DoubleProperty _locationY = locationYProperty();


    public GeneralCanvasObject() {
        super();
        setGridHeight(2);
        setGridWidth(2);
        addActivePoint(1,0);
        //addActivePoint(1,2);

    }


    @Override
    public void doPaint(GraphicsContext gc) {
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0, 0, getHeight(), getHeight());
        gc.setFill(getColor());
        gc.fillRect(0, 0, getWidth() / 2, getHeight() / 2);
        gc.fillRect(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2);
        gc.restore();

    }

    private Paint getColor() {
        if (isHovered()) {
            return Color.RED;
        } else if (isSelected()) {
            return Color.BLUEVIOLET;
        } else {
            return Color.GREENYELLOW;
        }
    }

    @Override
    public Component getComponent() {
        return component;
    }
}
