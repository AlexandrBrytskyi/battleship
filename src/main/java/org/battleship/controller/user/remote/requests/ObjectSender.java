package org.battleship.controller.user.remote.requests;


import java.io.Serializable;

public interface ObjectSender {

    void sendObject(Serializable object, boolean waitForResponse);

}
