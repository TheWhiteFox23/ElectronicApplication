package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.objects.GeneralMappingComponent;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

@ComponentType
public class CoupledInductor extends GeneralComponent {
    private final Component component = Component.COUPLED_INDUCTOR;

    private ActivePoint l1activePointIn;
    private ActivePoint l1activePointOut;

    private ActivePoint l2activePointIn;
    private ActivePoint l2activePointOut;

    private final String INDUCTANCE_L1 = "inductance_l1";
    private final String INITIAL_CONDITION_L1 = "initial_condition_l1";

    private final String INDUCTANCE_L2 = "inductance_l2";
    private final String INITIAL_CONDITION_L2 = "initial_condition_l2";

    private final String COUPLING_COEFFICIENT = "coupling_coefficient";


    @RawPropertyMapping
    @PropertyDialogField(name = "Inductance L1", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty inductance_l1 = new SimpleDoubleProperty(this, INDUCTANCE_L1, 1);

    @RawPropertyMapping
    @PropertyDialogField(name = "Initial condition L1", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty initial_condition_l1 = new SimpleDoubleProperty(this, INITIAL_CONDITION_L1, 0);

    @RawPropertyMapping
    @PropertyDialogField(name = "Inductance L2", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty inductance_l2 = new SimpleDoubleProperty(this, INDUCTANCE_L2, 1);

    @RawPropertyMapping
    @PropertyDialogField(name = "Initial condition L2", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty initial_condition_l2 = new SimpleDoubleProperty(this, INITIAL_CONDITION_L2, 0);

    @RawPropertyMapping
    @PropertyDialogField(name = "Coupling coefficient (0< K >=1)", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty coupling_coefficient = new SimpleDoubleProperty(this, COUPLING_COEFFICIENT, 1);



    private final String path = "M16.5,25.63c0-5.31-4-9.63-9-9.63H3V0H0V19H7.5c3.31,0,6,3,6,6.63s-2.69,6.62-6," +
            "6.62H0v3H7.5c3.31,0,6,3,6,6.63s-2.69,6.62-6,6.62H0v3H7.5c3.31,0,6,3,6,6.63s-2.69,6.62-6,6.62H0v3H7.5c3.31," +
            "0,6,3,6,6.63S10.81,81,7.5,81H0v19H3V84H7.5c5,0,9-4.32,9-9.62a9.79,9.79,0,0,0-4.2-8.13A10,10,0,0,0,12.3," +
            "50a10,10,0,0,0,0-16.25A9.79,9.79,0,0,0,16.5,25.63ZM15,84a1.5,1.5,0,1,0,1.5,1.5A1.5,1.5,0,0,0,15,84ZM100," +
            "0V16H95.5c-5,0-9,4.32-9,9.63a9.79,9.79,0,0,0,4.2,8.12A10,10,0,0,0,90.7,50a10,10,0,0,0,0,16.25,9.79,9.79,0," +
            "0,0-4.2,8.13c0,5.3,4,9.62,9,9.62H100v16h3V81H95.5c-3.31,0-6-3-6-6.62s2.69-6.63,6-6.63H103v-3H95.5c-3.31," +
            "0-6-3-6-6.62s2.69-6.63,6-6.63H103v-3H95.5c-3.31,0-6-3-6-6.62s2.69-6.63,6-6.63H103v-3H95.5c-3.31," +
            "0-6-3-6-6.62S92.19,19,95.5,19H103V0ZM88,84a1.5,1.5,0,1,0,1.5,1.5A1.5,1.5,0,0,0,88,84ZM51.5,5.75a24,24,0,0," +
            "0-23.66,20H24.38l4.62,8,4.62-8H30.89a21,21,0,0,1,41-1H69.38l4.62,8,4.62-8H75A24,24,0,0,0,51.5,5.75ZM24," +
            "54l1.16,1.47,1.63-1.32.64-.55a4.5,4.5,0,0,0,.49-.48c0,.26,0,.54,0,.83s0,.6,0,.9,0,.61,0," +
            ".92V65h2.31V50.72H28.23ZM71.2,62.88l2.47-2.4c.78-.74,1.45-1.43,2-2.07A8.24,8.24,0,0,0,77,56.48a5,5,0,0,0," +
            ".46-2.13,3.67,3.67,0,0,0-.54-2A3.55,3.55,0,0,0,75.41,51a5.33,5.33,0,0,0-2.35-.48,6.62,6.62,0,0,0-1.92.25," +
            "6.38,6.38,0,0,0-1.52.68,9.63,9.63,0,0,0-1.26.91l1.26,1.53a8,8,0,0,1,1.62-1A4,4,0,0,1,73,52.48a2.29,2.29," +
            "0,0,1,1.57.53,2,2,0,0,1,.59,1.56,3.49,3.49,0,0,1-.34,1.55,6.88,6.88,0,0,1-1,1.51c-.47.53-1,1.16-1.77," +
            "1.89l-3.7,3.75V65H78V63H71.2Z";

    public CoupledInductor(){
        super();
        setComponent(Component.COUPLED_INDUCTOR);
        getPathList().add(path);
        setTranslateX(-1.5);
        l1activePointIn = new ActivePoint();
        l1activePointOut = new ActivePoint();
        l2activePointIn = new ActivePoint();
        l2activePointOut = new ActivePoint();
        addActivePoint(l1activePointIn, 0,0);
        addActivePoint(l1activePointOut,0,2);
        addActivePoint(l2activePointIn, 2,0);
        addActivePoint(l2activePointOut,2,2);
    }

    @Override
    public String getSimulationComponent() {
        String command = "";
        // L1
        command += "LA" + getName()+" " + getNode(l1activePointIn).getName() + " "
                + getNode(l1activePointOut).getName() + " " +inductance_l1.get() + " ic=" +
                initial_condition_l1.get() + "\n";
        //L2
        command += "LB" + getName()+" " + getNode(l2activePointIn).getName() + " "
                + getNode(l2activePointOut).getName() + " " +inductance_l2.get() + " ic=" +
                initial_condition_l2.get() + "\n";
        //Coupled
        command += getComponentName() + " LA" + getName() + " LB" + getName() +" " +coupling_coefficient.get();

        return command;
    }

    @Override
    public String getComponentName() {
        return "K" + getName();
    }

}
