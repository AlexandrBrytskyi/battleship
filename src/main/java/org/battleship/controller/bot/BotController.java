package org.battleship.controller.bot;


import org.battleship.controller.Ships10x10GenUtils;
import org.battleship.controller.user.UserPlayerController;
import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.exceptions.SquareIsUnderShipException;
import org.battleship.exceptions.UnsupportedShipException;
import org.battleship.gui.GUI;
import org.battleship.model.boards.Board;
import org.battleship.model.boards.BoardSquare;
import org.battleship.model.ships.Ship;

import java.util.*;

/**
 * overrides some methods of {@link UserPlayerController} in order to set bot logic
 */
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
        Ships10x10GenUtils.generateAndAddShips(this, useGui, gui, getMyBoard().getBorderTopName());
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
    public void askedForBit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (useGui) gui.appendMessage("I am asked to bit!!!Ok");
        System.out.println("bot asked to bit");
//        0 because we don`t put
        BoardSquare generated = Ships10x10GenUtils.generateRandomSquareForAddingShips(0, boardTopChars);
        while (alreadyBittedSquares.contains(generated)) {
            generated = Ships10x10GenUtils.generateRandomSquareForAddingShips(0, boardTopChars);
        }
        System.out.println("generated such square for bit " + generated.getxPosition() + "-" + generated.getyPosition());
        try {
            bitOpponentBoardSquare(generated.getxPosition(), generated.getyPosition());
            alreadyBittedSquares.add(generated);
        } catch (CantBitBorderSquareException e) {
            e.printStackTrace();
            if (useGui) gui.appendMessage(e.getMessage());
        }
    }

    @Override
    public void bitOpponentBoardSquare(char x, int y) throws CantBitBorderSquareException {
        super.bitOpponentBoardSquare(x, y);
        alreadyBittedSquares.add(new BoardSquare(y, x));
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
        boolean won = winnerId.equals(myId) ? true : false;
        if (useGui) {
            gui.showWinner(won);
        } else {
            System.out.println(won ? "I won" : "I am looser");
        }
    }


}
