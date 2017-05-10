package org.battleship.controller.user.remote.requests;


public class NotifiedEndGameRequest extends Request{
    private boolean won;

    public NotifiedEndGameRequest(boolean won) {
        this.won = won;
    }

    @Override
    public void sendCallback() {

    }

    @Override
    public void requestReceivedAction() {
        eventWorker.gameEnd(won);
    }
}
