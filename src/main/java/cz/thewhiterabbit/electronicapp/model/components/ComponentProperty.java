package cz.thewhiterabbit.electronicapp.model.components;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ComponentProperty {
    String name();
    ComponentPropertyType type();
    String unit() default "";
    String[] values() default {""};
}


