package org.battleship.controller;


import org.battleship.gui.GUI;
import org.battleship.model.boards.BoardSquare;
import org.battleship.model.ships.StraightShip;
import org.battleship.service.UserService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShipsGenUtils {


    public static void generateAndAddShips(UserService service, boolean useGui, GUI gui, String boardTopChars) {
        int shipsAmountCounter = 1;
        Boolean horisontalShipGenFlipper = new Boolean(false);
        Set<BoardSquare> alreadyGenerated = new HashSet<>();
        for (int i = 4; i > 0; i--) {
            for (int j = 1; j <= shipsAmountCounter; j++) {
                tryToAddGeneratedShip(i, service, useGui, gui, alreadyGenerated, horisontalShipGenFlipper, boardTopChars);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            shipsAmountCounter++;
        }
    }

    private static void tryToAddGeneratedShip(int shipSize, UserService service, boolean useGui, GUI gui, Set<BoardSquare> alreadyGenerated, Boolean horisontalShipGenFlipper, String boardTopChars) {
        try {
            List<BoardSquare> shipPlaceOnBoard = generateShip(shipSize, alreadyGenerated, horisontalShipGenFlipper, boardTopChars);
            service.addShip(new StraightShip("Generated:" + shipSize, shipSize), shipPlaceOnBoard);
        } catch (Throwable e) {
//            System.out.println("Cant add generated ship, " + e.getMessage());
            if (useGui) gui.appendMessage(e.getMessage());
            tryToAddGeneratedShip(shipSize, service, useGui, gui, alreadyGenerated, horisontalShipGenFlipper, boardTopChars);
        }
    }

    private static List<BoardSquare> generateShip(int shipSize, Set<BoardSquare> alreadyGenerated, Boolean horisontalShipGenFlipper, String boardTopChars) {
//        System.out.println("Generating ship with size " + shipSize);

        BoardSquare generatedFirstSquare = null;
        while (alreadyGenerated.contains(generatedFirstSquare = generateRandomSquareForAddingShips(shipSize, boardTopChars))) {
            if (alreadyGenerated.size() > boardTopChars.length() * 2 - 5) alreadyGenerated.clear();
        }
        alreadyGenerated.add(generatedFirstSquare);
        List<BoardSquare> boardSquares;
        if (horisontalShipGenFlipper) {
            boardSquares = getVerticalShipSquares(generatedFirstSquare.getxPosition(), generatedFirstSquare.getyPosition(), shipSize, boardTopChars);
            horisontalShipGenFlipper = false;
        } else {
            boardSquares = getHorizontalShipSquares(generatedFirstSquare.getxPosition(), generatedFirstSquare.getyPosition(), shipSize, boardTopChars);
            horisontalShipGenFlipper = true;
        }

//sout squares
        String generatedSquaresInfo = buildSquaresInfo(boardSquares);
//        System.out.println(generatedSquaresInfo);
        return boardSquares;
    }

    private static String buildSquaresInfo(List<BoardSquare> boardSquares) {
        StringBuilder s = new StringBuilder("Generate ship squares:");
        for (BoardSquare boardSquare : boardSquares) {
            s.append(boardSquare.getxPosition()).append('-').append(boardSquare.getyPosition()).append(',');
        }
        return s.toString();
    }

    public static BoardSquare generateRandomSquareForAddingShips(int shipSize, String boardTopChars) {
        char x = boardTopChars.charAt(getRandomIndex(shipSize, boardTopChars.length()));
        int y = 0;
        while ((y = getRandomIndex(shipSize, boardTopChars.length())) == 0) ;
        return new BoardSquare(y, x);
    }


    private static int getRandomIndex(int shipSize, int maxAvalibaleValue) {
        return (int) (Math.random() * (maxAvalibaleValue - shipSize));
    }

    private static List<BoardSquare> getHorizontalShipSquares(char xChar, int y, int shipSize, String boardTopChars) {
        List<BoardSquare> res = new ArrayList<>();
        int beginIndex = boardTopChars.indexOf(xChar);
        int end = beginIndex + shipSize;
        char[] chars = boardTopChars.substring(beginIndex, end <= boardTopChars.length() ? end : boardTopChars.length()).toCharArray();
        for (int i = 0; i < chars.length; i++) {
            res.add(new BoardSquare(y, chars[i]));
        }
        return res;
    }

    private static List<BoardSquare> getVerticalShipSquares(char xChar, int y, int shipSize, String boardTopChars) {
        List<BoardSquare> res = new ArrayList<>();
        for (int i = y; i < y + shipSize; i++) {
            if (i <= boardTopChars.length())
                res.add(new BoardSquare(i, xChar));
        }
        return res;
    }
}
