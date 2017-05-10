package org.battleship.controller.user.remote.requests;


public class FilledRequest extends Request {


    @Override
    public void sendCallback() {

    }

    @Override
    public void requestReceivedAction() {
        eventWorker.setFilled(true);
    }
}
