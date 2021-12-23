package cz.thewhiterabbit.electronicapp.model.rawdocument;

import javafx.beans.property.*;

public class RawProperty extends SimpleStringProperty {
    private final SimpleStringProperty value;

    public RawProperty(String name, String value){
        this.value = new SimpleStringProperty(this, name, value);
    }

    public String getName() {
        return value.getName();
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}
