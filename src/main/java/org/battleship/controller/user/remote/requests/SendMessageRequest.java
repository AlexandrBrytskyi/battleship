package org.battleship.controller.user.remote.requests;



public class SendMessageRequest extends Request {
    private String message;

    public SendMessageRequest(String message) {
        this.message = message;
    }

    @Override
    public void sendCallback() {

    }

    @Override
    public void requestReceivedAction() {
        eventWorker.sendMessage(message);
    }
}
