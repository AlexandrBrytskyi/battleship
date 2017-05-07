package org.battleship.model.boards;


import org.battleship.model.ships.Ship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Board {

    private BoardSquare[][] squares;
    private String borderTopName = "RESPUBLIKA";
    private List<Ship> shipsOnBoard = new ArrayList<Ship>();

    public Board() {
        initBoard();
    }

    public Board(String borderTopName) {
        this.borderTopName = borderTopName;
        initBoard();
    }

    protected abstract void putShipOnBoard(Ship ship);

    protected abstract void squareAtacked(int x, int y);

    protected abstract void shipBitted(Ship ship);

    protected abstract void shipDestroyed(Ship ship);


    private void initBoard() {
        char[] borderTopNameChars = borderTopName.toCharArray();
        squares = new BoardSquare[borderTopNameChars.length][borderTopNameChars.length];
        int counterX = 0;
        for (char c : borderTopNameChars) {
            for (int i = 0; i < borderTopNameChars.length; i++) {
                squares[i][counterX] = new BoardSquare(i + 1, c);
            }
            counterX++;
        }
    }

    public BoardSquare[][] getSquares() {
        return squares;
    }

    public void setSquares(BoardSquare[][] squares) {
        this.squares = squares;
    }

    public String getBorderTopName() {
        return borderTopName;
    }

    public void setBorderTopName(String borderTopName) {
        this.borderTopName = borderTopName;
    }

    public List<Ship> getShipsOnBoard() {
        return shipsOnBoard;
    }

    public void setShipsOnBoard(List<Ship> shipsOnBoard) {
        this.shipsOnBoard = shipsOnBoard;
    }

    @Override
    public String toString() {
        return "Board{" +
                "squares=" + getStringArr(squares) +
                ", borderTopName='" + borderTopName + '\'' +
                '}';
    }

    private static String getStringArr(Object[][] arr) {
        StringBuilder res = new StringBuilder();
        for (Object[] subArr : arr) {
            res.append(Arrays.toString(subArr)).append("\n");
        }
        return res.toString();
    }

    public static void main(String[] args) {
        System.out.println(new TenXTenSimpleBoard());
    }
}
