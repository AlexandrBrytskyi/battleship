package org.battleship.model.boards;


import org.battleship.model.bits.BitResult;

public interface BoardSquareHitObservable {

    BitResult notifyParentShipAboutHitting(BoardSquare boardSquare);

}
