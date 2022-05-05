package cz.thewhiterabbit.electronicapp.model.similation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SimulationResult {
    private List<SimulationResultSet> resultSetList;
    private Result result;


    public SimulationResult(){
        this.resultSetList = new ArrayList<>();
        result = Result.OK;
    }

    public SimulationResult(Result result){
        this.resultSetList = new ArrayList<>();
        this.result = result;
    }

    public List<SimulationResultSet> getResultSetList() {
        return resultSetList;
    }

    public void setResultSetList(List<SimulationResultSet> resultSetList) {
        this.resultSetList = resultSetList;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public enum Result{
        OK,
        ERROR,
        ERROR_SIMULATION_TIMEOUT,
        ERROR_DATA_TOO_LARGE,
        ERROR_SIMULATION_FAILED;
    }

    public SimulationResultSet getByName(String name){
        AtomicReference<SimulationResultSet> set = new AtomicReference<>();
        resultSetList.forEach(r->{
            if(r.getName().equals(name)) set.set(r);
        });
        return set.get();
    }

}
