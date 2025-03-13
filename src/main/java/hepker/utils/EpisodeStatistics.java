package hepker.utils;

import lombok.Getter;

public final class EpisodeStatistics {
    private static final String EPISODE_STATISTICS_NAME = "EpisodeStatistics.csv";
    @Getter
    private static int episodeCount = 0;
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
        CSVHelper.EpochData csvData = CSVHelper.getPreviousEpochData(EPISODE_STATISTICS_NAME);
        turnAverage = csvData.getTurnAverage();
        episodeCount = csvData.getNumEpisodes();
        sumOfTurnsPlayed = csvData.getSumTurnsPlayed();
    }

    /**
     * Helper function performs the math for finding our average turn count
     * Average turncount is [sum of turns from each epoch] / [episode count]
     *
     * @param currentEpochTurnCount The number of turns that occurred in this current epoch
     */
    public static void processEpisode(int currentEpochTurnCount) {
        sumOfTurnsPlayed += currentEpochTurnCount;
        turnAverage = (double) sumOfTurnsPlayed / ++episodeCount;
    }

    public static void updateEpisodeCSV() {
        CSVHelper.queueEpochData(turnAverage, episodeCount, sumOfTurnsPlayed, true);
        if (CSVHelper.getDataSize() >= 100) {
            CSVHelper.writeData(EPISODE_STATISTICS_NAME);
        }
    }
}
