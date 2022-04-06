package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.components.Component;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Responsible for creating object corresponding to RawObjects
 */
public class DocumentObjectFactory {
    public static DocumentObject createDocumentObject(Component c) {
        try {
            final DocumentObject newInstance = (DocumentObject) c.getClazz().getConstructor().newInstance();
            return newInstance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO: add logging
    public static DocumentObject createDocumentObject(RawObject rawObject) {
        for (Component c : Component.values()) {
            if (c.getType().equals(rawObject.getType())) {
                try {
                    final DocumentObject newInstance = (DocumentObject) c.getClazz().getConstructor().newInstance();
                    newInstance.setRawObject(rawObject);
                    return init(newInstance);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static DocumentObject init(DocumentObject documentObject) {
        documentObject.getRawObject().getChildren().forEach(o -> {
            documentObject.getChildrenList().add(createDocumentObject(o));
        });
        documentObject.getChildrenList().forEach(ch->{
            ch.setParent(documentObject);
        });
        documentObject.init();
        return documentObject;
    }


}
