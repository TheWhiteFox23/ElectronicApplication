package cz.thewhiterabbit.electronicapp.canvas;

import cz.thewhiterabbit.electronicapp.canvas.objects.LineObject;

public class CanvasEventManager {
    /**
     * Get full access to drawing area and manage its events
     * @param drawingCanvas
     */
    private DrawingCanvas drawingCanvas;
    //private EventAggregator canvasEventAggregator;

    //LINE DRAWING
    LineObject firstLine;
    LineObject secondLine;

    public CanvasEventManager(DrawingCanvas drawingCanvas){
        this.drawingCanvas = drawingCanvas;
        //canvasEventAggregator = CanvasEventAggregator.getInstance();

        /**** LOGIC ****/
        registerListeners();
    }

    private void registerListeners() {
        /*** LINE DRAWING ***/
        //registerLineDrawingListeners();
    }

    /*private void registerLineDrawingListeners(){
        //TODO REFACTOR
        canvasEventAggregator.addEventHandler(CanvasMouseEvent.ACTIVE_POINT_DRAGGED, h->{
            ActivePoint activePoint = (ActivePoint) ((CanvasMouseEvent)h).getObject();
            if(drawingCanvas.getCanvasLayout() instanceof GridModel){
                int coordinateStartX = activePoint.getGridX();
                int coordinateStartY = activePoint.getGridY();
                int coordinateFinishX = ((GridModel)drawingCanvas.getCanvasLayout()).getGridCoordinate(((CanvasMouseEvent) h).getX(),
                        ((GridModel) drawingCanvas.getCanvasLayout()).getOriginX());
                int coordinateFinishY = ((GridModel)drawingCanvas.getCanvasLayout()).getGridCoordinate(((CanvasMouseEvent) h).getY(),
                        ((GridModel) drawingCanvas.getCanvasLayout()).getOriginY());

                setLines(coordinateStartX, coordinateStartY, coordinateFinishX, coordinateFinishY);

                canvasEventAggregator.fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
                if(firstLine != null){
                    ((GridModel) drawingCanvas.getCanvasLayout()).updatePaintProperties(firstLine);
                    firstLine.paint(drawingCanvas.getCanvas().getGraphicsContext2D());
                }
                if(secondLine != null){
                    ((GridModel) drawingCanvas.getCanvasLayout()).updatePaintProperties(secondLine);
                    secondLine.paint(drawingCanvas.getCanvas().getGraphicsContext2D());
                }
            }

        });

        canvasEventAggregator.addEventHandler(CanvasMouseEvent.ACTIVE_POINT_DRAG_DROPPED, e->{
            if(firstLine != null) {
                drawingCanvas.getCanvasLayout().add(firstLine);
                tryFinalizeLine(firstLine);
                firstLine = null;
            }
            if(secondLine != null) {
                drawingCanvas.getCanvasLayout().add(secondLine);
                tryFinalizeLine(secondLine);
                secondLine = null;
            }


        });
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
            tryFinalizeLine(firstLine);
            firstLine = secondLine;
            secondLine = null;
        }else if (secondLine != null && secondLine.getOrientation() == orientation){
            tryFinalizeLine(secondLine);
            secondLine = null;
        }
    }

    private void tryFinalizeLine(LineObject lineObject) {
        try {
            this.secondLine.finalize();
        }catch (Throwable throwable){

        }
    }

    private void adjustLine(LineObject lineObject, int firstPoint, int secondPoint, int level) {
        int length = secondPoint - firstPoint;
        if(lineObject.getOrientation() == LineObject.Orientation.HORIZONTAL){
            lineObject.set((length<0? secondPoint:firstPoint), level,1, Math.abs(length));
        }else{
            lineObject.set(level,(length<0? secondPoint:firstPoint), Math.abs(length), 1);
        }
    }*/
}
