package org.battleship.controller.user.remote.requests;

import org.battleship.exceptions.CantBitBorderSquareException;
import org.battleship.model.bits.BitResult;


public class OppenentBitsMyBoardSquareRequest extends Request {

    private char x;
    private int y;
    private CantBitBorderSquareException e;
    private BitResult res;

    public OppenentBitsMyBoardSquareRequest(char x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void sendCallback() {
        if (res != null) {
            eventWorker.sendObject(res);
        } else {
            eventWorker.sendObject(e);
        }
    }

    @Override
    public void requestReceivedAction() {
        try {
            res = eventWorker.oppenentBitsMyBoardSquare(x, y);
        } catch (CantBitBorderSquareException e) {
            e.printStackTrace();
            this.e = e;
        }
        sendCallback();
    }
}
