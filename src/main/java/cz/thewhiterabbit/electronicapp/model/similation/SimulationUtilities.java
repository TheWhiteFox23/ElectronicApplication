package cz.thewhiterabbit.electronicapp.model.similation;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.objects.TwoPointLineObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationUtilities {
    //private HashMap<String, List<ActivePoint>> activePointMap;
    private static final String spiceResource = App.class.getResource("ngspice/bin/ngspice_con.exe").toExternalForm();
    private static final String inputFile = "circuit.cir";
    private static final String outputFile = "result.txt";
    private static String[] argumString = new String[]{"cmd.exe", "/c", "start", spiceResource, "-b", inputFile, "-o", outputFile};
    private static File outData = new File("data.txt");
    private static File outResult = new File("result.txt");


    public static Netlist createNetlist(Document document) {
        Netlist netlist = new Netlist();
        List<DocumentObject> documentObjectList = document.getDocumentObjects();

        HashMap<NetlistNode, List<ActivePoint>> activePointsMap = new HashMap<>();
        getActivePointMap(documentObjectList).forEach((key, acl) -> {
            NetlistNode node = new NetlistNode(key);
            activePointsMap.put(node, acl);
            acl.forEach(ac -> {
                if (ac.get_probeActive()) {
                    node.setProbe(true);
                    node.setProbeName(ac.get_probeName());
                }
                CanvasObject co = ac.getParent();
                if (co instanceof SimulationComponent) {
                    SimulationComponent sc = (SimulationComponent) co;
                    sc.setNode(ac, node);
                    node.getSimulationComponentList().add(sc);
                    if (!netlist.getComponentList().contains(sc)) netlist.getComponentList().add(sc);
                    if (!netlist.getNodeList().contains(node)) netlist.getNodeList().add(node);
                }
            });
        });
        numberComponents(netlist);
        numberNodes(netlist);
        validateProbesNames(netlist);
        return netlist;
    }

    public static Netlist optimizeNetlist(Netlist netlist) {
        Stack<SimulationComponent> stack = new Stack();
        stack.addAll(netlist.getComponentList());
        while (!stack.empty()) {
            SimulationComponent component = stack.pop();
            if (component instanceof TwoPointLineObject) {
                removeEmptyLine(netlist, component);
            }
        }
        return netlist;
    }

    protected static void removeEmptyLine(Netlist netlist, SimulationComponent component) {
        netlist.getNodeList().remove(((TwoPointLineObject) component).getIn());
        exchangeNodes(((TwoPointLineObject) component).getIn(), ((TwoPointLineObject) component).getOut());
        removeComponent(netlist, component);
    }

    public static void numberComponents(Netlist netlist) {
        AtomicInteger number = new AtomicInteger();
        netlist.getComponentList().forEach(component -> {
            component.setName(String.valueOf(number.get()));
            number.getAndIncrement();
        });
    }

    public static void exchangeNodes(NetlistNode toRemove, NetlistNode newNode) {
        toRemove.getSimulationComponentList().forEach(c -> {
            if (toRemove.isProbe()) {
                newNode.setProbe(true);
                newNode.setProbeName(toRemove.getProbeName());
            }
            c.setNode(toRemove, newNode);
            newNode.getSimulationComponentList().add(c);
        });
    }

    public static void validateProbesNames(Netlist netlist) {
        List<String> individualProbeNames = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        validateNodeProbes(netlist, individualProbeNames, count);
        validateComponentProbes(netlist, individualProbeNames, count);
    }

    private static void validateNodeProbes(Netlist netlist, List<String> individualProbeNames, AtomicInteger count) {
        netlist.getNodeList().forEach(n -> {
            if (n.isProbe()) {
                String name = n.getProbeName();
                if (!individualProbeNames.contains(name)) {
                    individualProbeNames.add(name);
                } else {
                    while (individualProbeNames.contains(name + count)) {
                        count.getAndIncrement();
                    }
                    n.setProbeName(name + count);
                    individualProbeNames.add(name + count);
                }
            }
        });
    }

    private static void validateComponentProbes(Netlist netlist, List<String> individualProbeNames, AtomicInteger count) {
        netlist.getComponentList().forEach(n -> {
            if (n.isProbeActive()) {
                String name = n.getProbeName();
                if (!individualProbeNames.contains(name)) {
                    individualProbeNames.add(name);
                } else {
                    while (individualProbeNames.contains(name + count)) {
                        count.getAndIncrement();
                    }
                    n.setProbeName(name + count);
                    individualProbeNames.add(name + count);
                }
            }
        });
    }

    public static void removeComponent(Netlist netlist, SimulationComponent component) {
        netlist.getComponentList().remove(component);
        netlist.getNodeList().forEach(n -> {
            n.getSimulationComponentList().remove(component);
        });
    }

    public static Netlist numberNodes(Netlist netlist) {
        for (int i = 0; i < netlist.getNodeList().size(); i++) {
            netlist.getNodeList().get(i).setName(String.valueOf("n" + (i + 1)));
        }
        return netlist;
    }


    protected static HashMap<String, List<ActivePoint>> getActivePointMap(List<DocumentObject> documentObjects) {
        HashMap<String, List<ActivePoint>> activePointMap = new HashMap<>();
        Stack<DocumentObject> stack = new Stack();
        stack.addAll(documentObjects);
        while (!stack.empty()) {
            DocumentObject documentObject = stack.pop();
            documentObject.getChildrenList().forEach(ch -> {
                stack.add((DocumentObject) ch);
            });
            if (documentObject instanceof ActivePoint) {
                ActivePoint activePoint = (ActivePoint) documentObject;
                String key = activePoint.getGridX() + "_" + activePoint.getGridY();
                if (!activePointMap.containsKey(key)) activePointMap.put(key, new ArrayList<>());
                activePointMap.get(key).add(activePoint);
            }
        }
        return activePointMap;
    }

    public static SimulationResult simulate(SimulationFile simulationFile){
        resetSimulationResultFiles(simulationFile);
        createSimulationSpiceFile(simulationFile);
        Process process = startSimulation(); //Start simulation using generated spice file
        SimulationResult outData = awaitSimulationResultAndParse(process);
        if (outData != null) return outData;
        return new SimulationResult(SimulationResult.Result.ERROR);
    }

    public static Task getSimulationTask(SimulationFile simulationFile){
        Task simulationTask = new Task<SimulationResult>() {
            @Override
            protected SimulationResult call() throws Exception {
                updateMessage("Preparing output files");
                resetSimulationResultFiles(simulationFile);
                updateProgress(1, 10);

                updateMessage("Creating simulation spice file");
                createSimulationSpiceFile(simulationFile);
                updateProgress(2, 10);

                updateMessage("Simulation in progress");
                Process process = startSimulation(); //Start simulation using generated spice file
                updateProgress(4,10);

                SimulationResult outData = awaitSimulationResultAndParse(process);
                updateMessage("Simulation finished");
                updateProgress(10,10);
                if (outData != null) return outData;
                return new SimulationResult(SimulationResult.Result.ERROR);
            }
        };
        return simulationTask;
    }

    private static SimulationResult awaitSimulationResultAndParse(Process process) {
        try {
            process.waitFor();

            int count = 0;
            while (outData.length() ==0 && count <500){
                count++;
                Thread.sleep(10);
            }

            return parseResult(outData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Process startSimulation() {
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try{
            process = runtime.exec(argumString);
        }catch (Exception e){
            //todo return invalid simulation file
        }
        return process;
    }

    private static void createSimulationSpiceFile(SimulationFile simulationFile) {
        List<String> simulationFileList = simulationFile.createSimulationList();
        try {
            writeFile("circuit.cir", simulationFileList);
        } catch (IOException e) {
            //todo return invalid simulation file
        }
    }

    private static boolean resetSimulationResultFiles(SimulationFile simulationFile) {
        try {
            resetResultFiles();
        }catch (IOException e){
            return false;
        }
        simulationFile.setOutDataPath(outData.getPath());
        simulationFile.setOutResultPath(outResult.getPath());
        return true;
    }

    private static void writeFile(String path, List<String> simulationFileList) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        for(int i = 0; i< simulationFileList.size(); i++){
            fileWriter.write(simulationFileList.get(i) + "\n");
        }
        fileWriter.close();
    }

    private static SimulationResult parseResult(File file) throws FileNotFoundException {
        SimulationResult simulationResult = new SimulationResult();
        Scanner scanner = new Scanner(file);
        //create result set for each result vector
        parseVectors(simulationResult, scanner);
        parseValues(simulationResult, scanner);
        return simulationResult;
    }

    private static void parseValues(SimulationResult simulationResult, Scanner scanner) {
        while (scanner.hasNextLine()){
            String[] values = scanner.nextLine().split("\\s+");
            for(int i = 0; i< values.length; i++){
                if(values[i].length()!=0){
                    simulationResult.getResultSetList().get(i).getValues().add(Double.parseDouble(values[i]));
                }

            }
        }
    }

    private static void parseVectors(SimulationResult simulationResult, Scanner scanner) {
        if(scanner.hasNextLine()){
            String firstLine = scanner.nextLine();
            String[] variables = firstLine.split("\\s+");
            for(String s: variables){
                simulationResult.getResultSetList().add(new SimulationResultSet(s));
            }
        }
    }

    private static void resetResultFiles() throws IOException {
        if(outData.exists())outData.delete();
        if(outResult.exists())outResult.delete();
        outData.createNewFile();
        outResult.createNewFile();
    }
}
