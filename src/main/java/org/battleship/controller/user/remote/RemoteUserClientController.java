package org.battleship.controller.user.remote;


import org.battleship.controller.ShipsGenUtils;
import org.battleship.controller.user.UserPlayerController;
import org.battleship.controller.user.remote.requests.*;
import org.battleship.controller.user.remote.responses.ICallback;
import org.battleship.controller.user.remote.responses.SimpleCallback;
import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.exceptions.SquareIsUnderShipException;
import org.battleship.exceptions.UnsupportedShipException;
import org.battleship.gui.GUI;
import org.battleship.model.bits.BitResult;
import org.battleship.model.boards.Board;
import org.battleship.model.boards.BoardSquare;
import org.battleship.model.ships.Ship;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RemoteUserClientController extends RemoteUserController {




    public RemoteUserClientController(GUI gui, Board myBoard, Board opponentBoard, boolean autogenShips, String host) throws IOException {
        super(gui, myBoard, opponentBoard, autogenShips);
        clientSocket = new Socket(host, PORT);
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());
        initInputListener();
        readyToPlay = true;
        sendObject(new ReadyToPlayRequest(), false);
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
        if (myBoard.isReadyToPlay()) {
            sendObject(new FilledRequest(), false);
        }
    }

    public void askedForBit() {
        gui.askedForBit();
    }


    public void bitOpponentBoardSquare(char x, int y) throws CantBitBorderSquareException {
        System.out.println("before bitting");
        sendObject(new BitOpponentBoardSquareRequest(x, y), true);
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
        sendObject(new SendMessageRequest(message), false);
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
        executorService.submit(()->{request.setEventWorker(this);
            request.requestReceivedAction();});
    }
}
