package org.battleship;


import org.battleship.controller.bot.BotController;
import org.battleship.controller.GameController;
import org.battleship.controller.PlayerController;
import org.battleship.controller.user.UserPlayerController;
import org.battleship.controller.user.remote.RemoteUserClientController;
import org.battleship.controller.user.remote.RemoteUserOnServerController;
import org.battleship.gui.GUI;
import org.battleship.model.boards.TenXTenStandardBoard;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppBuilder {

    private static ExecutorService gameExecutor = Executors.newFixedThreadPool(3);

    public static void runBotBot(boolean botUseGUI) {
        final GUI[] gui = new GUI[2];
        try {
            if (botUseGUI) {
                gameExecutor.submit(() -> {
                    gui[0] = new GUI();
                }).get();
                gameExecutor.submit(() -> {
                    gui[1] = new GUI();
                }).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        PlayerController player1 = new BotController(gui[0], new TenXTenStandardBoard(), new TenXTenStandardBoard(), botUseGUI);
        PlayerController player2 = new BotController(gui[1], new TenXTenStandardBoard(), new TenXTenStandardBoard(), botUseGUI);
        if (botUseGUI) {
            gui[0].setPlayerController(player1);
            gui[1].setPlayerController(player2);
        }
        runGameController(player1, player2);
    }

    public static void runManMan(boolean shipsAutogen) {

        final GUI[] gui = new GUI[2];
        try {
            gameExecutor.submit(() -> {
                gui[0] = new GUI();
            }).get();
            gameExecutor.submit(() -> {
                gui[1] = new GUI();
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        PlayerController player1 = new UserPlayerController(gui[0], new TenXTenStandardBoard(), new TenXTenStandardBoard(), shipsAutogen);
        PlayerController player2 = new UserPlayerController(gui[1], new TenXTenStandardBoard(), new TenXTenStandardBoard(), shipsAutogen);
        gui[0].setPlayerController(player1);
        gui[1].setPlayerController(player2);
        runGameController(player1, player2);
    }

    public static void runManBot(boolean botUseGUI, boolean userShipsAutogen) {
        final GUI[] gui = new GUI[2];
        try {
            if (botUseGUI) gameExecutor.submit(() -> {
                gui[0] = new GUI();
            }).get();
            gameExecutor.submit(() -> {
                gui[1] = new GUI();
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TenXTenStandardBoard myBoard = new TenXTenStandardBoard();
        PlayerController player1 = new BotController(gui[0], myBoard, new TenXTenStandardBoard(), botUseGUI);
        PlayerController player2 = new UserPlayerController(gui[1], new TenXTenStandardBoard(), new TenXTenStandardBoard(), userShipsAutogen);
        if (botUseGUI) gui[0].setPlayerController(player1);
        gui[1].setPlayerController(player2);
        runGameController(player1, player2);
    }

    public static void runGameController(PlayerController player1, PlayerController player2) {
        gameExecutor.submit(() -> {
            GameController gameController = new GameController(player1, player2);
            player1.setGameService(gameController);
            player2.setGameService(gameController);
            gameController.startLogic();
        });
    }


    public static void runServer(boolean boardAutogen) throws IOException {
        GUI[] gui = createGui();

        TenXTenStandardBoard myBoard = new TenXTenStandardBoard();
        PlayerController player1 = new UserPlayerController(gui[0], myBoard, new TenXTenStandardBoard(), boardAutogen);
        PlayerController player2 = new RemoteUserOnServerController(null, new TenXTenStandardBoard(), new TenXTenStandardBoard(), false);
        gui[0].setPlayerController(player1);
        runGameController(player1, player2);
    }

    public static void runClient(String host, boolean boardAutogen) throws IOException {
        GUI[] gui = createGui();
        PlayerController remotePlayerController = new RemoteUserClientController(gui[0], new TenXTenStandardBoard(), new TenXTenStandardBoard(), boardAutogen, host);
        gui[0].setPlayerController(remotePlayerController);
    }

    public static GUI[] createGui() {
        GUI[] gui = new GUI[1];
        try {
            gameExecutor.submit(() -> {
                gui[0] = new GUI();
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return gui;
    }
}
