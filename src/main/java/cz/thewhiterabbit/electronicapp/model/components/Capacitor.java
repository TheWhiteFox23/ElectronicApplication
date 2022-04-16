package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Locale;
import java.util.ResourceBundle;


@ComponentType
public class Capacitor extends GeneralComponent {

    private final String CAPACITANCE = "capacitance";
    private final String INITIAL_CONDITION = "initial_condition";

    private ActivePoint activePointIn;
    private ActivePoint activePointOut;

    @RawPropertyMapping
    @PropertyDialogField(name = "capacitor.capacitance", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty capacitance = new SimpleDoubleProperty(this, CAPACITANCE, 1);

    @RawPropertyMapping
    @PropertyDialogField(name = "capacitor.initial_condition", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty initial_condition = new SimpleDoubleProperty(this, INITIAL_CONDITION, 0);

    private final String path ="M46.5,35V65h-3V51.5H0v-3H43.5V35Zm10,13.5V35h-3V65h3V51.5H100v-3ZM61.44,38a1.5,1.5,0,1," +
            "0-1.5-1.5A1.5,1.5,0,0,0,61.44,38Z";


    public Capacitor(){
        super();
        setComponent(Component.CAPACITOR);
        getPathList().add(path);
        activePointIn = new ActivePoint();
        activePointOut = new ActivePoint();
        addActivePoint(activePointIn, 0,1);
        addActivePoint(activePointOut,2,1);
    }

    @Override
    public String getSimulationComponent() {
        return getComponentName()+" " + getNode(activePointIn).getName() + " "
                + getNode(activePointOut).getName() + " " +capacitance.get() + " ic=" +
                initial_condition.get();
    }

    @Override
    public String getComponentName() {
        return "C" + getName();
    }


}
