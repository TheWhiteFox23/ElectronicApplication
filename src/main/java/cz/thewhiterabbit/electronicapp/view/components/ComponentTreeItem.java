package cz.thewhiterabbit.electronicapp.view.components;

import cz.thewhiterabbit.electronicapp.model.components.Component;
import javafx.scene.control.TreeItem;

public class ComponentTreeItem extends TreeItem {
    private String  component;
    private String htmlPath;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getHtmlPath() {
        return htmlPath;
    }

    public void setHtmlPath(String htmlPath) {
        this.htmlPath = htmlPath;
    }
}
