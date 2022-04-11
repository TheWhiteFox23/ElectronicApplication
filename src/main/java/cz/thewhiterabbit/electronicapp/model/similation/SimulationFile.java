package cz.thewhiterabbit.electronicapp.model.similation;

import javafx.beans.property.*;

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
    private DoubleProperty stopTime = new SimpleDoubleProperty(5);
    private DoubleProperty stepIncrement = new SimpleDoubleProperty(1e-3);
    private BooleanProperty useStartTime=new SimpleBooleanProperty(false);
    private DoubleProperty startTime = new SimpleDoubleProperty(0);
    private BooleanProperty useInternalStep = new SimpleBooleanProperty(false);
    private DoubleProperty maxStepSize = new SimpleDoubleProperty(5e-2);

    //AC SWEEP
    private DoubleProperty startValueAC = new SimpleDoubleProperty(1e-3);
    private DoubleProperty stopValueAC = new SimpleDoubleProperty(1e+3);
    private IntegerProperty numberOfPointsAC = new SimpleIntegerProperty(500);
    private IntegerProperty scaleTopAC = new SimpleIntegerProperty(-180);
    private IntegerProperty scaleBottomAC = new SimpleIntegerProperty(180);

    //AC SWEEP
    private DoubleProperty startValueDC = new SimpleDoubleProperty(1e-3);
    private DoubleProperty stopValueDC = new SimpleDoubleProperty(1e+3);
    private IntegerProperty numberOfPointsDC = new SimpleIntegerProperty(500);
    private IntegerProperty scaleTopDC = new SimpleIntegerProperty(-180);
    private IntegerProperty scaleBottomDC = new SimpleIntegerProperty(180);
    //GENERAl
    private BooleanProperty optimize = new SimpleBooleanProperty(true);
    private BooleanProperty onlyAnalyzeNodes =new SimpleBooleanProperty(false);

    public SimulationFile(Netlist netlist){
        this.netlist = netlist;
    }

    public SimulationFile(){
        stopTime.addListener(l->{
            System.out.println("stopTime changed: " + stopTime.get() + this);
        });
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

    public double getStartValueAC() {
        return startValueAC.get();
    }

    public DoubleProperty startValueACProperty() {
        return startValueAC;
    }

    public void setStartValueAC(double startValueAC) {
        this.startValueAC.set(startValueAC);
    }

    public double getStopValueAC() {
        return stopValueAC.get();
    }

    public DoubleProperty stopValueACProperty() {
        return stopValueAC;
    }

    public void setStopValueAC(double stopValueAC) {
        this.stopValueAC.set(stopValueAC);
    }

    public double getNumberOfPointsAC() {
        return numberOfPointsAC.get();
    }

    public IntegerProperty numberOfPointsACProperty() {
        return numberOfPointsAC;
    }

    public void setNumberOfPointsAC(int numberOfPointsAC) {
        this.numberOfPointsAC.set(numberOfPointsAC);
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

    public double getStartValueDC() {
        return startValueDC.get();
    }

    public DoubleProperty startValueDCProperty() {
        return startValueDC;
    }

    public void setStartValueDC(double startValueDC) {
        this.startValueDC.set(startValueDC);
    }

    public double getStopValueDC() {
        return stopValueDC.get();
    }

    public DoubleProperty stopValueDCProperty() {
        return stopValueDC;
    }

    public void setStopValueDC(double stopValueDC) {
        this.stopValueDC.set(stopValueDC);
    }

    public int getNumberOfPointsDC() {
        return numberOfPointsDC.get();
    }

    public IntegerProperty numberOfPointsDCProperty() {
        return numberOfPointsDC;
    }

    public void setNumberOfPointsDC(int numberOfPointsDC) {
        this.numberOfPointsDC.set(numberOfPointsDC);
    }

    public int getScaleTopAC() {
        return scaleTopAC.get();
    }

    public IntegerProperty scaleTopACProperty() {
        return scaleTopAC;
    }

    public void setScaleTopAC(int scaleTopAC) {
        this.scaleTopAC.set(scaleTopAC);
    }

    public int getScaleBottomAC() {
        return scaleBottomAC.get();
    }

    public IntegerProperty scaleBottomACProperty() {
        return scaleBottomAC;
    }

    public void setScaleBottomAC(int scaleBottomAC) {
        this.scaleBottomAC.set(scaleBottomAC);
    }

    public int getScaleTopDC() {
        return scaleTopDC.get();
    }

    public IntegerProperty scaleTopDCProperty() {
        return scaleTopDC;
    }

    public void setScaleTopDC(int scaleTopDC) {
        this.scaleTopDC.set(scaleTopDC);
    }

    public int getScaleBottomDC() {
        return scaleBottomDC.get();
    }

    public IntegerProperty scaleBottomDCProperty() {
        return scaleBottomDC;
    }

    public void setScaleBottomDC(int scaleBottomDC) {
        this.scaleBottomDC.set(scaleBottomDC);
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
