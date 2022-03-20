package cz.thewhiterabbit.electronicapp.view.components;

import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.view.events.EditControlEvent;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


public class GeneralCanvasContextMenu extends ContextMenu {
    private boolean selection;
    private boolean undo;
    private boolean redo;
    private boolean paste;

    public GeneralCanvasContextMenu(boolean selection, boolean undo, boolean redo, boolean paste){
        this.selection = selection;
        this.undo = undo;
        this.redo = redo;
        this.paste = paste;
        initMenu();
    }

    private void initMenu() {
        MenuItem copy = new MenuItem("Copy selection");
        copy.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.COPY));
        });
        if(!selection)copy.setDisable(true);
        getItems().add(copy);

        MenuItem paste = new MenuItem("Paste");
        paste.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.PASTE));
        });
        if(!this.paste)paste.setDisable(true);
        getItems().add(paste);

        MenuItem cut = new MenuItem("Cut selection");
        cut.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.CUT));
        });
        if(!selection)cut.setDisable(true);
        getItems().add(cut);

        getItems().add(new SeparatorMenuItem());

        MenuItem delete = new MenuItem("Delete selection");
        delete.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.DELETE));
        });
        if(!selection)delete.setDisable(true);
        getItems().add(delete);

        MenuItem rotateLeft = new MenuItem("Rotate selection left");
        rotateLeft.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.ROTATE_COUNTER_CLOCKWISE));
        });
        if(!selection)rotateLeft.setDisable(true);
        getItems().add(rotateLeft);

        MenuItem rotateRight = new MenuItem("Rotate selection right");
        rotateRight.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.ROTATE_CLOCKWISE));
        });
        if(!selection)rotateRight.setDisable(true);
        getItems().add(rotateRight);

        getItems().add(new SeparatorMenuItem());

        MenuItem undo = new MenuItem("Undo");
        undo.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.UNDO));
        });
        if(!this.undo)undo.setDisable(true);
        getItems().add(undo);

        MenuItem redo = new MenuItem("Redo");
        redo.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.REDO));
        });
        if(!this.redo)redo.setDisable(true);
        getItems().add(redo);

        getItems().add(new SeparatorMenuItem());

        MenuItem center = new MenuItem("Center Canvas");
        center.setOnAction(e->{
            //TODO add center canvas option
            System.out.println("Center Canvas");
        });
        getItems().add(center);

    }
}
