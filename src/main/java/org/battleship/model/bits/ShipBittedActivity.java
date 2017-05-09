package org.battleship.model.bits;

import org.battleship.model.boards.BoardSquare;

import java.util.List;

//like command pattern
public interface ShipBittedActivity extends OneMoreBittable {

    void markOpponentsSquareAsBitted(BoardSquare bittedSquare);

}
