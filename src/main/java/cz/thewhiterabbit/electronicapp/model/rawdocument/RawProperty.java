package cz.thewhiterabbit.electronicapp.model.rawdocument;

import javafx.beans.property.*;

public class RawProperty {

    private final String name;
    private final SimpleStringProperty value;

    public RawProperty(String name, String value){
        this.name = name;
        this.value = new SimpleStringProperty(value);
    }

    public String getName() {
        return name;
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
