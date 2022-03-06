package cz.thewhiterabbit.electronicapp.view.components;

import cz.thewhiterabbit.electronicapp.App;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class CategoryButton extends Button {
    private boolean active = false;
    private final List<ComponentButton> components = new ArrayList<>();

    public CategoryButton(){
        init();
    }

    public CategoryButton(String text){
        init();
        setText(text);
    }

    private void init() {
        getStylesheets().add(App.class.getResource("stylesheets/style.css").toExternalForm());
        manageClassStyle(active);
        setOnAction(h ->{
            setActive(!active);
        });
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        manageClassStyle(active);
    }

    private void manageClassStyle(boolean selected){
        if(selected){
            getStyleClass().clear();
            getStyleClass().add("selected");
        }else{
            getStyleClass().clear();
            getStyleClass().add("deselected");
        }
    }

    public List<ComponentButton> getComponents() {
        return components;
    }
}
