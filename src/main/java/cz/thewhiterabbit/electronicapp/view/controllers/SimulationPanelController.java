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
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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
        lineChart.setLegendVisible(false);
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
        String finalItemName = getComponentName(item);
        findAndSowComponent(finalItemName);
        findAndShowNode(finalItemName);


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

    private void findAndSowComponent(String finalItemName) {
        document.getDocumentObjects().forEach(d->{
            if (finalItemName.equals(((SimulationComponent)d).getName())){
                showComponent(d);
                return;
            }
        });
    }

    private void findAndShowNode(String finalItemName) {
        document.getSimulationFile().getNetlist().getNodeList().forEach(n->{
            if(n.getName().equals("n"+ finalItemName)){
                Stack<SimulationComponent> stack = new Stack<>();
                stack.addAll( document.getSimulationFile().getNetlist().getComponentList());
                while (!stack.empty()){
                    ActivePoint ac = stack.pop().getActivePoint(n);
                    if(ac != null){
                        showComponent(ac);
                        return;
                    }
                }
            }
        });
    }

    private void showComponent(DocumentObject d) {
        Stack<CanvasObject> stack = new Stack<>();
        stack.addAll(drawingArea.getModel().getCanvasObjects());
        CanvasObject toHighlight = null;
        while (!stack.empty()){
            CanvasObject c = stack.pop();
            stack.addAll(c.getChildrenList());
            c.setHighlight(false);
            if(c.getGridX() == d.getGridX() && c.getGridY() == d.getGridY() && c.getClass() == d.getClass()){
                toHighlight = c;
            }
        }
        if(toHighlight != null){
            toHighlight.setHighlight(true);
            ((GridModel)drawingArea.getModel()).centerOnObject(toHighlight);
        }
        drawingArea.repaint();
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
        Document documentCopy = new Document(document.getRawDocument());
        drawingArea.setModel(documentCopy.getGridModel());
        documentCopy.getGridModel().center();
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
        //get non time nodes
        List<NodeListItem> nodeListItems = getNodesTransient(result);
        nodeListView.getItems().clear();
        lineChart.getData().clear();
        if (nodeListItems != null) {
            document.getNodeListItems().clear();
            document.getNodeListItems().addAll(nodeListItems);
            nodeListView.getItems().addAll(nodeListItems);
        }

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
