package org.battleship.model.boards;


import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.model.bits.BitResult;
import org.battleship.model.bits.MissedResult;
import org.battleship.model.ships.Ship;

public class BoardSquare implements BoardSquareHitObservable {

    private int yPosition;
    private char xPosition;
    private boolean isUnderBoard = false;
    private boolean canBit = true;
    private Ship owner;

    public BoardSquare(int yPosition, char xPosition) {
        this.yPosition = yPosition;
        this.xPosition = xPosition;
    }

    public BitResult hitMe() throws CantBitBorderSquareException {
        if (!canBit) throw new CantBitBorderSquareException("Sorry, this border square can`t be bitten");
        canBit = false;
        if (isUnderBoard()) return notifyParentShipAboutHitting(this);
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

    public boolean isUnderBoard() {
        return isUnderBoard;
    }

    public void setUnderBoard(boolean underBoard) {
        isUnderBoard = underBoard;
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
                ", isUnderBoard=" + isUnderBoard +
                ", canBit=" + canBit +
                '}';
    }
}
