package org.battleship.model.ships;


import org.battleship.model.bits.BitResult;
import org.battleship.model.boards.BoardSquare;

import java.util.List;

public class StraightShip extends Ship {

    private int length;

    public StraightShip(String name, int length) {
        super(name);
        this.length = length;
    }

    public BitResult mySquareHitten(BoardSquare square) {
       return shipHitted(square);
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
