package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import cz.thewhiterabbit.electronicapp.model.similation.NetlistNode;
import cz.thewhiterabbit.electronicapp.model.similation.SimulationComponent;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


@ComponentType
public class VoltageSource extends GeneralComponent {
    private final String VOLTAGE = "1";

    private ActivePoint activePointIn;
    private ActivePoint activePointOut;


    @RawPropertyMapping
    @PropertyDialogField(name = "Voltage", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty voltage = new SimpleDoubleProperty(this, VOLTAGE, 1);

    private final String path ="M84,48.5V47.44l-.82-5.28-1.63-5.1-2.42-4.77L76,28l-3.77-3.8L67.92,21l-4.76-2.45-5.09-" +
            "1.67L52.79,16l-5.35,0-5.29.82-5.09,1.63-4.77,2.42L28,24l-3.8,3.77L21,32.08l-2.45,4.76-1.67,5.09L16,47.21V48" +
            ".5H0v3H16v1.06l.82,5.28,1.63,5.1,2.42,4.77L24,72.05l3.77,3.8L32.08,79l4.76,2.45,5.09,1.67,5.28.85,5.47,0," +
            "5.16-.82,5.1-1.63,4.77-2.42L72.05,76l3.8-3.77L79,67.92l2.45-4.76,1.67-5.09L84,52.79V51.5h16v-3Zm-65-.94.76" +
            "-4.82,1.51-4.64,2.22-4.35,2.86-3.95,3.45-3.45,3.95-2.86,4.35-2.22,4.64-1.51L47.56,19h4.88l4.82.76,4.64," +
            "1.51,4.35,2.22,3.94,2.86,3.46,3.45,2.86,3.95,2.22,4.35,1.51,4.64L81,47.56v.94H26.83L46.5," +
            "36.27l-1.58-2.54L21.15,48.5,19,49.83V47.56Zm62,4.88-.76,4.82L78.73,61.9l-2.22,4.35-2.86,3.94-3.46," +
            "3.46-3.94,2.86L61.9,78.73l-4.64,1.51L52.44,81H47.56l-4.82-.76L38.1,78.73l-4.35-2.22L29.8," +
            "73.65l-3.45-3.46-2.86-3.94L21.27,61.9l-1.51-4.64L19,52.44V50.17l2.14," +
            "1.33L44.92,66.27l1.58-2.54L26.82,51.5H81Z";


    public VoltageSource(){
        super();
        setComponent(Component.VOLTAGE_SOURCE);
        setPath(path);

        activePointIn = new ActivePoint();
        activePointOut = new ActivePoint();
        addActivePoint(activePointIn, 0,1);
        addActivePoint(activePointOut,2,1);
    }

    @Override
    public String getSimulationComponent() {
        //TODO implement
        return null;
    }
}
