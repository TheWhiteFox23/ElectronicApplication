package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import javafx.beans.property.*;


@ComponentType
public class VoltageSource extends GeneralComponent {

    private ActivePoint activePointIn;
    private ActivePoint activePointOut;

    private final String TYPE = "source_type";
    @RawPropertyMapping
    @PropertyDialogField(name = "Type", type = ComponentPropertyType.COMBO_BOX, values = {"Linear", "Pulse", "Sinusoidal"})
    private final StringProperty type = new SimpleStringProperty(this, TYPE, "Linear");

    //LINEAR
    @PropertyDialogField(name = "Linear", type = ComponentPropertyType.LABEL)
    private final StringProperty linear_text = new SimpleStringProperty(this, "linear", "");

    private final String VALUE = "value";
    @RawPropertyMapping
    @PropertyDialogField(name = "Value", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty value = new SimpleDoubleProperty(this, VALUE, 5);

    //PULSE
    @PropertyDialogField(name = "Pulse", type = ComponentPropertyType.LABEL)
    private final StringProperty pulse_text = new SimpleStringProperty(this, "pulse", "");

    private final String INITIAL_VALUE = "initial_value";
    @RawPropertyMapping
    @PropertyDialogField(name = "Initial value", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty initial_value = new SimpleDoubleProperty(this, INITIAL_VALUE, 0);

    private final String PULSE_VALUE = "pulse_value";
    @RawPropertyMapping
    @PropertyDialogField(name = "Pulse value", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty pulse_value = new SimpleDoubleProperty(this, PULSE_VALUE, 5);

    private final String DELAY_TIME = "delay_time";
    @RawPropertyMapping
    @PropertyDialogField(name = "Delay time", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty delay_time = new SimpleDoubleProperty(this, DELAY_TIME, 0);

    private final String RISE_TIME = "rise_time";
    @RawPropertyMapping
    @PropertyDialogField(name = "Rise time", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty rise_time = new SimpleDoubleProperty(this, RISE_TIME, 0);

    private final String FALL_TIME = "fall_time";
    @RawPropertyMapping
    @PropertyDialogField(name = "Fall time", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty fall_time = new SimpleDoubleProperty(this, FALL_TIME, 0);

    private final String PULSE_WIDTH = "pulse_width";
    @RawPropertyMapping
    @PropertyDialogField(name = "Pulse width", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty pulse_width = new SimpleDoubleProperty(this, PULSE_WIDTH, 5);

    private final String PERIOD = "period";
    @RawPropertyMapping
    @PropertyDialogField(name = "Period", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty period = new SimpleDoubleProperty(this, PERIOD, 5);

    private final String PHASE_PULSE = "phase_pulse";
    @RawPropertyMapping
    @PropertyDialogField(name = "Phase", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty phase = new SimpleDoubleProperty(this, PHASE_PULSE, 5);
    //SINUSOIDAL
    @PropertyDialogField(name = "Sinusoidal", type = ComponentPropertyType.LABEL)
    private final StringProperty sinusoidal_text = new SimpleStringProperty(this, "sinusoidal", "");

    private final String OFFSET = "offset";
    @RawPropertyMapping
    @PropertyDialogField(name = "Offset", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty offset = new SimpleDoubleProperty(this, OFFSET, 0);

    private final String AMPLITUDE = "amplitude";
    @RawPropertyMapping
    @PropertyDialogField(name = "Amplitude", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty amplitude = new SimpleDoubleProperty(this, AMPLITUDE, 1);

    private final String FREQUENCY = "frequency";
    @RawPropertyMapping
    @PropertyDialogField(name = "Frequency", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty frequency = new SimpleDoubleProperty(this, FREQUENCY, 100e+3);

    private final String DELAY = "delay";
    @RawPropertyMapping
    @PropertyDialogField(name = "Delay", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty delay = new SimpleDoubleProperty(this, DELAY, 1e-3);

    private final String DUMPING_FACTOR = "dumping_factor";
    @RawPropertyMapping
    @PropertyDialogField(name = "Dumping factor", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty dumping_factor = new SimpleDoubleProperty(this, DUMPING_FACTOR, 1e10);

    private final String PHASE_SINUSOIDAL = "phase_sinusoidal";
    @RawPropertyMapping
    @PropertyDialogField(name = "Phase sinusoidal", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty phase_sinusoidal = new SimpleDoubleProperty(this, PHASE_SINUSOIDAL, 0);

    private final String path ="M84,48.5V47.44l-.82-5.28-1.63-5.1-2.42-4.77L76,28l-3.77-3.8L67.92,21l-4.76-2.45-5.09-" +
            "1.67L52.79,16l-5.35,0-5.29.82-5.09,1.63-4.77,2.42L28,24l-3.8,3.77L21,32.08l-2.45,4.76-1.67,5.09L16,47.21V48" +
            ".5H0v3H16v1.06l.82,5.28,1.63,5.1,2.42,4.77L24,72.05l3.77,3.8L32.08,79l4.76,2.45,5.09,1.67,5.28.85,5.47,0," +
            "5.16-.82,5.1-1.63,4.77-2.42L72.05,76l3.8-3.77L79,67.92l2.45-4.76,1.67-5.09L84,52.79V51.5h16v-3Zm-65-.94.76" +
            "-4.82,1.51-4.64,2.22-4.35,2.86-3.95,3.45-3.45,3.95-2.86,4.35-2.22,4.64-1.51L47.56,19h4.88l4.82.76,4.64," +
            "1.51,4.35,2.22,3.94,2.86,3.46,3.45,2.86,3.95,2.22,4.35,1.51,4.64L81,47.56v.94H26.83L46.5," +
            "36.27l-1.58-2.54L21.15,48.5,19,49.83V47.56Zm62,4.88-.76,4.82L78.73,61.9l-2.22,4.35-2.86,3.94-3.46," +
            "3.46-3.94,2.86L61.9,78.73l-4.64,1.51L52.44,81H47.56l-4.82-.76L38.1,78.73l-4.35-2.22L29.8," +
            "73.65l-3.45-3.46-2.86-3.94L21.27,61.9l-1.51-4.64L19,52.44V50.17l2.14," +
            "1.33L44.92,66.27l1.58-2.54L26.82,51.5H81Z";

    private final String path1 = "M83.3,43l-2.2-6.8L77.6,30l-4.7-5.3l-5.7-4.2l-6.5-2.9L53.7,16h-7.1l-7,1.5l-6.5,2.9l-5.7,4.2l-4.8,5.3L19,36" +
            "l-2.2,6.8l-0.6,5.8H0v3h16.1l0.6,5.5l2.2,6.8l3.5,6.2l4.7,5.3l5.7,4.2l6.5,2.9l7,1.5h7.3l6.8-1.4l6.5-2.9l5.8-4.2l4.8-5.3L81,64" +
            "l2.2-6.8l0.6-5.8H100v-3H83.8L83.3,43z M80.3,56.5l-2,6.2L75,68.3l-4.3,4.8L65.5,77l-5.9,2.7L53.2,81h-6.5l-6.3-1.3L34.5,77" +
            "l-5.2-3.8l-4.3-4.8l-3.2-5.6l-2-6.2L19,50l0.7-6.5l2-6.2l3.2-5.6l4.3-4.9l5.2-3.8l5.9-2.6l6.3-1.4h6.5l6.3,1.4l5.9,2.6l5.2,3.8" +
            "l4.3,4.9l3.2,5.6l2,6.2L81,50L80.3,56.5z";
    private final String path2 = "M54.3,48.5H75v3H54.3V48.5z";
    private final String path3 = "M36.8,39h-3v9.5H25v3h8.8v8.2h3v-8.2h8.8v-3h-8.8V39z";


    public VoltageSource(){
        super();
        setComponent(Component.VOLTAGE_SOURCE);
        getPathList().add(path1);
        getPathList().add(path2);
        getPathList().add(path3);

        activePointIn = new ActivePoint();
        activePointOut = new ActivePoint();
        addActivePoint(activePointIn, 0,1);
        addActivePoint(activePointOut,2,1);
    }

    @Override
    public String getSimulationComponent() {
        if(type.getValue().equals("Sinusoidal")){
            return getSinusoidal();
        }else if (type.getValue().equals("Pulse")){
            return getPulse();
        }else{
            return getLinear();
        }

    }

    private String getSinusoidal() {
        String command = "";
        command += getComponentName() + " " + getNode(activePointIn).getName() + " " +
                getNode(activePointOut).getName() + " 0.001 AC 1 SIN(" +
                offset.getValue() + " " +
                amplitude.getValue() + " " +
                frequency.getValue() + " " +
                delay.getValue() + " " +
                dumping_factor.getValue() + " " +
                phase_sinusoidal.getValue() + ")";
        return command;
    }

    private String getPulse() {
        return getLinear();
    }

    private String getLinear() {
        return getComponentName() + " " + getNode(activePointIn).getName() + " " +
                getNode(activePointOut).getName() + " " + value.get();
    }

    @Override
    public String getComponentName() {
        return "V"+getName();
    }
}
