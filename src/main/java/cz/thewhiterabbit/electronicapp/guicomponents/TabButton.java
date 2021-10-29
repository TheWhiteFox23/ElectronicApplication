package cz.thewhiterabbit.electronicapp.guicomponents;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class TabButton extends HBox {
    @FXML
    private Text textField;

    public TabButton() {
        init();
    }

    public TabButton(String text) {
        init();
        textField.setText(text);
    }

    private void init() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("guicomponents/TabButton.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public StringProperty textProperty() {
        return textField.textProperty();
    }

    @FXML
    protected void closeTab() {
        System.out.println("CloseTab");
    }
}
