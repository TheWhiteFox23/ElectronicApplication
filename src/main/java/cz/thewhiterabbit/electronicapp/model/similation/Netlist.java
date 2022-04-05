package cz.thewhiterabbit.electronicapp.model.similation;

import java.util.ArrayList;
import java.util.List;

public class Netlist {
    private List<NetlistNode> nodeList;
    private List<SimulationComponent> componentList;

    public Netlist() {
        nodeList = new ArrayList<>();
        componentList = new ArrayList<>();
    }

    public List<NetlistNode> getNodeList() {
        return nodeList;
    }

    public List<SimulationComponent> getComponentList() {
        return componentList;
    }
}
