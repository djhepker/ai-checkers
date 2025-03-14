package hepker.ai.table;

import hepker.utils.EnvLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DataManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);
    private static final String SQL_URL_KEY = "SQL_URL";
    private static final String ENV_FILEPATH = ".env";
    private static final double FAILURE_RETURN_VALUE = 0.0;

    private final ConcurrentHashMap<String, double[]> updatedQValues;
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
        this.updatedQValues = new ConcurrentHashMap<>();
    }

    public int getMaxQIndex(String serialKey) {
        try {
            return db.getMaxQAction(serialKey);
        } catch (SQLException e) {
            LOGGER.error("Failed to get max Q index for serialKey: {}", serialKey, e);
            return (int) FAILURE_RETURN_VALUE;
        }
    }

    public double queryQTableForValue(String serialKey, int decisionNumber) {
        try {
            return db.getQValueFromTable(serialKey, decisionNumber);
        } catch (SQLException e) {
            LOGGER.error("Failed to query Q value for serialKey: {}, decision: {}", serialKey, decisionNumber, e);
            return FAILURE_RETURN_VALUE;
        }
    }

    public double getMaxQValue(String serialKey) {
        try {
            return db.getMaxQValue(serialKey);
        } catch (SQLException e) {
            LOGGER.error("Failed to get max Q value for serialKey: {}", serialKey, e);
            return FAILURE_RETURN_VALUE;
        }
    }

    public void putUpdatedValue(String serialKey, int index, double inputQ) {
        updatedQValues.compute(serialKey, (key, existingArray) -> {
            double[] resultArray;
            if (existingArray == null) {
                resultArray = new double[index + 1];
            } else if (index >= existingArray.length) {
                resultArray = Arrays.copyOf(existingArray, index + 1);
            } else {
                resultArray = existingArray.clone();
            }
            resultArray[index] = inputQ;
            // LOGGER.debug("Updated Q value for serialKey: {}, index: {}, value: {}", serialKey, index, inputQ);
            return resultArray;
        });
    }

    public void updateData() {
        try {
            Map<String, double[]> snapshot = new ConcurrentHashMap<>(updatedQValues); // Snapshot for consistency
            if (!snapshot.isEmpty()) {
                db.updateQTable(snapshot);
                LOGGER.info("Successfully updated QTable with {} entries", updatedQValues.size());
                updatedQValues.clear();
            }
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