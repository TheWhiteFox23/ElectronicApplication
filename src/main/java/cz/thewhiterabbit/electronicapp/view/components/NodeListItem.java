package cz.thewhiterabbit.electronicapp.view.components;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.model.similation.NetlistNode;
import cz.thewhiterabbit.electronicapp.model.similation.SimulationComponent;
import cz.thewhiterabbit.electronicapp.model.similation.SimulationResultSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class NodeListItem extends HBox {
    private final Label textLabel = new Label("Node name");
    private final Button showButton = new Button();
    private final CheckBox checkBox = new CheckBox();

    private NetlistNode netlistNode;
    private SimulationComponent simulationComponent;
    private SimulationResultSet simulationResultSet;
    XYChart.Series series;

    private BooleanProperty checkedProperty = new SimpleBooleanProperty(false);

    public NodeListItem(){
        init();
    }
    public NodeListItem(String name){
        setText(name);
        init();
    }

    private void init() {
        checkedProperty.bindBidirectional(checkBox.selectedProperty());
        initializeComponent();
        String css = App.class.getResource("stylesheets/style.css").toExternalForm();
        getStylesheets().add(css);
        getStyleClass().add("node_list_item");
    }

    private void initializeComponent() {
        getChildren().add(textLabel);
        HBox hBox = new HBox();
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setAlignment(Pos.BASELINE_RIGHT);
        hBox.getChildren().add(showButton);
        hBox.getChildren().add(checkBox);
        getChildren().add(hBox);
    }

    public SimulationResultSet getSimulationResultSet() {
        return simulationResultSet;
    }

    public void setSimulationResultSet(SimulationResultSet simulationResultSet) {
        this.simulationResultSet = simulationResultSet;
    }

    public XYChart.Series getSeries() {
        return series;
    }

    public void setSeries(XYChart.Series series) {
        this.series = series;
    }

    public boolean isCheckedProperty() {
        return checkedProperty.get();
    }

    public BooleanProperty checkedPropertyProperty() {
        return checkedProperty;
    }

    public void setCheckedProperty(boolean checkedProperty) {
        this.checkedProperty.set(checkedProperty);
    }

    public Button getShowButton() {
        return showButton;
    }

    public NetlistNode getNetlistNode() {
        return netlistNode;
    }

    public void setNetlistNode(NetlistNode netlistNode) {
        this.netlistNode = netlistNode;
    }

    public SimulationComponent getSimulationComponent() {
        return simulationComponent;
    }

    public void setSimulationComponent(SimulationComponent simulationComponent) {
        this.simulationComponent = simulationComponent;
    }

    public void setText(String text){
        textLabel.setText(text);
    }

    public String getText(){
        return  textLabel.getText();
    }
}
