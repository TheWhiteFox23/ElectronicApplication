package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import javafx.beans.property.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

@ComponentType
public class Resistor extends GeneralComponent {
    private final String RESISTANCE = "resistance";

    private ActivePoint activePointIn;
    private ActivePoint activePointOut;

    private final String CURRANT_PROBE = "currant_probe";
    private final String PROBE_NAME = "currant_probe_name";

    @RawPropertyMapping
    @PropertyDialogField(name = "Resistance", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty resistance = new SimpleDoubleProperty(this, RESISTANCE, 1);

    private final String path ="M66.67,65.57l-11.11-24-11.12,24-11.11-24-11.11,24L15.71,51.5H0v-3H17.63l4.59,9.93," +
            "11.11-24,11.11,24,11.12-24,11.11,24,11.11-24L84.29,48.5H100v3H82.37l-4.59-9.93Z";

    public Resistor(){
        super();
        setComponent(Component.RESISTOR);
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
        gc.fillText(String.valueOf(getComponentName() + " " +resistance.get()) + "Ω", 0,0);
    }

    @Override
    public String getSimulationComponent() {
        return getComponentName()+" " + getNode(activePointIn).getName() + " " + getNode(activePointOut).getName() + " " +resistance.get();
    }

    @Override
    public String getComponentName() {
        return "R" + getName();
    }


}

