package hepker.utils;

import lombok.Getter;

public final class EpisodeStatistics {
    private static final String EPISODE_STATISTICS_FILEPATH = "EpisodeStatistics.csv";
    @Getter
    private static int episodeCount = 0;
    @Getter
    private static double updatedTurnsPerEpisodeAverage = 0.0;
    private static double oldTurnsPerEpisodeAverage = 0.0;

    private EpisodeStatistics() {

    }

    /**
     * Helper method for setting member variables. First line is last data obtained.
     * Count is set first and average second.
     */
    public static void retrieveEpisodeData() {
        String[] csvData = CSVHelper.loadData(EPISODE_STATISTICS_FILEPATH);
        if (csvData.length != 0) {
            String[] dataFromPreviousEpisode = csvData[csvData.length - 1].split(",");
            episodeCount = Integer.parseInt(dataFromPreviousEpisode[0]);
            oldTurnsPerEpisodeAverage = Double.parseDouble(dataFromPreviousEpisode[1]);
        }
    }

    /**
     * Helper function performs the math for finding our average turn count
     * Average turncount is [sum of turns from each epoch] / [episode count]
     *
     * @param currentEpochTurnCount The number of turns that occurred in this current epoch
     */
    public static void processEpisode(int currentEpochTurnCount) {
        double sumTurnsOfEpochs = oldTurnsPerEpisodeAverage * episodeCount;
        sumTurnsOfEpochs += currentEpochTurnCount;
        ++episodeCount;
        updatedTurnsPerEpisodeAverage = sumTurnsOfEpochs / episodeCount;
        System.out.printf("Turncount of this episode was: %d\n", currentEpochTurnCount);

    }

    public static void updateEpisodeCSV() {
        CSVHelper.writeData(EPISODE_STATISTICS_FILEPATH, episodeCount, updatedTurnsPerEpisodeAverage);
    }

    public static void displayEpisodeStatistics() {
        if (!Graphing.graphIsDisplayed()) {
            Graphing.initializeLineChart("Episode Turn Averages", "Episode", "Average Turn Count");
            String[] data = CSVHelper.loadData(EPISODE_STATISTICS_FILEPATH);
            for (String datum : data) {
                String[] dataLine = datum.split(",");
                Graphing.addDataPoint(Integer.parseInt(dataLine[0]), Double.parseDouble(dataLine[1]));
            }
        } else {
            Graphing.addDataPoint(episodeCount, updatedTurnsPerEpisodeAverage);
        }
    }
}
