package org.battleship.service;


import org.battleship.service.players.Player;
import org.battleship.model.ships.Ship;

public interface PlayerGameService {

    void registerInGame(Player player);

    void sendMessageToChat(String message);

    void receiveMessage(String message);

    void putShipOnBoard(Ship ship);

    void pauseGame();

    void resumeGame();

    void capitulate();

    void bitSquare(char x, int y);

}
