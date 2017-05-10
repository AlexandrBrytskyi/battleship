package org.battleship.controller.user.remote.requests;

import java.io.Serializable;


public class MessageReceivedRequest extends Request {
   private String mess;

    public MessageReceivedRequest(String mess) {
        this.mess = mess;
    }

    @Override
    public void sendCallback() {

    }

    @Override
    public void requestReceivedAction() {
        eventWorker.messageReceived(mess);
    }
}
