package hepker.ai.ai;

import hepker.ai.table.DataManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

/**
 * STATE: A programmer-coded String value that is given to Agent to identify its learned values
 * ACTION: Moving pieces when it is Agent's turn
 * REWARD: Positive, Negative, & Neutral
 * EPISODE: An episode is a single training session for Agent. Ends when updateQTable() is called
 * Q-VALUE: Metrics used to evaluate actions at specific states
 * MODEL: Q(S,a,S') ─► Model "Q" is action "a" given state "S" results in "S'"
 * └► P(S'|S,a) = Probability of reaching a state "S'" if action "a" is taken in state "S"
 * "A" is the set of all possible actions
 * "A(s)" defines the set of actions that can be taken while in state "S"
 * POLICY: A mapping from "S" to "a"; a solution to the Markov decision process. Indicates
 * action "a" is to be taken while in state "S"
 * Class utilizes lombok @Getter, @Setter Member variables all have getters and setters
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
     * All values can be mutated and obtained with their respective getters,setters
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

    /**
     * Queues State Action pair to be placed into SQLite table
     *
     * @param stateKeyPrime The post-action encrypted state. Be sure to update your stateKey, post
     *                      action, before sending it as an argument
     * @param actionChoiceInt The index of the decision which resulted in stateKeyPrime. Should be
     *                        the return value of getActionInt()
     */
    public void update(String stateKeyPrime, int actionChoiceInt) {
        calculateMaxQPrime(stateKeyPrime);
        updateQValue(actionChoiceInt);
    }

    /**
     * Retrieves the decision of the Agent as an int. Decision will be [0, numDecisions) with
     * the only exception being if you are improperly creating stateKey
     *
     * @param numDecisions The upper limit of the number of decisions the Agent has to choose
     *                     from. Exclusive
     * @return An int value of [0,numDecisions)
     */
    public int getActionInt(int numDecisions) {
        if (Math.random() < epsilon) {
            return explore(numDecisions);
        } else {
            return exploit();
        }
    }

    /**
     * Retrieves Q(S,a) value from memory and sets this.currentQ
     *
     * @param inputStateKey The stateKey used to look up qValue from memory
     * @param actionChoiceInt The int representation of the decision chosen in this state. Typically, the
     *                        return value of getActionInt() unless debugging
     */
    public void loadCurrentQ(String inputStateKey, int actionChoiceInt) {
        this.currentQ = getQValue(inputStateKey, actionChoiceInt);
    }

    /**
     * Static method flags the Agent to push its newly learned values to memory storage. Must be called
     * at the end of every episode
     *
     */
    public static void finalizeQTableUpdate() {
        Q_TABLE_MGR.updateData();
    }

    /**
     * Safely closes the Agent's database. Call after finalizeQTableUpdate()
     *
     */
    public static void closeDatabase() {
        if (Q_TABLE_MGR != null) {
            Q_TABLE_MGR.close();
        }
    }

    /**
     * Retrieves from memory the maximum possible Q value in the given state and sets maxQPrime
     *
     * @param stateKeyPrimeString String representation of your post-action state
     */
    private void calculateMaxQPrime(String stateKeyPrimeString) {
        if (epsilon == 0.0) {
            return;
        }
        this.maxQPrime = Q_TABLE_MGR.getMaxQValue(stateKeyPrimeString);
    }

    /**
     * Retrieves the best-known action to take in the given state. Requires that stateKey be properly set first
     * agentObject.setStateKey(yourString);
     *
     * @return int index of the best-known action in the given state
     */
    private int exploit() {
        return Q_TABLE_MGR.getMaxQIndex(stateKey);
    }

    /**
     * Helper that retrieves a random decision [0,numDecisions)
     *
     * @param numDecisions Exclusive upper bound for decision-making
     * @return A random int [0,numDecisions)
     */
    private int explore(int numDecisions) {
        return new Random().nextInt(numDecisions);
    }

    /**
     * Retrieves the specific q-value for the given stateKey and action
     *
     * @param inputStateKey String stateKey representing this state
     * @param actionInt int representation of the Agent's decision in the given state
     * @return q-value of the given action in the given state
     */
    private double getQValue(String inputStateKey, int actionInt) {
        return Q_TABLE_MGR.queryQTableForValue(inputStateKey, actionInt);
    }

    /**
     * Calculates the new q-value after the Agent's decision has been chosen and applied to the state
     *
     * @param actionInt Index of the Agent's most-recent decision
     */
    private void updateQValue(int actionInt) {
        double updatedQ = currentQ + alpha * (rho + gamma * maxQPrime - currentQ);
        if (Math.abs(updatedQ - currentQ) > 0.01) {
            Q_TABLE_MGR.putUpdatedValue(stateKey, actionInt, updatedQ);
        }
    }
}