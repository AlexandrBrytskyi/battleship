package org.battleship.model.bits;


import org.battleship.model.boards.BoardSquare;

import java.util.List;

public class ShipDestroyedResult extends BitResult {

    private List<BoardSquare> shipSquares;
    private List<BoardSquare> bittedSquares;

    public ShipDestroyedResult(List<BoardSquare> shipSquares, List<BoardSquare> bittedSquares) {
        this.shipSquares = shipSquares;
        this.bittedSquares = bittedSquares;
    }

    public void afterResultReceivedAction(boolean attacker) {
        if (attacker) {
            event.markMissedSquaresOnOpponenBoard(bittedSquares);
            event.markShipOnOpponentBoard(shipSquares);
            event.bitOneMore();
        } else {
            System.out.println("updated");
            bittedSquares.forEach(event::mySquareChanged);
            shipSquares.forEach(event::mySquareChanged);
        }
    }

}
