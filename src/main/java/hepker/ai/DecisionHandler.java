package hepker.ai;

/**
 * Outlines logic required by Agent for AI integration
 * */
public interface DecisionHandler {

    void updateDecisionContainer();

    int getNumDecisions();
}
