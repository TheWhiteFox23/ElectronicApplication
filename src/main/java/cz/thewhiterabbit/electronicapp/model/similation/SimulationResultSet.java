package cz.thewhiterabbit.electronicapp.model.similation;

import java.util.ArrayList;
import java.util.List;

public class SimulationResultSet {
    private final String name;
    private final List<Double> values;
    public SimulationResultSet(String name){
        this.name = name;
        this.values = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Double> getValues() {
        return values;
    }
}
