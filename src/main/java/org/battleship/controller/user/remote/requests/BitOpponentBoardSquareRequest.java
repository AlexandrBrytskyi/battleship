package org.battleship.controller.user.remote.requests;

import org.battleship.exceptions.CantBitBorderSquareException;


public class BitOpponentBoardSquareRequest extends Request {

    private char x;
    private int y;
    private CantBitBorderSquareException e;

    public BitOpponentBoardSquareRequest(char x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void sendCallback() {

        if (e != null) {
            eventWorker.sendObject(e);
        }

    }

    @Override
    public void requestReceivedAction() {
        try {
            System.out.println();
            eventWorker.bitOpponentBoardSquare(x, y);
        } catch (CantBitBorderSquareException e) {
            e.printStackTrace();
            this.e = e;
        }
        sendCallback();
    }
}
