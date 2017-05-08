package org.battleship.model.bits;


import org.battleship.model.boards.BoardSquare;

public class ShipBittedResult extends BitResult {

    private BoardSquare bittedBoardSquare;

    public ShipBittedResult(BoardSquare bittedBoardSquare) {
        this.bittedBoardSquare = bittedBoardSquare;
    }

    public void afterResultReceivedAction() {
        event.markSquareAsBitted(bittedBoardSquare);
        event.bitOneMore();
    }

}
