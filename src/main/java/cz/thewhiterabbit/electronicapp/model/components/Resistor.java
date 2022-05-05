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
    @PropertyDialogField(name = "resistor.resistance", type = ComponentPropertyType.TEXT_FIELD,unit = "Ω")
    private final DoubleProperty resistance = new SimpleDoubleProperty(this, RESISTANCE, 1);

    private final String path ="M100,48.5H84V35H16V48.5H0v3H16V65H84V51.5h16ZM81,62H19V38H81Z";

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

