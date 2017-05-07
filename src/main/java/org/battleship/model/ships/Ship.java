package org.battleship.model.ships;


import org.battleship.model.boards.BoardSquare;

import java.util.List;

public abstract class Ship {

    private String name;
    private List<BoardSquare> placeOnBoard;

    public Ship(String name) {
        this.name = name;
    }

    protected abstract void shipHitted(BoardSquare boardSquare);

    protected abstract void shipDestroyed();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BoardSquare> getPlaceOnBoard() {
        return placeOnBoard;
    }

    public void setPlaceOnBoard(List<BoardSquare> placeOnBoard) {
        this.placeOnBoard = placeOnBoard;
    }


    @Override
    public String toString() {
        return "Ship{" +
                "name='" + name + '\'' +
                ", placeOnBoard=" + placeOnBoard +
                '}';
    }
}
