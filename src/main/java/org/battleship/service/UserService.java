package org.battleship.service;


import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.exceptions.SquareIsUnderShipException;
import org.battleship.exceptions.UnsupportedShipException;
import org.battleship.model.bits.BitResult;
import org.battleship.model.boards.Board;
import org.battleship.model.boards.BoardSquare;
import org.battleship.model.ships.Ship;

import java.util.List;

public interface UserService {

    void registerToGame();

    void askedForAddingShips();

    void notifiedGameStarts();

    void addShip(Ship ship, List<BoardSquare> shipPlaceOnBoard) throws SquareIsUnderShipException, UnsupportedShipException;

    BitResult askedForBit();

    BitResult bitOpponentBoardSquare(char x, int y) throws CantBitBorderSquareException;

    BitResult oppenentBitsMyBoardSquare(char x, int y) throws CantBitBorderSquareException;

    void setMyId(String id);

    String getMyId();

    void messageReceived(String mess);

    void notifiedEndGame(String winnerId);

    void sendMessage(String message);

    boolean getIsFilled();
}
