package org.battleship.controller.user.remote;


import org.battleship.controller.Ships10x10GenUtils;
import org.battleship.controller.user.remote.requests.*;
import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.exceptions.SquareIsUnderShipException;
import org.battleship.exceptions.UnsupportedShipException;
import org.battleship.gui.GUI;
import org.battleship.model.boards.Board;
import org.battleship.model.boards.BoardSquare;
import org.battleship.model.ships.Ship;

import java.io.*;
import java.net.Socket;
import java.util.List;


/**
 * Controller which is runned on remote client and communicate with server over {@link Socket}
 *
 * @see ObjectInputStream
 * @see ObjectOutputStream
 */
public class RemoteUserClientController extends RemoteUserController {


    public RemoteUserClientController(GUI gui, Board myBoard, Board opponentBoard, boolean autogenShips, String host) throws IOException {
        super(gui, myBoard, opponentBoard, autogenShips);
        clientSocket = new Socket(host, PORT);
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());
        initInputListener();
        readyToPlay = true;
        sendObject(new ReadyToPlayRequest());
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
        if (myBoard.isReadyToPlay()) {
            sendObject(new FilledRequest());
        }
    }

    public void askedForBit() {
        gui.askedForBit();
    }


    public void bitOpponentBoardSquare(char x, int y) throws CantBitBorderSquareException {
        System.out.println("before bitting");
        sendObject(new BitOpponentBoardSquareRequest(x, y));
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
//        realized on server
    }

    public void messageReceived(String mess) {
        gui.appendMessage(mess);
    }


    @Override
    public void sendMessage(String message) {
        sendObject(new SendMessageRequest(message));
    }

    @Override
    public boolean getFilled() {
        return false;
    }

    @Override
    public void setFilled(boolean filled) {

    }


    @Override
    public void gameEnd(boolean won) {
        gui.showWinner(won);
    }


    @Override
    public void requestReceived(Request request) {
        executorService.submit(() -> {
            request.setEventWorker(this);
            request.requestReceivedAction();
        });
    }
}
