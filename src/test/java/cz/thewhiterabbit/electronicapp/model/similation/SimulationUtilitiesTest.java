package cz.thewhiterabbit.electronicapp.model.similation;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.documnet.FileService;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import javafx.scene.layout.StackPane;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class SimulationUtilitiesTest {
    Document document;

    @BeforeEach
    void setUp() {
        FileService service = new FileService();
        try {
           document = service.load(new File(App.class.getResource("prefab/test_change_5.aeon").getFile()));
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
        System.out.println("**************RAW DOCUMENT***********");
        Stack<RawObject> rawObjects = new Stack<>();
        rawObjects.addAll(document.getRawDocument().getObjects());
        while (!rawObjects.empty()){
            RawObject o = rawObjects.pop();
            o.getChildren().forEach(ch -> rawObjects.add(ch));
            System.out.println(o.getType());
        }
    }

    @Test
    void getActivePointMap() {
    }
}