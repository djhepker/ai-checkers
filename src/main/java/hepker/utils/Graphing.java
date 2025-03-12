package hepker.utils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.XYChart.Data;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class Graphing extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Graphing.class);
    private static final List<Data<Number, Number>> PENDING_DATA_POINTS = new ArrayList<>();

    private static LineChart<Number, Number> lineChart;
    private static Series<Number, Number> lineSeries;
    private static Stage stage;
    private static volatile boolean initializing = false;

    private static String chartTitle;
    private static String xAxisLabel;
    private static String yAxisLabel;

    private static NumberAxis xAxis;
    private static NumberAxis yAxis;

    public Graphing() {

    }

    @Override
    public void start(Stage primaryStage) {
        if (chartTitle == null || xAxisLabel == null || yAxisLabel == null) {
            LOGGER.error("Chart parameters not set");
            throw new IllegalStateException("Chart parameters not set");
        }

        // Set up the chart using the primary stage
        xAxis = new NumberAxis();
        xAxis.setLabel(xAxisLabel);
        yAxis = new NumberAxis();
        yAxis.setLabel(yAxisLabel);
        xAxis.setTickUnit(1);
        yAxis.setTickUnit(1);

        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);

        lineSeries = new Series<>();
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.getData().add(lineSeries);
        lineChart.setTitle(chartTitle);
        lineChart.setLegendVisible(false);
        lineChart.setStyle("-fx-background-color: lightgray;");


        Scene scene = new Scene(lineChart, 800, 600);
        stage = primaryStage; // Use the primary stage
        stage.setTitle(chartTitle);
        stage.setScene(scene);
        stage.show();

        // Handle pending data points after the stage is shown
        stage.setOnShown(event -> {
            LOGGER.info("Stage is now shown");
            initializing = false;
            if (lineSeries != null) {
                Platform.runLater(() -> {
                    synchronized (PENDING_DATA_POINTS) {
                        for (Data<Number, Number> data : PENDING_DATA_POINTS) {
                            lineSeries.getData().add(data);
                        }
                        PENDING_DATA_POINTS.clear();
                    }
                });
            }
        });
    }

    public static void initializeLineChart(String title, String xLabel, String yLabel) {
        if (initializing) {
            LOGGER.warn("Initialization already in progress, skipping...");
            return;
        }
        initializing = true;
        chartTitle = title;
        xAxisLabel = xLabel;
        yAxisLabel = yLabel;
        try {
            new Thread(() -> Application.launch(Graphing.class)).start();
        } catch (IllegalStateException e) {
            LOGGER.error("Cannot call launch() more than once or within an existing JavaFX app", e);
        }
    }

    public static void addDataPoint(Number xValue, Number yValue) {
        LOGGER.debug("addDataPoint called");
        if (xValue == null || yValue == null) {
            LOGGER.error(String.format("Null input: xValue %s, yValue %s", xValue, yValue));
            throw new IllegalArgumentException("Null addDataPoint() argument");
        }
        Data<Number, Number> data = new Data<>(xValue, yValue);
        if (lineSeries == null) {
            synchronized (PENDING_DATA_POINTS) {
                PENDING_DATA_POINTS.add(data);
                LOGGER.debug("Added dataPoint: " + data);
            }
        } else {
            LOGGER.debug("Inside of addDataPoint -> runLater and lineSeries is not null");
            Platform.runLater(() -> {
                lineSeries.getData().add(data);


                int minX = (int) lineSeries.getData().stream().mapToDouble(d -> d.getXValue().doubleValue()).min().getAsDouble();
                int maxX = (int) lineSeries.getData().stream().mapToDouble(d -> d.getXValue().doubleValue()).max().getAsDouble();
                int minY = (int) lineSeries.getData().stream().mapToDouble(d -> d.getYValue().doubleValue()).min().getAsDouble();
                int maxY = (int) lineSeries.getData().stream().mapToDouble(d -> d.getYValue().doubleValue()).max().getAsDouble();

                xAxis.setLowerBound(minX - 5);
                xAxis.setUpperBound(maxX + 5);
                yAxis.setLowerBound(minY - 5);
                yAxis.setUpperBound(maxY + 5);

            });
        }
    }

    public static boolean graphIsDisplayed() {
        return lineChart != null && stage != null && stage.isShowing();
    }
}