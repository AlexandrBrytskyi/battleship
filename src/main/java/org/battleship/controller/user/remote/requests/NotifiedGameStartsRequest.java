package org.battleship.controller.user.remote.requests;



public class NotifiedGameStartsRequest extends Request{


    @Override
    public void sendCallback() {

    }

    @Override
    public void requestReceivedAction() {
        eventWorker.notifiedGameStarts();
        sendCallback();
    }
}
