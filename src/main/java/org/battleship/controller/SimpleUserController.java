package org.battleship.controller;

import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.exceptions.SquareIsUnderShipException;
import org.battleship.exceptions.UnsupportedShipException;
import org.battleship.gui.GUI;
import org.battleship.model.bits.*;
import org.battleship.model.boards.Board;
import org.battleship.model.boards.BoardSquare;
import org.battleship.model.ships.Ship;
import org.battleship.service.GameService;
import org.battleship.service.UserService;

import java.util.List;

public class SimpleUserController implements UserService, BitResultEvent {

    private Board myBoard;
    private Board opponentBoard;
    private GameService gameService;
    private GUI gui;
    private String myId;

    public SimpleUserController(GUI gui, Board myBoard, Board opponentBoard) {
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

    public BitResult bitOpponentBoardSquare(char x, int y) throws CantBitBorderSquareException {
        BitResult result = gameService.bitOpponent(x, y, myId);
        result.setActivityRealizator(this);
        result.afterResultReceivedAction();
        return result;
    }

    @Override
    public BitResult oppenentBitsMyBoardSquare(char x, int y) throws CantBitBorderSquareException {
        return myBoard.squareAtacked(x, y);
    }

    public void markBorderSquareAsMissed(BoardSquare missedBorderSquare) {
        BoardSquare square = opponentBoard.getBorderSquareByCharXIntY(missedBorderSquare.getxPosition(), missedBorderSquare.getyPosition());
        square.setCanBit(false);
        square.setUnderShip(false);
        gui.updateOpponentSquare(square);
    }

    public void markSquareAsBitted(BoardSquare bittedSquare) {
        BoardSquare oppSquare = opponentBoard.getBorderSquareByCharXIntY(bittedSquare.getxPosition(), bittedSquare.getyPosition());
        oppSquare.setCanBit(false);
        oppSquare.setUnderShip(true);
        gui.updateOpponentSquare(oppSquare);
    }

    public void bitOneMore() {
        gui.askedForBit();
    }

    public void markShipOnOpponentBoard(List<BoardSquare> squares) {

        for (BoardSquare square : squares) {
            markSquareAsBitted(square);
        }

        gameService.shipKilled(myId);
    }

    public void markMissedSquaresOnOpponenBoard(List<BoardSquare> bittedSquares) {
        for (BoardSquare bittedSquare : bittedSquares) {
            markBorderSquareAsMissed(bittedSquare);
        }
    }

    public void messageReceived(String mess) {
        gui.appendMessage(mess);
    }

    public void notifiedEndGame(String winnerId) {
        gui.showWinnerAndExit(winnerId);
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
