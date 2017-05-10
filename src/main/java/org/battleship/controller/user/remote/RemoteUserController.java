package org.battleship.controller.user.remote;



import org.battleship.controller.user.UserPlayerController;
import org.battleship.controller.user.remote.requests.*;

import org.battleship.gui.GUI;
import org.battleship.model.bits.BitResult;
import org.battleship.model.boards.Board;


import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class RemoteUserController extends UserPlayerController implements RemoteUserService {

    public static final int PORT = 9999;
    protected Socket clientSocket;
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;
    protected ExecutorService executorService = Executors.newFixedThreadPool(4);
    protected BlockingQueue<BitResult> bitResults = new ArrayBlockingQueue<BitResult>(1);

    public RemoteUserController(GUI gui, Board myBoard, Board opponentBoard, boolean autogenShips) throws IOException {
        super(gui, myBoard, opponentBoard, autogenShips);
    }


    protected void initInputListener() {
        executorService.submit(new InputListener(ois));
    }


    @Override
    public void sendObject(Serializable object, boolean waitForResponse) {
            try {
                oos.writeObject(object);
                System.out.println("sent " + object.getClass().getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    @Override
    public void gameEnd(boolean won) {

    }


    protected class InputListener implements Runnable {

        private ObjectInputStream ois;

        public InputListener(ObjectInputStream ois) {
            this.ois = ois;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Object received = ois.readObject();
                    System.out.println("received:" + received.getClass().getName());
                    if (received instanceof Request) {
                        requestReceived((Request) received);
                    } else if (received instanceof BitResult) {
                        bitResults.put((BitResult) received);
                    } else {
                        gui.appendMessage("received: " + received);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    gui.showDialog("Disconnected", "Err", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }


}
