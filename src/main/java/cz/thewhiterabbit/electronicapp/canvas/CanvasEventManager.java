package cz.thewhiterabbit.electronicapp.canvas;

import cz.thewhiterabbit.electronicapp.events.CanvasEvent;
import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

/**
 * Manages event from canvas and transform them into specific canvas events
 */
public class CanvasEventManager {
    //Drag Event handling
    private double dragBeginX;
    private double dragBeginY;

    private double lastDragX;
    private double lastDragY;

    public CanvasEventManager(Canvas canvas){
        registerListeners(canvas);
    }

    private void registerListeners(Canvas canvas) {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e->{
            if(e.isMiddleButtonDown()){
                canvas.fireEvent(new CanvasEvent(CanvasEvent.MOVE_ORIGIN_START, e.getX(), e.getY()));
            }else if(e.isPrimaryButtonDown()){
                canvas.fireEvent(new CanvasEvent(CanvasEvent.SELECTION_START, e.getX(), e.getY()));
            }

        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->{
            if(e.isMiddleButtonDown()){
                canvas.fireEvent(new CanvasEvent(CanvasEvent.MOVE_ORIGIN, e.getX(), e.getY()));
            }else if(e.isPrimaryButtonDown()){
                canvas.fireEvent(new CanvasEvent(CanvasEvent.SELECTION_MOVE, e.getX(), e.getY()));
            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e->{
            //todo add logic to decide the right event based on drag event origin
            canvas.fireEvent(new CanvasEvent(CanvasEvent.MOVE_ORIGIN_FINISH, e.getX(), e.getY()));
            canvas.fireEvent(new CanvasEvent(CanvasEvent.SELECTION_FINISH, e.getX(), e.getY()));
        });
    }


}
