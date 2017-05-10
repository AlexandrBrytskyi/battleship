package org.battleship.controller.user.remote.responses;


import java.io.Serializable;

public class SimpleCallback implements Serializable {

    private boolean success = false;

    public SimpleCallback(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
