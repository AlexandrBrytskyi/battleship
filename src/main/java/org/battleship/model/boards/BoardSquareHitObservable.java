package org.battleship.model.boards;


public interface BoardSquareHitObservable {

    void notifyParentShipAboutHitting(BoardSquare boardSquare);

}
