package org.battleship.controller.user.remote.requests;



public class AskedForAddingShipRequest extends Request {


    @Override
    public void requestReceivedAction() {
        eventWorker.askedForAddingShips();

    }

    @Override
    public void sendCallback() {

    }
}
