package org.battleship.model.ships;


import org.battleship.model.bits.BitResult;
import org.battleship.model.boards.BoardSquare;

/**
 * interface must be realized by entity which should know about changing state
 * of observable {@link BoardSquare}*/
public interface BoardSquareHitObserver {

    BitResult mySquareHitten(BoardSquare square);

}
