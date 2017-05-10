package org.battleship.controller;


import org.battleship.model.bits.BitResultEvent;
import org.battleship.model.boards.Board;
import org.battleship.service.GameService;
import org.battleship.service.UserService;

/**
 * wrapper on {@link UserService} which does not fully override methods of {@link BitResultEvent}
 *
 * @see org.battleship.controller.user.UserPlayerController
 */
public abstract class PlayerController implements UserService, BitResultEvent {

    protected Board myBoard;
    protected Board opponentBoard;
    protected GameService gameService;
    protected String myId;
    protected boolean readyToPlay = true;

    public PlayerController() {
    }

    public PlayerController(Board myBoard, Board opponentBoard, GameService gameService) {
        this.myBoard = myBoard;
        this.opponentBoard = opponentBoard;
        this.gameService = gameService;
    }

    public Board getMyBoard() {
        return myBoard;
    }

    public void setMyBoard(Board myBoard) {
        this.myBoard = myBoard;
    }

    public Board getOpponentBoard() {
        return opponentBoard;
    }

    public void setOpponentBoard(Board opponentBoard) {
        this.opponentBoard = opponentBoard;
    }

    public GameService getGameService() {
        return gameService;
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public boolean getReadyToPlay() {
        return readyToPlay;
    }

    public void setReadyToPlay(boolean isReady) {
        this.readyToPlay = isReady;
    }

}
