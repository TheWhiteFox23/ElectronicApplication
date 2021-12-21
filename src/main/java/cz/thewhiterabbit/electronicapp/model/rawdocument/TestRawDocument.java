package cz.thewhiterabbit.electronicapp.model.rawdocument;


public class TestRawDocument extends RawDocument{
    int id = 0;
    public TestRawDocument(String name) {
        super(name);
        for(int i = 0; i< 100; i+= 4){
            for(int j = 0; j< 100; j+=4){
                RawObject rawObject = new RawObject(String.valueOf(id), "TEST_OBJECT");
                rawObject.addProperty(new RawProperty("gridX", String.valueOf(i)));
                rawObject.addProperty(new RawProperty("gridY", String.valueOf(j)));
                rawObject.addProperty(new RawProperty("gridWidth", String.valueOf(2)));
                rawObject.addProperty(new RawProperty("gridHeight", String.valueOf(2)));
                addObject(rawObject);
                id++;
            }
        }
    }
}
