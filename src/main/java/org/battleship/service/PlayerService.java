package org.battleship.service;


import org.battleship.model.players.Player;
import org.battleship.model.ships.Ship;

public interface PlayerService {

    void register(Player player);

    void sendMessageToChat(String message);

    void putShipOnBoard(Ship ship);

    void pauseGame();

    void resumeGame();

    void capitulate();

    void bangBoardSquare(int x, int y);

}
