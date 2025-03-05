package hepker.ai.utils;

import hepker.utils.FileLoader;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class AgentStats {

    private static final String AGENT_WIN_KEY = "win_count=";
    private static final String AGENT_LOSS_KEY = "loss_count=";
    private static final String AGENT_WINRATE_KEY = "win_percentage=";
    private final FileLoader fileMgr;
    private int agentWinCount;
    private int agentLossCount;

    public AgentStats(String filePath) {
        this.fileMgr = new FileLoader(filePath);
        initializeAgentStats(this.fileMgr);
    }

    public void initializeAgentStats(FileLoader fileLoader) {
        try {
            agentWinCount = Integer.parseInt(
                    fileLoader.getLine(AGENT_WIN_KEY).replace(AGENT_WIN_KEY, "").trim());
            agentLossCount = Integer.parseInt(
                    fileLoader.getLine(AGENT_LOSS_KEY).replace(AGENT_LOSS_KEY, "").trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processEpisode(boolean gameWon) {
        if (gameWon) {
            agentWinCount++;
            fileMgr.updateLineByKey(AGENT_WIN_KEY, Integer.toString(agentWinCount));
        } else {
            agentLossCount++;
            fileMgr.updateLineByKey(AGENT_LOSS_KEY, Integer.toString(agentLossCount));
        }
        BigDecimal winRatio = new BigDecimal(((float) agentWinCount / (agentWinCount + agentLossCount)) * 100)
                .setScale(2, RoundingMode.HALF_UP);
        fileMgr.updateLineByKey(AGENT_WINRATE_KEY, winRatio.toString());
    }
}
