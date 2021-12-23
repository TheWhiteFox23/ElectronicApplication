package cz.thewhiterabbit.electronicapp.model.rawdocument;


public class TestRawDocument extends RawDocument{
    public TestRawDocument(String name) {
        super(name);
        for(int i = 0; i< 100; i+= 4){
            for(int j = 0; j< 100; j+=4){
                RawObject rawObject = new RawObject("TEST_OBJECT");
                rawObject.addProperty(new RawProperty("gridX", String.valueOf(i)));
                rawObject.addProperty(new RawProperty("gridY", String.valueOf(j)));
                rawObject.addProperty(new RawProperty("gridWidth", String.valueOf(2)));
                rawObject.addProperty(new RawProperty("gridHeight", String.valueOf(2)));
                rawObject.addProperty(new RawProperty("rotation", String.valueOf(0)));

                RawObject linkedObject = new RawObject( "ACTIVE_POINT");
                linkedObject.addProperty(new RawProperty("gridX", String.valueOf(i+1)));
                linkedObject.addProperty(new RawProperty("gridY", String.valueOf(j)));
                linkedObject.addProperty(new RawProperty("gridWidth", String.valueOf(2)));
                linkedObject.addProperty(new RawProperty("gridHeight", String.valueOf(2)));
                linkedObject.addProperty(new RawProperty("rotation", String.valueOf(0)));
                rawObject.getChildren().add(linkedObject);

                addObject(rawObject);
            }
        }

        RawObject rawObject = new RawObject( "LINE");
        rawObject.addProperty(new RawProperty("X1", String.valueOf(-10)));
        rawObject.addProperty(new RawProperty("Y1", String.valueOf(-10)));
        rawObject.addProperty(new RawProperty("X2", String.valueOf(-10)));
        rawObject.addProperty(new RawProperty("Y2", String.valueOf(0)));
        addObject(rawObject);
    }
}
