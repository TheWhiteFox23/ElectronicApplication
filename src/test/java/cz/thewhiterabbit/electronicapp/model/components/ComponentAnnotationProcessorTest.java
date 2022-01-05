package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.property.*;
import javafx.beans.property.*;
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
        void getPropertiesSimple() throws IllegalAccessException {
            Object o = new SimpleValidObject();
            List<VisibleProperty> properties = annotationProcessor.getProperties(o);
            Assertions.assertTrue(properties.size() == 1);
            VisibleProperty property = properties.get(0);
            Assertions.assertTrue(property.getName().equals("TestProperty"));
            Assertions.assertTrue(property.getType().equals(ComponentPropertyType.TEXT_FIELD));
            Assertions.assertTrue(property.getUnit().equals("testUnit"));
            Assertions.assertTrue(Arrays.equals(property.getValues(), new String[]{"value1", "value2"}));
        }

        @Test
        void getPropertiesMultiple() throws IllegalAccessException {
            Object o = new MultipleValidObject();
            List<VisibleProperty> properties = annotationProcessor.getProperties(o);
            Assertions.assertTrue(properties.size() == 2);
        }
    }





    @ComponentType
    private class SimpleValidObject {
        @PropertyDialogField(name = "TestProperty", type = ComponentPropertyType.TEXT_FIELD, unit = "testUnit", values = {"value1", "value2"})
        private final StringProperty stringPropertyValid = new SimpleStringProperty("Test");

        private final StringProperty stringPropertyInvalid = new SimpleStringProperty("Test");
    }

    @ComponentType
    private class MultipleValidObject {
        @PropertyDialogField(name = "TestProperty", type = ComponentPropertyType.TEXT_FIELD, unit = "testUnit", values = {"value1", "value2"})
        private final StringProperty stringPropertyValid = new SimpleStringProperty("Test");
        @PropertyDialogField(name = "TestProperty", type = ComponentPropertyType.TEXT_FIELD, unit = "testUnit", values = {"value1", "value2"})
        private final StringProperty stringPropertyInvalid = new SimpleStringProperty("Test");
    }

    private class InvalidObject{ }
}