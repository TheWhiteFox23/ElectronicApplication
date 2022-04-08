package cz.thewhiterabbit.electronicapp.utilities;

import javafx.beans.property.*;

public class ValueValidator {
    //TODO temporal method
    public static boolean validateProperty(Property property, Object value){
        if(property instanceof IntegerProperty){
            try {
                Integer.parseInt(value.toString());
                return true;
            }catch (Exception e){
                return false;
            }
        }else if(property instanceof DoubleProperty){
            try {
                Double.parseDouble(value.toString());
                return true;
            }catch (Exception e){
                return false;
            }
        }else if(property instanceof FloatProperty){
            try {
                Float.parseFloat(value.toString());
                return true;
            }catch (Exception e){
                return false;
            }
        }else if(property instanceof BooleanProperty){
            try {
                Boolean.parseBoolean(value.toString());
                return true;
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }
}
