package cz.thewhiterabbit.electronicapp.model.similation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Describe simulation file and its parameters. Create simulation file to run with ngspice.
 */
public class SimulationFile {

    private Netlist netlist;

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

    public void createSimulationFile() throws IOException {
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
        netlist.getNodeList().forEach(c->{
            if(c.isProbe()){
                toWrite.add("print V(" +c.getName() +")");
            }
        });
        toWrite.add(".endc");
        FileWriter fileWriter = new FileWriter("circuit.cir");
        toWrite.forEach(s->{
            try {
                fileWriter.write(s + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileWriter.close();
    }

}
