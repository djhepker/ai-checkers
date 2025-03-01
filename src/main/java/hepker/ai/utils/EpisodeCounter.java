package hepker.ai.utils;

import hepker.game.utils.FileLoader;

import java.io.IOException;

public final class EpisodeCounter {
    private static final String EPISODE_COUNT_KEY = "episode_count=";
    private final FileLoader fileMgr;
    private int episodeCount;

    public EpisodeCounter(String filePath) {
        this.fileMgr = new FileLoader(filePath);
        this.episodeCount = getEpisodeCount(fileMgr);
    }

    public int getEpisodeCount(FileLoader fileLoader) {
        try {
            return Integer.parseInt(fileLoader.getLine(EPISODE_COUNT_KEY).replace(EPISODE_COUNT_KEY, "").trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void processEpisode() {
        episodeCount++;
        fileMgr.updateLineByKey(EPISODE_COUNT_KEY, Integer.toString(episodeCount));
    }

    public int getEpisodeCount() {
        return episodeCount;
    }
}
