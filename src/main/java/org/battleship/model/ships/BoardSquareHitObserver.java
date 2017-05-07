package org.battleship.model.ships;


import org.battleship.model.bits.BitResult;
import org.battleship.model.boards.BoardSquare;

public interface BoardSquareHitObserver {

    BitResult mySquareHitten(BoardSquare square);

}
