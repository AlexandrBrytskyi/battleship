package org.battleship.controller.user.remote.requests;


import org.battleship.model.boards.BoardSquare;


public class MySquareChangedRequest extends Request {

    BoardSquare toUpdate;

    public MySquareChangedRequest(BoardSquare toUpdate) {
        this.toUpdate = toUpdate;
    }

    @Override
    public void sendCallback() {
    }

    @Override
    public void requestReceivedAction() {
        eventWorker.mySquareChanged(toUpdate);
    }
}
