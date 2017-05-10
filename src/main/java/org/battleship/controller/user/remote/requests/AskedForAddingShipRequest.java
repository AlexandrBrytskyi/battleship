package org.battleship.controller.user.remote.requests;


import org.battleship.controller.user.remote.responses.SimpleCallback;

public class AskedForAddingShipRequest extends Request {


    @Override
    public void requestReceivedAction() {
        eventWorker.askedForAddingShips();

    }

    @Override
    public void sendCallback() {

    }
}
