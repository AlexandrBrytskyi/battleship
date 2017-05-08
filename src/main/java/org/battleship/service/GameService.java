package org.battleship.service;




public interface GameService {

    void askForAddingShips();

    void notifyGameStarted();

    void askForBit(String playerId);

    void sendMessage(String playerId, String message);

    void shipKilled(String killerId);

    void endGame(String winnerId);
}
