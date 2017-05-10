package org.battleship.controller.user.remote.requests;


public class SetMyIdRequest extends Request {

    private String id;

    public SetMyIdRequest(String id) {
        this.id = id;
    }

    @Override
    public void sendCallback() {

    }

    @Override
    public void requestReceivedAction() {
        eventWorker.setMyId(id);
    }
}
