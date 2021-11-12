package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.Document;
import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.events.DocumentModelEvent;
import cz.thewhiterabbit.electronicapp.events.MenuEvent;
import cz.thewhiterabbit.electronicapp.events.TabPaneEvent;
import cz.thewhiterabbit.electronicapp.guicomponents.TabButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.HashMap;


public class TabPaneController {
    @FXML private Button newFile;
    @FXML private HBox tabPane;
    //logic
    private HashMap<Document, TabButton> buttonMap = new HashMap<>();

    /*
    Initialize listeners and eventHandlers
     */
    @FXML private void initialize(){
        newFile.setOnAction(e -> {
            MenuEvent menuEvent = new MenuEvent(MenuEvent.NEW_FILE);
            GUIEventAggregator.getInstance().fireEvent(menuEvent);
        });
        GUIEventAggregator.getInstance().registerHandler(DocumentModelEvent.DOCUMENT_OPENED, event -> {
            onDocumentOpened((DocumentModelEvent) event);
        });

        GUIEventAggregator.getInstance().registerHandler(DocumentModelEvent.DOCUMENT_CLOSED, event -> {
            onDocumentClosed((DocumentModelEvent) event);
        });

    }

    /**
     * Remove tabButton representing given document
     * @param event
     */
    private void onDocumentClosed(DocumentModelEvent event) {
        //Getting document
        DocumentModelEvent documentModelEvent = event;
        Document document = documentModelEvent.getDocument();

        //Creating tabButton
        TabButton button = buttonMap.get(document);
        tabPane.getChildren().remove(button);
        buttonMap.remove(document);
    }

    /**
     * Add new tabButton representing opened document
     * @param event
     */
    private void onDocumentOpened(DocumentModelEvent event) {
        //Getting document
        DocumentModelEvent documentModelEvent = event;
        Document document = documentModelEvent.getDocument();

        //Creating tabButton
        TabButton button = new TabButton();
        button.setOnAction(e ->{
            GUIEventAggregator.getInstance().fireEvent(new TabPaneEvent(TabPaneEvent.TAB_CLOSED, document));
        });
        HBox.setHgrow(button, Priority.ALWAYS);
        button.setDocument(documentModelEvent.getDocument());

        //adding to tabPane and buttonMap
        tabPane.getChildren().add(tabPane.getChildren().size()-1, button);
        buttonMap.put( documentModelEvent.getDocument(), button);
    }


}
