package hepker.utils;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SwingUtilities;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class Graphing {
    private static final Logger LOGGER = LoggerFactory.getLogger(Graphing.class);
    private static final String CSV_FILE_NAME = "./averageTurns.csv";
    private static final int UPDATE_FREQUENCY = 10;

    private static XYChart chart;
    private static List<Integer> episodes;
    private static List<Double> turnCountAverages;
    private static List<Integer> turnCounts;
    private static SwingWrapper<XYChart> swingWrapper;
    private static FileWriter csvWriter;
    private static boolean isChartDisplayed = false;

    private Graphing() {
    }

    /**
     * Initializes the chart and loads existing data from CSV, if available.
     */
    public static void renderEpisodeStatistics() {
        try {
            // Initialize data lists
            episodes = new ArrayList<>();
            turnCountAverages = new ArrayList<>();
            turnCounts = new ArrayList<>();

            // Load existing data from CSV
            loadFromCSV();

            // Open CSV writer in append mode
            csvWriter = new FileWriter(CSV_FILE_NAME, true);
            if (!new File(CSV_FILE_NAME).exists()) {
                csvWriter.append("Episode,TurnCount,AverageTurnCount\n");
            }

            // Create the chart
            chart = new XYChartBuilder()
                    .width(800)
                    .height(600)
                    .title("Q-Learning Progress (Live)")
                    .xAxisTitle("Episode")
                    .yAxisTitle("Turn Count")
                    .build();

            chart.getStyler().setPlotGridLinesVisible(true);
            chart.getStyler().setMarkerSize(6);
            chart.getStyler().setLegendVisible(true);

            // Add series only if data exists
            if (!episodes.isEmpty()) {
                XYSeries avgSeries = chart.addSeries("Average Turn Count", episodes, turnCountAverages);
                avgSeries.setMarker(SeriesMarkers.CIRCLE);
                avgSeries.setLineWidth(1.5f);

                XYSeries turnSeries = chart.addSeries("Turn Count", episodes, turnCounts);
                turnSeries.setMarker(SeriesMarkers.DIAMOND);
                turnSeries.setLineWidth(0.5f);

                // Display the chart on the EDT
                swingWrapper = new SwingWrapper<>(chart);
                SwingUtilities.invokeLater(() -> {
                    swingWrapper.displayChart();
                    isChartDisplayed = true;
                });
            } else {
                LOGGER.info("No data available yet. Chart will be displayed when data is added.");
            }
        } catch (IOException e) {
            LOGGER.error("Failed to initialize graph", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds new episode data and updates the chart.
     */
    public static void addEpisodeValues(int episode, int turnCount, double averageTurnCount) {
        if (chart == null) {
            LOGGER.warn("Graph not initialized. Call renderEpisodeStatistics() first.");
            return;
        }

        // Add new data to lists
        episodes.add(episode);
        turnCounts.add(turnCount);
        turnCountAverages.add(averageTurnCount);

        // Write to CSV
        try {
            csvWriter.append(String.valueOf(episode))
                    .append(",")
                    .append(String.valueOf(turnCount))
                    .append(",")
                    .append(String.valueOf(averageTurnCount))
                    .append("\n");
            csvWriter.flush();
        } catch (IOException e) {
            LOGGER.error("Could not write to CSV", e);
        }

        // If chart isn’t displayed yet, initialize and display it
        if (!isChartDisplayed) {
            XYSeries avgSeries = chart.addSeries("Average Turn Count", episodes, turnCountAverages);
            avgSeries.setMarker(SeriesMarkers.CIRCLE);
            avgSeries.setLineWidth(1.5f);

            XYSeries turnSeries = chart.addSeries("Turn Count", episodes, turnCounts);
            turnSeries.setMarker(SeriesMarkers.DIAMOND);
            turnSeries.setLineWidth(0.5f);

            swingWrapper = new SwingWrapper<>(chart);
            SwingUtilities.invokeLater(() -> {
                swingWrapper.displayChart();
                isChartDisplayed = true;
            });
        } else {
            // Update existing series on the EDT
            SwingUtilities.invokeLater(() -> {
                chart.updateXYSeries("Average Turn Count", episodes, turnCountAverages, null);
                chart.updateXYSeries("Turn Count", episodes, turnCounts, null);
                swingWrapper.repaintChart();
            });
        }

        // Save chart periodically
        if (episode % UPDATE_FREQUENCY == 0) {
            saveChart("./progress_at_episode_" + episode + ".png");
        }
    }

    /**
     * Saves the chart as a PNG file.
     */
    public static void saveChart(String fileName) {
        if (chart == null || !isChartDisplayed) {
            LOGGER.warn("No chart to save. Waiting for data.");
            return;
        }
        try {
            BitmapEncoder.saveBitmap(chart, fileName, BitmapEncoder.BitmapFormat.PNG);
            LOGGER.debug("Saved chart to {}", fileName);
        } catch (IOException e) {
            LOGGER.error("Could not save chart to {}", fileName, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the CSV writer.
     */
    public static void close() {
        if (csvWriter != null) {
            try {
                csvWriter.close();
            } catch (IOException e) {
                LOGGER.error("Could not close CSV writer", e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Loads existing data from the CSV file into the data lists.
     */
    private static void loadFromCSV() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(CSV_FILE_NAME));
            if (lines.isEmpty()) {
                return;
            }
            // Skip header row
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length == 3) {
                    int episode = Integer.parseInt(parts[0]);
                    int turnCount = Integer.parseInt(parts[1]);
                    double averageTurnCount = Double.parseDouble(parts[2]);
                    episodes.add(episode);
                    turnCounts.add(turnCount);
                    turnCountAverages.add(averageTurnCount);
                }
            }
        } catch (IOException e) {
            LOGGER.info("No existing CSV file found. Starting fresh.");
        }
    }
}