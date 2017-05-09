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


import java.util.List;

public class UserPlayerController extends PlayerController {

    protected GUI gui;
    protected boolean autogenShips = false;

    public UserPlayerController(GUI gui, Board myBoard, Board opponentBoard, boolean autogenShips) {
        this.gui = gui;
        this.myBoard = myBoard;
        this.opponentBoard = opponentBoard;
        this.autogenShips = autogenShips;
    }

    public void registerToGame() {
//        we do not need to realize this method here as it`s goal is to receive id
//        in this case id is already setted
    }

    public void askedForAddingShips() {
        if (!autogenShips) {
            gui.askedForAddingShips();
        } else {
           ShipsGenUtils.generateAndAddShips(this, true, gui, getMyBoard().getBorderTopName());
        }
    }

    public void notifiedGameStarts() {
        gui.notifiedGameStarts();
    }

    public void addShip(Ship ship, List<BoardSquare> shipPlaceOnBoard) throws SquareIsUnderShipException, UnsupportedShipException {
        myBoard.putShipOnBoard(ship, shipPlaceOnBoard);
        gui.markSquaresAsMyShips(shipPlaceOnBoard);
    }

    public BitResult askedForBit() {
        return gui.askedForBit();
    }


    public BitResult bitOpponentBoardSquare(char x, int y) throws CantBitBorderSquareException {
        System.out.println("UPC: sending game controller request to bit");
        BitResult result = gameService.bitOpponent(x, y, myId);
        System.out.println("UPC: received bit result");
        result.setActivityRealizator(this);
        result.afterResultReceivedAction(true);
        System.out.println("UPC: executed and returning bit result");
        return result;
    }

    @Override
    public BitResult oppenentBitsMyBoardSquare(char x, int y) throws CantBitBorderSquareException {
        System.out.println("UPC: somebody wants to bit me");
        BitResult result = myBoard.squareAtacked(x, y);
        System.out.println("UPC: he bited");
        result.setActivityRealizator(this);
        result.afterResultReceivedAction(false);
        System.out.println("UPC: executed and returning bit result from bitted me");
        return result;
    }

    public void markOpponentsBorderSquareAsMissed(BoardSquare missedBorderSquare) {
        BoardSquare square = opponentBoard.getBorderSquareByCharXIntY(missedBorderSquare.getxPosition(), missedBorderSquare.getyPosition());
        square.setCanBit(false);
        square.setUnderShip(false);
        gui.updateOpponentSquare(square);
    }

    @Override
    public void mySquareChanged(BoardSquare toUpdate) {
        gui.updateMySquare(toUpdate);
    }

    public void markOpponentsSquareAsBitted(BoardSquare bittedSquare) {
        BoardSquare oppSquare = opponentBoard.getBorderSquareByCharXIntY(bittedSquare.getxPosition(), bittedSquare.getyPosition());
        oppSquare.setCanBit(false);
        oppSquare.setUnderShip(true);
        gui.updateOpponentSquare(oppSquare);
    }

    public void bitOneMore() {
        askedForBit();
    }

    public void markShipOnOpponentBoard(List<BoardSquare> squares) {

        for (BoardSquare square : squares) {
            markOpponentsSquareAsBitted(square);
        }

        gameService.shipKilled(myId);
    }

    public void markMissedSquaresOnOpponenBoard(List<BoardSquare> bittedSquares) {
        for (BoardSquare bittedSquare : bittedSquares) {
            markOpponentsBorderSquareAsMissed(bittedSquare);
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
}
