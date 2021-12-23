package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.objects.GeneralCanvasObject;
import cz.thewhiterabbit.electronicapp.model.objects.TwoPointLineObject;
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
                return init(new GeneralCanvasObject(rawObject));
            case "ACTIVE_POINT":
                return init(new ActivePoint(rawObject));
            case "LINE":
                return init(new TwoPointLineObject(rawObject));
            default:
                return null;
        }
    }

    private static DocumentObject init(DocumentObject documentObject){
        documentObject.getRawObject().getChildren().forEach(o -> {
            documentObject.getChildrenList().add(createDocumentObject(o));
        });
        documentObject.init();
        return documentObject;
    }
}
