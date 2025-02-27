package hepker.ai;

/**
 * Outlines logic required by Agent for AI integration
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
}
