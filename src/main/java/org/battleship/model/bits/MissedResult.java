package org.battleship.model.bits;


import org.battleship.model.boards.BoardSquare;

public class MissedResult extends BitResult {

    private MissedActivity missedActivity = event;
    private BoardSquare missedBorderSquare;

    public MissedResult(BoardSquare missedBorderSquare) {
        this.missedBorderSquare = missedBorderSquare;
    }

    public void afterResultReceivedAction() {
        missedActivity.markBorderSquareAsMissed(missedBorderSquare);
    }

    public void setMissedActivity(MissedActivity missedActivity) {
        this.missedActivity = missedActivity;
    }
}
