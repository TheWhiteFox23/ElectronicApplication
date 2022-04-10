package cz.thewhiterabbit.electronicapp.model.similation;

import java.io.FileWriter;
import java.io.IOException;
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
    private int stopTime = 0;
    private Unit stopTimeUnit = Unit.NONE;
    private int stepIncrement = 0;
    private Unit stepIncrementUnit = Unit.NONE;

    private boolean useStartTime=false;
    private int startTime = 0;
    private Unit startTimeUnit = Unit.NONE;

    private boolean useInternalStep = false;
    private int maxStepSize = 0;
    private Unit maxStepUnit = Unit.NONE;

    //AC SWEEP / DC ANALYSIS
    private int startValue = 0;
    private Unit startValueUnit =Unit.NONE;
    private int stopValue = 0;
    private Unit stopValueUnit= Unit.NONE;
    private int numberOfPoints= 0;

    private Scale scale = Scale.DECIMAL;
    //GENERAl
    private boolean optimize = true;
    private boolean onlyAnalyzeNodes = false;

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
        /*netlist.getComponentList().forEach(c->{
            if(c.isProbeActive()){
                toWrite.add("print I(" +c.getComponentName() +")");
            }
        });*/
        /*netlist.getNodeList().forEach(c->{
            if(c.isProbe()){
                toWrite.add("print V(" +c.getName() +")");
            }
        });*/
        switch (mode){
            case TRANSIENT -> {
                String command = "tran "+stepIncrement + stepIncrementUnit.suffix + " " + stopTime + stopTimeUnit.getSuffix();
                if(useStartTime){
                    command += " " + startTime + startTimeUnit.getSuffix();
                }else{
                    command += " 0";
                }
                if(useInternalStep) command += " " + maxStepSize + maxStepUnit.getSuffix();
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

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }

    public Unit getStopTimeUnit() {
        return stopTimeUnit;
    }

    public void setStopTimeUnit(Unit stopTimeUnit) {
        this.stopTimeUnit = stopTimeUnit;
    }

    public int getStepIncrement() {
        return stepIncrement;
    }

    public void setStepIncrement(int stepIncrement) {
        this.stepIncrement = stepIncrement;
    }

    public Unit getStepIncrementUnit() {
        return stepIncrementUnit;
    }

    public void setStepIncrementUnit(Unit stepIncrementUnit) {
        this.stepIncrementUnit = stepIncrementUnit;
    }

    public boolean isUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(boolean useStartTime) {
        this.useStartTime = useStartTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public Unit getStartTimeUnit() {
        return startTimeUnit;
    }

    public void setStartTimeUnit(Unit startTimeUnit) {
        this.startTimeUnit = startTimeUnit;
    }

    public boolean isUseInternalStep() {
        return useInternalStep;
    }

    public void setUseInternalStep(boolean useInternalStep) {
        this.useInternalStep = useInternalStep;
    }

    public int getMaxStepSize() {
        return maxStepSize;
    }

    public void setMaxStepSize(int maxStepSize) {
        this.maxStepSize = maxStepSize;
    }

    public Unit getMaxStepUnit() {
        return maxStepUnit;
    }

    public void setMaxStepUnit(Unit maxStepUnit) {
        this.maxStepUnit = maxStepUnit;
    }

    public int getStartValue() {
        return startValue;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
    }

    public Unit getStartValueUnit() {
        return startValueUnit;
    }

    public void setStartValueUnit(Unit startValueUnit) {
        this.startValueUnit = startValueUnit;
    }

    public int getStopValue() {
        return stopValue;
    }

    public void setStopValue(int stopValue) {
        this.stopValue = stopValue;
    }

    public Unit getStopValueUnit() {
        return stopValueUnit;
    }

    public void setStopValueUnit(Unit stopValueUnit) {
        this.stopValueUnit = stopValueUnit;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public boolean isOptimize() {
        return optimize;
    }

    public void setOptimize(boolean optimize) {
        this.optimize = optimize;
    }

    public boolean isOnlyAnalyzeNodes() {
        return onlyAnalyzeNodes;
    }

    public void setOnlyAnalyzeNodes(boolean onlyAnalyzeNodes) {
        this.onlyAnalyzeNodes = onlyAnalyzeNodes;
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
