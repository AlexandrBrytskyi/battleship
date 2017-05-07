package org.battleship.model.ships;


import org.battleship.model.boards.BoardSquare;

import java.util.List;

public class StraightShip extends Ship {

    private int length;

    public StraightShip(String name, int length) {
        super(name);
        this.length = length;
    }

    public void mySquareHitten(BoardSquare square) {
        shipHitted(square);
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
