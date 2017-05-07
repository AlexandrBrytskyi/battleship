package org.battleship.model.ships;


import org.battleship.model.boards.Board;
import org.battleship.model.boards.BoardSquare;

import java.util.List;

public abstract class Ship implements BoardSquareHitObserver {

    private String name;
    private List<BoardSquare> placeOnBoard;
    protected int hittedSquaresCounter = 0;
    private Board ownerBoard;

    public Ship(String name) {
        this.name = name;
    }

    public void setShipPlaceOnBoard(List<BoardSquare> boardSquares, Board board) {
        ownerBoard = board;
        setPlaceOnBoard(boardSquares);
    }

    protected void shipHitted(BoardSquare boardSquare) {
        ownerBoard.shipBitted(boardSquare, this);
        hittedSquaresCounter++;
        if (hittedSquaresCounter == placeOnBoard.size()) shipDestroyed();
    }

    protected void shipDestroyed() {
        ownerBoard.shipDestroyed(this);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BoardSquare> getPlaceOnBoard() {
        return placeOnBoard;
    }

    protected void setPlaceOnBoard(List<BoardSquare> placeOnBoard) {
        this.placeOnBoard = placeOnBoard;
    }

    public int getHittedSquaresCounter() {
        return hittedSquaresCounter;
    }

    public void setHittedSquaresCounter(int hittedSquaresCounter) {
        this.hittedSquaresCounter = hittedSquaresCounter;
    }

    public Board getOwnerBoard() {
        return ownerBoard;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "name='" + name + '\'' +
                ", placeOnBoard=" + placeOnBoard +
                '}';
    }
}
