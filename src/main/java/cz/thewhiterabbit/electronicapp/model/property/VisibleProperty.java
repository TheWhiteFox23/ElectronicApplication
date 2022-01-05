package cz.thewhiterabbit.electronicapp.model.property;

import javafx.beans.property.Property;

public class VisibleProperty<T extends Property> {
        private String name;
        private T linkedProperty; //TODO add factory to create appropriate property
        private ComponentPropertyType type;
        private String unit;
        private String[] values;

        public VisibleProperty(PropertyDialogField propertyDialogField, T linkedProperty){
            this.name = propertyDialogField.name();
            this.linkedProperty = linkedProperty;
            this.type = propertyDialogField.type();
            this.unit = propertyDialogField.unit();
            this.values = propertyDialogField.values();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getLinkedProperty() {
            return linkedProperty;
        }

        public void setLinkedProperty(T property) {
            this.linkedProperty = property;
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

