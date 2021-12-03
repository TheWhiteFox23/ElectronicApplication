package cz.thewhiterabbit.electronicapp;

import cz.thewhiterabbit.electronicapp.canvas.layout.PropertiesListener;
import cz.thewhiterabbit.electronicapp.events.DocumentObjectCommand;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Basic document object, represents component or some part of the circuit (conductor, node)
 */
public class DocumentObject {
    private GUIEventAggregator guiEventAggregator = GUIEventAggregator.getInstance();

    /***** PROPERTIES *****/
    private final Map<String, Property> propertyMap = new HashMap<>();

    private ReadOnlyIntegerWrapper gridX;
    private ReadOnlyIntegerWrapper gridY;
    private ReadOnlyIntegerWrapper gridHeight;
    private ReadOnlyIntegerWrapper gridWidth;

    private int _gridX;
    private int _gridY;
    private int _gridHeight;
    private int _gridWidth;

    /**** CONSTRUCTORS ****/

    public DocumentObject() {
        this(0,0,0,0);
    }

    public DocumentObject(int _gridX, int _gridY, int _gridHeight, int _gridWidth) {
        this._gridX = _gridX;
        this._gridY = _gridY;
        this._gridHeight = _gridHeight;
        this._gridWidth = _gridWidth;
        registerListeners();
    }



    private void registerListeners() {
        guiEventAggregator.registerHandler(DocumentObjectCommand.CHANGE_PROPERTY, (event -> { //TODO handle by command manager
            DocumentObjectCommand command = (DocumentObjectCommand) event;
            if(command.getDocumentComponent() == this){
                propertyMap.get(command.fxProperty().getName()).setValue(command.getNewVal());
                propertiesChange();//todo -> replace with firing event through aggregator
            }
        }));
    }

    /**** GRID X ****/
    public final ReadOnlyIntegerProperty gridX(){
        if (this.gridX == null) {
            this.gridX = new ReadOnlyIntegerWrapper(this._gridX) {
                public Object getBean() {return DocumentObject.this;}
                public String getName() {return "gridX";}
            };
            propertyMap.put("gridX", this.gridX);
        }
        return this.gridX.getReadOnlyProperty();
    }

    /**** GRID Y ****/
    public final ReadOnlyIntegerProperty gridY(){
        if (this.gridY == null) {
            this.gridY = new ReadOnlyIntegerWrapper(this._gridY) {
                public Object getBean() {return DocumentObject.this;}
                public String getName() {return "gridY";}
            };
            propertyMap.put("gridY", this.gridY);
        }
        return this.gridY.getReadOnlyProperty();
    }

    /**** GRID HEIGHT ****/
    public final ReadOnlyIntegerProperty gridHeight(){
        if (this.gridHeight == null) {
            this.gridHeight = new ReadOnlyIntegerWrapper(this._gridHeight) {
                public Object getBean() {return DocumentObject.this;}
                public String getName() {return "gridHeight";}
            };
            propertyMap.put("gridHeight", this.gridHeight);
        }
        return this.gridHeight.getReadOnlyProperty();
    }

    /**** GRID WIDTH ****/
    public final ReadOnlyIntegerProperty gridWidth(){
        if (this.gridWidth == null) {
            this.gridWidth = new ReadOnlyIntegerWrapper(this._gridWidth) {
                public Object getBean() {return DocumentObject.this;}
                public String getName() {return "gridWidth";}
            };
            propertyMap.put("gridWidth", this.gridWidth);
        }
        return this.gridWidth.getReadOnlyProperty();
    }

    /***** TEMPORALY METHODS FOR FOR REFACTORING ******/
    private List<PropertiesListener> propertiesListenerList = new ArrayList<>(); //TODO move to document object

    public int getGridX() {
        return gridX().getValue();
        //return _gridX;
    }

    public int getGridY() {
        return gridY().getValue();
        //return _gridY;
    }

    public int getGridHeight() {
        return gridHeight().getValue();
        //return _gridHeight;
    }

    public int getGridWidth() {
        return gridWidth().getValue();
        //return _gridWidth;
    }

    public void set(int gridX, int gridY, int gridHeight, int gridWidth) {
        this._gridX = gridX;
        this._gridY = gridY;
        this._gridHeight = gridHeight;
        this._gridWidth = gridWidth;
        propertiesChange();
    }


    public void addPropertiesListener(PropertiesListener propertiesListener){
        propertiesListenerList.add(propertiesListener);
    }
    public void propertiesChange(){
        propertiesListenerList.forEach(l -> l.onPropertiesChange());
    }
}
