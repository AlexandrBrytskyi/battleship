package org.battleship.model.bits;


import org.battleship.model.boards.BoardSquare;

public class ShipBittedResult extends BitResult {

    public ShipBittedActivity shipBittedActivity= (ShipBittedActivity) vasia;
    private BoardSquare bittedBoardSquare;

    public ShipBittedResult(BoardSquare bittedBoardSquare) {
        this.bittedBoardSquare = bittedBoardSquare;
    }

    public void afterResultReceivedAction() {
        shipBittedActivity.markSquareAsBitted(bittedBoardSquare);
        shipBittedActivity.bitOneMore();
    }

    public void setShipBittedActivity(ShipBittedActivity shipBittedActivity) {
        this.shipBittedActivity = shipBittedActivity;
    }
}
