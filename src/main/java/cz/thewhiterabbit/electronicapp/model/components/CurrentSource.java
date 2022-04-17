package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;


@ComponentType
public class CurrentSource extends GeneralComponent {

    private ActivePoint activePointIn;
    private ActivePoint activePointOut;

    private final String TYPE = "source_type";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.type", type = ComponentPropertyType.COMBO_BOX, values = {"Linear", "Pulse", "Sinusoidal"})
    private final StringProperty type = new SimpleStringProperty(this, TYPE, "Linear");

    //LINEAR
    @PropertyDialogField(name = "source.linear", type = ComponentPropertyType.LABEL)
    private final StringProperty linear_text = new SimpleStringProperty(this, "linear", "");

    private final String VALUE = "value";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.value", type = ComponentPropertyType.TEXT_FIELD, unit = "A")
    private final DoubleProperty value = new SimpleDoubleProperty(this, VALUE, 0.1);

    //PULSE
    @PropertyDialogField(name = "source.pulse", type = ComponentPropertyType.LABEL)
    private final StringProperty pulse_text = new SimpleStringProperty(this, "pulse", "");

    private final String INITIAL_VALUE = "initial_value";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.initial_value", type = ComponentPropertyType.TEXT_FIELD, unit = "A")
    private final DoubleProperty initial_value = new SimpleDoubleProperty(this, INITIAL_VALUE, 0);

    private final String PULSE_VALUE = "pulse_value";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.pulse_value", type = ComponentPropertyType.TEXT_FIELD, unit = "A")
    private final DoubleProperty pulse_value = new SimpleDoubleProperty(this, PULSE_VALUE, 0.1);

    private final String DELAY_TIME = "delay_time";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.delay_time", type = ComponentPropertyType.TEXT_FIELD, unit = "s")
    private final DoubleProperty delay_time = new SimpleDoubleProperty(this, DELAY_TIME, 0);

    private final String RISE_TIME = "rise_time";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.rise_time", type = ComponentPropertyType.TEXT_FIELD, unit = "s")
    private final DoubleProperty rise_time = new SimpleDoubleProperty(this, RISE_TIME, 0);

    private final String FALL_TIME = "fall_time";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.fall_time", type = ComponentPropertyType.TEXT_FIELD, unit = "s")
    private final DoubleProperty fall_time = new SimpleDoubleProperty(this, FALL_TIME, 0);

    private final String PULSE_WIDTH = "pulse_width";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.pulse_width", type = ComponentPropertyType.TEXT_FIELD, unit = "s")
    private final DoubleProperty pulse_width = new SimpleDoubleProperty(this, PULSE_WIDTH, 1);

    private final String PERIOD = "period";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.period", type = ComponentPropertyType.TEXT_FIELD, unit = "s")
    private final DoubleProperty period = new SimpleDoubleProperty(this, PERIOD, 2);

    private final String PHASE_PULSE = "phase_pulse";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.phase", type = ComponentPropertyType.TEXT_FIELD, unit = "deg")
    private final DoubleProperty phase_pulse = new SimpleDoubleProperty(this, PHASE_PULSE, 0);
    //SINUSOIDAL
    @PropertyDialogField(name = "source.sinusoidal", type = ComponentPropertyType.LABEL)
    private final StringProperty sinusoidal_text = new SimpleStringProperty(this, "sinusoidal", "");

    private final String OFFSET = "offset";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.offset", type = ComponentPropertyType.TEXT_FIELD, unit = "A")
    private final DoubleProperty offset = new SimpleDoubleProperty(this, OFFSET, 0);

    private final String AMPLITUDE = "amplitude";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.amplitude", type = ComponentPropertyType.TEXT_FIELD , unit = "A")
    private final DoubleProperty amplitude = new SimpleDoubleProperty(this, AMPLITUDE, 1);

    private final String FREQUENCY = "frequency";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.frequency", type = ComponentPropertyType.TEXT_FIELD, unit = "Hz")
    private final DoubleProperty frequency = new SimpleDoubleProperty(this, FREQUENCY, 0);

    private final String DELAY = "delay";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.delay", type = ComponentPropertyType.TEXT_FIELD, unit = "s")
    private final DoubleProperty delay = new SimpleDoubleProperty(this, DELAY, 1e-3);

    private final String DUMPING_FACTOR = "dumping_factor";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.dumping_factor", type = ComponentPropertyType.TEXT_FIELD, unit = "1/sec")
    private final DoubleProperty dumping_factor = new SimpleDoubleProperty(this, DUMPING_FACTOR, 0);

    private final String PHASE_SINUSOIDAL = "phase_sinusoidal";
    @RawPropertyMapping
    @PropertyDialogField(name = "source.phase_sinusoidal", type = ComponentPropertyType.TEXT_FIELD, unit = "deg")
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


    public CurrentSource(){
        super();
        setComponent(Component.CURRANT_SOURCE);
        getPathList().add(path);


        activePointIn = new ActivePoint();
        activePointOut = new ActivePoint();
        addActivePoint(activePointIn, 2,1);
        addActivePoint(activePointOut,0,1);
    }

    @Override
    protected void doPaint(GraphicsContext gc){
        super.doPaint(gc);
        gc.setFont(new Font(25));
        gc.fillText(String.valueOf(getComponentName() + " " +value.get()) + "A", 0,0);
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
                getNode(activePointOut).getName() + " SIN(" +
                offset.getValue() + " " +
                amplitude.getValue() + " " +
                frequency.getValue() + " " +
                delay.getValue() + " " +
                dumping_factor.getValue() + " " +
                phase_sinusoidal.getValue() + ")";
        return command;
    }

    private String getPulse() {
        String command = "";
        command += getComponentName() + " " + getNode(activePointIn).getName() + " " +
                getNode(activePointOut).getName() + " PULSE(" +
                initial_value.getValue() + " " +
                pulse_value.getValue() + " " +
                delay_time.getValue() + " " +
                rise_time.getValue() + " " +
                fall_time.getValue() + " " +
                pulse_width.getValue() + " " +
                period.getValue() + " " +
                phase_pulse.getValue() + ")";
        return command;
    }

    private String getLinear() {
        return getComponentName() + " " + getNode(activePointIn).getName() + " " +
                getNode(activePointOut).getName() + " " + value.get();
    }

    @Override
    public String getComponentName() {
        return "I"+getName();
    }
}
