package org.battleship.model.bits;


import org.battleship.model.boards.BoardSquare;

public class MissedResult extends BitResult {

    private BoardSquare missedBorderSquare;

    public MissedResult(BoardSquare missedBorderSquare) {
        this.missedBorderSquare = missedBorderSquare;
    }

    public void afterResultReceivedAction(boolean attacker) {
        if (attacker) {
            event.markOpponentsBorderSquareAsMissed(missedBorderSquare);
        } else {
            event.mySquareChanged(missedBorderSquare);
        }
    }

}
