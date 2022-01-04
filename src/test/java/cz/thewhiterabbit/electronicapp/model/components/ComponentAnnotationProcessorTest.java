package cz.thewhiterabbit.electronicapp.model.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

class ComponentAnnotationProcessorTest {
    private ComponentAnnotationProcessor annotationProcessor;

    @BeforeEach
    void setUp() {
        annotationProcessor = new ComponentAnnotationProcessor();
    }

    @AfterEach
    void tearDown() {
        annotationProcessor = null;
    }

    @Nested
    class isComponent{
        @Test
        void isComponentValid() {
            Assertions.assertTrue(annotationProcessor.isComponent(new SimpleValidObject()));
        }

        @Test
        void isComponentInvalid() {
            Assertions.assertFalse(annotationProcessor.isComponent(new InvalidObject()));
        }
    }

    @Nested
    class getProperties{
        @Test
        void getPropertiesSimple() {
            Object o = new SimpleValidObject();
            List<AnnotationProperty> properties = annotationProcessor.getProperties(o);
            Assertions.assertTrue(properties.size() == 1);
            AnnotationProperty property = properties.get(0);
            Assertions.assertTrue(property.getName().equals("TestProperty"));
            Assertions.assertTrue(property.getType().equals(ComponentPropertyType.TEXT));
            Assertions.assertTrue(property.getUnit().equals("testUnit"));
            Assertions.assertTrue(Arrays.equals(property.getValues(), new String[]{"value1", "value2"}));
        }

        @Test
        void getPropertiesMultiple() {
            Object o = new MultipleValidObject();
            List<AnnotationProperty> properties = annotationProcessor.getProperties(o);
            Assertions.assertTrue(properties.size() == 2);
        }
    }





    @ComponentType
    private class SimpleValidObject {
        @ComponentProperty(name = "TestProperty", type = ComponentPropertyType.TEXT, unit = "testUnit", values = {"value1", "value2"})
        private final StringProperty stringPropertyValid = new SimpleStringProperty("Test");

        private final StringProperty stringPropertyInvalid = new SimpleStringProperty("Test");
    }

    @ComponentType
    private class MultipleValidObject {
        @ComponentProperty(name = "TestProperty", type = ComponentPropertyType.TEXT, unit = "testUnit", values = {"value1", "value2"})
        private final StringProperty stringPropertyValid = new SimpleStringProperty("Test");
        @ComponentProperty(name = "TestProperty", type = ComponentPropertyType.TEXT, unit = "testUnit", values = {"value1", "value2"})
        private final StringProperty stringPropertyInvalid = new SimpleStringProperty("Test");
    }

    private class InvalidObject{ }
}