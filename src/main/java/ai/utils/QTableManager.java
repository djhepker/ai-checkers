package main.java.ai.utils;

import main.java.game.utils.EnvLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QTableManager {
    private HashMap<String, double[]> updatedQValues;

    private final DataBaseHandler db;

    public QTableManager() {
        this.db = new DataBaseHandler();
        this.db.createTable();
        this.updatedQValues = new HashMap<>();
    }

    public int getMaxQIndex(String serialKey) {
        return db.getMaxQAction(serialKey);
    }

    public boolean isWithinSize(String serialKey, int qIndex) {
        return updatedQValues.containsKey(serialKey) && updatedQValues.get(serialKey).length > qIndex;
    }

    public double queryQTableForValue(String serialKey, int moveChoice) {
        return db.getQValueFromTable(serialKey, moveChoice);
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

    public void updateQData(boolean gameWon) {
        printUpdatedValues();
        db.updateQTable(updatedQValues);
        db.updateEpisodes();
        db.updateAgentStats(gameWon);
    }

    private void printUpdatedValues() {
        for (String key : updatedQValues.keySet()) {
            for (int i = 0; i < updatedQValues.get(key).length; i++) {
                System.out.println(key + ": " + updatedQValues.get(key)[i]);
            }
        }
    }

    private class DataBaseHandler {
        private final String SQL_URL_KEY = "SQL_URL";
        private final String ENV_FILEPATH = ".env";
        private final String url;
        private EnvLoader envLoader;

        public DataBaseHandler() {
            this.envLoader = new EnvLoader(ENV_FILEPATH);
            this.url = envLoader.get(SQL_URL_KEY);
        }

        public void createTable() {
            final String sqlCreateTable = "CREATE TABLE IF NOT EXISTS QTable ("
                    + "HexKey TEXT NOT NULL, "
                    + "Action INTEGER NOT NULL, "
                    + "QValue REAL NOT NULL, "
                    + "PRIMARY KEY (HexKey, Action) "
                    + ") WITHOUT ROWID;";

            final String sqlCreateIndex = "CREATE INDEX IF NOT EXISTS idx_qvalue ON QTable (HexKey, QValue DESC);";

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sqlCreateTable);  // Create table if not exists
                stmt.execute(sqlCreateIndex);  // Create index if not exists
            } catch (SQLException e) {
                System.out.println("Error creating table or index: " + e.getMessage());
            }
        }

        public int getMaxQAction(String serialKey) {
            final String sql = "SELECT Action FROM QTable WHERE HexKey = ? ORDER BY QValue DESC LIMIT 1";
            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, serialKey);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("Action");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error fetching max Q-action: " + e.getMessage());
            }
            return 0;
        }

        public double getQValueFromTable(String serialKey, int moveChoice) {
            final String sql = "SELECT QValue FROM QTable WHERE HexKey = ? AND Action = ?";
            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, serialKey);
                pstmt.setInt(2, moveChoice);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble("QValue");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error fetching Q-value: " + e.getMessage());
            }
            return 0.0;
        }

        public double getMaxQValue(String serialKey) {
            final String sql = "SELECT MAX(QValue) AS maxQValue FROM QTable WHERE HexKey = ?";
            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, serialKey);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble("maxQValue");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error fetching max Q-value: " + e.getMessage());
            }
            return 0.0;
        }

        public void updateQTable(Map<String, double[]> qTable) {
            final String sql = "INSERT OR REPLACE INTO QTable (HexKey, Action, QValue) VALUES (?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement ppdStmt = connection.prepareStatement(sql)) {
                connection.setAutoCommit(false); // Speed optimization for batch inserts

                for (Map.Entry<String, double[]> entry : qTable.entrySet()) {
                    String key = entry.getKey();
                    double[] qValues = entry.getValue();
                    for (int i = 0; i < qValues.length; i++) {
                        if (Double.isNaN(qValues[i])) {
                            continue;
                        }
                        ppdStmt.setString(1, key);
                        ppdStmt.setInt(2, i);
                        ppdStmt.setDouble(3, qValues[i]);
                        ppdStmt.addBatch();
                    }
                }
                ppdStmt.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                System.out.println("Error updating QTable: " + e.getMessage());
                e.printStackTrace();
            }
        }

        public void updateEpisodes() {
            final String EPISODE_KEY = "EPISODE_COUNT_FILEPATH";
            EpisodeCounter episodeCounter = new EpisodeCounter(envLoader.get(EPISODE_KEY));
            episodeCounter.processEpisode();
        }

        public void updateAgentStats(boolean gameWon) {
            final String STATS_KEY = "AGENT_STATS_FILEPATH";
            AgentStats agentStatsHandler = new AgentStats(envLoader.get(STATS_KEY));
            agentStatsHandler.processEpisode(gameWon);
        }
    }

}
