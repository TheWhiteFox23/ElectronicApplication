package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;

public abstract class DocumentCanvasObject extends CanvasObject {
    private DocumentObject documentObject;

    public DocumentCanvasObject(DocumentObject documentObject) {
        this.documentObject = documentObject;
    }

    public DocumentCanvasObject(double width, double height, DocumentObject documentObject) {
        super(width, height);
        this.documentObject = documentObject;
    }

    public DocumentCanvasObject(double locationX, double locationY, double width, double height, DocumentObject documentObject) {
        super(locationX, locationY, width, height);
        this.documentObject = documentObject;
    }

    public DocumentObject getDocumentObject() {
        return documentObject;
    }

    public void setDocumentObject(DocumentObject documentObject) {
        this.documentObject = documentObject;
    }
}
