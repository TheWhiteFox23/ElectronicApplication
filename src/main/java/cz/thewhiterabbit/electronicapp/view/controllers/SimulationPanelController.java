package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentManager;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawDocument;
import cz.thewhiterabbit.electronicapp.model.similation.*;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingCanvas;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;
import cz.thewhiterabbit.electronicapp.view.components.NodeListItem;
import cz.thewhiterabbit.electronicapp.view.components.NodeListView;
import cz.thewhiterabbit.electronicapp.view.dialogs.SimulationProgressDialog;
import cz.thewhiterabbit.electronicapp.view.events.NodeListEvent;
import cz.thewhiterabbit.electronicapp.view.events.SimulationEvents;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimulationPanelController {
    //LOGIC
    private Document document = new Document(new RawDocument("MT"));
    //FXML component
    @FXML
    private DrawingCanvas drawingArea;
    @FXML
    private AnchorPane lineChartHolder;
    @FXML
    private NodeListView nodeListView;
    @FXML
    private Button selectAll;
    @FXML
    private Button cleanSelection;
    Tooltip mousePositionToolTip = new Tooltip("at stack tool");
    //CHART
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    private final LineChart<Double, Double> lineChart = new LineChart(xAxis, yAxis);
    //VARIABLES
    private final double previewWidth = 300;
    private final double previewHeight = 200;


    @FXML
    private void initialize() {
        initListeners();
        initComponents();
        initializeNodeListListeners();
        initButtons();
    }

    private void initButtons() {
        selectAll.setOnAction(e->{
            nodeListView.getItems().forEach(i->{
                i.checkedPropertyProperty().set(true);
            });
        });
        cleanSelection.setOnAction(e->{
            nodeListView.getItems().forEach(i->{
                i.checkedPropertyProperty().set(false);
            });
        });
    }

    private void initListeners() {
        GUIEventAggregator.getInstance().addEventHandler(DocumentManager.DocumentManagerEvent.ACTIVE_DOCUMENT_CHANGED, e -> {
            setDocument(((DocumentManager.DocumentManagerEvent) e).getDocument());
        });
        GUIEventAggregator.getInstance().addEventHandler(SimulationEvents.SIMULATE_CLICKED, e -> {
            startSimulation(((SimulationEvents) e).getSimulationFile());
        });
    }

    private void initComponents() {
        drawingArea.setPrefSize(previewWidth, previewHeight);
        drawingArea.setMinSize(previewWidth, previewHeight);
        drawingArea.setMaxSize(previewWidth, previewHeight);

        AnchorPane.setLeftAnchor(lineChart, 0d);
        AnchorPane.setRightAnchor(lineChart, 0d);
        AnchorPane.setTopAnchor(lineChart, 0d);
        AnchorPane.setBottomAnchor(lineChart, 0d);

        initLineChart();
    }

    private void initLineChart() {
        lineChart.setCursor(Cursor.CROSSHAIR);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(true);
        lineChartHolder.getChildren().add(lineChart);
        lineChart.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            if (e.getPickResult().getIntersectedNode() instanceof StackPane) {
                StackPane node = (StackPane) e.getPickResult().getIntersectedNode();
                lineChart.getData().forEach(o -> {
                    o.getData().forEach(d -> {
                        if (d.getNode() == node) {
                            showPointValue(d, o.getName(), e);
                        }
                    });
                });
            } else {
                hidePointValue();
            }
        });
    }

    private void hidePointValue() {
        mousePositionToolTip.hide();
    }

    private void showPointValue(XYChart.Data<Double, Double> node, String seriesNane, MouseEvent event) {
        String msg = "Time: " + node.getXValue() + "\n" +
                "Value: " + node.getYValue() + "\n" +
                "Node: " + seriesNane;
        mousePositionToolTip.setText(msg);
        Node n = (Node) event.getSource();
        mousePositionToolTip.show(n, event.getScreenX() + 10, event.getScreenY() + 10);
    }

    private void initializeNodeListListeners() {
        nodeListView.addEventHandler(NodeListEvent.NODE_CHECK_CHANGE, e -> {
            NodeListItem item = e.getNodeListItem();
            onNodeCheckChange(item);
        });
        nodeListView.addEventHandler(NodeListEvent.SHOW_NODE, e -> {
            NodeListItem item = e.getNodeListItem();
            onShowNode(item);
        });
    }

    private void onShowNode(NodeListItem item) {
        CanvasObject ca = item.getCanvasObject();
        ((GridModel)drawingArea.getModel()).centerOnObject(ca);
        drawingArea.repaint();
    }

    private String getComponentName(NodeListItem item) {
        String itemName = "";
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(item.getText());
        if(m.find()) {
            itemName = m.group();
        }
        String finalItemName = itemName;
        return finalItemName;
    }

    private void onNodeCheckChange(NodeListItem item) {
        if (item.checkedPropertyProperty().get()) {
            if (!lineChart.getData().contains(item.getSeries())) lineChart.getData().add(item.getSeries());
        } else {
            if (lineChart.getData().contains(item.getSeries())) lineChart.getData().remove(item.getSeries());
        }
    }

    private void setDocument(Document document) {
        this.document = document;
        drawingArea.setModel(document.getSimulationModel());
        this.nodeListView.getItems().clear();
        this.nodeListView.getItems().addAll(document.getNodeListItems());
        lineChart.getData().clear();
        nodeListView.getItems().forEach(i -> {
            if (i.isCheckedProperty() && !lineChart.getData().contains(i.getSeries())) {
                lineChart.getData().add(i.getSeries());
            }
        });
        drawingArea.repaint();
    }

    private void startSimulation(SimulationFile simulationFile) {
        Netlist netlist = SimulationUtilities.createNetlist(document);
        //TODO optimize if selected
        SimulationUtilities.optimizeNetlist(netlist);
        simulationFile.setNetlist(netlist);
        Task<SimulationResult> task = SimulationUtilities.getSimulationTask(simulationFile);
        Thread thread = new Thread(task);
        SimulationProgressDialog progressDialog = new SimulationProgressDialog(task);
        progressDialog.showDialog();
        thread.start();
        try {
            thread.join();
            progressDialog.closeDialog();
            task.valueProperty().addListener(e -> {
                setResult(task.getValue());
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setResult(SimulationResult result) {
        document.setSimulationResult(result);
        document.setSimulationModel(new Document(document.getRawDocument()).getGridModel());
        //TODO probably will be necessary do make copy of the raw document
        drawingArea.setModel(document.getSimulationModel());
        document.getSimulationModel().center();
        //get non time nodes
        List<NodeListItem> nodeListItems = getNodesTransient(result);
        nodeListView.getItems().clear();
        lineChart.getData().clear();
        if (nodeListItems != null) {
            document.getNodeListItems().clear();
            document.getNodeListItems().addAll(nodeListItems);
            nodeListView.getItems().addAll(nodeListItems);
        }
        prepareSimulationModel(document.getSimulationModel(), document);
        drawingArea.repaint();
    }

    private void prepareSimulationModel(GridModel simulationModel, Document document) {
        document.getNodeListItems().forEach(nli->{
            CanvasObject canvasObject = findCanvasObject(nli, document, simulationModel);
            if(canvasObject != null){
                nli.setCanvasObject(canvasObject);
                if(canvasObject instanceof ActivePoint){
                    canvasObject.setNameTag(nli.getText());
                    canvasObject.setPaintNameTag(true);
                }else if (canvasObject instanceof SimulationComponent){
                    ((SimulationComponent) canvasObject).setName(getComponentName(nli));
                }
            }
        });
    }

    private CanvasObject findCanvasObject(NodeListItem nodeListItem, Document document, GridModel simulationModel){
        CanvasObject canvasObject = null;
        if(nodeListItem.getText().startsWith("n")){
            canvasObject = findActivePoint_new(getComponentName(nodeListItem), document);
        }else{
            canvasObject =  findComponent(getComponentName(nodeListItem), document);
        }
        if(canvasObject == null)return null;
        return findCorrespondingCanvasObject(canvasObject, simulationModel);
    }

    private CanvasObject findCorrespondingCanvasObject(CanvasObject co, GridModel simulationModel) {
        if(co == null)return null;
        AtomicReference<CanvasObject> toReturn = new AtomicReference<>();
        simulationModel.getCanvasObjects().forEach(o->{
            if(o.getGridY() == co.getGridY() && o.getGridX()==co.getGridX() && o.getClass()==co.getClass()){
                toReturn.set(o);
                return;
            }
        });
        return toReturn.get();
    }

    private CanvasObject findComponent(String componentName, Document document) {
        AtomicReference<CanvasObject> canvasObject = new AtomicReference<>();
        document.getSimulationFile().getNetlist().getComponentList().forEach(c->{
            if(c.getName().equals(componentName)){
                canvasObject.set( (CanvasObject) c);
                return;
            }
        });
        return canvasObject.get();
    }

    private CanvasObject findActivePoint_new(String componentName, Document document) {
        AtomicReference<ActivePoint> activePoint = new AtomicReference<>();
        document.getSimulationFile().getNetlist().getNodeList().forEach(e->{
            if(e.getName().equals(("n"+componentName))){
                e.getSimulationComponentList().forEach(o->{
                    if(o.getActivePoint(e) != null);
                    activePoint.set(o.getActivePoint(e));
                    return;
                });
                if(activePoint.get()!= null)return;
            }
        });
        return activePoint.get();
    }

    private List<NodeListItem> getNodesTransient(SimulationResult simulationResult) {
        //get time data set
        List<NodeListItem> nodeListItems = new ArrayList<>();
        SimulationResultSet time = simulationResult.getByName("time");
        if (time == null) return null;
        //Get nodes
        simulationResult.getResultSetList().forEach(r -> {
            if (r.getName().startsWith("n")) {
                nodeListItems.add(parseNode(r, time));
            } else if (!r.getName().equals("time") && !r.getName().equals("")) {
                nodeListItems.add(parseNode(r, time));//TODO apply parse simulationComponentMethod
            }
        });
        return nodeListItems;
    }

    private NodeListItem parseNode(SimulationResultSet r, SimulationResultSet time) {
        NodeListItem nodeListItem = new NodeListItem(r.getName());
        nodeListItem.setSimulationResultSet(r);
        nodeListItem.setSeries(createSeries(time, r));
        return nodeListItem;
    }

    private XYChart.Series createSeries(SimulationResultSet time, SimulationResultSet r) {
        XYChart.Series series = new XYChart.Series();
        Stack<Double> timeStack = new Stack<>();
        timeStack.addAll(time.getValues());
        Stack<Double> resultStack = new Stack<>();
        resultStack.addAll(r.getValues());
        while (!resultStack.empty()) {
            series.getData().add(new XYChart.Data(timeStack.pop(), resultStack.pop()));
        }
        series.setName(r.getName());
        return series;
    }
}
