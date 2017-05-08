package org.battleship.controller;


import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.model.bits.BitResult;
import org.battleship.model.bits.OneMoreBittable;
import org.battleship.service.GameService;
import org.battleship.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class GameController implements GameService {

    private Map<String, UserService> idUserServicesMap = new HashMap<String, UserService>();
    private Map<String, AtomicInteger> idUserKilledShipsMap = new HashMap<String, AtomicInteger>();
    public static final int GOAL_SHIPS_TO_KILL = 10;
    private ExecutorService taskExecutor = Executors.newFixedThreadPool(4);

    public GameController(UserService user1service, UserService user2service) {
        putNewUserToMapAndGiveItIdentifier(user1service);
        putNewUserToMapAndGiveItIdentifier(user2service);
    }

    public void startLogic() {
        askForAddingShips();
        notifyGameStarted();
        while (true) {
            for (String s : idUserServicesMap.keySet()) {
                askForBit(s);
            }
        }
    }

    private void putNewUserToMapAndGiveItIdentifier(UserService user1service) {
        String id = String.valueOf(System.nanoTime());
        user1service.setMyId(id);
        idUserServicesMap.put(id, user1service);
        idUserKilledShipsMap.put(id, new AtomicInteger());
    }


    public void askForAddingShips() {
        for (UserService userService : idUserServicesMap.values()) {
            taskExecutor.submit(() -> userService.askedForAddingShips());
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
            res &= service.getIsFilled();
        }
        return res;
    }

    public void notifyGameStarted() {
        for (UserService userService : idUserServicesMap.values()) {
            userService.notifiedGameStarts();
        }
    }


    public void askForBit(String playerId) {
        BitResult result = idUserServicesMap.get(playerId).askedForBit();
        if (result instanceof OneMoreBittable) askForBit(playerId);
    }

// TODO: 08.05.2017 when game controller asks for beat action should be in concrette userService and return result back
// to game controller in order to initiator service got correct result as far as actual ships situation is full only on
// board which is composed in concrette user service

    public void sendMessage(String playerId, String message) {
        for (UserService service : idUserServicesMap.values()) {
            service.messageReceived(playerId + ": " + message);
        }
    }

    @Override
    public BitResult bitOpponent(char x, int y, String attackerServiceId) throws CantBitBorderSquareException {
        return getOpponentService(attackerServiceId).oppenentBitsMyBoardSquare(x, y);
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
