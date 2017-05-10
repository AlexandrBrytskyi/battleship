package org.battleship.controller.user.remote.requests;


public class AskedForBitRequest extends Request {

    @Override
    public void sendCallback() {

    }

    @Override
    public void requestReceivedAction() {
        eventWorker.askedForBit();
    }

}
