package main.java.ai.table;

import main.java.ai.utils.AgentStats;
import main.java.ai.utils.EpisodeCounter;
import main.java.game.utils.EnvLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

public class DataManager {

    private static final Logger logger = LoggerFactory.getLogger(DataManager.class);

    private HashMap<String, double[]> updatedQValues;

    private final String SQL_URL_KEY = "SQL_URL";
    private final String ENV_FILEPATH = ".env";

    private final QValueRepository db;
    private final EnvLoader envLoader;


    public DataManager() {
        this.envLoader = new EnvLoader(ENV_FILEPATH);
        QValueRepository tempDb;
        try {
            tempDb = new QValueRepository(envLoader.get(SQL_URL_KEY));
            logger.info("Successfully initialized QValueRepository with URL: {}", envLoader.get(SQL_URL_KEY));
        } catch (SQLException e) {
            logger.error("Failed to initialize QValueRepository with URL: {}", envLoader.get(SQL_URL_KEY), e);
            throw new RuntimeException("Database initialization failed", e);
        }
        this.db = tempDb;
        this.updatedQValues = new HashMap<>();
    }

    public int getMaxQIndex(String serialKey) {
        try {
            int maxQIndex = db.getMaxQAction(serialKey);
            logger.debug("Retrieved max Q index {} for serialKey: {}", maxQIndex, serialKey);
            return maxQIndex;
        } catch (SQLException e) {
            logger.error("Failed to get max Q index for serialKey: {}", serialKey, e);
            return 0;
        }
    }

    public boolean isWithinSize(String serialKey, int qIndex) {
        return updatedQValues.containsKey(serialKey) && updatedQValues.get(serialKey).length > qIndex;
    }

    public double queryQTableForValue(String serialKey, int decisionNumber) {
        try {
            double qValue = db.getQValueFromTable(serialKey, decisionNumber);
            logger.debug("Queried Q value {} for serialKey: {}, decision: {}", qValue, serialKey, decisionNumber);
            return qValue;
        } catch (SQLException e) {
            logger.error("Failed to query Q value for serialKey: {}, decision: {}", serialKey, decisionNumber, e);
            return 0.0;
        }
    }

    public double getMaxQValue(String serialKey) {
        try {
            double maxQValue = db.getMaxQValue(serialKey);
            logger.debug("Retrieved max Q value {} for serialKey: {}", maxQValue, serialKey);
            return maxQValue;
        } catch (SQLException e) {
            logger.error("Failed to get max Q value for serialKey: {}", serialKey, e);
            return 0.0;
        }
    }

    public void putUpdatedValue(String serialKey, int index, double inputQ) {
        updatedQValues.compute(serialKey, (key, existingArray) -> {
            if (existingArray == null) {
                double[] newArray = new double[index + 1];
                newArray[index] = inputQ;
                logger.debug("Created new Q array for serialKey: {}, index: {}, value: {}", serialKey, index, inputQ);
                return newArray;
            } else if (index >= existingArray.length) {
                double[] newArray = Arrays.copyOf(existingArray, index + 1);
                newArray[index] = inputQ;
                logger.debug("Expanded Q array for serialKey: {}, index: {}, value: {}", serialKey, index, inputQ);
                return newArray;
            } else {
                existingArray[index] = inputQ;
                logger.debug("Updated Q value for serialKey: {}, index: {}, value: {}", serialKey, index, inputQ);
                return existingArray;
            }
        });
    }

    public void updateData(boolean episodeOver) {
        try {
            db.updateQTable(updatedQValues);
            logger.info("Successfully updated QTable with {} entries", updatedQValues.size());
        } catch (SQLException e) {
            logger.error("Failed to update QTable with {} entries", updatedQValues.size(), e);
        }
        try {
            db.close();
            logger.info("Database connection pool closed successfully");
        } catch (SQLException e) {
            logger.error("Failed to close database connection pool", e);
        }

        updateEpisodes();
        updateAgentStats(episodeOver);
    }

    private void updateEpisodes() {
        final String EPISODE_KEY = "EPISODE_COUNT_FILEPATH";
        EpisodeCounter episodeCounter = new EpisodeCounter(envLoader.get(EPISODE_KEY));
        try {
            episodeCounter.processEpisode();
            logger.info("Successfully updated episode count");
        } catch (Exception e) {
            logger.error("Failed to update episode count", e);
        }
    }

    private void updateAgentStats(boolean gameWon) {
        final String STATS_KEY = "AGENT_STATS_FILEPATH";
        AgentStats agentStatsHandler = new AgentStats(envLoader.get(STATS_KEY));
        try {
            agentStatsHandler.processEpisode(gameWon);
            logger.info("Successfully updated agent stats, gameWon: {}", gameWon);
        } catch (Exception e) { // Assuming processEpisode might throw a generic Exception
            logger.error("Failed to update agent stats, gameWon: {}", gameWon, e);
        }
    }
}