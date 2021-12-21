package cz.thewhiterabbit.electronicapp.view.components;

import cz.thewhiterabbit.electronicapp.App;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;



public class TabButton extends AnchorPane {
    private Label tabText;
    private Button closeButton;
    //private Document document;


    public TabButton()  {
        getStylesheets().add(App.class.getResource("stylesheets/tab-button.css").toExternalForm());
        initGraphics();
    }

    private void initGraphics(){
        tabText = new Label("new file");
        closeButton = new Button("x");

        setPrefWidth(200);

        AnchorPane.setBottomAnchor(tabText, 0d);
        AnchorPane.setTopAnchor(tabText, 0d);
        AnchorPane.setLeftAnchor(tabText, 0d);
        AnchorPane.setRightAnchor(tabText, 0d);
        AnchorPane.setRightAnchor(closeButton, 0d);
        AnchorPane.setBottomAnchor(closeButton, 0d);
        AnchorPane.setTopAnchor(closeButton, 0d);

        this.getChildren().addAll(tabText, closeButton);
    }


    public void setOnAction(EventHandler eventHandler){
        closeButton.setOnAction(eventHandler);
    }

}
