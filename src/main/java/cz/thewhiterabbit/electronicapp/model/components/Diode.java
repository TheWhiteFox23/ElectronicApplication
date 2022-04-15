package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

@ComponentType
public class Diode extends GeneralComponent {
    private ActivePoint activePointIn;
    private ActivePoint activePointOut;

    private final String path ="M67.8,34.2L36.4,48.5h-1.2V35h-3v13.5H0v3h32.3V65h3V51.5h1.2l31.3,14.3V51.5H100v-3H67.8V34.2z";

    public Diode(){
        super();
        setComponent(Component.DIODE);
        getPathList().add(path);
        activePointIn = new ActivePoint();
        activePointOut = new ActivePoint();
        addActivePoint(activePointIn, 2,1);
        addActivePoint(activePointOut,0,1);
    }

    @Override
    protected void doPaint(GraphicsContext gc){
        super.doPaint(gc);
    }

    @Override
    public String getSimulationComponent() {
        return getComponentName()+" " + getNode(activePointIn).getName() + " "
                + getNode(activePointOut).getName() + " DMOD" +
                "\n.model DMOD D";
    }

    @Override
    public String getComponentName() {
        return "D" + getName();
    }


}

