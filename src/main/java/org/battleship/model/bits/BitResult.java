package org.battleship.model.bits;


public abstract class BitResult {

    protected BitResultEvent event;

    public abstract void afterResultReceivedAction();

    public void setActivityRealizator(BitResultEvent realizator){
        this.event = realizator;
    }
    
}
