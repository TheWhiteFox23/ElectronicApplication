package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


@ComponentType
public class Ground extends GeneralComponent {

    private final String path ="M47.12,84V51.5H100v-3H47.12V16l-45,32.5L0,50l2.08,1.5ZM7.22,48.5,44.11,21.88V78.12L7.22," +
            "51.5,5.14,50Z";
    private ActivePoint activePointIn;

    public Ground(){
        super();
        setComponent(Component.GROUND);
        setPath(path);
        activePointIn = new ActivePoint();
        addActivePoint(activePointIn, 2,1);

    }

    @Override
    public String getSimulationComponent() {
        return getComponentName()+ " " + getNode(activePointIn).getName() + " 0 0";
    }

    @Override
    public String getComponentName() {
        return "R"+getName();
    }


}
