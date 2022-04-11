package cz.thewhiterabbit.electronicapp.model.similation;

import cz.thewhiterabbit.electronicapp.model.components.GeneralComponent;
import cz.thewhiterabbit.electronicapp.model.components.Resistor;
import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.FileService;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.objects.TwoPointLineObject;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SimulationUtilitiesTest {
    private Document test_circuit;
    private Document test_circuit_2;
    private Document mapping_test_3;
    private Document mapping_test_2;
    private Document mapping_test;

    @BeforeEach
    void setUp() {
        FileService service = new FileService();
        try {
            test_circuit = service.load(new File("src/test/resources/test_circuit.aeon"));
            test_circuit_2 = service.load(new File("src/test/resources/test_circuit_2.aeon"));
            mapping_test_3 = service.load(new File("src/test/resources/mapping_test_3.aeon"));
            mapping_test_2 = service.load(new File("src/test/resources/mapping_test_2.aeon"));
            mapping_test = service.load(new File("src/test/resources/mapping_test.aeon"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getSimulationProcess(){
        Netlist netlist = SimulationUtilities.createNetlist(test_circuit);
        SimulationUtilities.optimizeNetlist(netlist);
        SimulationFile simulationFile = new SimulationFile(netlist);
        simulationFile.setMode(SimulationFile.SimulationMode.TRANSIENT);
        simulationFile.setStepIncrement(1);
        simulationFile.setStopTime(5);
        simulationFile.setUseStartTime(false);
        simulationFile.setStartTime(3);
        simulationFile.setUseInternalStep(false);
        simulationFile.setMaxStepSize(1);
        final SimulationResult[] simulationResult = new SimulationResult[1];
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                simulationResult[0] = SimulationUtilities.simulate(simulationFile);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
            System.out.println(simulationResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createSimulationFile(){
        Netlist netlist = SimulationUtilities.createNetlist(test_circuit);
        SimulationUtilities.optimizeNetlist(netlist);
        SimulationFile simulationFile = new SimulationFile(netlist);
        simulationFile.createSimulationList();
    }


    @Nested
    class  CreateNetlist{
        @Test
        void createNetlist() {
            Netlist netlist = SimulationUtilities.createNetlist(test_circuit);
            SimulationUtilities.optimizeNetlist(netlist);
            netlist.getComponentList().forEach(c->{
                System.out.println(c.getSimulationComponent());
            });
            netlist.getNodeList().forEach(n->{
                if(n.isProbe()) System.out.println(n.getProbeName());
            });
            netlist.getComponentList().forEach(n->{
                if(n.isProbeActive()) System.out.println(n.getProbeName());
            });
        }
        @Test
        void createNetlist_containsAllElements() {
            Netlist netlist = SimulationUtilities.createNetlist(test_circuit_2);
            test_circuit_2.getDocumentObjects().forEach(dc->{
                assertTrue(netlist.getComponentList().contains(dc));
            });
        }
        @Test
        void createNetlist_containsAllNodes() {
            int nodeSize = SimulationUtilities.getActivePointMap(test_circuit_2.getDocumentObjects()).size();
            Netlist netlist = SimulationUtilities.createNetlist(test_circuit_2);
            assertTrue(netlist.getNodeList().size()==nodeSize);
        }
    }


    @Nested
    class ActivePointMappingTest {
        @Test
        void getActivePointMap() {
            Map<String, List<ActivePoint>> activePointMap = SimulationUtilities.getActivePointMap(test_circuit.getDocumentObjects());
            Map<String, Integer> stringCountCompare = new HashMap<>() {{
                put("-1_-5", 2);
                put("2_-10", 2);
                put("-1_-7", 2);
                put("7_-10", 2);
                put("-1_-9", 2);
                put("-1_-10", 2);
                put("7_-8", 2);
                put("4_-10", 2);
                put("7_-5", 2);
                put("7_-6", 2);
                put("3_-5", 3);
            }};
            activePointMap.forEach((s, ac) -> {
                Assert.assertTrue(stringCountCompare.get(s) == ac.size());
            });
        }

        @Test
        void getActivePointMap_2() {
            Map<String, List<ActivePoint>> activePointMap = new SimulationUtilities().getActivePointMap(mapping_test_2.getDocumentObjects());
            Map<String, Integer> stringCountCompare = new HashMap<>() {{
                put("14_2", 2);
                put("0_0", 2);
                put("14_0", 2);
                put("2_0", 2);
                put("0_2", 2);
                put("12_2", 2);
                put("3_0", 2);
                put("11_2", 2);
                put("2_2", 2);
                put("12_0", 2);
                put("3_2", 2);
                put("5_0", 2);
                put("11_0", 2);
                put("6_0", 2);
                put("5_2", 2);
                put("6_2", 2);
                put("8_0", 2);
                put("9_0", 2);
                put("8_2", 2);
                put("9_2", 2);
            }};
            activePointMap.forEach((s, ac) -> {
                Assert.assertTrue(stringCountCompare.get(s) == ac.size());
            });
        }
    }

    @Nested
    class RemoveEmptyLineTest {
        @Test
        void removeEmptyLineTest_removeLine() {
            Netlist netlist = SimulationUtilities.createNetlist(test_circuit);
            Stack<SimulationComponent> stack = new Stack<>();
            stack.addAll(netlist.getComponentList());
            while (!stack.empty()) {
                SimulationComponent c = stack.pop();
                if (c instanceof TwoPointLineObject) {
                    SimulationUtilities.removeEmptyLine(netlist, c);
                    assertTrue(!netlist.getComponentList().contains(c));
                }
            }
        }

        @Test
        void removeEmptyLineTest_removeNodeIn() {
            Netlist netlist = SimulationUtilities.createNetlist(test_circuit);
            Stack<SimulationComponent> stack = new Stack<>();
            stack.addAll(netlist.getComponentList());
            while (!stack.empty()) {
                SimulationComponent c = stack.pop();
                if (c instanceof TwoPointLineObject) {
                    NetlistNode nodeIn = ((TwoPointLineObject) c).getIn();
                    SimulationUtilities.removeEmptyLine(netlist, c);
                    assertTrue(!netlist.getNodeList().contains(nodeIn));
                }
            }
        }

        @Test
        void removeEmptyLineTest_remainNodeOut() {
            Netlist netlist = SimulationUtilities.createNetlist(test_circuit);
            Stack<SimulationComponent> stack = new Stack<>();
            stack.addAll(netlist.getComponentList());
            while (!stack.empty()) {
                SimulationComponent c = stack.pop();
                if (c instanceof TwoPointLineObject) {
                    NetlistNode nodeOut = ((TwoPointLineObject) c).getOut();
                    SimulationUtilities.removeEmptyLine(netlist, c);
                    assertTrue(netlist.getNodeList().contains(nodeOut));
                }
            }
        }

        @Test
        void removeEmptyLineTest_pointSwitch() {
            Netlist netlist = SimulationUtilities.createNetlist(test_circuit);
            Stack<SimulationComponent> stack = new Stack<>();
            stack.addAll(netlist.getComponentList());
            while (!stack.empty()) {
                SimulationComponent c = stack.pop();
                if (c instanceof TwoPointLineObject) {
                    NetlistNode nodeIn = ((TwoPointLineObject) c).getIn();
                    List<SimulationComponent> nodeInComponents = new ArrayList<>();
                    nodeInComponents.addAll(nodeIn.getSimulationComponentList());
                    SimulationUtilities.removeEmptyLine(netlist, c);
                    netlist.getComponentList().forEach(com->{
                        assertTrue(!com.getNodes().contains(nodeIn));
                    });

                }
            }
        }
    }

    @Test
    void exchangeNodeTest(){
        Netlist netlist = new Netlist();
        SimulationComponent resistor = new Resistor();
        ActivePoint rIn = (ActivePoint) ((GeneralComponent)resistor).getChildrenList().get(0);
        ActivePoint rOut = (ActivePoint) ((GeneralComponent)resistor).getChildrenList().get(1);
        ActivePoint lineOut = new ActivePoint();
        NetlistNode rInNode = new NetlistNode("a");
        NetlistNode rOutNode = new NetlistNode("b");
        NetlistNode lineOutNode = new NetlistNode("c");
        SimulationComponent line = new TwoPointLineObject();
        resistor.setNode(rIn, rInNode);
        resistor.setNode(rOut, rOutNode);
        line.setNode(rOut, rOutNode);
        line.setNode(lineOut, lineOutNode);
        rInNode.getSimulationComponentList().add(resistor);
        rOutNode.getSimulationComponentList().add(resistor);
        rOutNode.getSimulationComponentList().add(line);
        lineOutNode.getSimulationComponentList().add(line);
        netlist.getComponentList().add(resistor);
        netlist.getComponentList().add(line);
        netlist.getNodeList().add(rInNode);
        netlist.getNodeList().add(rOutNode);
        netlist.getNodeList().add(lineOutNode);

        SimulationUtilities.exchangeNodes(rOutNode, lineOutNode);
        assertTrue(resistor.getNodes().contains(rInNode));
        assertTrue(!resistor.getNodes().contains(rOutNode));
        assertTrue(resistor.getNodes().contains(lineOutNode));

    }


}