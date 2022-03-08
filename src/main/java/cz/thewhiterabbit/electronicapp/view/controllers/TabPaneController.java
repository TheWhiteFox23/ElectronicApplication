package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentManager;
import cz.thewhiterabbit.electronicapp.view.components.TabButton;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;


public class TabPaneController {
    @FXML private Button newFile;
    @FXML private HBox tabPane;

    private final HashMap<TabButton, Document> documentHashMap = new HashMap<>();
    private final EventAggregator eventAggregator = GUIEventAggregator.getInstance();

    @FXML private void initialize(){
        registerHandlers();

        newFile.setOnAction(e -> {
            MenuEvent menuEvent = new MenuEvent(MenuEvent.NEW_DOCUMENT);
            GUIEventAggregator.getInstance().fireEvent(menuEvent);
        });
    }

    private void registerHandlers(){
        eventAggregator.addEventHandler(DocumentManager.DocumentManagerEvent.DOCUMENT_OPENED, h->{
            Document document = ((DocumentManager.DocumentManagerEvent)h).getDocument();
            onDocumentCreated(document);
        });
        eventAggregator.addEventHandler(DocumentManager.DocumentManagerEvent.DOCUMENT_CLOSED, h->{
            Document document = ((DocumentManager.DocumentManagerEvent)h).getDocument();
            onDocumentRemoved(document);
        });
        eventAggregator.addEventHandler(DocumentManager.DocumentManagerEvent.ACTIVE_DOCUMENT_CHANGED, h->{
            Document document = ((DocumentManager.DocumentManagerEvent)h).getDocument();
            onActiveDocumentChanged(document);
        });
        eventAggregator.addEventHandler(DocumentManager.DocumentManagerEvent.DOCUMENT_RENAMED, h->{
            Document document = ((DocumentManager.DocumentManagerEvent)h).getDocument();
            onDocumentRenamed(document);
        });
    }

    private void onDocumentCreated(Document document){
        TabButton button = new TabButton();
        button.setText(document.getName());
        documentHashMap.put(button, document);
        tabPane.getChildren().add(tabPane.getChildren().size()-1, button);
        button.setOnClose(h ->{
            eventAggregator.fireEvent(new MenuEvent(MenuEvent.CLOSE_DOCUMENT, documentHashMap.get(button)));
        });
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, h->{
            eventAggregator.fireEvent(new MenuEvent(MenuEvent.CHANGE_ACTIVE_DOCUMENT, documentHashMap.get(button)));
        });
    }

    private void onDocumentRemoved(Document document) {
        TabButton button = getTabButton(document);
        if(button != null){
            documentHashMap.remove(button);
            tabPane.getChildren().remove(button);
        }
        if(tabPane.getChildren().size()==1){
            eventAggregator.fireEvent(new MenuEvent(MenuEvent.CLEAN_CANVAS));
        }
    }

    private void onActiveDocumentChanged(Document document){
        documentHashMap.keySet().forEach(b -> b.setSelected(false));
        TabButton button = getTabButton(document);
        if (button!=null) button.setSelected(true);
    }

    private void onDocumentRenamed(Document document){
        TabButton button = getTabButton(document);
        button.setText(document.getName());
    }

    private TabButton getTabButton(Document document) {
        AtomicReference<TabButton> toRemove = new AtomicReference<>();
        documentHashMap.forEach((button, doc)->{
            if(doc == document) toRemove.set(button);
        });
        return toRemove.get();
    }


}
