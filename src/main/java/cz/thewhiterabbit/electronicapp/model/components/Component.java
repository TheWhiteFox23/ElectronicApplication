package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.objects.GeneralCanvasObject;
import cz.thewhiterabbit.electronicapp.model.objects.TwoPointLineObject;
import javafx.fxml.FXML;


public enum Component {
    RESISTOR("RESISTOR", Resistor.class, Category.R_C_L, true, "img/resistor_IEEE@0.5x.png"),
    CAPACITOR("CAPACITOR", Capacitor.class, Category.R_C_L, true, "img/capacitor@0.5x.png"),
    INDUCTOR("CONDUCTOR", Inductor.class, Category.R_C_L, true, "img/inductor@0.5x.png"),
    COUPLED_INDUCTOR("COUPLED_CONDUCTOR", CoupledInductor.class, Category.R_C_L, true, "img/coupled_inductor@0.5x.png"),
    GROUND("GROUND", Ground.class, Category.SOURCES, true, "img/ground@0.5x.png"),
    VOLTAGE_SOURCE("VOLTAGE_SOURCE", VoltageSource.class, Category.SOURCES, true, "img/voltage_source@0.5x.png"),
    CURRANT_SOURCE("CURRANT_SOURCE", CurrentSource.class, Category.SOURCES, true, "img/current_source@0.5x.png"),
    DIODE("DIODE", Diode.class, Category.DIODES, true, "img/diode@0.5x.png"),
    TRANSISTOR_NPN("TRANSISTOR_NPN", TransistorNPN.class, Category.TRANSISTORS, true, "img/npn_transistor@0.5x.png"),
    TRANSISTOR_PNP("TRANSISTOR_PNP", TransistorPNP.class, Category.TRANSISTORS, true, "img/pnp_transistor@0.5x.png"),
    TIMER555("TIMER555", Timer555.class, Category.MISC, true, "img/timer555@0.5x.png"),
    TEST_COMPONENT("TEST_OBJECT", GeneralCanvasObject.class, Category.MISC), //TODO temporally
    ACTIVE_POINT("ACTIVE_POINT", ActivePoint.class, Category.MISC),//TODO temporally
    LINE("LINE", TwoPointLineObject.class, Category.MISC)
    ;//TODO temporally

    private final String type;
    private final Class clazz;
    private final Category category;
    private final boolean image;
    private final String imagePath;

    Component(String type, Class<? extends DocumentObject> clazz, Category category){
        this.type = type;
        this.clazz = clazz;
        this.category = category;
        this.image = false;
        this.imagePath = "";
    }

    Component(String type, Class<? extends DocumentObject> clazz, Category category, boolean image, String imagePath){
        this.type = type;
        this.clazz = clazz;
        this.category = category;
        this.image = image;
        this.imagePath = imagePath;

    }


    public String getType() {
        return type;
    }

    public boolean isImage() {
        return image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Class getClazz() {
        return clazz;
    }

    public Category getCategory() {
        return category;
    }

    public static Component getComponent(String type){
        for (Component c: Component.values()){
            if(c.getType().equals(type))return c;
        }
        return null;
    }
}
