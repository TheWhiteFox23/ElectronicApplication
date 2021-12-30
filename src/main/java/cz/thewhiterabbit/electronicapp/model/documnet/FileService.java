package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawDocument;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.List;

//TODO REFACTORING

public class FileService {
    private final String OBJECTS = "objects";
    private final String NAME = "name";
    private final String VALUE = "value";
    private final String CHILDREN = "children";
    private final String PROPERTIES = "properties";


    public void save(Document document, File file) throws IOException {
        JSONObject jsonFile = createJSON(document);
        FileWriter writer = new FileWriter(file);
        writer.write(jsonFile.toJSONString());
        writer.flush();
        writer.close();
    }

    public JSONObject createJSON(Document document){
        JSONObject obj = new JSONObject();
        JSONArray objects = new JSONArray();
        document.getRawDocument().getObjects().forEach(o -> {
            objects.add(rawObjectToJason(o));
        });
        obj.put(OBJECTS, objects);
        obj.put(NAME, document.getName());
        return obj;
    }

    private JSONObject rawObjectToJason(RawObject rawObject){
        JSONObject object = new JSONObject();

        //properties
        JSONArray propertiesArray = new JSONArray();
        List<RawProperty> properties = rawObject.getProperties();
        for(int i = 0; i< properties.size(); i++){
            RawProperty property = properties.get(i);
            JSONObject jsonProperty = new JSONObject();
            jsonProperty.put(NAME, property.getName());
            jsonProperty.put(VALUE, property.getValue());
            propertiesArray.add(jsonProperty);
        }
        //children
        JSONArray childrenArray = new JSONArray();
        List<RawObject> children = rawObject.getChildren();
        for(int i = 0; i< children.size(); i++){
            JSONObject child = rawObjectToJason(children.get(i));
            childrenArray.add(child);
        }
        object.put(CHILDREN, childrenArray);
        object.put(PROPERTIES, propertiesArray);
        return object;
    }

    public Document load(File file) throws IOException, ParseException {
        //TODO validation of the file and exception handling

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(file));
        JSONObject object = (JSONObject) obj;
        Object name = object.get(NAME);
        RawDocument document = new RawDocument(name.toString());
        JSONArray objects = (JSONArray) object.get(OBJECTS);
        for(int i = 0; i< objects.size(); i++){
            RawObject rawObject = parseRawObject(objects.get(i));
            document.getObjects().add(rawObject);
        }
        return new Document(document);
    }

    private RawObject parseRawObject(Object o){
        JSONObject jsonObject = (JSONObject) o;
        RawObject rawObject = new RawObject();
        initProperties(jsonObject, rawObject);
        initChildren(jsonObject, rawObject);
        return  rawObject;
    }

    private void initChildren(JSONObject jsonObject, RawObject rawObject) {
        JSONArray children = (JSONArray) jsonObject.get(CHILDREN);
        for(int i = 0; i<children.size(); i++){
            JSONObject object = ((JSONObject) children.get(i));
            RawObject child = parseRawObject(object);
            rawObject.getChildren().add(child);
        }
    }

    private void initProperties(JSONObject jsonObject, RawObject rawObject) {
        JSONArray properties = (JSONArray) jsonObject.get(PROPERTIES);
        for(int i = 0; i<properties.size(); i++){
            JSONObject object = ((JSONObject) properties.get(i));
            String propertyName = object.get(NAME).toString();
            String propertyValue = object.get(VALUE).toString();
            RawProperty property = new RawProperty(propertyName, propertyValue);
            rawObject.addProperty(property);
            //System.out.println(property.getValue() + " : " +property.getName());
        }
    }

}
