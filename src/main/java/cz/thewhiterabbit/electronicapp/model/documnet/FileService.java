package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.components.Component;
import cz.thewhiterabbit.electronicapp.model.property.ComponentAnnotationProcessor;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawDocument;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import javafx.beans.property.Property;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//TODO REFACTORING

public class FileService {
    private final String OBJECTS = "objects";
    private final String NAME = "name";
    private final String VALUE = "value";
    private final String CHILDREN = "children";
    private final String PROPERTIES = "properties";

    private List<RawObject> invalidRawObjects = new ArrayList<>();

    public void save(Document document, File file) throws IOException {
        JSONObject jsonFile = createJSON(document);
        FileWriter writer = new FileWriter(file);
        writer.write(jsonFile.toJSONString());
        writer.flush();
        writer.close();
    }

    public JSONObject createJSON(Document document) {
        JSONObject obj = new JSONObject();
        JSONArray objects = new JSONArray();
        document.getRawDocument().getObjects().forEach(o -> {
            objects.add(rawObjectToJason(o));
        });
        obj.put(OBJECTS, objects);
        obj.put(NAME, document.getName());
        return obj;
    }

    private JSONObject rawObjectToJason(RawObject rawObject) {
        JSONObject object = new JSONObject();

        //properties
        JSONArray propertiesArray = new JSONArray();
        List<RawProperty> properties = rawObject.getProperties();
        for (int i = 0; i < properties.size(); i++) {
            RawProperty property = properties.get(i);
            JSONObject jsonProperty = new JSONObject();
            jsonProperty.put(NAME, property.getName());
            jsonProperty.put(VALUE, property.getValue());
            propertiesArray.add(jsonProperty);
        }
        //children
        JSONArray childrenArray = new JSONArray();
        List<RawObject> children = rawObject.getChildren();
        for (int i = 0; i < children.size(); i++) {
            JSONObject child = rawObjectToJason(children.get(i));
            childrenArray.add(child);
        }
        object.put(CHILDREN, childrenArray);
        object.put(PROPERTIES, propertiesArray);
        return object;
    }

    public Document load(File file) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(file));
        JSONObject object = (JSONObject) obj;
        invalidRawObjects.clear();
        return getDocument(object);
    }

    private Document getDocument(JSONObject object) throws ParseException {
        Object name = object.get(NAME);
        if(name == null)throw new ParseException(0);

        RawDocument document = new RawDocument(name.toString());
        JSONArray objects = (JSONArray) object.get(OBJECTS);
        if(objects == null)throw new ParseException(0);

        for (int i = 0; i < objects.size(); i++) {
            RawObject rawObject = parseRawObject(objects.get(i));
            if (isValid(rawObject)) {
                document.getObjects().add(rawObject);
            } else {
                invalidRawObjects.add(rawObject);
            }
        }
        return new Document(document);
    }


    private boolean isValid(RawObject rawObject) {
        try {
            DocumentObjectFactory.createDocumentObject(rawObject);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private RawObject parseRawObject(Object o) throws ParseException {
        JSONObject jsonObject = (JSONObject) o;
        RawObject rawObject = new RawObject();
        initProperties(jsonObject, rawObject);
        initChildren(jsonObject, rawObject);
        return rawObject;
    }

    private void initChildren(JSONObject jsonObject, RawObject rawObject) throws ParseException {
        JSONArray children = (JSONArray) jsonObject.get(CHILDREN);
        if(children == null)throw new ParseException(0);
        for (int i = 0; i < children.size(); i++) {
            JSONObject object = ((JSONObject) children.get(i));
            RawObject child = parseRawObject(object);
            rawObject.getChildren().add(child);
        }
    }

    private void initProperties(JSONObject jsonObject, RawObject rawObject) throws ParseException {
        JSONArray properties = (JSONArray) jsonObject.get(PROPERTIES);
        if(properties == null)throw new ParseException(0);

        for (int i = 0; i < properties.size(); i++) {
            JSONObject object = ((JSONObject) properties.get(i));
            String propertyName = object.get(NAME).toString();
            if(propertyName == null)throw new ParseException(0);

            String propertyValue = object.get(VALUE).toString();
            if(propertyValue == null)throw new ParseException(0);

            RawProperty property = new RawProperty(propertyName, propertyValue);
            rawObject.addProperty(property);
        }
    }

    public List<RawObject> getInvalidRawObjects() {
        return invalidRawObjects;
    }

    public Document correctCorruptedObjects(Document document, List<RawObject> rawObjects) {
        RawDocument rawDocument = document.getRawDocument();
        for (RawObject rawObject : rawObjects) {
            RawObject rawObjectToAdd = tryReconstruct(rawObject);
            if (rawObject != null) rawDocument.addObject(rawObjectToAdd);

        }
        return new Document(rawDocument);
    }

    private  RawObject tryReconstruct(RawObject rawObject) {
        DocumentObject documentObject = DocumentObjectFactory.createDocumentObject(Component.getComponent(rawObject.getType()));
        if (documentObject != null){
            List<Property> properties = ComponentAnnotationProcessor.getMappingProperties(documentObject);
            for (Property p: properties){
                try{
                    documentObject.getRawObject().getProperty(p.getName()).setValue(rawObject.getProperty(p.getName()).getValue());
                    //p.setValue(rawObject.getProperty(p.getName()).getValue());
                }catch (Exception e){
                    //System.out.println(e);//TODO log exception
                }
            }
        }
        return documentObject.toRawObject();
    }
}
