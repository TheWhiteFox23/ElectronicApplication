package cz.thewhiterabbit.electronicapp.canvas.objects;


import cz.thewhiterabbit.electronicapp.canvas.model.GridModel;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasPaintEvent;
import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ActivePoint extends CanvasObject{

    private LineObject firstLine;
    private LineObject secondLine;

    /***** OVERRIDES *****/
    @Override
    public void paint(GraphicsContext gc) {
        double height = getHeight()*0.4;
        double locationX = getLocationX()-height/2;
        double locationY = getLocationY()-height/2;
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillOval(locationX, locationY, height, height);
    }

    @Override
    public boolean isInBounds(double x, double y) {
        double height = getHeight()*0.5;
        double locationX = getLocationX()-height/2;
        double locationY = getLocationY()-height/2;
        return ((x>=locationX && x<=locationX+height)&&(y>=locationY && y<= locationY+height));
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    protected void onObjectDropped(Event e) {
        e.consume();
        activePontDragDropped();
    }

    @Override
    protected void onObjectDragged(Event e) {
        e.consume();
        activePointDragged((CanvasMouseEvent) e);
    }


    private void activePontDragDropped() {
        if(firstLine != null) {
            //drawingCanvas.getCanvasLayout().add(firstLine);// insertLineObject
            firstLine = null;
        }
        if(secondLine != null) {
            //drawingCanvas.getCanvasLayout().add(secondLine);// insert LineObject
            secondLine = null;
        }
    }

    private void activePointDragged(CanvasMouseEvent h) {
        if(getParentModel() instanceof GridModel){
            GridModel model = (GridModel)getParentModel();

            int coordinateStartX = getGridX();
            int coordinateStartY = getGridY();
            int coordinateFinishX = model.getGridCoordinate(h.getX(), model.getOriginX());
            int coordinateFinishY = model.getGridCoordinate(h.getY(), model.getOriginY());

            setLines(coordinateStartX, coordinateStartY, coordinateFinishX, coordinateFinishY);

            getEventAggregator().fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));
            if(firstLine != null){
                model.updatePaintProperties(firstLine);
                firstLine.paint(getParentModel().getCanvas().getGraphicsContext2D());
            }
            if(secondLine != null){
               model.updatePaintProperties(secondLine);
               secondLine.paint(getParentModel().getCanvas().getGraphicsContext2D());
            }
        }
    }

    private void setLines(int activePointX, int activePointY, int cursorX, int cursorY){
        if(activePointX != cursorX){
            createAndSetLine(activePointX, cursorX, activePointY, cursorY, LineObject.Orientation.HORIZONTAL);
        }else{
            deleteAndAdjustOrder(LineObject.Orientation.HORIZONTAL);
        }

        if(activePointY != cursorY){
            createAndSetLine(activePointY, cursorY,activePointX, cursorX, LineObject.Orientation.VERTICAL);
        }else{
            deleteAndAdjustOrder(LineObject.Orientation.VERTICAL);
        }
    }

    private void createAndSetLine(int firstLinePoint, int secondLinePoint,int firsLineLevel, int secondLineLevel, LineObject.Orientation orientation) {
        if (firstLine == null){
            firstLine = new LineObject();
            firstLine.setOrientation(orientation);
            adjustLine(firstLine, firstLinePoint, secondLinePoint, firsLineLevel);
        }else if (firstLine.getOrientation() == orientation) {
            adjustLine(firstLine, firstLinePoint, secondLinePoint, firsLineLevel);
        }else if (secondLine == null)  {
            secondLine = new LineObject();
            secondLine.setOrientation(orientation);
            adjustLine(secondLine, firstLinePoint, secondLinePoint, secondLineLevel);
        }else if (secondLine.getOrientation() == orientation) {
            adjustLine(secondLine, firstLinePoint, secondLinePoint, secondLineLevel);
        }

    }

    private void deleteAndAdjustOrder(LineObject.Orientation orientation){
        if(firstLine != null && firstLine.getOrientation() == orientation){
            firstLine = secondLine;
            secondLine = null;
        }else if (secondLine != null && secondLine.getOrientation() == orientation){
            secondLine = null;
        }
    }


    private void adjustLine(LineObject lineObject, int firstPoint, int secondPoint, int level) {
        int length = secondPoint - firstPoint;
        if(lineObject.getOrientation() == LineObject.Orientation.HORIZONTAL){
            lineObject.set((length<0? secondPoint:firstPoint), level,1, Math.abs(length));
        }else{
            lineObject.set(level,(length<0? secondPoint:firstPoint), Math.abs(length), 1);
        }
    }
}
