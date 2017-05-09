package org.battleship.model.boards;


import org.battleship.exceptions.SquareIsUnderShipException;
import org.battleship.exceptions.UnsupportedShipException;
import org.battleship.exceptions.WrongBoardStringException;
import org.battleship.model.bits.BitResult;
import org.battleship.model.bits.ShipBittedResult;
import org.battleship.model.bits.ShipDestroyedResult;
import org.battleship.model.ships.Ship;
import org.battleship.model.ships.StraightShip;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TenXTenStandardBoard extends Board {

    public static int SHIPS_AMOUNT_NEEDED = 10;

    public TenXTenStandardBoard() {

    }

    public TenXTenStandardBoard(String borderTopName) throws WrongBoardStringException {
        super(borderTopName);
        checkName(borderTopName);
    }

    public void putShipOnBoard(Ship ship, List<BoardSquare> squares) throws UnsupportedShipException, SquareIsUnderShipException {
        if (!(ship instanceof StraightShip))
            throw new UnsupportedShipException(ship.getClass().getName() + " is not supported for this board!");
        StraightShip straightShip = (StraightShip) ship;
        if (straightShip.getLength() != squares.size())
            throw new UnsupportedShipException("Ship type and squares for its placing differs");
        Set<BoardSquare> neededSquares = wrapShipSquaresWithNeededSquares(squares);
        checkIfSquaresAreFree(neededSquares);
        changeSquaresAndPutShipOnBoard(ship, squares);
        shipsOnBoardCounter.addShipToCounter(straightShip);
        if (shipsOnBoard.size() == SHIPS_AMOUNT_NEEDED) isReadyToPlay = true;
    }

    private void changeSquaresAndPutShipOnBoard(Ship ship, List<BoardSquare> squares) {
        for (BoardSquare square : squares) {
            square = getBorderSquareByCharXIntY(square.getxPosition(), square.getyPosition());
            square.setOwner(ship);
            square.setUnderShip(true);
        }
        ship.setShipPlaceOnBoard(squares, this);

        shipsOnBoard.add(ship);
    }

    private void checkIfSquaresAreFree(Set<BoardSquare> neededSquares) throws SquareIsUnderShipException {
        for (BoardSquare square : neededSquares) {
            if (square.isUnderShip()) {
                throw new SquareIsUnderShipException("Square " + square.getxPosition() + "-"
                        + square.getyPosition() + " is already under ship " + square.getOwnerShip().getName(), square);
            }
        }
    }

    public Set<BoardSquare> wrapShipSquaresWithNeededSquares(List<BoardSquare> squares) {
        Set<BoardSquare> result = new HashSet<>();
        for (BoardSquare square : squares) {
            int xPositionOfSquare = getIndexesXOfSquaresInArrayByCharacter().get(square.getxPosition());
            int yPositionOfSquare = square.getyPosition() - 1;
            for (int x = xPositionOfSquare - 1; x <= xPositionOfSquare + 1; x++) {
                for (int y = yPositionOfSquare - 1; y <= yPositionOfSquare + 1; y++) {
                    if ((x >= 0 && x < getSquares().length) &&
                            (y >= 0 && y < getSquares().length))
                        result.add(this.squares[y][x]);
                }
            }
        }
        return result;
    }

    public BitResult shipBitted(BoardSquare square, Ship ship) {

        return new ShipBittedResult(square);
    }

    public BitResult shipDestroyed(Ship ship) {
// make squares around not beatable
        List<BoardSquare> allBeatedSquares = wrapShipSquaresWithNeededSquares(ship.getPlaceOnBoard()).stream().collect(Collectors.toList());
        for (BoardSquare beatedSquare : allBeatedSquares) {
            beatedSquare.setCanBit(false);
            beatedSquare.setUnderShip(false);
        }
        ship.getPlaceOnBoard().stream().forEach((s) -> {
            s.setUnderShip(true);
            s.setCanBit(false);
        });
        return new ShipDestroyedResult(ship.getPlaceOnBoard(), allBeatedSquares);
    }

    public void initShipsOnBoardCounter() {
        shipsOnBoardCounter = new Simple10X10BoardStraightShipsCounter();
    }

    private void checkName(String borderTopName) throws WrongBoardStringException {
        if (borderTopName.length() != 10) throw new WrongBoardStringException("Lenght must be equal 10");
        if (!isUniqueChars2(borderTopName))
            throw new WrongBoardStringException("All characters in String must be unique");
    }

    private boolean isUniqueChars2(String str) {
        // Create a new boolean array of 256 characters to account for basic ascii and extended ascii characters
        boolean[] char_set = new boolean[256];

        //iterate through the string we are testing
        for (int i = 0; i < str.length(); i++) {

            // Get the numerical (ascii) value of the character in the `str` at position `i`.
            int val = str.charAt(i);

            // If char_set[val] has been set, that means that this character was already present in the string. (so in string 'hello' this would be true for the second 'l')
            if (char_set[val]) {
                return false;
            }
            // If the character hasn't been encountered yet (otherwise we would have returned true above), then mark this particular character as present in the string
            char_set[val] = true;
        }
        // If the function hasn't returned false after going through the entire string that means that each character is unique - thus returning true
        return true;
    }

    public static class Simple10X10BoardStraightShipsCounter implements ShipsOnBoardCounter<StraightShip> {

        //    Entry{length, shipAmount}
        private Map<Integer, AtomicInteger> shipsAmountByLength = new ConcurrentHashMap<Integer, AtomicInteger>() {{
            for (int i = 1; i <= 4; i++) {
                put(i, new AtomicInteger(0));
            }
        }};

        public void addShipToCounter(StraightShip ship) throws UnsupportedShipException {
            int shipLength = ship.getLength();
            AtomicInteger shipAmount = shipsAmountByLength.get(shipLength);
            switch (shipLength) {
                case 1: {
                    if (shipAmount.get() == 4)
                        throw new UnsupportedShipException("There is already " + shipAmount + " this ships on board");
                    break;
                }
                case 2: {
                    if (shipAmount.get() == 3)
                        throw new UnsupportedShipException("There is already " + shipAmount + " this ships on board");
                    break;
                }
                case 3: {
                    if (shipAmount.get() == 2)
                        throw new UnsupportedShipException("There is already " + shipAmount + " this ships on board");
                    break;
                }
                case 4: {
                    if (shipAmount.get() == 1)
                        throw new UnsupportedShipException("There is already " + shipAmount + " this ships on board");
                    break;
                }
                default: {
                    throw new UnsupportedShipException("This ship must have length from 1 to 4");
                }
            }
            shipAmount.incrementAndGet();
        }

    }


}

