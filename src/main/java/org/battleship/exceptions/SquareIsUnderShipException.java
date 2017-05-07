package org.battleship.exceptions;


import org.battleship.model.boards.BoardSquare;

public class SquareIsUnderShipException extends Throwable {
    private BoardSquare square;
    public SquareIsUnderShipException(String mess, BoardSquare square) {
        super(mess);
        this.square = square;
    }

    public BoardSquare getSquare() {
        return square;
    }
}
