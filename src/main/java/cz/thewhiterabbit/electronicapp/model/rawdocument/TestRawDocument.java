package cz.thewhiterabbit.electronicapp.model.rawdocument;


import cz.thewhiterabbit.electronicapp.model.objects.GeneralCanvasObject;

public class TestRawDocument extends RawDocument{
    public TestRawDocument(String name) {
        super(name);
        for(int i = 0; i< 100; i+= 4){
            for(int j = 0; j< 100; j+=4){
                GeneralCanvasObject generalCanvasObject = new GeneralCanvasObject();
                generalCanvasObject.setGridX(i);
                generalCanvasObject.setGridY(j);
                generalCanvasObject.setGridHeight(2);
                generalCanvasObject.setGridWidth(2);
                generalCanvasObject.setRotation(0);
                RawObject rawObject = generalCanvasObject.toRawObject();
                addObject(rawObject);

               /* RawObject rawObject = new RawObject("TEST_OBJECT");
                rawObject.addProperty(new RawProperty("gridX", String.valueOf(i)));
                rawObject.addProperty(new RawProperty("gridY", String.valueOf(j)));
                rawObject.addProperty(new RawProperty("gridWidth", String.valueOf(2)));
                rawObject.addProperty(new RawProperty("gridHeight", String.valueOf(2)));
                rawObject.addProperty(new RawProperty("rotation", String.valueOf(0)));
                addObject(rawObject);*/
            }
        }
        System.out.println("test");
    }
}
