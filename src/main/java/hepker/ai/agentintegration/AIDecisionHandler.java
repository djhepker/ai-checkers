package hepker.ai.agentintegration;

import hepker.game.entity.GameBoardPiece;
import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;
import hepker.utils.AIEnvironment;
import lombok.Getter;

import static hepker.game.entity.GameBoardPiece.PieceColor.DUSKY;

/**
 * AI Utility for calculations
 * */
public final class AIDecisionHandler implements AIEnvironment {
    private final PieceManager pMgr;

    private int numOptionsNaught;
    private int numEnemyOptionsNaught;
    private int pointsFromDecision;
    private int numEnemiesNaught;
    private int reasonableTurnCount;

    @Getter
    private final GameBoardPiece.PieceColor pieceColor;

    private double decayingScalar;

    private ActionNode[] decisionArray;

    public AIDecisionHandler(PieceManager inputPMgr, boolean isDusky) {
        this.pieceColor = isDusky ? GameBoardPiece.PieceColor.DUSKY : GameBoardPiece.PieceColor.LIGHT;
        this.pMgr = inputPMgr;
        this.reasonableTurnCount = 40;
        this.decayingScalar = 1.0;
    }

    public String generateStateKey() {
        return AITools.getEncryptedGameStateString(pMgr, pieceColor == DUSKY);
    }

    public void updateDecisionContainer() {
        decisionArray = AITools.getDecisionArray(pMgr, pieceColor);
    }

    public double getDecisionReward() {
        double ratioOptions = (double) decisionArray.length / AITools.getNumOpponentOptions(pMgr, pieceColor)
                - (double) numOptionsNaught / numEnemyOptionsNaught;
        double alliedPieces = pieceColor == DUSKY ? pMgr.getNumDusky() : pMgr.getNumLight();
        double ratioPieces = alliedPieces / (pieceColor == DUSKY ? pMgr.getNumLight() : pMgr.getNumDusky())
                - alliedPieces / numEnemiesNaught;
        int pointsEarned = pointsFromDecision - AITools.getMaximumOpponentReward(pMgr, pieceColor);
        double reward = ratioOptions + ratioPieces + pointsEarned;
        if (reasonableTurnCount < 0) {
            decayingScalar -= 0.1;
            return reward > 0 ? decayingScalar * reward : -Math.abs(reward * decayingScalar);
        } else {
            --reasonableTurnCount;
            return reward;
        }
    }

    public int getNumDecisions() {
        return decisionArray.length;
    }

    public void performAction(int moveChoice) throws ArrayIndexOutOfBoundsException {
        pMgr.machineMovePiece(decisionArray[moveChoice]);
        pMgr.updateAllPieces();
    }

    public void setPreDecisionRewardParameters(int moveChoice) throws ArrayIndexOutOfBoundsException {
        setPreDecisionRewardParameters(decisionArray[moveChoice]);
    }

    public void setPreDecisionRewardParameters(ActionNode actionChosen) {
        this.numEnemiesNaught = pieceColor == DUSKY ? pMgr.getNumLight() : pMgr.getNumDusky();
        this.numOptionsNaught = decisionArray.length;
        this.numEnemyOptionsNaught = AITools.getNumOpponentOptions(pMgr, pieceColor);
        pointsFromDecision = actionChosen.getReward();
    }
}
