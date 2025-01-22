package main.java.game.ai;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
                    + "id TEXT PRIMARY KEY,\n"
                    + "q_index INTEGER NOT NULL,\n"
                    + "value REAL NOT NULL\n"
                    + ");";

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:database_name.db");
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Table created successfully.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


        public void insert(String id, int qIndex, double qValue) {

        }
    }
}
