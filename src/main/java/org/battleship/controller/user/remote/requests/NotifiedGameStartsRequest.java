package org.battleship.controller.user.remote.requests;


import org.battleship.controller.user.remote.responses.SimpleCallback;

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
