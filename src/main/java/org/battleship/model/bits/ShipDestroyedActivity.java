package org.battleship.model.bits;


import org.battleship.model.boards.BoardSquare;

import java.util.List;

public interface ShipDestroyedActivity  extends OneMoreBittable{

    void markShipOnBoard(List<BoardSquare> squares);

    void markBittedSquares(List<BoardSquare> bittedSquares);


}
