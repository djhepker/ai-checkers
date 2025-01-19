package main.java.ai;

import java.util.HashMap;

class QTableManager {
    private AgentTools toolbox;
    private HashMap<String, double[]> qTable;

    public QTableManager(AgentTools toolbox) {
        this.toolbox = toolbox;
        this.qTable = new HashMap<>();
    }
}
