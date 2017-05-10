package org.battleship.controller;


import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.model.bits.BitResult;
import org.battleship.model.bits.MissedActivity;
import org.battleship.model.bits.MissedResult;
import org.battleship.service.GameService;
import org.battleship.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GameController implements GameService {

    private Map<String, PlayerController> idUserServicesMap = new HashMap<String, PlayerController>();
    private Map<String, AtomicInteger> idUserKilledShipsMap = new HashMap<String, AtomicInteger>();
    public static final int GOAL_SHIPS_TO_KILL = 10;
    private ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
    private AtomicReference<String> nowGoingId = new AtomicReference<>();

    public GameController(PlayerController user1Controller, PlayerController user2Controller) {
        putNewUserToMapAndGiveItIdentifier(user1Controller);
        putNewUserToMapAndGiveItIdentifier(user2Controller);
    }

    public void startLogic() {
        checkReady();
        askForAddingShips();
        notifyGameStarted();
        while (true) {
            for (String s : idUserServicesMap.keySet()) {
                askForBit(s);
            }
        }
    }

    private void checkReady() {
        PlayerController notReady = null;

        for (PlayerController controller : idUserServicesMap.values()) {
            if (controller.getReadyToPlay()) {
                controller.messageReceived("Ok) see you are ready, waiting for opponent");
            } else {
                notReady = controller;
            }
        }

        if (notReady != null) {
            while (!notReady.getReadyToPlay()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void putNewUserToMapAndGiveItIdentifier(PlayerController playerController) {
        String id = String.valueOf(System.nanoTime());
        playerController.setMyId(id);
        idUserServicesMap.put(id, playerController);
        idUserKilledShipsMap.put(id, new AtomicInteger());
    }


    public void askForAddingShips() {
        for (UserService userService : idUserServicesMap.values()) {
            taskExecutor.submit(() -> userService.askedForAddingShips());
//            System.out.println(userService.getMyId());
        }


        try {
            taskExecutor.submit(() -> {
                while (!checkIfBoardsIsFilled()) {
                    Thread.sleep(1000);
                }
                return null;
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private boolean checkIfBoardsIsFilled() {
        boolean res = true;
        for (UserService service : idUserServicesMap.values()) {
            res &= service.getFilled();
        }
        return res;
    }

    public void notifyGameStarted() {
        for (UserService userService : idUserServicesMap.values()) {
            userService.notifiedGameStarts();
        }
    }


    public void askForBit(String playerId) {
        nowGoingId.set(playerId);
        idUserServicesMap.get(playerId).askedForBit();
        try {
            taskExecutor.submit(() -> {
                while (playerId.equals(nowGoingId.get())) try {
                    System.out.println("waiting");
                    System.out.println(playerId);
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).get();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(String playerId, String message) {
        for (PlayerController service : idUserServicesMap.values()) {
            service.messageReceived((playerId.equals(service.getMyId()) ? "Me" : "Opponent") + ": " + message);
        }
    }

    @Override
    public BitResult bitOpponent(char x, int y, String attackerServiceId) throws CantBitBorderSquareException {
        System.out.println(attackerServiceId + "try to bit");
        System.out.println("game controller: pitting opponent");
        BitResult result = getOpponentService(attackerServiceId).oppenentBitsMyBoardSquare(x, y);
        System.out.println(attackerServiceId + ", " + nowGoingId.get() + ", " + result.getClass().getName());
        if (attackerServiceId.equals(nowGoingId.get()) && (result instanceof MissedResult)) {
            nowGoingId.set(null);
            System.out.println("setted null");
        }
        System.out.println("game controller: returning res");
        return result;
    }

    private UserService getOpponentService(String attackerServiceId) {
        return idUserServicesMap.entrySet().stream().filter(e -> !e.getKey().equals(attackerServiceId)).findFirst().get().getValue();

    }

    public void shipKilled(String killerId) {
        int killedByUser = idUserKilledShipsMap.get(killerId).incrementAndGet();
        if (killedByUser == 10) endGame(killerId);
    }

    public void endGame(String winnerId) {
        for (UserService userService : idUserServicesMap.values()) {
            userService.notifiedEndGame(winnerId);
        }
    }

}
