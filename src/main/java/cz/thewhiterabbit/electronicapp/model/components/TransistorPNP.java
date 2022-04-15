package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import javafx.scene.canvas.GraphicsContext;

@ComponentType
public class TransistorPNP extends GeneralComponent {
    private ActivePoint activePointBase;
    private ActivePoint activePointCollector;
    private ActivePoint activePointEmitter;

    private final String path ="M97,0v14.1L64.7,27.4L62,20.7l-5.3,6.8L53.3,32l-1.8,0.8V16h-3v32.5H0v3h48.5V84h3V67.2L97,85.9V100h3V83.8" +
            "L51.5,64V36l3-1.2l5.5,0.7l8.5,1.1l-2.7-6.5l34.1-14V0H97z";

    public TransistorPNP(){
        super();
        setComponent(Component.TRANSISTOR_PNP);
        getPathList().add(path);
        activePointBase = new ActivePoint();
        activePointCollector = new ActivePoint();
        activePointEmitter = new ActivePoint();
        addActivePoint(activePointBase, 0,1);
        addActivePoint(activePointCollector,2,2);
        addActivePoint(activePointEmitter,2,0);
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
                "\n.model QMOD PNP level=4";
    }

    @Override
    public String getComponentName() {
        return "Q" + getName();
    }


}

