package main.java.game.ai;

import java.util.HashMap;

class QTableManager {
    private AgentTools toolbox;
    private HashMap<String, double[]> qTable;
    private double[] qValues;

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

    public void updateQValues(String stateKey) {
        if (!qTable.containsKey(stateKey)) {
            System.exit(2);
        }
        qValues = qTable.get(stateKey);
    }

    public int getMaxQIndex() {
        int maxQIndex = 0;
        double maxQValue = Double.MIN_VALUE;
        for (int i = 0; i < qValues.length; i++) {
            if (maxQValue < qValues[i]) {
                maxQValue = qValues[i];
                maxQIndex = i;
            }
        }
        return maxQIndex;
    }

    public double getQValue(int qIndex) {
        return qValues[qIndex];
    }

    public HashMap<String, double[]> initializeQTable() {
        // TODO: logic to import table
        return null;
    }
}
