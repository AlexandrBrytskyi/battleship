package org.battleship.model.bits;


import org.battleship.model.boards.BoardSquare;

import java.util.List;

public class ShipDestroyedResult extends BitResult {

    private ShipDestroyedActivity shipDestroyedActivity;
    private List<BoardSquare> shipSquares;
    private List<BoardSquare> bittedSquares;

    public ShipDestroyedResult(List<BoardSquare> shipSquares, List<BoardSquare> bittedSquares) {
        this.shipSquares = shipSquares;
        this.bittedSquares = bittedSquares;
    }

    public void afterResultReceivedAction() {
        shipDestroyedActivity.markBittedSquares(bittedSquares);
        shipDestroyedActivity.markShipOnBoard(shipSquares);
        shipDestroyedActivity.bitOneMore();
    }

    public void setShipDestroyedActivity(ShipDestroyedActivity shipDestroyedActivity) {
        this.shipDestroyedActivity = shipDestroyedActivity;
    }
}
