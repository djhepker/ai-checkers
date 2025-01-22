package main.java.game.ai;

import java.sql.*;
import java.util.HashMap;

class QTableManager {
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
        this.qTable = initializeQTable();
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

    public HashMap<String, double[]> initializeQTable() {
        // TODO: logic to import table
        return null;
    }

    private class SQLDatabase {
        private String url = "jdbc:mysql://localhost:3306/game";

        public void createTable() {
            String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                    + "key TEXT PRIMARY KEY,\n"
                    + "q_index INTEGER NOT NULL,\n"
                    + "q_value REAL NOT NULL\n"
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
            String sql = "INSERT INTO users (key, q_index, q_Value) VALUES (?,?,?) "
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

        public double getData(String serialKey, int qIndex) {
            String sql = "SELECT * FROM users WHERE key = ? AND q_index = ?";

            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement ppdStmt = connection.prepareStatement(sql)) {

                ppdStmt.setString(1, serialKey);
                ppdStmt.setInt(2, qIndex);

                try (ResultSet rs = ppdStmt.executeQuery()) {
                    if (rs.next()) {
                        double retrievedValue = rs.getDouble("q_value");

                        return retrievedValue;
                    } else {
                        System.out.println("No matching data found for key " + serialKey + " and index " + qIndex);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }
            return 0;
        }
    }
}
