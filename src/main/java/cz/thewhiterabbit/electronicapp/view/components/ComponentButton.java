package cz.thewhiterabbit.electronicapp.view.components;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.components.Component;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ComponentButton extends AnchorPane {
    private Label image = new Label("IMAGE"); //TODO replace with image
    //private Label text;
    private Tooltip tooltip;
    private Button info;
    private Component component;
    private Image icon;


    public ComponentButton(String text, Component component){
        //info button
        initInfoButton();
        //arrange components
        VBox holder = getHolder(text, component);

        holder.addEventHandler(MouseEvent.MOUSE_MOVED, e->{
            showTooltip(e);
        });
        holder.addEventHandler(MouseEvent.MOUSE_EXITED, e->{
            hideTooltip();
        });

        getChildren().addAll(holder, info);
        initDragDetection(this);
        initListeners();
    }

    private void initListeners() {
        addEventHandler(MouseEvent.MOUSE_ENTERED, h->{
            info.setVisible(true);
        });
        addEventHandler(MouseEvent.MOUSE_EXITED, h->{
            info.setVisible(false);
        });
    }

    private VBox getHolder(String text, Component component) {
        VBox holder = new VBox();
        this.component = component;
        getStylesheets().add(App.class.getResource("stylesheets/style.css").toExternalForm());
        //this.text = new Label(text);//TODO text to tooltip
        tooltip = new Tooltip(text);
        if(component.isImage()){
            icon = new Image(App.class.getResourceAsStream(component.getImagePath()));
            ImageView imageView = new ImageView(icon);
            double co = 50/ icon.getWidth();
            double height = icon.getHeight()*co;
            double width = icon.getWidth()*co;
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            holder.getChildren().addAll(imageView);
        }else{
            holder.getChildren().addAll(image);
        }
        return holder;
    }

    private void initInfoButton() {
        this.info = new Button();
        info.setVisible(false);
        info.setMaxSize(15 ,15);
        info.setMinSize(15,15);
        info.getStyleClass().add("info_button");
        info.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            GUIEventAggregator.getInstance().fireEvent(new MenuEvent(MenuEvent.SHOW_INFO_DIALOG, component));
        });
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

    private void hideTooltip() {
        tooltip.hide();
    }

    private void showTooltip(MouseEvent event) {
        Node n = (Node) event.getSource();
        tooltip.show(n, event.getScreenX() + 10, event.getScreenY() + 10);
    }

}
