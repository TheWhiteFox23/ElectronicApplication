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
import javafx.scene.text.Font;

@ComponentType
public class Inductor extends GeneralComponent {

    private final String INDUCTANCE = "inductance";
    private final String INITIAL_CONDITION = "initial_condition";


    private ActivePoint activePointIn;
    private ActivePoint activePointOut;

    @RawPropertyMapping
    @PropertyDialogField(name = "inductor.inductance", type = ComponentPropertyType.TEXT_FIELD, unit = "H")
    private final DoubleProperty inductance = new SimpleDoubleProperty(this, INDUCTANCE, 1);

    @RawPropertyMapping
    @PropertyDialogField(name = "inductor.initial_condition", type = ComponentPropertyType.TEXT_FIELD, unit = "A")
    private final DoubleProperty initial_condition = new SimpleDoubleProperty(this, INITIAL_CONDITION, 0);

    private final String path ="M100,48.5v3H81V44c0-3.31-3-6-6.62-6s-6.63,2.69-6.63,6v7.5h-3V44c0-3.31-3-6-6.62-6s-6.63," +
            "2.69-6.63,6v7.5h-3V44c0-3.31-3-6-6.62-6s-6.63,2.69-6.63,6v7.5h-3V44c0-3.31-3-6-6.62-6S19,40.69,19," +
            "44v7.5H0v-3H16V44c0-5,4.32-9,9.63-9a9.79,9.79,0,0,1,8.12,4.2A10,10,0,0,1,50,39.2a10,10,0,0,1,16.25,0A9.79," +
            "9.79,0,0,1,74.38,35c5.3,0,9.62,4,9.62,9v4.5ZM85.5,38A1.5,1.5,0,1,0,84,36.5,1.5,1.5,0,0,0,85.5,38Z";

    public Inductor(){
        super();
        setComponent(Component.INDUCTOR);
        getPathList().add(path);
        activePointIn = new ActivePoint();
        activePointOut = new ActivePoint();
        addActivePoint(activePointIn, 0,1);
        addActivePoint(activePointOut,2,1);
    }

    @Override
    protected void doPaint(GraphicsContext gc){
        super.doPaint(gc);
        gc.setFont(new Font(25));
        gc.fillText(String.valueOf(getComponentName() + " " +inductance.get()) + "H", 0,0);
    }

    @Override
    public String getSimulationComponent() {
        return getComponentName()+" " + getNode(activePointIn).getName() + " "
                + getNode(activePointOut).getName() + " " +inductance.get() + " ic=" +
                initial_condition.get();
    }

    @Override
    public String getComponentName() {
        return "L" + getName();
    }


}
