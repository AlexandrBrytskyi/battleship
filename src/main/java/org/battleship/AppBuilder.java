package org.battleship;


import org.battleship.controller.ManManGameController;
import org.battleship.controller.SimpleUserController;
import org.battleship.gui.GUI;
import org.battleship.model.boards.TenXTenStandardBoard;
import org.battleship.service.GameService;
import org.battleship.service.UserService;

public class AppBuilder {

    public static void runManMan() {

        GUI gui1 = new GUI();
        GUI gui2 = new GUI();
        UserService service1 = new SimpleUserController(gui1, new TenXTenStandardBoard(), new TenXTenStandardBoard());
        UserService service2 = new SimpleUserController(gui2, new TenXTenStandardBoard(), new TenXTenStandardBoard());
        gui1.setUserService(service1);
        gui2.setUserService(service2);
        new Thread(() -> {
            ManManGameController gameController = new ManManGameController(service1, service2);
            ((SimpleUserController)service1).setGameService(gameController);
            ((SimpleUserController)service2).setGameService(gameController);
            gameController.startLogic();
        }).start();
    }

}
