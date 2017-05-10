package org.battleship.model.boards;


import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.gui.GUI;
import org.battleship.model.bits.BitResult;
import org.battleship.model.bits.MissedResult;
import org.battleship.model.ships.Ship;

import java.io.Serializable;

public class BoardSquare implements BoardSquareHitObservable, Serializable {

    private int yPosition;
    private char xPosition;
    private boolean isUnderShip = false;
    private boolean canBit = true;
    private transient Ship owner;

    public BoardSquare(int yPosition, char xPosition) {
        this.yPosition = yPosition;
        this.xPosition = xPosition;
    }

    public BitResult hitMe() throws CantBitBorderSquareException {
        if (!canBit) throw new CantBitBorderSquareException("Sorry, this border square can`t be bitten");
        canBit = false;
        if (isUnderShip()) return notifyParentShipAboutHitting(this);
        return new MissedResult(this);
    }

    public BitResult notifyParentShipAboutHitting(BoardSquare boardSquare) {
       return owner.mySquareHitten(boardSquare);
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public char getxPosition() {
        return xPosition;
    }

    public void setxPosition(char xPosition) {
        this.xPosition = xPosition;
    }

    public boolean isUnderShip() {
        return isUnderShip;
    }

    public void setUnderShip(boolean underShip) {
        isUnderShip = underShip;
    }

    public boolean isCanBit() {
        return canBit;
    }

    public void setCanBit(boolean canBit) {
        this.canBit = canBit;
    }

    public Ship getOwnerShip() {
        return owner;
    }

    public void setOwner(Ship owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof GUI.MyBoardButton) {
            if (((GUI.MyBoardButton) o).getYVal()==getyPosition() && ((GUI.MyBoardButton) o).getXVal()==getxPosition()) return true;
        }
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardSquare that = (BoardSquare) o;

        if (yPosition != that.yPosition) return false;
        return xPosition == that.xPosition;
    }

    @Override
    public int hashCode() {
        int result = yPosition;
        result = 31 * result + (int) xPosition;
        return result;
    }

    @Override
    public String toString() {
        return "BoardSquare{" +
                "yPosition=" + yPosition +
                ", xPosition=" + xPosition +
                ", isUnderShip=" + isUnderShip +
                ", canBit=" + canBit +
                '}';
    }
}
