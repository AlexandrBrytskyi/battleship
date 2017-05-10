package org.battleship.controller.user.remote.requests;


public class ReadyToPlayRequest extends Request {


    @Override
    public void sendCallback() {

    }

    @Override
    public void requestReceivedAction() {
        eventWorker.setReadyToPlay(true);
    }
}
