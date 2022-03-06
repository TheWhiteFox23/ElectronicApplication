package cz.thewhiterabbit.electronicapp.view.components;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.model.components.Component;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

public class ComponentButton extends VBox {
    private Label image = new Label("IMAGE"); //TODO replace with image
    private Label text;
    private Component component;
    private Image icon;

    public ComponentButton(String text, Component component){
        this.component = component;
        getStylesheets().add(App.class.getResource("stylesheets/style.css").toExternalForm());
        this.text = new Label(text);
        if(component.isImage()){
            icon = new Image(App.class.getResourceAsStream(component.getImagePath()));
            ImageView imageView = new ImageView(icon);
            double co = 50/ icon.getWidth();
            double height = icon.getHeight()*co;
            double width = icon.getWidth()*co;
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            getChildren().addAll(imageView, this.text);
        }else{
            getChildren().addAll(image, this.text);
        }
        //getChildren().addAll(image, this.text);
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
