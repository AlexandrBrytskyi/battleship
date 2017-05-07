package org.battleship.model.ships;


import org.battleship.model.boards.BoardSquare;

import java.util.List;

public class StraightShip extends Ship {

    private int length;

    public StraightShip(String name, int length) {
        super(name);
        this.length = length;
    }

    protected void setShipPlaceOnBoard(List<BoardSquare> boardSquares) {

    }

    protected void shipHitted(BoardSquare boardSquare) {

    }

    protected void shipDestroyed() {

    }

    public int getLength() {
        return length;
    }


    @Override
    public String toString() {
        return "StraightShip{" +
                "length=" + length +
                "} " + super.toString();
    }
}
