package org.battleship.model.ships;


import org.battleship.model.boards.BoardSquare;

public interface BoardSquareHitObserver {

    void mySquareHitten(BoardSquare square);

}
