package ui;

import kmeans.*;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static javafx.scene.chart.XYChart.*;

public class ChartController {

    @FXML
    public ScatterChart<Double, Double> plotScatterChart;
    @FXML
    public Slider groupCountSlider;
    @FXML
    public Slider imagesCountSlider;
    @FXML
    public BorderPane borderPane;
    @FXML
    public Button startButton;

    public ChartController() {
        converter = new PointConverter();
        service = new AlgorithmService();
        service.setOnSucceeded(event -> {
            showIterationResults();
            startNewIteration();
        });
        executorService = Executors.newCachedThreadPool();
    }

    private void startNewIteration() {
        if (service.isNextIterationExist()) {
            service.restart();
        }
    }

    private AlgorithmService service;
    private PointConverter converter;
    private ExecutorService executorService;
    private IPRFactory factory;
    private int coreCount;
    private int pointCount;

    private IterativePatternRecognition createAlgorithmWithInput() {
        pointCount = (int) imagesCountSlider.getValue();
        Point[] points = converter.generatePointList(pointCount);
        coreCount = (int) groupCountSlider.getValue();
        Point[] cores = converter.generateCoreList(coreCount, points);
        return factory.createIPR(points,cores);
    }

    private void showIterationResults() {
        if ((service.getIterationIndex() %
                (pointCount / 1000 / (coreCount+1) + 1) == 0)
                || !service.isNextIterationExist())
            addPointsToChart(service.getValue());
    }


    private List<Callable<Series<Double, Double>>> createTasksForSeries(
            List<Cluster> clusters) {
        ArrayList<Callable<Series<Double, Double>>> tasks
                = new ArrayList<>(clusters.size());
        LinkedList<Point> cores = new LinkedList<>();
        coreCount = cores.size();
        for (Cluster cluster : clusters) {
            tasks.add(() -> getSeries(cluster.getImages()));
            cores.add(cluster.getCore());
        }
        tasks.add(() -> getSeries(cores));
        return tasks;
    }

    private void runTasksForSeries(
            List<Callable<Series<Double, Double>>> tasks)
            throws InterruptedException {
        List<Future<Series<Double, Double>>> futures
                = executorService.invokeAll(tasks, 1, TimeUnit.MINUTES);
        futures.forEach(this::addSeriesToPlot);
    }

    private void addSeriesToPlot(Future<Series<Double, Double>> future) {
        try {
            plotScatterChart.getData().add(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void addPointsToChart(List<Cluster> clusters) {
        clearPlot();
        try {
            runTasksForSeries(createTasksForSeries(clusters));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void clearPlot() {
        plotScatterChart.getData().clear();
    }


    private Series<Double, Double> getSeries(List<Point> points) {
        Series<Double, Double> series = new Series<>();
        points.forEach(point -> {
            if (point != null) {
                series.getData().add(converter.getDataFromPoint(point));
            }
        });
        return series;
    }

    public void initialize(Stage stage,IPRFactory factory) {
        stage.onCloseRequestProperty().addListener((event) -> service.cancel());
        this.factory =factory;
    }

    @FXML
    void startButtonOnActionHandle() {
        service.restart(createAlgorithmWithInput());
    }
}
