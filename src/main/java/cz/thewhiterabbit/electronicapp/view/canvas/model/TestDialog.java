package cz.thewhiterabbit.electronicapp.view.canvas.model;

import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TestDialog extends Stage {
    private CanvasObject canvasObject;

    public TestDialog(){
        this.setWidth(200);
        this.setHeight(100);
        this.initStyle(StageStyle.UNDECORATED);
    }

    public CanvasObject getCanvasObject() {
        return canvasObject;
    }

    public void setCanvasObject(CanvasObject canvasObject) {
        this.canvasObject = canvasObject;
    }

    public void showDialog(CanvasObject canvasObject){
        this.canvasObject = canvasObject; 
    }
}
