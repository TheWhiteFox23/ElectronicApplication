package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;


public class Timer555 extends GeneralComponent {
    private ActivePoint acDIS;
    private ActivePoint acRST;
    private ActivePoint acVCC;
    private ActivePoint acOUT;
    private ActivePoint acTHR;
    private ActivePoint acTRI;
    private ActivePoint acGND;
    private ActivePoint acCON;


    private final String path ="M100,51.5c0.8,0,1.5-0.7,1.5-1.5s-0.7-1.5-1.5-1.5h-3V5.1l4.1-4.1c0.6-0.6,0.6-1.5,0-2.1c-0.6-0.6-1.5-0.6-2.1,0L94.9,3\n" +
            "\t\t\t\tH51.5V0c0-0.8-0.7-1.5-1.5-1.5S48.5-0.8,48.5,0v3H5.1L1.1-1.1c-0.6-0.6-1.5-0.6-2.1,0c-0.6,0.6-0.6,1.5,0,2.1L3,5.1v43.4H0\n" +
            "\t\t\t\tc-0.8,0-1.5,0.7-1.5,1.5s0.7,1.5,1.5,1.5h3v43.4l-4.1,4.1c-0.6,0.6-0.6,1.5,0,2.1c0.3,0.3,0.7,0.4,1.1,0.4s0.8-0.1,1.1-0.4\n" +
            "\t\t\t\tL5.1,97h43.4v3c0,0.8,0.7,1.5,1.5,1.5s1.5-0.7,1.5-1.5v-3h43.4l4.1,4.1c0.3,0.3,0.7,0.4,1.1,0.4s0.8-0.1,1.1-0.4\n" +
            "\t\t\t\tc0.6-0.6,0.6-1.5,0-2.1L97,94.9V51.5H100z M94,8.1v40.4v3v40.4V94h-2.1H51.5h-3H8.1H6v-2.1V51.5v-3V8.1V6h2.1h40.4h3h40.4H94V8.1\n" +
            "\t\t\t\tz";

    public Timer555(){
        super();
        setComponent(Component.TIMER555);
        getPathList().add(path);

        acDIS = new ActivePoint();
        addActivePoint(acDIS, 0 ,0);

        acRST = new ActivePoint();
        addActivePoint(acRST, 1 ,0);

        acVCC = new ActivePoint();
        addActivePoint(acVCC, 2, 0);

        acTHR = new ActivePoint();
        addActivePoint(acTHR, 0,1);

        acOUT = new ActivePoint();
        addActivePoint(acOUT, 2,1);

        acTRI = new ActivePoint();
        addActivePoint(acTRI, 0,2);

        acGND = new ActivePoint();
        addActivePoint(acGND, 1,2);

        acCON = new ActivePoint();
        addActivePoint(acCON, 2,2);
    }

    @Override
    protected void doPaint(GraphicsContext gc){
        super.doPaint(gc);
        gc.setFont(new Font(10));
        gc.fillText("DIS", 10, 20);
        gc.fillText("THR", 10, 55);
        gc.fillText("TRI", 10, 90);

        gc.fillText("RST", 40, 20);
        gc.setFont(new Font(15));
        gc.fillText("555", 37, 55);
        gc.setFont(new Font(10));
        gc.fillText("GND", 40, 90);

        gc.fillText("VCC", 70, 20);
        gc.fillText("OUT", 70, 55);
        gc.fillText("CON", 70, 90);
    }

    @Override
    public String getSimulationComponent() {
        return null;
    }

    @Override
    public String getComponentName() {
        return null;
    }


}

