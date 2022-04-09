package cz.thewhiterabbit.electronicapp.model.similation;

import java.util.ArrayList;
import java.util.List;

public class NetlistNode {
    private String name;
    private List<SimulationComponent> simulationComponentList;
    private boolean probe = false;
    private String probeName = "probe";

    public NetlistNode(String name) {
        this.name = name;
        this.simulationComponentList = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SimulationComponent> getSimulationComponentList() {
        return simulationComponentList;
    }

    public boolean isProbe() {
        return probe;
    }

    public void setProbe(boolean probe) {
        this.probe = probe;
    }

    public String getProbeName() {
        return probeName;
    }

    public void setProbeName(String probeName) {
        this.probeName = probeName;
    }
}
