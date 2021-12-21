package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;

/**
 * Responsible for creating object corresponding to RawObjects
 */
public class DocumentObjectFactory {
    //TODO: add logging
    public static DocumentObject createDocumentObject(RawObject rawObject)
    {
        switch (rawObject.getType()){
            case "TEST_OBJECT":
                return init(new TestObject(rawObject));
            case "ACTIVE_POINT":
                return init(new TestActivePoint(rawObject));
            default:
                return null;
        }
    }

    private static DocumentObject init(DocumentObject documentObject){
        documentObject.getRawObject().getChildren().forEach(o -> {
            documentObject.getChildren().add(createDocumentObject(o));
        });
        return documentObject;
    }
}