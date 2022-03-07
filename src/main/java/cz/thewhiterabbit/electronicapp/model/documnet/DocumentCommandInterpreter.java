package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import cz.thewhiterabbit.electronicapp.utilities.ValueValidator;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;

/**
 * Interprets incoming command
 */
public class DocumentCommandInterpreter {
    private Document parentDocument;

    public DocumentCommandInterpreter(Document document){
        this.parentDocument = document;
    }

    /**
     * Apply changes represented by given command
     * @param drawingAreaEvent
     */
    public void interpret(DrawingAreaEvent drawingAreaEvent){
        if (DrawingAreaEvent.OBJECT_DELETED.equals(drawingAreaEvent.getEventType())) {
            onObjectDeleted(drawingAreaEvent);
        }else if(DrawingAreaEvent.OBJECT_ADDED.equals(drawingAreaEvent.getEventType())){
            onObjectAdded(drawingAreaEvent);
        }else if(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE.equals(drawingAreaEvent.getEventType())){
            onPropertyChange(drawingAreaEvent);
        }else if(DrawingAreaEvent.SELECTION_CHANGED.equals(drawingAreaEvent.getEventType())){
            onSelectionChanged(drawingAreaEvent);
        }
    }

    /**
     * Apply reversed changes represented by given command
     * @param drawingAreaEvent
     */
    public void interpretReverse(DrawingAreaEvent drawingAreaEvent){
        if (DrawingAreaEvent.OBJECT_DELETED.equals(drawingAreaEvent.getEventType())) {
            onObjectDeletedReverse(drawingAreaEvent);
        }else if(DrawingAreaEvent.OBJECT_ADDED.equals(drawingAreaEvent.getEventType())){
            onObjectAddedReverse(drawingAreaEvent);
        }else if(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE.equals(drawingAreaEvent.getEventType())){
            onPropertyChangeReverse(drawingAreaEvent);
        }else if(DrawingAreaEvent.SELECTION_CHANGED.equals(drawingAreaEvent.getEventType())){
            onSelectionChangedReverse(drawingAreaEvent);
        }
    }


    private void onPropertyChange(DrawingAreaEvent event) {
        DocumentObject o = (DocumentObject) event.getCanvasObject();
        RawProperty p = o.getProperty(event.getProperty().getName());
        setRawPropertyIfValid(p, event.getProperty(), event.getNewValue());
    }

    private void setRawPropertyIfValid(RawProperty rawProperty, Property linkedProperty, Object value){
        if(rawProperty != null && ValueValidator.validateProperty(linkedProperty, value)){
            rawProperty.setValue(value.toString());
            //todo update object
        }
    }

    private void onObjectAdded(DrawingAreaEvent event) {
        parentDocument.add((DocumentObject) (event.getCanvasObject()));
    };

    private void onObjectDeleted(DrawingAreaEvent event) {
        DocumentObject o = (DocumentObject) event.getCanvasObject();
        if(o!= null){
            parentDocument.remove(o);
        }
    };

    private void onObjectAddedReverse(DrawingAreaEvent event){
        onObjectDeleted(event);
    }

    private void onObjectDeletedReverse(DrawingAreaEvent event){
        onObjectAdded(event);
    }

    private void onPropertyChangeReverse(DrawingAreaEvent event){
        DocumentObject o = (DocumentObject) event.getCanvasObject();
        RawProperty p = o.getProperty(event.getProperty().getName());
        setRawPropertyIfValid(p, event.getProperty(), event.getOldValue());
    }

    private void onSelectionChanged(DrawingAreaEvent event){
        event.getCanvasObject().setSelected(event.getNewSelected());
    }

    private void onSelectionChangedReverse(DrawingAreaEvent event){
        event.getCanvasObject().setSelected(event.getOldSelected());
    }
}
