package hepker.ai.ai;

import hepker.ai.table.DataManager;
import lombok.Setter;

import java.util.Random;

public final class Agent implements AI {
    private static final DataManager Q_TABLE_MGR;

    static {
        Q_TABLE_MGR = new DataManager();
    }

    private final double gamma = 0.9;
    private final double alpha = 0.82;
    private double epsilon = 0.7;
    private double rho;
    private double currentQ;
    private double maxQPrime;

    @Setter
    private String stateKey;

    public Agent() {
        this.currentQ = 0.0;
        this.maxQPrime = 0.0;
        this.rho = 0.0;
    }

    public void update(String stateKeyPrime, int actionChoiceInt) {
        calculateMaxQPrime(stateKeyPrime);
        updateQValue(actionChoiceInt);
    }

    public int getActionInt(int numDecisions) {
        if (Math.random() < epsilon) {
            return explore(numDecisions);
        } else {
            return exploit();
        }
    }

    public void updateCurrentQ(String inputStateKey, int actionChoiceInt) {
        this.currentQ = getQValue(inputStateKey, actionChoiceInt);
    }

    private int exploit() {
        return Q_TABLE_MGR.getMaxQIndex(stateKey);
    }

    private int explore(int numDecisions) {
        return new Random().nextInt(numDecisions);
    }

    private double getQValue(String inputStateKey, int moveChoice) {
        if (Q_TABLE_MGR.isWithinSize(inputStateKey, moveChoice)) {
            return Q_TABLE_MGR.queryQTableForValue(inputStateKey, moveChoice);
        } else {
            return 0.0;
        }
    }

    public void updateRho(double updatedReward) {
        this.rho = updatedReward;
    }

    public void calculateMaxQPrime(String stateKeyPrimeString) {
        if (epsilon == 0.0) {
            return;
        }
        this.maxQPrime = Q_TABLE_MGR.getMaxQValue(stateKeyPrimeString);
    }

    public void finalizeQTableUpdate() {
        Q_TABLE_MGR.updateData();
    }

    public void setEpsilon(double inputEpsilon) {
        this.epsilon = inputEpsilon < 0.0 ? 0.0 : Math.min(inputEpsilon, 1.0);
    }

    private void updateQValue(int moveChoice) {
        double updatedQ = currentQ + alpha * (rho + gamma * maxQPrime - currentQ);
        if (Math.abs(updatedQ - currentQ) > 0.01) {
            Q_TABLE_MGR.putUpdatedValue(stateKey, moveChoice, updatedQ);
        }
    }

    public static void closeDatabase() {
        if (Q_TABLE_MGR != null) {
            Q_TABLE_MGR.close();
        }
    }
}