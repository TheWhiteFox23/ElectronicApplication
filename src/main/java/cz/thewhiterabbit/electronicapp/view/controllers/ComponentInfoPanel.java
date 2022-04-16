package cz.thewhiterabbit.electronicapp.view.controllers;


import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.components.Component;
import cz.thewhiterabbit.electronicapp.view.components.ComponentTreeItem;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import java.util.Stack;

public class ComponentInfoPanel extends BorderPane {
    @FXML
    Button closeButton;
    @FXML
    TreeView componentTreeView;
    @FXML
    WebView componentInfoWebView;

    @FXML
    private void initialize() {
        GUIEventAggregator.getInstance().addEventHandler(MenuEvent.SHOW_INFO_DIALOG, e -> {
            if (((MenuEvent) e).getComponent() != null) {
                setActiveComponent(((MenuEvent) e).getComponent());
            }
        });
        closeButton.setOnAction(e -> {
            GUIEventAggregator.getInstance().fireEvent(new MenuEvent(MenuEvent.HIDE_INFO_DIALOG));
        });
        componentTreeView.getSelectionModel().selectedItemProperty().addListener(l -> {
            if (componentTreeView.getSelectionModel().getSelectedItem() instanceof ComponentTreeItem) {
                String path = ((ComponentTreeItem) componentTreeView.getSelectionModel().getSelectedItem()).getHtmlPath();
                String url = App.class.getResource(path).toExternalForm();
                componentInfoWebView.getEngine().load(url);
            }

        });
    }

    public void setActiveComponent(Component component) {
        TreeItem item = getComponentTreeItem(componentTreeView.getRoot(), component);
        if (item != null) componentTreeView.getSelectionModel().select(item);
    }

    private TreeItem getComponentTreeItem(TreeItem treeItem, Component component) {
        Stack<TreeItem> stack = new Stack<>();
        stack.addAll(treeItem.getChildren());
        while (!stack.empty()) {
            TreeItem item = stack.pop();
            if (item instanceof ComponentTreeItem &&
                    ((ComponentTreeItem) item).getComponent() != null &&
                    ((ComponentTreeItem) item).getComponent() == component) {
                return item;
            } else {
                stack.addAll(item.getChildren());
            }
        }
        return null;
    }

}
