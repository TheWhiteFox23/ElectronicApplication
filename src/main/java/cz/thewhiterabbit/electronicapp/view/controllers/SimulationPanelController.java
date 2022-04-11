package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentManager;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawDocument;
import cz.thewhiterabbit.electronicapp.model.similation.*;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingCanvas;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;
import cz.thewhiterabbit.electronicapp.view.dialogs.SimulationProgressDialog;
import cz.thewhiterabbit.electronicapp.view.events.SimulationEvents;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class SimulationPanelController {
    private Document document = new Document(new RawDocument("MT"));

    @FXML
    private DrawingCanvas drawingArea;
    @FXML
    private ListView listView;
    @FXML
    private AnchorPane lineChartHolder;

    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    private final LineChart<Double,Double> lineChart = new LineChart(xAxis,yAxis);


    @FXML private void initialize(){
        GUIEventAggregator.getInstance().addEventHandler(DocumentManager.DocumentManagerEvent.ACTIVE_DOCUMENT_CHANGED, e->{
            setDocument(((DocumentManager.DocumentManagerEvent)e).getDocument());
        });
        GUIEventAggregator.getInstance().addEventHandler(SimulationEvents.SIMULATE_CLICKED,e->{
           startSimulation(((SimulationEvents)e).getSimulationFile());
        });
        initListViewListener();
        drawingArea.setPrefSize(300,300);
        drawingArea.setMinSize(300,300);
        drawingArea.setMaxSize(300,300);

        AnchorPane.setLeftAnchor(lineChart,0d);
        AnchorPane.setRightAnchor(lineChart,0d);
        AnchorPane.setTopAnchor(lineChart,0d);
        AnchorPane.setBottomAnchor(lineChart,0d);
        lineChartHolder.getChildren().add(lineChart);
    }

    private void initListViewListener() {
        listView.getSelectionModel().selectedItemProperty().addListener(e->{
            SimulationResult simulationResult = document.getSimulationResult();
            SimulationResultSet time = null;
            for(int i = 0; i< simulationResult.getResultSetList().size(); i++){
                if(simulationResult.getResultSetList().get(i).getName().equals("time")){
                    time = simulationResult.getResultSetList().get(i);
                }
            }
            SimulationResultSet value = null;
            for(int i = 0; i< simulationResult.getResultSetList().size(); i++){
                if(simulationResult.getResultSetList().get(i).getName().equals(listView.getSelectionModel().getSelectedItem().toString())){
                    value = simulationResult.getResultSetList().get(i);
                }
            }
            setGraphContent(time, value);
        });
    }

    private void setDocument(Document document) {
        this.document = document;
        Document documentCopy = new Document(document.getRawDocument());
        drawingArea.setModel(documentCopy.getGridModel());
        documentCopy.getGridModel().center();
        setResult(document.getSimulationResult());
        drawingArea.repaint();
    }

    private void startSimulation(SimulationFile simulationFile){
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
            task.valueProperty().addListener(e->{
                setResult(task.getValue());
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setResult(SimulationResult result){
        document.setSimulationResult(result);
        listView.getItems().clear();
        result.getResultSetList().forEach(i->{
            listView.getItems().add(i.getName());
        });
        //get time

    }

    private void setGraphContent(SimulationResultSet time,SimulationResultSet set){
        if(time == null || set ==null)return;
        lineChart.getData().clear();

        //lineChart.getXAxis().setAutoRanging(true);
        //lineChart.getYAxis().setAutoRanging(true);

        XYChart.Series series = new XYChart.Series();
        series.setName("No of schools in an year");

        for(int i = 0; i< set.getValues().size(); i++){
            series.getData().add(new XYChart.Data(time.getValues().get(i), set.getValues().get(i)));
        }
        lineChart.getData().add(series);
    }
}
