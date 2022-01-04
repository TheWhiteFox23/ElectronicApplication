package cz.thewhiterabbit.electronicapp.view.canvas.model;

import cz.thewhiterabbit.electronicapp.model.objects.GeneralCanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;

import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class CanvasModelTest {
    private CanvasModel canvasModel;
    @BeforeEach
    void setUp() {
        canvasModel = new CanvasModelImpl();
    }

    @AfterEach
    void tearDown() {
        canvasModel = null;
    }

    @Test
    void getSelectedObject() {
       fillModel(canvasModel);
       List<CanvasObject> toSelect = new ArrayList<>();
       canvasModel.getAll().forEach(o -> o.setSelected(false));
       for (int i = 0; i< 100; i+= 4){
           toSelect.add(canvasModel.getAll().get(i));
       }
       toSelect.forEach(o -> o.setSelected(true));
       List<CanvasObject> actuallySelected = canvasModel.getSelected();

       //ASSERT ACTUAL AND VALIDATION LIST HAS THE SAME SIZE
       Assertions.assertTrue(actuallySelected.size() == toSelect.size());

       //ASSERT ALL OBJECT FROM VALIDATION ARE IN THE ACTUAL
       for(CanvasObject o : toSelect){
           Assertions.assertTrue(actuallySelected.contains(o));
       }
    }

    private void fillModel(CanvasModel canvasModel){
        for(int i = 0; i< 100; i++){
            canvasModel.add(new GeneralCanvasObject());
        }
    }

    private void fillModelPriority(CanvasModel canvasModel){
        for(int i = 0; i< 100; i++){
            TestCanvasObject testCanvasObject = new TestCanvasObject();
            if(i<20){
                testCanvasObject.setPriority(CanvasModel.Priority.ALWAYS_ON_TOP);
            }else if(i<40){
                testCanvasObject.setPriority(CanvasModel.Priority.HIGH);
            }else if(i<60){
                testCanvasObject.setPriority(CanvasModel.Priority.NONE);
            }else if(i<80){
                testCanvasObject.setPriority(CanvasModel.Priority.LOW);
            }else if(i<100){
                testCanvasObject.setPriority(CanvasModel.Priority.ALWAYS_ON_BOTTOM);
            }
            canvasModel.add(testCanvasObject);
        }
    }

    /****
     * TEST
     * 1.addedObject has set model to "this" model
     * 2.eventAggregator is set to model eventAggregator
     * 3.all children of the object are added
     * ****/

    @Test
    void add() {
        GeneralCanvasObject parentObject = new GeneralCanvasObject();
        GeneralCanvasObject childObject1 = new GeneralCanvasObject();
        parentObject.addChildren(childObject1);
        canvasModel.add(parentObject);

        Assertions.assertTrue(canvasModel.containsObject(parentObject),
                "Model do not contains added object");
        Assertions.assertTrue(canvasModel.containsObject(childObject1),
                "Model do not contains child object of an added object");
        Assertions.assertTrue(parentObject.getParentModel()==canvasModel,
                "Object has invalid parentModel Object");
        Assertions.assertTrue(parentObject.getEventAggregator()==canvasModel.getInnerEventAggregator(),
                "Object has invalid eventAggregator Object");
    }

    @Test
    void getIndex() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        fillModelPriority(canvasModel);
        Method method = CanvasModel.class.getDeclaredMethod("getIndex", CanvasModel.Priority.class);
        method.setAccessible(true);
        int index = (int) method.invoke(canvasModel, CanvasModel.Priority.ALWAYS_ON_BOTTOM);
        Assertions.assertTrue(index == 20);
        index = (int) method.invoke(canvasModel, CanvasModel.Priority.LOW);
        Assertions.assertTrue(index == 40);
        index = (int) method.invoke(canvasModel, CanvasModel.Priority.NONE);
        Assertions.assertTrue(index == 60);
        index = (int) method.invoke(canvasModel, CanvasModel.Priority.HIGH);
        Assertions.assertTrue(index == 80);
        index = (int) method.invoke(canvasModel, CanvasModel.Priority.ALWAYS_ON_TOP);
        Assertions.assertTrue(index == 100);
    }


    class CanvasModelImpl extends CanvasModel{
        @Override
        public void updatePaintProperties(CanvasObject object) {

        }
    }

    class TestCanvasObject extends CanvasObject{
        @Override
        protected void doPaint(GraphicsContext gc) {
        }
    }
}
