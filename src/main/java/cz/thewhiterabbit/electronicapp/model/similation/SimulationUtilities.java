package cz.thewhiterabbit.electronicapp.model.similation;

import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class SimulationUtilities {
    //private HashMap<String, List<ActivePoint>> activePointMap;

    public static Netlist createNetlist(Document document) {
        Netlist netlist = new Netlist();
        List<DocumentObject> documentObjectList = document.getDocumentObjects();
        //get all active point and create node map

        return netlist;
    }

    protected HashMap<String, List<ActivePoint>> getActivePointMap(List<DocumentObject> documentObjects){
        int activePointCount = 0;
        HashMap<String, List<ActivePoint>> activePointMap = new HashMap<>();
        Stack<DocumentObject> stack = new Stack();
        stack.addAll(documentObjects);
        while(!stack.empty()){
            DocumentObject documentObject = stack.pop();
            documentObject.getChildrenList().forEach(ch -> {
                stack.add((DocumentObject) ch);
            });
            if(documentObject instanceof ActivePoint){
                activePointCount++;
                ActivePoint activePoint = (ActivePoint)documentObject;
                String key = activePoint.getGridX()+"_"+ activePoint.getGridY();
                if(!activePointMap.containsKey(key))activePointMap.put(key, new ArrayList<>());
                activePointMap.get(key).add(activePoint);
            }
        }
        System.out.println(activePointCount);
        return activePointMap;
    }
}
