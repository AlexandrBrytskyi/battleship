package org.battleship.model.boards;


import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.exceptions.SquareIsUnderShipException;
import org.battleship.exceptions.UnsupportedShipException;
import org.battleship.model.ships.Ship;

import java.util.*;

public abstract class Board {

    protected BoardSquare[][] squares;
    protected String borderTopName = "RESPUBLIKA";
    protected Map<Character, Integer> indexesXOfSquaresInArrayByCharacter = new HashMap<Character, Integer>();
    protected List<Ship> shipsOnBoard = new ArrayList<Ship>();
    protected ShipsOnBoardCounter shipsOnBoardCounter;

    public Board() {
        initBoard();
        initShipsOnBoardCounter();
    }

    public Board(String borderTopName) {
        this.borderTopName = borderTopName;
        initShipsOnBoardCounter();
        initBoard();
    }


    protected abstract void putShipOnBoard(Ship ship, List<BoardSquare> squares) throws UnsupportedShipException, SquareIsUnderShipException;

    public void squareAtacked(char x, int y) throws CantBitBorderSquareException {
        getBorderSquareByCharXIntY(x, y).hitMe();
    }

    public abstract void shipBitted(BoardSquare square, Ship ship);

    public abstract void shipDestroyed(Ship ship);


    private void initBoard() {
        char[] borderTopNameChars = borderTopName.toCharArray();
        squares = new BoardSquare[borderTopNameChars.length][borderTopNameChars.length];
        int counterX = 0;
        for (char c : borderTopNameChars) {
            for (int i = 0; i < borderTopNameChars.length; i++) {
                squares[i][counterX] = new BoardSquare(i + 1, c);
            }
            indexesXOfSquaresInArrayByCharacter.put(c, counterX++);
        }
    }

    public BoardSquare getBorderSquareByCharXIntY(char x, int y) {
        int xPositionOfSquare = getIndexesXOfSquaresInArrayByCharacter().get(x);
        int yPositionOfSquare = y - 1;
        return squares[yPositionOfSquare][xPositionOfSquare];
    }

    public ShipsOnBoardCounter getShipsOnBoardCounter() {
        return shipsOnBoardCounter;
    }

    public abstract void initShipsOnBoardCounter();

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

    public Map<Character, Integer> getIndexesXOfSquaresInArrayByCharacter() {
        return indexesXOfSquaresInArrayByCharacter;
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


}
