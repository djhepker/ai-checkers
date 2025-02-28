package hepker.engine.agentintegration;

import hepker.ai.ai.Agent;

record AgentRecord(Agent agent, Environment environment, AIDecisionHandler decisionHandler) {

    public Agent getAgent() {
        return agent;
    }

    public AIDecisionHandler getDecisionHandler() {
        return decisionHandler;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
