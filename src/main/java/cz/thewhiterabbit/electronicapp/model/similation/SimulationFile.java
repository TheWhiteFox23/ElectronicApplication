package cz.thewhiterabbit.electronicapp.model.similation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe simulation file and its parameters. Create simulation file to run with ngspice.
 */
public class SimulationFile {
    private String outResultPath = "";
    private String outDataPath = "";
    private Netlist netlist;

    private SimulationMode mode = SimulationMode.TRANSIENT;
    //TRANSIENT VARIABLES
    private DoubleProperty stopTime = new SimpleDoubleProperty(0);
    private DoubleProperty stepIncrement = new SimpleDoubleProperty(0);

    private BooleanProperty useStartTime=new SimpleBooleanProperty(false);
    private DoubleProperty startTime = new SimpleDoubleProperty(0);

    private BooleanProperty useInternalStep = new SimpleBooleanProperty(false);
    private DoubleProperty maxStepSize = new SimpleDoubleProperty(0);

    //AC SWEEP / DC ANALYSIS
    private DoubleProperty startValue = new SimpleDoubleProperty(0);

    private DoubleProperty stopValue = new SimpleDoubleProperty(0);

    private DoubleProperty numberOfPoints= new SimpleDoubleProperty(0);

    private Scale scale = Scale.DECIMAL;
    //GENERAl
    private BooleanProperty optimize = new SimpleBooleanProperty(true);
    private BooleanProperty onlyAnalyzeNodes =new SimpleBooleanProperty(false);

    public SimulationFile(Netlist netlist){
        this.netlist = netlist;
    }

    public SimulationFile(){

    }

    public Netlist getNetlist() {
        return netlist;
    }

    public void setNetlist(Netlist netlist) {
        this.netlist = netlist;
    }

    public List<String> createSimulationList(){
        List<String> toWrite = new ArrayList<>();
        toWrite.add("* Title: Test circuit");
        toWrite.add("");
        toWrite.add("* Netlist");

        netlist.getComponentList().forEach(c->{
            toWrite.add(c.getSimulationComponent());
        });
        toWrite.add("");
        toWrite.add(".control");
        toWrite.add("op");

        switch (mode){
            case TRANSIENT -> {
                String command = "tran "+getStepIncrement()  + " " + getStopTime();
                if(isUseStartTime()){
                    command += " " + getStartTime();
                }else{
                    command += " 0";
                }
                if(isUseInternalStep()) command += " " + getMaxStepSize();
                toWrite.add(command);
            }
        }

        toWrite.add("echo output test > " +outResultPath+" $ start new file");
        toWrite.add("set appendwrite");
        toWrite.add("set wr_vecnames");
        toWrite.add("set wr_singlescale");
        toWrite.add("wrdata " +outDataPath+" all");
        toWrite.add(".endc");
        return  toWrite;
    }


    public SimulationMode getMode() {
        return mode;
    }

    public void setMode(SimulationMode mode) {
        this.mode = mode;
    }

    public double getStopTime() {
        return stopTime.get();
    }

    public DoubleProperty stopTimeProperty() {
        return stopTime;
    }

    public void setStopTime(double stopTime) {
        this.stopTime.set(stopTime);
    }

    public double getStepIncrement() {
        return stepIncrement.get();
    }

    public DoubleProperty stepIncrementProperty() {
        return stepIncrement;
    }

    public void setStepIncrement(double stepIncrement) {
        this.stepIncrement.set(stepIncrement);
    }


    public double getStartTime() {
        return startTime.get();
    }

    public DoubleProperty startTimeProperty() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime.set(startTime);
    }

    public boolean isUseStartTime() {
        return useStartTime.get();
    }

    public BooleanProperty useStartTimeProperty() {
        return useStartTime;
    }

    public void setUseStartTime(boolean useStartTime) {
        this.useStartTime.set(useStartTime);
    }

    public boolean isUseInternalStep() {
        return useInternalStep.get();
    }

    public BooleanProperty useInternalStepProperty() {
        return useInternalStep;
    }

    public void setUseInternalStep(boolean useInternalStep) {
        this.useInternalStep.set(useInternalStep);
    }

    public double getMaxStepSize() {
        return maxStepSize.get();
    }

    public DoubleProperty maxStepSizeProperty() {
        return maxStepSize;
    }

    public void setMaxStepSize(double maxStepSize) {
        this.maxStepSize.set(maxStepSize);
    }

    public double getStartValue() {
        return startValue.get();
    }

    public DoubleProperty startValueProperty() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue.set(startValue);
    }

    public double getStopValue() {
        return stopValue.get();
    }

    public DoubleProperty stopValueProperty() {
        return stopValue;
    }

    public void setStopValue(double stopValue) {
        this.stopValue.set(stopValue);
    }

    public double getNumberOfPoints() {
        return numberOfPoints.get();
    }

    public DoubleProperty numberOfPointsProperty() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(double numberOfPoints) {
        this.numberOfPoints.set(numberOfPoints);
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public boolean isOptimize() {
        return optimize.get();
    }

    public BooleanProperty optimizeProperty() {
        return optimize;
    }

    public void setOptimize(boolean optimize) {
        this.optimize.set(optimize);
    }

    public boolean isOnlyAnalyzeNodes() {
        return onlyAnalyzeNodes.get();
    }

    public BooleanProperty onlyAnalyzeNodesProperty() {
        return onlyAnalyzeNodes;
    }

    public void setOnlyAnalyzeNodes(boolean onlyAnalyzeNodes) {
        this.onlyAnalyzeNodes.set(onlyAnalyzeNodes);
    }

    public String getOutResultPath() {
        return outResultPath;
    }

    public void setOutResultPath(String outResultPath) {
        this.outResultPath = outResultPath;
    }

    public String getOutDataPath() {
        return outDataPath;
    }

    public void setOutDataPath(String outDataPath) {
        this.outDataPath = outDataPath;
    }

    public enum SimulationMode{
        TRANSIENT,
        AC_SWEEP,
        DC_ANALYSIS
    }

    public enum Scale{
        LINEAR,
        DECIMAL,
        OCTAVE
    }

    public enum Unit{
        FEMTO("f(Femto)", "f"),
        PICO("p(Pico)", "p"),
        NANO("n(Nano)", "n"),
        MICRO("μ(Micro)", "u"),
        MILLI("m(Milli)", "m"),
        NONE("", ""),
        KILO("k(Kilo)", "k"),
        MEGA("M(Mega)", "meg"),
        GIGA("G(Giga)", "g");
        private String text;
        private String suffix;
        Unit(String text, String suffix){
            this.text = text;
            this.suffix = suffix;
        }
        public String getText(){
            return text;
        }

        public String getSuffix() {
            return suffix;
        }
    }



}
