package hepker.ai.agentintegration;

import hepker.ai.Agent;
import hepker.game.gameworld.PieceManager;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for connecting game to AI
 * */
public final class AIEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(AIEngine.class);

    private final List<AgentRecord> agents;
    private final PieceManager pMgr;
    @Getter
    @Setter
    private static int numTurns = 0;

    private int agentTurnSwitch;

    private String stateKey;

    public AIEngine(PieceManager inputPMgr, boolean lightChosen, String gameTypeString) {
        this.agentTurnSwitch = 0;
        this.stateKey = "";
        this.pMgr = inputPMgr;
        this.agents = new ArrayList<>();
        loadGameState(gameTypeString, lightChosen);
    }

    public void update() {
        if (agentTurnSwitch == 0) {
            ++numTurns;
        }
        try {
            updateAgent(agents.get(agentTurnSwitch).agent(), agents.get(agentTurnSwitch).decisionHandler());
        } catch (Exception e) {
            StringBuilder errorBuilder = new StringBuilder()
                    .append("Agent Manager Exception ")
                    .append("During Agent ")
                    .append(agentTurnSwitch)
                    .append(" Turn ");
            LOGGER.error(errorBuilder.toString(), e);
        }
    }

    private void updateAgent(Agent inputAgent, AIDecisionHandler aiActions) {
        stateKey = aiActions.generateStateKey();
        inputAgent.setStateKey(stateKey);
        aiActions.updateDecisionContainer();
        int numDecisions = aiActions.getNumDecisions();
        if (numDecisions == 0) {
            pMgr.flagGameOver();
            LOGGER.info("{} Agent has no decisions.", aiActions.getPieceColor());
            return;
        }

        int actionChoiceInt = inputAgent.getActionInt(numDecisions);
        inputAgent.loadCurrentQ(stateKey, actionChoiceInt);
        aiActions.setPreDecisionRewardParameters(actionChoiceInt);
        aiActions.performAction(actionChoiceInt);

        aiActions.updateDecisionContainer();
        String stateKeyPrime = aiActions.generateStateKey();
        inputAgent.setRho(aiActions.getDecisionReward());

        inputAgent.learn(stateKeyPrime, actionChoiceInt);
    }

    private void loadGameState(String gameTypeString, boolean lightChosen) {
        switch (gameTypeString) {
            case "Agent Vs Player" -> generateAgent(false, lightChosen);
            case "Stochastic Vs Player" -> generateAgent(true, lightChosen);
            case "Agent Vs Stochastic" -> {
                generateAgent(true, !lightChosen);
                generateAgent(false, lightChosen);
            }
            case "Agent Vs Agent" -> {
                generateAgent(false, !lightChosen);
                generateAgent(false, lightChosen);
                for (AgentRecord record : agents) {
                    record.agent.setEpsilon(0.6);
                }
            }
            default -> throw new IllegalArgumentException("Invalid gameTypeString: " + gameTypeString);
        }
    }

    public void finishGame(boolean gameWon) {
        new AgentStats("src/main/resources/data/agentstats").processEpisode(gameWon);
        Agent.pushQTableUpdate();
    }

    public void flipAgentSwitch() {
        agentTurnSwitch ^= 1;
    }

    public boolean agentZeroTurn() {
        return agentTurnSwitch == 0;
    }

    /**
     * Generates a new agent. If stochastic, Epsilon will be set to 1.0 (full random decisions)
     * @param isStochastic true if fully agent is stochastic, no use of learned information
     * @param duskyAgent True if this agent uses dark pieces
     */
    private void generateAgent(boolean isStochastic, boolean duskyAgent) {
        Agent zero = new Agent();
        if (isStochastic) {
            zero.setEpsilon(1.0);
        } else {
            zero.setEpsilon(6.0);
        }
        agents.add(new AgentRecord(zero, new AIDecisionHandler(pMgr, duskyAgent)));
        LOGGER.info("Generated agent: stochastic={}, color={}, number: ",
                isStochastic, duskyAgent ? "DUSKY" : "LIGHT", agents.size() - 1);
    }

    record AgentRecord(Agent agent, AIDecisionHandler decisionHandler) {

    }
}