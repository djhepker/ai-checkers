package hepker.ai.ai;

import hepker.ai.table.DataManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

/**
 * STATE: pieces[] converted to a byte String
 * ACTION: Moving pieces when it is Agent's turn
 * REWARD: Positive, Negative, & Neutral
 * EPISODE: GameEngine calls updateGame() in-between episodes
 * Q-VALUE: Metrics used to evaluate actions at specific states
 * MODEL: Q(S,a,S') ─► Model "Q" is action "a" given state "S" results in "S'"
 * └► P(S'|S,a) = Probability of reaching a state "S'" if action "a" is taken in state "S"
 * "A" is the set of all possible actions
 * "A(s)" defines the set of actions that can be taken while in state "S"
 * POLICY: A mapping from "S" to "a"; a solution to the Markov decision process. Indicates
 * action "a" is to be taken while in state "S"
 * Class utilizes lombok @Data; Member variables all have getters, setters, default constructor, and all arg
 * constructor
 *
 */
@Getter
@Setter
public final class Agent {
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private static final DataManager Q_TABLE_MGR;

    static {
        Q_TABLE_MGR = new DataManager();
    }

    private double gamma = 0.90;
    private double alpha = 0.82;
    private double epsilon = 0.85;
    private double rho = 0.0;
    private double currentQ = 0.0;
    private double maxQPrime = 0.0;

    private String stateKey;

    /**
     * Empty default constructor for a new Agent. Member variables are initialized prior
     * stateKey = null (You need to set this prior to calling updateQValue() or getActionInt())
     * gamma = 0.90 (How much the agent values learned information; delayed gratification vs instantaneous)
     * alpha = 0.82 (Rate at which Agent learns)
     * epsilon = 0.85 (Probability that Agent will select a random action)
     * rho = 0.0 (updated throughout training, set to 0.0 unless testing)
     * currentQ = 0.0 (updated by loadCurrentQ())
     * maxQPrime = 0.0 (This is updated by calculateMaxQPrime())
     *
     */
    public Agent() {

    }

    /**
     * Parameterized constructor for initializing Agent with custom values
     * @param inputGamma Percentage Agent values delayed reward/gratification
     * @param inputAlpha Percentage Agent values learning
     * @param inputEpsilon Percent chance Agent will make a random decision
     */
    public Agent(double inputGamma, double inputAlpha, double inputEpsilon) {
        this.gamma = inputGamma;
        this.alpha = inputAlpha;
        this.epsilon = inputEpsilon;
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

    public void loadCurrentQ(String inputStateKey, int actionChoiceInt) {
        this.currentQ = getQValue(inputStateKey, actionChoiceInt);
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

    public static void finalizeQTableUpdate() {
        Q_TABLE_MGR.updateData();
    }

    public void setEpsilon(double inputEpsilon) {
        this.epsilon = inputEpsilon < 0.0 ? 0.0 : Math.min(inputEpsilon, 1.0);
    }

    public static void closeDatabase() {
        if (Q_TABLE_MGR != null) {
            Q_TABLE_MGR.close();
        }
    }

    private int exploit() {
        return Q_TABLE_MGR.getMaxQIndex(stateKey);
    }

    private int explore(int numDecisions) {
        return new Random().nextInt(numDecisions);
    }

    private double getQValue(String inputStateKey, int moveChoice) {
        return Q_TABLE_MGR.queryQTableForValue(inputStateKey, moveChoice);
    }

    private void updateQValue(int moveChoice) {
        double updatedQ = currentQ + alpha * (rho + gamma * maxQPrime - currentQ);
        if (Math.abs(updatedQ - currentQ) > 0.01) {
            Q_TABLE_MGR.putUpdatedValue(stateKey, moveChoice, updatedQ);
        }
    }
}