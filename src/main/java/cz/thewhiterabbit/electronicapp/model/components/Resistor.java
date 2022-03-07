package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

@ComponentType
public class Resistor extends GeneralComponent {
    private final String RESISTANCE = "1";

    @RawPropertyMapping
    @PropertyDialogField(name = "Resistance", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty resistance = new SimpleDoubleProperty(this, RESISTANCE, 1);

    private final String path ="M66.67,65.57l-11.11-24-11.12,24-11.11-24-11.11,24L15.71,51.5H0v-3H17.63l4.59,9.93," +
            "11.11-24,11.11,24,11.12-24,11.11,24,11.11-24L84.29,48.5H100v3H82.37l-4.59-9.93Z";

    public Resistor(){
        super();
        setComponent(Component.RESISTOR);
        setPath(path);
        addActivePoint(0,1);
        addActivePoint(2,1);
    }


}

