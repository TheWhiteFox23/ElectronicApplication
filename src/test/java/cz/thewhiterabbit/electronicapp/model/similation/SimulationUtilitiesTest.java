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
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class SimulationUtilitiesTest {
    private Document test_circuit;
    private Document mapping_test_3;
    private Document mapping_test_2;
    private Document mapping_test;

    @BeforeEach
    void setUp() {
        FileService service = new FileService();
        try {
            test_circuit = service.load(new File("src/test/resources/test_circuit.aeon"));
            mapping_test_3 = service.load(new File("src/test/resources/mapping_test_3.aeon"));
            mapping_test_2 = service.load(new File("src/test/resources/mapping_test_2.aeon"));
            mapping_test = service.load(new File("src/test/resources/mapping_test.aeon"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createNetlist() {
    }

    @Nested
    class ActivePointMappingTest {
        @Test
        void getActivePointMap() {
            Map<String, List<ActivePoint>> activePointMap = new SimulationUtilities().getActivePointMap(test_circuit.getDocumentObjects());
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
            activePointMap.forEach((s, ac)->{
                Assert.assertTrue(stringCountCompare.get(s)==ac.size());
            });
        }

        @Test
        void getActivePointMap_2() {
            Map<String, List<ActivePoint>> activePointMap = new SimulationUtilities().getActivePointMap(mapping_test_2.getDocumentObjects());
            Map<String, Integer> stringCountCompare = new HashMap<>() {{
                put("14_2",2);
                put("0_0",2);
                put("14_0",2);
                put("2_0",2);
                put("0_2",2);
                put("12_2",2);
                put("3_0",2);
                put("11_2",2);
                put("2_2",2);
                put("12_0",2);
                put("3_2",2);
                put("5_0",2);
                put("11_0",2);
                put("6_0",2);
                put("5_2",2);
                put("6_2",2);
                put("8_0",2);
                put("9_0",2);
                put("8_2",2);
                put("9_2",2);
            }};
            activePointMap.forEach((s, ac)->{
                Assert.assertTrue(stringCountCompare.get(s)==ac.size());
            });
        }
    }

}