package org.battleship.controller;

import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.exceptions.SquareIsUnderShipException;
import org.battleship.exceptions.UnsupportedShipException;
import org.battleship.gui.GUI;
import org.battleship.model.bits.*;
import org.battleship.model.boards.Board;
import org.battleship.model.boards.BoardSquare;
import org.battleship.model.boards.TenXTenStandardBoard;
import org.battleship.model.ships.Ship;
import org.battleship.service.GameService;
import org.battleship.service.UserService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleUserController implements UserService, BitResultEvent {

    private Board myBoard;
    private Board opponentBoard;
    private GameService gameService;
    private GUI gui;
    private String myId;

    public SimpleUserController(GUI gui,Board myBoard, Board opponentBoard) {
        this.gui = gui;
        this.myBoard = myBoard;
        this.opponentBoard = opponentBoard;
    }

    public void registerToGame() {
//        we do not need to realize this method here as it`s goal is to receive id
//        in this case id is already setted
    }

    public void askedForAddingShips() {
        gui.askedForAddingShips();
    }

    public void notifiedGameStarts() {
        gui.notifiedGameStarts();
    }

    public void addShip(Ship ship, List<BoardSquare> shipPlaceOnBoard) throws SquareIsUnderShipException, UnsupportedShipException {
        myBoard.putShipOnBoard(ship, shipPlaceOnBoard);
    }

    public BitResult askedForBit() {
       return gui.askedForBit();
    }

    public void mySquareBitted(BoardSquare square) {
        BoardSquare mySquare = myBoard.getBorderSquareByCharXIntY(square.getxPosition(), square.getyPosition());
        mySquare.setCanBit(false);
        gui.updateMySquare(mySquare);
    }

    public BitResult bit(char x, int y) throws CantBitBorderSquareException {
        BitResult result = opponentBoard.squareAtacked(x, y);

        result.afterResultReceivedAction();
        return result;
    }

    public void markBorderSquareAsMissed(BoardSquare missedBorderSquare) {
        opponentBoard.getBorderSquareByCharXIntY(missedBorderSquare.getxPosition(), missedBorderSquare.getyPosition()).setCanBit(false);
        gui.updateOpponentSquare(missedBorderSquare);
    }

    public void markSquareAsBitted(BoardSquare bittedSquare) {
        BoardSquare oppSquare = opponentBoard.getBorderSquareByCharXIntY(bittedSquare.getxPosition(), bittedSquare.getyPosition());
        oppSquare.setCanBit(false);
        oppSquare.setUnderBoard(true);
        gui.updateOpponentSquare(bittedSquare);
    }

    public void bitOneMore() {
        gui.askedForBit();
    }

    public void markShipOnBoard(List<BoardSquare> squares) {
        for (BoardSquare o : ((TenXTenStandardBoard) opponentBoard).wrapShipSquaresWithNeededSquares(squares)) {
            markBorderSquareAsMissed(o);
        }
        for (BoardSquare square : squares) {
            markSquareAsBitted(square);
        }
        gameService.shipKilled(myId);
    }

    public void markBittedSquares(List<BoardSquare> bittedSquares) {
        for (BoardSquare bittedSquare : bittedSquares) {
            markSquareAsBitted(bittedSquare);
        }
    }

    public void messageReceived(String mess) {
        gui.appendMessage(mess);
    }

    public void notifiedEndGame(String winnerId) {
        gui.showWinnerAndExit(winnerId  );
    }

    @Override
    public void sendMessage(String message) {
        gameService.sendMessage(myId, message);
    }

    @Override
    public boolean getIsFilled() {
        return myBoard.isReadyToPlay();
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getMyId() {
        return myId;
    }

    public GameService getGameService() {
        return gameService;
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public GUI getGui() {
        return gui;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
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
}
