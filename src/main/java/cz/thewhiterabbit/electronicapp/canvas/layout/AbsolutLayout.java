package cz.thewhiterabbit.electronicapp.canvas.layout;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import javafx.scene.canvas.Canvas;


public class AbsolutLayout extends CanvasLayout{
    public AbsolutLayout(Canvas canvas, EventAggregator eventAggregator){
        super(canvas, eventAggregator);
    }

    @Override
    protected void updatePaintProperties(CanvasObject object) {
        AbsolutLayoutProperties properties = (AbsolutLayoutProperties) object.getLayoutProperties();
        object.setHeight(properties.getHeight());
        object.setWidth(properties.getWidth());
        object.setLocationX(properties.getLocationX());
        object.setLocationY(properties.getLocationY());
    }
}
