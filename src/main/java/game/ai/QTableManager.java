package main.java.game.ai;

import java.sql.*;
import java.util.*;

class QTableManager {
    private final String url = "jdbc:mysql://localhost:3306/game";
    private AgentTools toolbox;
    private HashMap<String, double[]> qTable;

    /*
    * qTable contains a hexadecimal serialized String of the gamestate which
    * corresponds to an array of Q-values resulting from actions
    * TODO: initializeQMap must import map of q values
    * */
    public QTableManager(AgentTools toolbox) {
        this.toolbox = toolbox;
        this.qTable = new HashMap<>();
        this.qTable = fetchQTable();
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

    public HashMap<String, double[]> fetchQTable() {
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



    private String[] fetchUniqueKeys() {
        List<String> uniqueKeys = new ArrayList<>();

        String sql = "SELECT DISTINCT key FROM QTable";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                uniqueKeys.add(rs.getString("key"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching unique keys: " + e.getMessage());
        }

        return uniqueKeys.toArray(new String[0]);
    }


    private class SQLDatabase {
        public void createTable() {
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

        public void updateQData(String serialKey, int qIndex, double qValue) {
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

        public Optional<Double> getData(String serialKey, int qIndex) {
            String sql = "SELECT q_value FROM QTable WHERE key = ? AND q_index = ?";

            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement ppdStmt = connection.prepareStatement(sql)) {

                ppdStmt.setString(1, serialKey);
                ppdStmt.setInt(2, qIndex);

                try (ResultSet rs = ppdStmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(rs.getDouble("q_value"));
                    } else {
                        System.out.println("No matching data found for key " + serialKey + " and index " + qIndex);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving data: " + e.getMessage());
            }
            return Optional.empty();  // Return empty if not found
        }

        public void displayAllData() {
            String sql = "SELECT key, q_index, q_value FROM QTable";

            try (Connection connection = DriverManager.getConnection(url);
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                // Print the column headers
                System.out.println("Key\t\tQ_Index\tQ_Value");
                System.out.println("----------------------------------");

                // Print all rows
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
