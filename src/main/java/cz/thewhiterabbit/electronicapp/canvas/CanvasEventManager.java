package cz.thewhiterabbit.electronicapp.canvas;

import cz.thewhiterabbit.electronicapp.events.CanvasDragEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasMoveOrigin;
import cz.thewhiterabbit.electronicapp.events.CanvasSelection;

import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

/**
 * Manages event from canvas and transform them into specific canvas events
 */
public class CanvasEventManager {
    private enum Type {
        MOVE_ORIGIN,
        SELECTION
    }
    MoveCrate moveOrigin = new MoveCrate(Type.MOVE_ORIGIN);
    MoveCrate selection = new MoveCrate(Type.SELECTION);


    public CanvasEventManager(Canvas canvas){
        registerListeners(canvas);
    }

    private void registerListeners(Canvas canvas) {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e->{
            if(e.isMiddleButtonDown()){
                moveOrigin.setValues(e.getX(), e.getY(), e.getX(), e.getY(),e.getX(), e.getY());
                canvas.fireEvent(constructEvent(CanvasMoveOrigin.START, moveOrigin));
            }else if(e.isPrimaryButtonDown()){
                selection.setValues(e.getX(), e.getY(), e.getX(), e.getY(),e.getX(), e.getY());
                canvas.fireEvent(constructEvent(CanvasSelection.START, selection));
            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->{
            if(e.isMiddleButtonDown()){
                moveOrigin.setValues(moveOrigin.x, moveOrigin.y, e.getX(), e.getY());
                canvas.fireEvent(constructEvent(CanvasMoveOrigin.MOVE, moveOrigin));
            }else if(e.isPrimaryButtonDown()){
                selection.setValues(selection.x, selection.y, e.getX(), e.getY());
                canvas.fireEvent(constructEvent(CanvasSelection.MOVE, selection));
            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e->{
            if(!e.isMiddleButtonDown() && moveOrigin.beginningX!=-1){
                moveOrigin.setValues(moveOrigin.x, moveOrigin.y, e.getX(), e.getY());
                canvas.fireEvent(constructEvent(CanvasMoveOrigin.FINISH, moveOrigin));
                moveOrigin.setValues(-1,-1,-1,-1,-1,-1);
            }else if(!e.isPrimaryButtonDown() && selection.beginningX!=-1){
                System.out.println();
                selection.setValues(selection.x, selection.y, e.getX(), e.getY());
                canvas.fireEvent(constructEvent(CanvasSelection.FINISH, selection));
                selection.setValues(-1,-1,-1,-1,-1,-1);
            }
        });
    }


     private <T extends CanvasDragEvent> T constructEvent(EventType<T> eventType, MoveCrate moveCrate){
        T event;
        switch (moveCrate.type){
            case SELECTION:
                 event = (T)new CanvasSelection(eventType);
                 break;
            case MOVE_ORIGIN:
                event = (T)new CanvasMoveOrigin(eventType);
                break;
            default:
                event = (T)new CanvasSelection(eventType);

        }
        event.setBeginningX(moveCrate.beginningX);
        event.setBeginningY(moveCrate.beginningY);
        event.setLastX(moveCrate.lastX);
        event.setLastY(moveCrate.lastY);
        event.setX(moveCrate.x);
        event.setY(moveCrate.y);

        return event;
     }


    class MoveCrate{
        Type type;
        double beginningX = -1;
        double beginningY = -1;
        double lastX = -1;
        double lastY = -1;
        double x = -1;
        double y = -1;

        public MoveCrate(Type type) {
            this.type = type;
        }

        public void setValues(double begX, double begY, double lastX, double lastY, double x, double y){
            this.beginningX = begX;
            this.beginningY = begY;
            this.lastX = lastX;
            this.lastY = lastY;
            this.x = x;
            this.y = y;
        }

        public void setValues(double lastX, double lastY, double x, double y){
            this.lastX = lastX;
            this.lastY = lastY;
            this.x = x;
            this.y = y;
        }

    }
}
