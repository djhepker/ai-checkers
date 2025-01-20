package main.java.game.ai;

import java.util.Arrays;
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

    public double getMaxQOfState(String serialKey) {
        if (qTable.containsKey(serialKey)) {
            double[] qValues = qTable.get(serialKey);
            return Arrays.stream(qValues).max().getAsDouble();
        }
        System.out.println("Error, key not found");
        System.exit(0);
        return 0.0;
    }

    public double[] getQValuesOfState(String stateKey) {
        if (!qTable.containsKey(stateKey)) {
            System.exit(2);
        } else {
            return qTable.get(stateKey);
        }
        return new double[0];
    }

    public int getTableSize() {
        return qTable.size();
    }

    public HashMap<String, double[]> initializeQTable() {
        return null;
    }
}
