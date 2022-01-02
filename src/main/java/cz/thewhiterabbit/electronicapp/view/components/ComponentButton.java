package cz.thewhiterabbit.electronicapp.view.components;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.model.components.Component;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

public class ComponentButton extends VBox {
    private Label image = new Label("IMAGE"); //TODO replace with image
    private Label text;
    private Component component;

    public ComponentButton(String text, Component component){
        this.component = component;
        getStylesheets().add(App.class.getResource("stylesheets/style.css").toExternalForm());
        this.text = new Label(text);
        getChildren().addAll(image, this.text);
        initDragDetection(this);
    }

    private void initDragDetection(ComponentButton componentButton){
        componentButton.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Dragboard db = componentButton.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString(componentButton.component.getType());
                db.setContent(content);
                event.consume();
            }
        });
    }

}
