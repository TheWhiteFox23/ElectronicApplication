package cz.thewhiterabbit.electronicapp.model.similation;

import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.objects.TwoPointLineObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationUtilities {
    //private HashMap<String, List<ActivePoint>> activePointMap;

    public static Netlist createNetlist(Document document) {
        Netlist netlist = new Netlist();
        List<DocumentObject> documentObjectList = document.getDocumentObjects();

        HashMap<NetlistNode, List<ActivePoint>> activePointsMap= new HashMap<>();
        getActivePointMap(documentObjectList).forEach((key, acl)->{
            NetlistNode node = new NetlistNode(key);
            activePointsMap.put(node, acl);
            acl.forEach(ac->{
                if(ac.get_probeActive()){
                    node.setProbe(true);
                    node.setProbeName(ac.get_probeName());
                }
                CanvasObject co = ac.getParent();
                if(co instanceof SimulationComponent){
                    SimulationComponent sc = (SimulationComponent) co;
                    sc.setNode(ac, node);
                    node.getSimulationComponentList().add(sc);
                    if(!netlist.getComponentList().contains(sc))netlist.getComponentList().add(sc);
                    if(!netlist.getNodeList().contains(node))netlist.getNodeList().add(node);
                }
            });
        });
        numberComponents(netlist);
        numberNodes(netlist);
        validateProbesNames(netlist);
        return netlist;
    }

    public static Netlist optimizeNetlist(Netlist netlist){
        Stack<SimulationComponent> stack = new Stack();
        stack.addAll(netlist.getComponentList());
        while (!stack.empty()){
            SimulationComponent component = stack.pop();
            if(component instanceof TwoPointLineObject){
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

    public static void numberComponents(Netlist netlist){
        AtomicInteger number = new AtomicInteger();
        netlist.getComponentList().forEach(component -> {
            component.setName(String.valueOf(number.get()));
            number.getAndIncrement();
        });
    }

    public static void exchangeNodes(NetlistNode toRemove, NetlistNode newNode){
        toRemove.getSimulationComponentList().forEach(c->{
            if(toRemove.isProbe()){
                newNode.setProbe(true);
                newNode.setProbeName(toRemove.getProbeName());
            }
            c.setNode(toRemove, newNode);
            newNode.getSimulationComponentList().add(c);
        });
    }

    public static void validateProbesNames(Netlist netlist){
        List<String> individualProbeNames = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        validateNodeProbes(netlist, individualProbeNames, count);
        validateComponentProbes(netlist, individualProbeNames, count);
    }

    private static void validateNodeProbes(Netlist netlist, List<String> individualProbeNames, AtomicInteger count) {
        netlist.getNodeList().forEach(n->{
            if(n.isProbe()){
                String name = n.getProbeName();
                if(!individualProbeNames.contains(name)){
                    individualProbeNames.add(name);
                }else{
                    while (individualProbeNames.contains(name+count)){
                        count.getAndIncrement();
                    }
                    n.setProbeName(name+count);
                    individualProbeNames.add(name+count);
                }
            }
        });
    }
    private static void validateComponentProbes(Netlist netlist, List<String> individualProbeNames, AtomicInteger count) {
        netlist.getComponentList().forEach(n->{
            if(n.isProbeActive()){
                String name = n.getProbeName();
                if(!individualProbeNames.contains(name)){
                    individualProbeNames.add(name);
                }else{
                    while (individualProbeNames.contains(name+count)){
                        count.getAndIncrement();
                    }
                    n.setProbeName(name+count);
                    individualProbeNames.add(name+count);
                }
            }
        });
    }

    public static void removeComponent(Netlist netlist, SimulationComponent component){
        netlist.getComponentList().remove(component);
        netlist.getNodeList().forEach(n->{
            n.getSimulationComponentList().remove(component);
        });
    }

    public static Netlist numberNodes(Netlist netlist){
        for(int i = 0; i< netlist.getNodeList().size(); i++){
            netlist.getNodeList().get(i).setName(String.valueOf("n"+(i+1)));
        }
        return netlist;
    }



    protected static HashMap<String, List<ActivePoint>> getActivePointMap(List<DocumentObject> documentObjects){
        HashMap<String, List<ActivePoint>> activePointMap = new HashMap<>();
        Stack<DocumentObject> stack = new Stack();
        stack.addAll(documentObjects);
        while(!stack.empty()){
            DocumentObject documentObject = stack.pop();
            documentObject.getChildrenList().forEach(ch -> {
                stack.add((DocumentObject) ch);
            });
            if(documentObject instanceof ActivePoint){
                ActivePoint activePoint = (ActivePoint)documentObject;
                String key = activePoint.getGridX()+"_"+ activePoint.getGridY();
                if(!activePointMap.containsKey(key))activePointMap.put(key, new ArrayList<>());
                activePointMap.get(key).add(activePoint);
            }
        }
        return activePointMap;
    }

    public SimulationFile createSimulationFile(Document document){

        return new SimulationFile();
    }
}
