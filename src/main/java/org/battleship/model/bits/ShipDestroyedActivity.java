package org.battleship.model.bits;


import org.battleship.model.boards.BoardSquare;

import java.util.List;

public interface ShipDestroyedActivity {

    void markShipOnBoard(List<BoardSquare> squares);

    void markBittedSquares(List<BoardSquare> bittedSquares);

    void bitOneMore();

}
