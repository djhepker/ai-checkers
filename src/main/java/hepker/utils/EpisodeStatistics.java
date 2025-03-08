package hepker.utils;

import lombok.Getter;

public final class EpisodeStatistics {
    private static final String EPISODE_STATISTICS_FILEPATH = "EpisodeStatistics.csv";
    @Getter
    private static int episodeCount = 0;
    @Getter
    private static double averageTurnCount = 0.0;

    private EpisodeStatistics() {

    }

    /**
     * Helper method for setting member variables. First line is most recent data obtained.
     * Count is set first and average second
     */
    public static void retrieveEpisodeData() {
        String[] csvData = CSVHelper.loadData(EPISODE_STATISTICS_FILEPATH);
        if (csvData.length != 0) {
            String[] data = csvData[csvData.length - 1].split(",");
            episodeCount = Integer.parseInt(data[0]);
            averageTurnCount = Double.parseDouble(data[1]);
        }
    }

    public static void processEpisode(int turnCount) {
        double currentSumOfTurns = episodeCount * averageTurnCount + turnCount;
        ++episodeCount;
        averageTurnCount = currentSumOfTurns / episodeCount;
    }

    public static void updateEpisodeCSV() {
        CSVHelper.writeData(EPISODE_STATISTICS_FILEPATH, episodeCount, averageTurnCount);
    }

    public static void displayEpisodeStatistics() {
        if (!Graphing.graphIsDisplayed()) {
            Graphing.initializeLineChart("Episode Turn Averages", "Episode", "Average Turn Count");
            String[] data = CSVHelper.loadData(EPISODE_STATISTICS_FILEPATH);
            for (String datum : data) {
                String[] dataLine = datum.split(",");
                Graphing.addDataPoint(Integer.parseInt(data[0]), Double.parseDouble(data[1]));
            }
        } else {
            Graphing.addDataPoint(episodeCount, averageTurnCount);
        }
    }
}