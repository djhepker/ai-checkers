package main.java.ai.table;

import main.java.ai.utils.AgentStats;
import main.java.ai.utils.EpisodeCounter;
import main.java.game.utils.EnvLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class DataManager {
    private HashMap<String, double[]> updatedQValues;

    private final QValueRepository db;
    private final EnvLoader envLoader;

    private final String SQL_URL_KEY = "SQL_URL";
    private final String ENV_FILEPATH = ".env";

    public DataManager() {
        try {
            this.envLoader = new EnvLoader(ENV_FILEPATH);
            this.db = new QValueRepository(envLoader.get(SQL_URL_KEY));
            this.db.createTable();
            this.updatedQValues = new HashMap<>();
        } catch (IOException e) {
            //todo
        }
    }

    public int getMaxQIndex(String serialKey) {
        return db.getMaxQAction(serialKey);
    }

    public boolean isWithinSize(String serialKey, int qIndex) {
        return updatedQValues.containsKey(serialKey) && updatedQValues.get(serialKey).length > qIndex;
    }

    public double queryQTableForValue(String serialKey, int decisionNumber) {
        return db.getQValueFromTable(serialKey, decisionNumber);
    }

    public double getMaxQValue(String serialKey) {
        return db.getMaxQValue(serialKey);
    }

    public void putUpdatedValue(String serialKey, int index, double inputQ) {
        updatedQValues.compute(serialKey, (key, existingArray) -> {
            if (existingArray == null) {
                double[] newArray = new double[index + 1];
                newArray[index] = inputQ;
                return newArray;
            } else if (index >= existingArray.length) {
                double[] newArray = Arrays.copyOf(existingArray, index + 1);
                newArray[index] = inputQ;
                return newArray;
            } else {
                existingArray[index] = inputQ;
                return existingArray;
            }
        });
    }

    public void updateData(boolean episodeOver) {
        db.updateQTable(updatedQValues);
        updateEpisodes();
        updateAgentStats(episodeOver);
    }

    private void printUpdatedValues() {
        for (String key : updatedQValues.keySet()) {
            for (int i = 0; i < updatedQValues.get(key).length; i++) {
                System.out.println(key + ": " + updatedQValues.get(key)[i]);
            }
        }
    }

    private void updateEpisodes() {
        final String EPISODE_KEY = "EPISODE_COUNT_FILEPATH";
        EpisodeCounter episodeCounter = new EpisodeCounter(envLoader.get(EPISODE_KEY));
        episodeCounter.processEpisode();
    }

    private void updateAgentStats(boolean gameWon) {
        final String STATS_KEY = "AGENT_STATS_FILEPATH";
        AgentStats agentStatsHandler = new AgentStats(envLoader.get(STATS_KEY));
        agentStatsHandler.processEpisode(gameWon);
    }
}
