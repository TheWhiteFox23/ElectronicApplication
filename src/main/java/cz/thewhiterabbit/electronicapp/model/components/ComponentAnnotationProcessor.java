package cz.thewhiterabbit.electronicapp.model.components;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ComponentAnnotationProcessor {
    /**
     * Return if object is annotated with ComponentType annotation
     * @param object
     */
    public boolean isComponent(Object object){
        return object.getClass().isAnnotationPresent(ComponentType.class);
    }

    /**
     * Return list of obtained from object annotations
     * @return
     */
    public List<AnnotationProperty> getProperties(Object object){
        List<AnnotationProperty> properties = new ArrayList<>();
        List<Field> fields = getAnnotatedFields(object);
        for(Field f: fields){
            ComponentProperty annotation = f.getAnnotation(ComponentProperty.class);
            properties.add(new AnnotationProperty(annotation, f));
        }
        return properties;
    }

    private List<Field> getAnnotatedFields(Object object) {
        List<Field> fieldList = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field f : fields){
            if(f.isAnnotationPresent(ComponentProperty.class)){
                fieldList.add(f);
            }
        }
        return fieldList;
    }
}
