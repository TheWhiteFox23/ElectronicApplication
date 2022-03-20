package cz.thewhiterabbit.electronicapp.view.components;

import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.events.EditControlEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class CanvasObjectContextMenu extends ContextMenu {
    CanvasObject canvasObject;

    public CanvasObjectContextMenu(CanvasObject canvasObject){
        this.canvasObject = canvasObject;
        initMenu();
    }

    private void initMenu() {
        MenuItem rotateLeft = new MenuItem("Rotate left");
        rotateLeft.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.ROTATE_COUNTER_CLOCKWISE, canvasObject));
        });
        getItems().add(rotateLeft);

        MenuItem rotateRight = new MenuItem("Rotate right");
        rotateRight.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.ROTATE_CLOCKWISE, canvasObject));
        });
        getItems().add(rotateRight);

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.DELETE, canvasObject));
        });
        getItems().add(delete);
    }
}
