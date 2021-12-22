package cz.thewhiterabbit.electronicapp.model.rawdocument;

import java.util.List;

public interface RawObject {
    public String getId();
    public String getType();
    public void addProperty(RawProperty rawProperty);
    public RawProperty getProperty(String name);
    public List<RawProperty> getProperties();
    public List<RawObjectImpl> getChildren();
}
