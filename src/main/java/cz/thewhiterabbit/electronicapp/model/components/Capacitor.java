package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


@ComponentType
public class Capacitor extends GeneralComponent {

    private final String CAPACITANCE = "1";

    @RawPropertyMapping
    @PropertyDialogField(name = "Capacitance", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty capacitance = new SimpleDoubleProperty(this, CAPACITANCE, 1);

    private final String path ="M46.5,35V65h-3V51.5H0v-3H43.5V35Zm10,13.5V35h-3V65h3V51.5H100v-3ZM61.44,38a1.5,1.5,0,1," +
            "0-1.5-1.5A1.5,1.5,0,0,0,61.44,38Z";


    public Capacitor(){
        super();
        setComponent(Component.CAPACITOR);
        setPath(path);
    }

    @Override
    public String getSimulationComponent() {
        return null;
    }


}
