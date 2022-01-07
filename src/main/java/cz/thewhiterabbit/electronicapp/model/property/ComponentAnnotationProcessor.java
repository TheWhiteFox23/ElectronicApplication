package cz.thewhiterabbit.electronicapp.model.property;

import javafx.beans.property.Property;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ComponentAnnotationProcessor {
    /**
     * Return if object is annotated with ComponentType annotation
     * @param object
     */
    public static boolean isComponent(Object object){
        return object.getClass().isAnnotationPresent(ComponentType.class);
    }

    /**
     * Return list of obtained from object annotations
     * @return
     */
    public static List<VisibleProperty> getProperties(Object object) throws IllegalAccessException {
        List<VisibleProperty> properties = new ArrayList<>();
        List<Field> fields = getAnnotatedFields(object, PropertyDialogField.class);
        for(Field f: fields){
            f.setAccessible(true);
            PropertyDialogField annotation = f.getAnnotation(PropertyDialogField.class);
            if(f.get(object) instanceof Property){
                var property = new VisibleProperty<>(annotation, (Property) f.get(object));
                properties.add(property);
            }
        }
        return properties;
    }

    public static List<Property> getMappingProperties(Object object) {
        List<Property> properties = new ArrayList<>();
        List<Field> fields = getAnnotatedFieldsRecursive(object,RawPropertyMapping.class );
        for(Field f: fields){
            f.setAccessible(true);
            try {
                if(f.get(object) instanceof Property){
                    properties.add((Property) f.get(object));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    private static List<Field> getAnnotatedFields(Object object, Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field f : fields){
            if(f.isAnnotationPresent(clazz)){
                fieldList.add(f);
            }
        }
        return fieldList;
    }

    private static List<Field> getAnnotatedFieldsRecursive(Object object, Class clazz){
        List<Field> fieldList = new ArrayList<>();
        Class<?> type = object.getClass();
        while (type != null) {
            Field[] fields = type.getDeclaredFields();
            for(Field f : fields){
                if(f.isAnnotationPresent(clazz)){
                    fieldList.add(f);
                }
            }
            type = type.getSuperclass();
        }
        return fieldList;
    }
}
