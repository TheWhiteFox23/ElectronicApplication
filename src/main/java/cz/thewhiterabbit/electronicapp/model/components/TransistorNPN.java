package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import javafx.scene.canvas.GraphicsContext;

@ComponentType
public class TransistorNPN extends GeneralComponent {
    private ActivePoint activePointBase;
    private ActivePoint activePointCollector;
    private ActivePoint activePointEmitter;

    private final String path ="M97,0v14.1L51.5,32.8V16h-3v32.5H0v3h48.5V84h3V67.2l18.2,7.4l-2.7,6.6l8.5-1.2l5.7-0.8L97,85.9V100h3V83.8" +
            "l-17.9-7.3l-3.4-4.3l-5.3-6.8l-2.7,6.5L51.5,64V36L100,16.2V0H97z";

    public TransistorNPN(){
        super();
        setComponent(Component.TRANSISTOR_NPN);
        getPathList().add(path);
        activePointBase = new ActivePoint();
        activePointCollector = new ActivePoint();
        activePointEmitter = new ActivePoint();
        addActivePoint(activePointBase, 0,1);
        addActivePoint(activePointCollector,2,0);
        addActivePoint(activePointEmitter,2,2);
    }

    @Override
    protected void doPaint(GraphicsContext gc){
        super.doPaint(gc);
    }

    @Override
    public String getSimulationComponent() {
        return getComponentName()+" " + getNode(activePointCollector).getName() + " "
                + getNode(activePointBase).getName() + " " +
                getNode(activePointEmitter).getName() + " QMOD" +
                "\n.model QMOD NPN level=4";
    }

    @Override
    public String getComponentName() {
        return "Q" + getName();
    }


}

