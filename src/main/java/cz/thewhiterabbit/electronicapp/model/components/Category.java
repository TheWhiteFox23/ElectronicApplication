package cz.thewhiterabbit.electronicapp.model.components;

public enum Category {
    //PROBES_AE("Probes/AE"),
    R_C_L("category.r_c_l"),
    DIODES("category.diodes"),
    //TRANSFORMERS("Transformers"),
    //AMPLIFIERS("Amplifiers"),
    SOURCES("category.sources"),
    //SWITCHES("Switches"),
    TRANSISTORS("category.transistors"),
    //LOGIC("Logic"),
    MISC("category.misc"),
    NON_VISIBLE("Non Visible");

    private final String text;
    Category(String text){
        this.text = text;
    }
    public String getText() {
        return text;
    }
}
