package org.battleship.controller.user.remote;


import org.battleship.controller.user.remote.requests.*;
import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.exceptions.SquareIsUnderShipException;
import org.battleship.exceptions.UnsupportedShipException;
import org.battleship.gui.GUI;
import org.battleship.model.bits.BitResult;
import org.battleship.model.boards.Board;
import org.battleship.model.boards.BoardSquare;
import org.battleship.model.ships.Ship;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;


/**
 * Controller which is runned on server, most of all methods wrapped by sending {@link Request}
 * and receiving {@link BitResult}
 * over {@link Socket}
 *
 * @see ObjectInputStream
 * @see ObjectOutputStream
 */
public class RemoteUserOnServerController extends RemoteUserController {

    public static final int PORT = 9999;
    protected ServerSocket serverSocket;
    protected Socket clientSocket;
    private boolean isFilled = false;


    public RemoteUserOnServerController(GUI gui, Board myBoard, Board opponentBoard, boolean autogenShips) throws IOException {
        super(gui, myBoard, opponentBoard, autogenShips);
        serverSocket = new ServerSocket(PORT);
        waitForClient();
        readyToPlay = false;
    }


    private void waitForClient() {
        executorService.submit(() -> {
            try {
                clientSocket = serverSocket.accept();
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());
                clientConnected();
                initInputListener();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void clientConnected() {
        registerToGame();
    }

    public void registerToGame() {
        readyToPlay = true;
    }

    public void askedForAddingShips() {
        sendObject(new AskedForAddingShipRequest());
    }

    public void notifiedGameStarts() {
        sendObject(new NotifiedGameStartsRequest());
    }

    public void addShip(Ship ship, List<BoardSquare> shipPlaceOnBoard) throws SquareIsUnderShipException, UnsupportedShipException {
//        realized on client side
    }

    public void askedForBit() {
        sendObject(new AskedForBitRequest());
        System.out.println("sent ask for bit");
    }


    @Override
    public BitResult oppenentBitsMyBoardSquare(char x, int y) throws CantBitBorderSquareException {
//        must be realized by client
        sendObject(new OppenentBitsMyBoardSquareRequest(x, y));
        try {
            BitResult res = bitResults.poll(1, TimeUnit.DAYS);
            System.out.println("opponent bit res " + res.getClass().getName());
            return res;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void markOpponentsBorderSquareAsMissed(BoardSquare missedBorderSquare) {
        sendObject(new MarkOpponentsBorderSquareAsMissedRequest(missedBorderSquare));
    }

    @Override
    public void mySquareChanged(BoardSquare toUpdate) {
        sendObject(new MySquareChangedRequest(toUpdate));
    }

    public void markOpponentsSquareAsBitted(BoardSquare bittedSquare) {
        sendObject(new MarkOpponentsSquareAsBittedRequest(bittedSquare));
    }


    public void bitOneMore() {
        executorService.submit(() -> askedForBit());
    }


    public void markMissedSquaresOnOpponenBoard(List<BoardSquare> bittedSquares) {
        for (BoardSquare bittedSquare : bittedSquares) {
            markOpponentsBorderSquareAsMissed(bittedSquare);
        }
    }

    public void messageReceived(String mess) {
        sendObject(new MessageReceivedRequest(mess));
    }

    public void notifiedEndGame(String winnerId) {

        sendObject(new NotifiedEndGameRequest(winnerId.equals(myId) ? true : false));
    }

    @Override
    public void sendMessage(String message) {
//        must be called by client
        gameService.sendMessage(myId, message);
    }


    @Override
    public boolean getFilled() {
        return isFilled;
    }

    @Override
    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }


    @Override
    public void gameEnd(boolean won) {

    }

    @Override
    public void requestReceived(Request request) {
        executorService.submit(() -> {
            request.setEventWorker(this);
            request.requestReceivedAction();
        });
    }


}
