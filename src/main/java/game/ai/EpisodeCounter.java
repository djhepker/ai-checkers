package main.java.game.ai;

import main.java.game.utils.FileLoader;

import java.io.IOException;

public class EpisodeCounter {
    private final String EPISODE_COUNT_KEY = "episode_count=";
    private final FileLoader fileLoader;
    private int episodeCount;

    public EpisodeCounter(String filePath) {
        this.fileLoader = new FileLoader(filePath);
        this.episodeCount = getEpisodeCount(fileLoader);
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
        fileLoader.updateLineByKey(EPISODE_COUNT_KEY, Integer.toString(episodeCount));
    }

    public int getEpisodeCount() {
        return episodeCount;
    }
}
