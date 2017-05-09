package org.battleship.controller;


import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.exceptions.SquareIsUnderShipException;
import org.battleship.exceptions.UnsupportedShipException;
import org.battleship.gui.GUI;
import org.battleship.model.bits.BitResult;
import org.battleship.model.boards.Board;
import org.battleship.model.boards.BoardSquare;
import org.battleship.model.ships.Ship;

import java.util.*;

public class BotController extends UserPlayerController {


    private boolean useGui = true;
    private String boardTopChars;
    private Set<BoardSquare> alreadyBittedSquares = new HashSet<>();

    public BotController(GUI gui, Board myBoard, Board opponentBoard, boolean useGui) {
        super(gui, myBoard, opponentBoard, true);
        this.useGui = useGui;
        boardTopChars = getMyBoard().getBorderTopName();
        if (useGui) gui.setTitle(gui.getTitle() + " Bot Vasiliy");
    }


    @Override
    public void askedForAddingShips() {
        ShipsGenUtils.generateAndAddShips(this, useGui, gui, getMyBoard().getBorderTopName());
    }

    @Override
    public void notifiedGameStarts() {
        if (useGui) gui.notifiedGameStarts();
        System.out.println("Game starts");
    }

    @Override
    public void addShip(Ship ship, List<BoardSquare> shipPlaceOnBoard) throws SquareIsUnderShipException, UnsupportedShipException {
        myBoard.putShipOnBoard(ship, shipPlaceOnBoard);

        if (useGui) gui.markSquaresAsMyShips(shipPlaceOnBoard);
    }

    @Override
    public BitResult askedForBit() {
        if (useGui) gui.appendMessage("I am asked to bit!!!Ok");
        System.out.println("bot asked to bit");
//        0 because we don`t put
        BoardSquare generated = ShipsGenUtils.generateRandomSquareForAddingShips(0, boardTopChars);
        while (alreadyBittedSquares.contains(generated)) {
            generated = ShipsGenUtils.generateRandomSquareForAddingShips(0, boardTopChars);
        }
        System.out.println("generated such square for bit " + generated.getxPosition() + "-" + generated.getyPosition());
        try {
            BitResult result = bitOpponentBoardSquare(generated.getxPosition(), generated.getyPosition());
            alreadyBittedSquares.add(generated);
            return result;
        } catch (CantBitBorderSquareException e) {
            e.printStackTrace();
            if (useGui) gui.appendMessage(e.getMessage());
        }
        return null;
    }

    @Override
    public BitResult bitOpponentBoardSquare(char x, int y) throws CantBitBorderSquareException {
        BitResult result = super.bitOpponentBoardSquare(x, y);
        alreadyBittedSquares.add(new BoardSquare(y, x));
        return result;
    }


    @Override
    public void markOpponentsBorderSquareAsMissed(BoardSquare missedBorderSquare) {
        BoardSquare square = opponentBoard.getBorderSquareByCharXIntY(missedBorderSquare.getxPosition(), missedBorderSquare.getyPosition());
        square.setCanBit(false);
        square.setUnderShip(false);
        if (useGui) gui.updateOpponentSquare(square);
    }

    @Override
    public void markOpponentsSquareAsBitted(BoardSquare bittedSquare) {
        BoardSquare oppSquare = opponentBoard.getBorderSquareByCharXIntY(bittedSquare.getxPosition(), bittedSquare.getyPosition());
        oppSquare.setCanBit(false);
        oppSquare.setUnderShip(true);
        if (useGui) gui.updateOpponentSquare(oppSquare);
    }

    @Override
    public void mySquareChanged(BoardSquare toUpdate) {
        if (useGui) gui.updateMySquare(toUpdate);
    }

    @Override
    public void messageReceived(String mess) {
        if (useGui) gui.appendMessage(mess);
        System.out.println("Message:" + mess);
    }

    @Override
    public void notifiedEndGame(String winnerId) {
        if (useGui) {
            gui.showWinnerAndExit(winnerId);
        } else {
            System.out.println(myId.equals(winnerId) ? "I won" : "I am looser");
        }
    }


}
