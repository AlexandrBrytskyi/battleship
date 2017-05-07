package org.battleship.model.bits;

import org.battleship.model.boards.BoardSquare;

//like command pattern
public interface ShipBittedActivity {

    void markSquareAsBitted(BoardSquare bittedSquare);

    void bitOneMore();

}
