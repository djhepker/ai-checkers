package main.java.game.ai;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


class QTableManager {
    private HashMap<String, double[]> qTable;

    public QTableManager() {
        this.qTable = new HashMap<>();
        this.qTable = SQLDatabase.fetchQTable();
    }

    public int getMaxQIndex(String serialKey) {
        int maxQIndex = 0;
        double maxQValue = Double.MIN_VALUE;
        double[] qValues = qTable.get(serialKey);
        for (int i = 0; i < qValues.length; i++) {
            if (maxQValue < qValues[i]) {
                maxQValue = qValues[i];
                maxQIndex = i;
            }
        }
        return maxQIndex;
    }

    public double getQValue(String serialKey, int qIndex) {
        return qTable.get(serialKey)[qIndex];
    }

    public void setQValue(String serialKey, int index, double inputQ) {
        qTable.get(serialKey)[index] = inputQ;
    }

    private class SQLDatabase {
        private static final String url = "jdbc:mysql://localhost:3306/game";

        public static HashMap<String, double[]> fetchQTable() {
            HashMap<String, double[]> qTable = new HashMap<>();
            String sql = "SELECT key, q_index, q_value FROM QTable";
            try (Connection connection = DriverManager.getConnection(url);
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String key = rs.getString("key");
                    int qIndex = rs.getInt("q_index");
                    double qValue = rs.getDouble("q_value");
                    if (!qTable.containsKey(key)) {
                        qTable.put(key, new double[qIndex + 1]);
                    } else {
                        double[] qValues = qTable.get(key);
                        if (qValues.length <= qIndex) {
                            qValues = Arrays.copyOf(qValues, qIndex + 1);
                            qTable.put(key, qValues);
                        }
                    }
                    double[] qValues = qTable.get(key);
                    qValues[qIndex] = qValue;
                }
            } catch (SQLException e) {
                System.out.println("Error fetching Q-table data: " + e.getMessage());
            }
            return qTable;
        }

        public static void createTable() {
            String sql = "CREATE TABLE IF NOT EXISTS QTable (\n"
                    + "key TEXT PRIMARY KEY,\n"
                    + "q_index INTEGER NOT NULL,\n"
                    + "q_value REAL NOT NULL\n"
                    + "PRIMARY KEY (key, q_index)\n)"
                    + ");";
            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Table reference successfully.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        public static void updateQData(String serialKey, int qIndex, double qValue) {
            String sql = "INSERT INTO QTable (key, q_index, q_Value) VALUES (?,?,?) "
                    + "ON DUPLICATE KEY UPDATE q_value = VALUES(q_value)";
            try (Connection connection = DriverManager.getConnection(url);
            PreparedStatement ppdStmt = connection.prepareStatement(sql)) {
                ppdStmt.setString(1, serialKey);
                ppdStmt.setInt(2, qIndex);
                ppdStmt.setDouble(3, qValue);
                ppdStmt.executeUpdate();
                System.out.println("Inserted data successfully.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }

        public static void displayAllData() {
            String sql = "SELECT key, q_index, q_value FROM QTable";
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
    }
}
