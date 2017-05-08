package org.battleship.model.bits;


public abstract class BitResult {

    protected Object vasia;

    public abstract void afterResultReceivedAction();

    public void setActivityRealizator(Object realizator){
        this.vasia = realizator;
    }
    
}
