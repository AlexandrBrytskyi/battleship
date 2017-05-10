package org.battleship.model.bits;


import java.io.Serializable;

public abstract class BitResult implements Serializable {


    protected transient BitResultEvent event;

    public abstract void afterResultReceivedAction(boolean attacker);

    public void setActivityRealizator(BitResultEvent realizator){
        this.event = realizator;
    }

}
