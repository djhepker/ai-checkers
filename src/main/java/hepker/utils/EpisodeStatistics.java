package hepker.utils;

import lombok.Getter;

public final class EpisodeStatistics {
    private static final String EPISODE_STATISTICS_NAME = "EpisodeStatistics.csv";
    @Getter
    private static double episodeCount = 0;
    @Getter
    private static int sumOfTurnsPlayed = 0;
    private static double turnAverage = 0.0;

    private EpisodeStatistics() {

    }

    /**
     * Helper method for setting member variables. First line is last data obtained.
     * Count is set first and average second.
     */
    public static void retrieveEpisodeData() {
        String[] csvData = CSVHelper.loadData(EPISODE_STATISTICS_NAME);
        if (csvData.length != 0) {
            String[] dataFromPreviousEpisode = csvData[csvData.length - 1].split(",");
            episodeCount = Double.parseDouble(dataFromPreviousEpisode[0]);
            turnAverage = Double.parseDouble(dataFromPreviousEpisode[1]);
            sumOfTurnsPlayed = Integer.parseInt(dataFromPreviousEpisode[2]);
        }
    }

    /**
     * Helper function performs the math for finding our average turn count
     * Average turncount is [sum of turns from each epoch] / [episode count]
     *
     * @param currentEpochTurnCount The number of turns that occurred in this current epoch
     */
    public static void processEpisode(int currentEpochTurnCount) {
        sumOfTurnsPlayed += currentEpochTurnCount;
        turnAverage = sumOfTurnsPlayed / ++episodeCount;
    }

    public static void updateEpisodeCSV() {
        CSVHelper.writeData(EPISODE_STATISTICS_NAME, turnAverage, episodeCount, sumOfTurnsPlayed);
    }
}
