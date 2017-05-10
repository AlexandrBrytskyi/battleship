package org.battleship.model.bits;


import java.io.Serializable;


/**
 * class used to make some actions which should be done differently dependently
 * of {@link org.battleship.model.boards.Board#squareAtacked(char, int)} result
 * used by {@link org.battleship.controller.PlayerController}
 */
public abstract class BitResult implements Serializable {

    protected transient BitResultEvent event;

    public abstract void afterResultReceivedAction(boolean attacker);

    public void setActivityRealizator(BitResultEvent realizator) {
        this.event = realizator;
    }

}
