package cz.thewhiterabbit.electronicapp.model.similation;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.documnet.FileService;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import javafx.beans.property.SimpleMapProperty;
import javafx.scene.layout.StackPane;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class SimulationUtilitiesTest {
    private Document document;

    @BeforeEach
    void setUp() {
        FileService service = new FileService();
        try {
           document = service.load(new File(App.class.getResource("prefab/mapping_test_3.aeon").getFile()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createNetlist() {
        System.out.println("**************DOCUMENT**************");
        Stack<DocumentObject> objects = new Stack<>();
        objects.addAll(document.getDocumentObjects());
        while (!objects.empty()){
            DocumentObject o = objects.pop();
            o.getChildrenList().forEach(ch-> {
                if(ch instanceof DocumentObject){
                    objects.add((DocumentObject) ch);
                }
            });
            System.out.println(o.getType());
        }
    }

    @Test
    void getActivePointMap() {
        Map<String, List<ActivePoint>> activePointMap = new SimulationUtilities().getActivePointMap(document.getDocumentObjects());
        activePointMap.forEach((s, activePoints) -> {
            System.out.println(s + " : " +activePoints.size());
        });
    }
}