package org.battleship.controller.user.remote.requests;

import org.battleship.controller.user.remote.responses.SimpleCallback;
import org.battleship.model.boards.BoardSquare;

import java.io.Serializable;


public class MarkOpponentsSquareAsBittedRequest extends Request{
    BoardSquare bittedSquare;

    public MarkOpponentsSquareAsBittedRequest(BoardSquare bittedSquare) {
        this.bittedSquare = bittedSquare;
    }

    @Override
    public void sendCallback() {
    }

    @Override
    public void requestReceivedAction() {
        eventWorker.markOpponentsSquareAsBitted(bittedSquare);
    }
}
