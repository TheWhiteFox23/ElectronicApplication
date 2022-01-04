package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.objects.GeneralCanvasObject;
import cz.thewhiterabbit.electronicapp.model.objects.TwoPointLineObject;
import cz.thewhiterabbit.electronicapp.view.controllers.PropertiesPaneController;

public enum Component {
    RESISTOR("RESISTOR", Resistor.class, Category.R_C_L),
    TEST_COMPONENT("TEST_OBJECT", GeneralCanvasObject.class, Category.MISC), //TODO temporally
    ACTIVE_POINT("ACTIVE_POINT", ActivePoint.class, Category.MISC),//TODO temporally
    LINE("LINE", TwoPointLineObject.class, Category.MISC);//TODO temporally

    private final String type;
    private final Class clazz;
    private final Category category;

    Component(String type, Class<? extends DocumentObject> clazz, Category category){
        this.type = type;
        this.clazz = clazz;
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public Class getClazz() {
        return clazz;
    }

    public Category getCategory() {
        return category;
    }
}
