package cz.thewhiterabbit.electronicapp.model.similation;

import java.util.ArrayList;
import java.util.List;

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
        ERROR;
    }

}
