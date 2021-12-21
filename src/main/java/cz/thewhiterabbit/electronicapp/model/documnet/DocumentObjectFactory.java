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
                return new TestObject(rawObject);
            default:
                return null;
        }
    }
}
