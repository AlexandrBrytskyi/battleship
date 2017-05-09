package org.battleship.model.bits;


import org.battleship.model.boards.BoardSquare;

import java.util.List;

public interface ShipDestroyedActivity  extends OneMoreBittable{

    void markShipOnOpponentBoard(List<BoardSquare> squares);

    void markMissedSquaresOnOpponenBoard(List<BoardSquare> bittedSquares);

}
