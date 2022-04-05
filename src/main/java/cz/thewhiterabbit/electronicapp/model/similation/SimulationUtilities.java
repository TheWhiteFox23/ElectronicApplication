package cz.thewhiterabbit.electronicapp.model.similation;

import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulationUtilities {
    //private HashMap<String, List<ActivePoint>> activePointMap;

    public static Netlist createNetlist(Document document) {
        Netlist netlist = new Netlist();
        List<DocumentObject> documentObjectList = document.getDocumentObjects();
        //get all active point and create node map

        return netlist;
    }

    protected HashMap<String, List<ActivePoint>> getActivePointMap(List<DocumentObject> documentObjects){
        HashMap<String, List<ActivePoint>> activePointMap = new HashMap<>();
        documentObjects.forEach(d ->{
            if(d instanceof ActivePoint){
                ActivePoint activePoint = (ActivePoint)d;
                String key = activePoint.getGridX()+"_"+ activePoint.getGridY();
                if(!activePointMap.containsKey(key))activePointMap.put(key, new ArrayList<>());
                activePointMap.get(key).add(activePoint);
            }
        });
        return activePointMap;
    }
}
