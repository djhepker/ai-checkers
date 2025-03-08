package hepker.utils;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.XYChart.Data;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Graphing {
    private static final Logger LOGGER = LoggerFactory.getLogger(Graphing.class);

    private static LineChart<Number, Number> lineChart;
    private static Series<Number, Number> lineSeries;
    private static Stage stage;

    private Graphing() {

    }

    /**
     * Initializes the line chart with the given title and axis labels.
     * Call this method once at the start to set up the chart.
     *
     * @param title  the title of the chart
     * @param xLabel the label for the x-axis
     * @param yLabel the label for the y-axis
     */
    public static void initializeLineChart(String title, String xLabel, String yLabel) {
        Platform.runLater(() -> {
            if (title == null || xLabel == null || yLabel == null) {
                LOGGER.error(String.format(
                        "Null input: title %s, xLabel %s, yLabel %s", title, xLabel, yLabel));
                throw new IllegalArgumentException("Null initializeLineChart() argument");
            }
            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel(xLabel);
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel(yLabel);

            lineSeries = new Series<>();
            lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.getData().add(lineSeries);
            lineChart.setTitle(title);

            Scene scene = new Scene(lineChart, 800, 600);
            stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        });
    }

    /**
     * Adds a new data point to the existing line chart.
     * Call this method at the end of each episode or at your chosen frequency.
     *
     * @param xValue the x-value of the new data point (e.g., episode number)
     * @param yValue the y-value of the new data point (e.g., performance metric)
     */
    public static void addDataPoint(Number xValue, Number yValue) {
        if (lineSeries == null) {
            LOGGER.error("Line chart not initialized. Call initializeLineChart() first.");
            throw new IllegalStateException("Line chart not initialized.");
        }
        if (xValue == null || yValue == null) {
            LOGGER.error(String.format("Null input: xValue %s, yValue %s", xValue, yValue));
            throw new IllegalArgumentException("Null addDataPoint() argument");
        }
        // Ensure updates occur on the JavaFX application thread
        Platform.runLater(() -> {
            lineSeries.getData().add(new Data<>(xValue, yValue));
        });
    }

    /**
     * Getter for checking if the Graph is currently being displayed
     * @return True if stage.isShowing()
     */
    public static boolean graphIsDisplayed() {
        // Check if lineChart and stage are initialized and if the stage is visible
        return lineChart != null && stage != null && stage.isShowing();
    }
}