package org.battleship.controller.user;

import org.battleship.controller.PlayerController;
import org.battleship.controller.Ships10x10GenUtils;
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

/**
 * realization of {@link PlayerController} which fully overrides methods of {@link BitResultEvent}
 *
 * @see org.battleship.controller.bot.BotController
 * @see org.battleship.controller.user.remote.RemoteUserController
 */
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
            Ships10x10GenUtils.generateAndAddShips(this, true, gui, getMyBoard().getBorderTopName());
        }
    }

    public void notifiedGameStarts() {
        gui.notifiedGameStarts();
    }

    public void addShip(Ship ship, List<BoardSquare> shipPlaceOnBoard) throws SquareIsUnderShipException, UnsupportedShipException {
        myBoard.putShipOnBoard(ship, shipPlaceOnBoard);
        gui.markSquaresAsMyShips(shipPlaceOnBoard);
    }

    public void askedForBit() {
        gui.askedForBit();
    }


    public void bitOpponentBoardSquare(char x, int y) throws CantBitBorderSquareException {
        System.out.println("UPC: sending game controller request to bit");
        BitResult result = gameService.bitOpponent(x, y, myId);
        System.out.println("UPC: received bit result");
        result.setActivityRealizator(this);
        result.afterResultReceivedAction(true);
        System.out.println("UPC: executed and returning bit result");
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
        sendMessage("missed on: " + missedBorderSquare.getxPosition() + ":" + missedBorderSquare.getyPosition());
        gui.updateOpponentSquare(square);
    }

    @Override
    public void mySquareChanged(BoardSquare toUpdate) {
        gui.updateMySquare(toUpdate);
    }

    public void markOpponentsSquareAsBitted(BoardSquare bittedSquare) {
        BoardSquare oppSquare = opponentBoard.getBorderSquareByCharXIntY(bittedSquare.getxPosition(), bittedSquare.getyPosition());
        sendMessage("bitted on: " + bittedSquare.getxPosition() + ":" + bittedSquare.getyPosition());
        oppSquare.setCanBit(false);
        oppSquare.setUnderShip(true);
        gui.updateOpponentSquare(oppSquare);
    }

    public void bitOneMore() {
        askedForBit();
    }

    public void markShipOnOpponentBoard(List<BoardSquare> squares) {
        StringBuilder mess = new StringBuilder("Destroyed ship on: ");
        for (BoardSquare square : squares) {
            markOpponentsSquareAsBitted(square);
            mess.append(square.getxPosition()).append('-').append(square.getyPosition()).append(',');
        }
        sendMessage(mess.toString());

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
        gui.showWinner(winnerId.equals(myId) ? true : false);
    }

    @Override
    public void sendMessage(String message) {
        gameService.sendMessage(myId, message);
    }

    @Override
    public boolean getReadyToPlay() {
        return readyToPlay;
    }

    @Override
    public boolean getFilled() {
        return myBoard.isReadyToPlay();
    }

    @Override
    public void setFilled(boolean filled) {

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
