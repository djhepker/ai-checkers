package hepker.ai.table;

import hepker.game.utils.EnvLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

public final class DataManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);
    private static final String SQL_URL_KEY = "SQL_URL";
    private static final String ENV_FILEPATH = ".env";

    private HashMap<String, double[]> updatedQValues;
    private final QValueRepository db;

    public DataManager() {
        QValueRepository tempDb;
        EnvLoader envLoader;
        String sqlKey = "No URL";
        try {
            envLoader = new EnvLoader(ENV_FILEPATH);
            sqlKey = envLoader.get(SQL_URL_KEY);
            tempDb = new QValueRepository(sqlKey);
            LOGGER.info("Successfully initialized QValueRepository with URL: {}", sqlKey);
        } catch (SQLException | IOException e) {
            LOGGER.error("Failed to initialize QValueRepository with URL: {}", sqlKey, e);
            throw new RuntimeException("Database initialization failed", e);
        }
        this.db = tempDb;
        this.updatedQValues = new HashMap<>();
    }

    public int getMaxQIndex(String serialKey) {
        try {
            return db.getMaxQAction(serialKey);
        } catch (SQLException e) {
            LOGGER.error("Failed to get max Q index for serialKey: {}", serialKey, e);
            return 0;
        }
    }

    public double queryQTableForValue(String serialKey, int decisionNumber) {
        try {
            return db.getQValueFromTable(serialKey, decisionNumber);
        } catch (SQLException e) {
            LOGGER.error("Failed to query Q value for serialKey: {}, decision: {}", serialKey, decisionNumber, e);
            return 0.0;
        }
    }

    public double getMaxQValue(String serialKey) {
        try {
            return db.getMaxQValue(serialKey);
        } catch (SQLException e) {
            LOGGER.error("Failed to get max Q value for serialKey: {}", serialKey, e);
            return 0.0;
        }
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
                LOGGER.debug("Updated Q value for serialKey: {}, index: {}, value: {}", serialKey, index, inputQ);
                return existingArray;
            }
        });
    }

    public void updateData() {
        try {
            db.updateQTable(updatedQValues);
            LOGGER.info("Successfully updated QTable with {} entries", updatedQValues.size());
        } catch (SQLException e) {
            LOGGER.error("Failed to update QTable with {} entries", updatedQValues.size(), e);
        }
    }

    public void close() {
        try {
            db.close();
            LOGGER.info("Database connection pool closed successfully");
        } catch (SQLException e) {
            LOGGER.error("Failed to close database connection pool", e);
        }
    }
}