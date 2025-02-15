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
    private HashMap<String, double[]> qTable;
    private SQLITEDatabase db;

    public QTableManager() {
        this.db = new SQLITEDatabase();
        this.qTable = new HashMap<>();
        this.db.createTable();
        this.qTable = db.fetchQTable();
    }

    public int getMaxQIndex(String serialKey) {
        int maxQIndex = 0;
        double maxQValue = Double.MIN_VALUE;
        double[] qValues = qTable.get(serialKey);
        if (qValues != null) {
            for (int i = 0; i < qValues.length; i++) {
                if (maxQValue < qValues[i]) {
                    maxQValue = qValues[i];
                    maxQIndex = i;
                }
            }
            return maxQIndex;
        }
        return maxQIndex;
    }

    public boolean isWithinSize(String serialKey, int qIndex) {
        return qTable.containsKey(serialKey) && qTable.get(serialKey).length > qIndex;
    }

    public double getQValue(String serialKey, int qIndex) {
        return qTable.get(serialKey)[qIndex];
    }

    public void setQValue(String serialKey, int index, double inputQ) {
        if (!qTable.containsKey(serialKey)) {
            qTable.put(serialKey, new double[index + 1]);
        }
        double[] qValues = qTable.get(serialKey);
        if (index >= qValues.length) {
            qValues = Arrays.copyOf(qValues, index + 1);
            qTable.put(serialKey, qValues);
        }
        qValues[index] = inputQ;
    }


    public void updateQData() {
        db.updateQTable(qTable);
        db.updateEpisodes();
    }

    private class SQLITEDatabase {
        private final String SQL_URL_KEY = "SQL_URL";
        private final String ENV_FILEPATH = ".env";
        private final String url;
        private EnvLoader envLoader;

        public SQLITEDatabase() {
            this.envLoader = new EnvLoader(ENV_FILEPATH);
            this.url = envLoader.get(SQL_URL_KEY);
        }

        public void createTable() {
            final String sql = "CREATE TABLE IF NOT EXISTS QTable (\n"
                    + "key TEXT NOT NULL,\n"
                    + "q_index INTEGER NOT NULL,\n"
                    + "q_value REAL NOT NULL,\n"
                    + "PRIMARY KEY (key, q_index)\n"
                    + ");";
            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Table created successfully.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        public HashMap<String, double[]> fetchQTable() {
            HashMap<String, double[]> qTable = new HashMap<>();
            final String sql = "SELECT key, q_index, q_value FROM QTable";
            try (Connection connection = DriverManager.getConnection(url);
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String key = rs.getString("key");
                    int qIndex = rs.getInt("q_index");
                    double qValue = rs.getDouble("q_value");
                    double[] qValues = qTable.getOrDefault(key, new double[qIndex + 1]);
                    if (qValues.length <= qIndex) {
                        qValues = Arrays.copyOf(qValues, qIndex + 1);
                        qTable.put(key, qValues);
                    }
                    qValues[qIndex] = qValue;
                }
            } catch (SQLException e) {
                System.out.println("Error fetching Q-table data: " + e.getMessage());
            }
            return qTable;
        }

        public void updateQTable(Map<String, double[]> qTable) {
            final String sql = "INSERT OR REPLACE INTO QTable (key, q_index, q_value) VALUES (?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement ppdStmt = connection.prepareStatement(sql)) {
                for (Map.Entry<String, double[]> entry : qTable.entrySet()) {
                    String key = entry.getKey();
                    double[] qValues = entry.getValue();
                    for (int i = 0; i < qValues.length; i++) {
                        if (Double.isNaN(qValues[i])) {
                            continue;  // Skip NaN values
                        }
                        ppdStmt.setString(1, key);
                        ppdStmt.setInt(2, i);
                        ppdStmt.setDouble(3, qValues[i]);
                        ppdStmt.addBatch();
                    }
                }
                int[] result = ppdStmt.executeBatch();
                System.out.println("Updated " + result.length + " rows in the QTable.");
            } catch (SQLException e) {
                System.out.println("Error updating QTable: " + e.getMessage());
                e.printStackTrace();
            }
        }

        public void displayAllData() {
            System.out.println("Displaying all QTable data.");
            final String sql = "SELECT key, q_index, q_value FROM QTable";
            try (Connection connection = DriverManager.getConnection(url);
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                System.out.println("Key\t\tQ_Index\tQ_Value");
                System.out.println("----------------------------------");
                while (rs.next()) {
                    String key = rs.getString("key");
                    int qIndex = rs.getInt("q_index");
                    double qValue = rs.getDouble("q_value");
                    System.out.println(key + "\t" + qIndex + "\t" + qValue);
                }
            } catch (SQLException e) {
                System.out.println("Error displaying data: " + e.getMessage());
            }
        }

        public void updateEpisodes() {
            final String EPISODE_KEY = "EPISODE_COUNT_FILEPATH";
            EpisodeCounter episodeCounter = new EpisodeCounter(envLoader.get(EPISODE_KEY));
            episodeCounter.processEpisode();
            System.out.printf("Number of episodes: %d\n", episodeCounter.getEpisodeCount());
        }
    }
}
