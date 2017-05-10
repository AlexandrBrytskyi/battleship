package org.battleship.controller.user.remote.requests;

import org.battleship.model.boards.BoardSquare;


public class MarkOpponentsBorderSquareAsMissedRequest extends Request {
    private BoardSquare missedBorderSquare;

    public MarkOpponentsBorderSquareAsMissedRequest(BoardSquare missedBorderSquare) {
        this.missedBorderSquare = missedBorderSquare;
    }

    @Override
    public void sendCallback() {

    }

    @Override
    public void requestReceivedAction() {
        eventWorker.markOpponentsBorderSquareAsMissed(missedBorderSquare);
    }
}
