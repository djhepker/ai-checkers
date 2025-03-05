package hepker.ai.utils;

import hepker.utils.FileLoader;

import java.io.IOException;

public final class EpisodeStatistics {
    private static final String EPISODE_COUNT_KEY = "episode_count=";
    private static final String AVERAGE_TURN_COUNT_KEY = "average_num_turns=";

    private final FileLoader fileMgr;
    private final double averageTurnCount;

    private int episodeCount;

    public EpisodeStatistics(String filePath) {
        this.fileMgr = new FileLoader(filePath);
        this.episodeCount = (int) loadDataValue(fileMgr, EPISODE_COUNT_KEY);
        this.averageTurnCount = loadDataValue(fileMgr, AVERAGE_TURN_COUNT_KEY);
    }

    public double loadDataValue(FileLoader fileLoader, String key) {
        try {
            String line = fileLoader.getLine(key).replace(key, "").trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                return Double.parseDouble(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public void processEpisode(int numTurns) {
        double currentSumOfTurns = episodeCount * averageTurnCount + numTurns;
        double averageTurnsPrime = currentSumOfTurns / ++episodeCount;
        fileMgr.updateLineByKey(AVERAGE_TURN_COUNT_KEY, String.format("%.3f", averageTurnsPrime));
        fileMgr.updateLineByKey(EPISODE_COUNT_KEY, Integer.toString(episodeCount));
    }
}
