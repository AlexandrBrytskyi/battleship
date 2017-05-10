package org.battleship.controller.user.remote.requests;


import org.battleship.model.bits.BitResultEvent;
import org.battleship.service.UserService;

public interface RemoteUserService extends UserService, ObjectSender,BitResultEvent{

    void requestReceived(Request request);

    void gameEnd(boolean won);
}
