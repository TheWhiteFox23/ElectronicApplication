package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.model.components.Category;
import cz.thewhiterabbit.electronicapp.model.components.Component;
import cz.thewhiterabbit.electronicapp.view.components.CategoryButton;
import cz.thewhiterabbit.electronicapp.view.components.ComponentButton;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.List;

public class ComponentMenuController {
    @FXML private HBox componentsBox;
    @FXML private HBox categoriesBox;

    private final HashMap<Category, CategoryButton> categoriesMap = new HashMap<>();


    @FXML private void initialize(){
        initMap();
        fillMap();
        setActive(categoriesMap.get(Category.MISC));
    }

    private void initMap(){
        for(Category c : Category.values()){
            CategoryButton button = new CategoryButton(c.getText());
            addCategory(c, button);
        }
    }

    private void addCategory(Category c, CategoryButton button) {
        categoriesMap.put(c, button);
        categoriesBox.getChildren().add(button);
        button.setOnAction(e ->{
            setActive(button);
        });
    }

    private void setActive(CategoryButton button) {
        categoriesMap.values().forEach(b -> b.setActive(false));
        button.setActive(true);
        loadComponents(button.getComponents());
    }

    private void loadComponents(List<ComponentButton> components) {
        componentsBox.getChildren().clear();
        componentsBox.getChildren().addAll(components);
    }

    private void fillMap(){
        for(Component c : Component.values()){
            categoriesMap.get(c.getCategory()).getComponents().add(new ComponentButton(c.getType(), c));
        }
    }

}
