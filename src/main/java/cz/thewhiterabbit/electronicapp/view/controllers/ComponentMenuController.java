package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.components.Category;
import cz.thewhiterabbit.electronicapp.model.components.Component;
import cz.thewhiterabbit.electronicapp.view.components.CategoryButton;
import cz.thewhiterabbit.electronicapp.view.components.ComponentButton;

import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.List;

public class ComponentMenuController {
    @FXML private HBox componentsBox;
    @FXML private HBox categoriesBox;
    @FXML private Button simulationButton;

    private final HashMap<Category, CategoryButton> categoriesMap = new HashMap<>();


    @FXML private void initialize(){
        initMap();
        fillMap();
        setActive(categoriesMap.get(Category.R_C_L));
        simulationButton.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new MenuEvent(MenuEvent.SWITCH_MODE_SIMULATION));
        });
    }

    private void initMap(){
        for(Category c : Category.values()){
            if(c != Category.NON_VISIBLE){
                CategoryButton button = new CategoryButton(c.getText());
                addCategory(c, button);
            }
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
            if(c.getCategory() != Category.NON_VISIBLE){
                categoriesMap.get(c.getCategory()).getComponents().add(new ComponentButton(c.getType(), c));
            }
        }
    }

}
