package org.battleship.controller.user.remote.requests;


import org.battleship.controller.user.remote.responses.ICallback;

import java.io.Serializable;


public abstract class Request implements ICallback {

    protected transient RemoteUserService eventWorker;

    public abstract void requestReceivedAction();

    public RemoteUserService getEventWorker() {
        return eventWorker;
    }

    public void setEventWorker(RemoteUserService eventWorker) {
        this.eventWorker = eventWorker;
    }
}
