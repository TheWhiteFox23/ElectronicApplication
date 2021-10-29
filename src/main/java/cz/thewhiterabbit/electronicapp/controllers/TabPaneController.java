package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.Document;
import cz.thewhiterabbit.electronicapp.DocumentManager;
import cz.thewhiterabbit.electronicapp.DocumentManagerListener;
import cz.thewhiterabbit.electronicapp.listeners.TabPaneListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabPaneController {
    @FXML private Button newFile;
    @FXML private HBox tabPane;
    //logic
    private DocumentManager documentManager = DocumentManager.getInstance();
    private HashMap<Document, Button> buttonMap = new HashMap<>();

    private List<TabPaneListener> listeners = new ArrayList<>();
    public void addTabPaneListener(TabPaneListener listener){
        this.listeners.add(listener);
    }

    @FXML private void initialize(){
        newFile.setOnAction(e -> listeners.forEach(a -> a.onNewFileClicked(e)));
        documentManager.addDocumentManagerListener(() -> onDocumentModelChange());
    }

    private void onDocumentModelChange(){
        List<Document> documentList = documentManager.getDocumentList();

        //addNewDocuments
        documentList.forEach(d -> {
            if(!buttonMap.containsKey(d)){
                Button button = new Button(d.getFileName());
                buttonMap.put(d, button);
                tabPane.getChildren().add(button);
            }
        });


        //closeOldDocuments
        List<Document> documentsToRemove = new ArrayList<>();
        buttonMap.forEach((d, b) ->{
            if(!documentList.contains(d)){
                tabPane.getChildren().remove(b);
                documentsToRemove.add(d);
            }
        });
        documentsToRemove.forEach(d ->{
            buttonMap.remove(d);
        });
    }


}
