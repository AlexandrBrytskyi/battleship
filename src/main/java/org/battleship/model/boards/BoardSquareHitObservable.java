package org.battleship.model.boards;


import org.battleship.model.bits.BitResult;

/**
 * Observable
 * @see org.battleship.model.ships.BoardSquareHitObserver*/
public interface BoardSquareHitObservable {

    BitResult notifyParentShipAboutHitting(BoardSquare boardSquare);

}
