package org.battleship.model.bits;


import org.battleship.model.boards.BoardSquare;

public interface MissedActivity {

    void markOpponentsBorderSquareAsMissed(BoardSquare missedBorderSquare);

    void mySquareChanged(BoardSquare toUpdate);

}
