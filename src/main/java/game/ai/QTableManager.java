package main.java.game.ai;

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
}
