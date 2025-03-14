package hepker.ai.utils;

/**
 * Outlines logic required by Agent for AI integration
 *
 * */
public interface DecisionHandler {

    /**
     * Updates your custom container for actions/decisions. Could be an array, a list, or otherwise. All possible
     * decisions/actions the Agent may make in this particular state.
     * */
    void updateDecisionContainer();

    /**
     * The int number of decisions the Agent could possibly choose from in this scenario. If DecisionContainer
     * is an array, for example, this is simply myArr.length
     * */
    int getNumDecisions();

    /**
     * Implement logic to return the outcome of the Agent's decision. Reward is placed into learning calculation
     * for future use. Output should be an argument to agentObject.setQValue()
     * @return The reward the Agent receives for choosing this action in this state.
     */
    double getDecisionReward();
}
