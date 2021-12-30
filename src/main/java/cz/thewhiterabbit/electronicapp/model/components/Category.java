package cz.thewhiterabbit.electronicapp.model.components;

public enum Category {
    PROBES_AE("Probes/AE"),
    R_C_L("R,C,L"),
    DIODES("Diodes"),
    TRANSFORMERS("Transformers"),
    AMPLIFIERS("Amplifiers"),
    SOURCES("Sources"),
    SWITCHES("Switches"),
    TRANSISTORS("Transistors"),
    LOGIC("Logic"),
    MISC("Misc");

    private final String text;
    Category(String text){
        this.text = text;
    }
    public String getText() {
        return text;
    }
}
