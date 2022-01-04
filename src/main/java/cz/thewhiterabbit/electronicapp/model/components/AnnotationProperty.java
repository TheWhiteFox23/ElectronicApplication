package cz.thewhiterabbit.electronicapp.model.components;

import java.lang.reflect.Field;

public class AnnotationProperty {
        private String name;
        private Object property; //TODO add factory to create appropriate property
        private ComponentPropertyType type;
        private String unit;
        private String[] values;

        public AnnotationProperty(ComponentProperty componentProperty, Field field){
            this.name = componentProperty.name();
            this.property = field;
            this.type = componentProperty.type();
            this.unit = componentProperty.unit();
            this.values = componentProperty.values();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getProperty() {
            return property;
        }

        public void setProperty(Object property) {
            this.property = property;
        }

        public ComponentPropertyType getType() {
            return type;
        }

        public void setType(ComponentPropertyType type) {
            this.type = type;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String[] getValues() {
            return values;
        }

        public void setValues(String[] values) {
            this.values = values;
        }
    }

