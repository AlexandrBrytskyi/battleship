package org.battleship.model.bits;


import org.battleship.model.boards.BoardSquare;

public interface MissedActivity {

    void markBorderSquareAsMissed(BoardSquare missedBorderSquare);

}
