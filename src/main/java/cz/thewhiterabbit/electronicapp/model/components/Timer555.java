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

    private final String subCircuit = " .SUBCKT UA555  32 30 19 23 33 1  21\n" +
            "    *           TR O  R  F  TH D  V\n" +
            "    *\n" +
            "    * Taken from the Fairchild data book (1982) page 9-3\n" +
            "    *SYM=UA555\n" +
            "    *DWG=C:\\SPICE\\555\\UA555.DWG\n" +
            "    Q4 25 2 3 QP\n" +
            "    Q5 0 6 3 QP\n" +
            "    Q6 6 6 8 QP\n" +
            "    R1 9 21 4.7K\n" +
            "    R2 3 21 830\n" +
            "    R3 8 21 4.7K\n" +
            "    Q7 2 33 5 QN\n" +
            "    Q8 2 5 17 QN\n" +
            "    Q9 6 4 17 QN\n" +
            "    Q10 6 23 4 QN\n" +
            "    Q11 12 20 10 QP\n" +
            "    R4 10 21 1K\n" +
            "    Q12 22 11 12 QP\n" +
            "    Q13 14 13 12 QP\n" +
            "    Q14 0 32 11 QP\n" +
            "    Q15 14 18 13 QP\n" +
            "    R5 14 0 100K\n" +
            "    R6 22 0 100K\n" +
            "    R7 17 0 10K\n" +
            "    Q16 1 15 0 QN\n" +
            "    Q17 15 19 31 QP\n" +
            "    R8 18 23 5K\n" +
            "    R9 18 0 5K\n" +
            "    R10 21 23 5K\n" +
            "    Q18 27 20 21 QP\n" +
            "    Q19 20 20 21 QP\n" +
            "    R11 20 31 5K\n" +
            "    D1 31 24 DA\n" +
            "    Q20 24 25 0 QN\n" +
            "    Q21 25 22 0 QN\n" +
            "    Q22 27 24 0 QN\n" +
            "    R12 25 27 4.7K\n" +
            "    R13 21 29 6.8K\n" +
            "    Q23 21 29 28 QN\n" +
            "    Q24 29 27 16 QN\n" +
            "    Q25 30 26 0 QN\n" +
            "    Q26 21 28 30 QN\n" +
            "    D2 30 29 DA\n" +
            "    R14 16 15 100\n" +
            "    R15 16 26 220\n" +
            "    R16 16 0 4.7K\n" +
            "    R17 28 30 3.9K\n" +
            "    Q3 2 2 9 QP\n" +
            "    .MODEL DA D (RS=40 IS=1.0E-14 CJO=1PF)\n" +
            "    .MODEL QP PNP (BF=20 BR=0.02 RC=4 RB=25 IS=1.0E-14 VA=50 NE=2)\n" +
            "    + CJE=12.4P VJE=1.1 MJE=.5 CJC=4.02P VJC=.3 MJC=.3 TF=229P TR=159N)\n" +
            "    .MODEL QN NPN (IS=5.07F NF=1 BF=100 VAF=161 IKF=30M ISE=3.9P NE=2\n" +
            "    + BR=4 NR=1 VAR=16 IKR=45M RE=1.03 RB=4.12 RC=.412 XTB=1.5\n" +
            "    + CJE=12.4P VJE=1.1 MJE=.5 CJC=4.02P VJC=.3 MJC=.3 TF=229P TR=959P)\n" +
            " .ENDS";

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
        String command = subCircuit + "\n" +
                getComponentName() + " " +
                getNode(acTRI).getName() + " " +
                getNode(acOUT).getName() + " " +
                getNode(acRST).getName() + " " +
                getNode(acCON).getName() + " " +
                getNode(acTHR).getName() + " " +
                getNode(acDIS).getName() + " " +
                getNode(acVCC).getName() + " ua555" ;
        return command;
    }

    @Override
    public String getComponentName() {
        return "X" + getName();
    }


}

