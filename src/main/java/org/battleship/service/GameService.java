package org.battleship.service;


import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.model.bits.BitResult;

public interface GameService {

    void askForAddingShips();

    void notifyGameStarted();

    void askForBit(String playerId);

    void sendMessage(String playerId, String message);

    BitResult bitOpponent(char x, int y, String attackerServiceId) throws CantBitBorderSquareException;

    void shipKilled(String killerId);

    void endGame(String winnerId);
}
