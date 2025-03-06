package hepker.utils;

import lombok.Getter;

public final class EpisodeStatistics {
    @Getter
    private static int episodeCount = 0;
    @Getter
    private static double averageTurnCount = 0.0;

    private EpisodeStatistics() {

    }

    public static void processEpisode(int turnCount) {
        double currentSumOfTurns = episodeCount * averageTurnCount + turnCount;
        episodeCount++;
        averageTurnCount = currentSumOfTurns / episodeCount;
        Graphing.addEpisodeValues(episodeCount, turnCount, averageTurnCount);
    }
}