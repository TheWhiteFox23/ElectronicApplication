package cz.thewhiterabbit.electronicapp.model.rawdocument;


public class TestRawDocument extends RawDocument{
    int id = 0;
    public TestRawDocument(String name) {
        super(name);
        for(int i = 0; i< 100; i+= 4){
            for(int j = 0; j< 100; j+=4){
                RawObjectImpl rawObjectImpl = new RawObjectImpl(String.valueOf(id), "TEST_OBJECT");
                rawObjectImpl.addProperty(new RawProperty("gridX", String.valueOf(i)));
                rawObjectImpl.addProperty(new RawProperty("gridY", String.valueOf(j)));
                rawObjectImpl.addProperty(new RawProperty("gridWidth", String.valueOf(2)));
                rawObjectImpl.addProperty(new RawProperty("gridHeight", String.valueOf(2)));
                id++;

                RawObjectImpl linkedObject = new RawObjectImpl(String.valueOf(id), "ACTIVE_POINT");
                linkedObject.addProperty(new RawProperty("gridX", String.valueOf(i+1)));
                linkedObject.addProperty(new RawProperty("gridY", String.valueOf(j)));
                linkedObject.addProperty(new RawProperty("gridWidth", String.valueOf(2)));
                linkedObject.addProperty(new RawProperty("gridHeight", String.valueOf(2)));
                rawObjectImpl.getChildren().add(linkedObject);
                id++;

                addObject(rawObjectImpl);
            }
        }
    }
}
