package org.battleship.model.boards;


import org.battleship.exceptions.WrongBoardStringException;
import org.battleship.model.ships.Ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TenXTenSimpleBoard extends Board {

    private Map<Integer, List<Ship>> shipsByLength = new HashMap<Integer, List<Ship>>() {{
        for (int i = 1; i <= 4; i++) {
            put(i, new ArrayList<Ship>());
        }
    }};


    public TenXTenSimpleBoard() {

    }

    public TenXTenSimpleBoard(String borderTopName) throws WrongBoardStringException {
        super(borderTopName);
        checkName(borderTopName);
    }

    protected void putShipOnBoard(Ship ship) {

    }

    protected void squareAtacked(int x, int y) {

    }

    protected void shipBitted(Ship ship) {

    }

    protected void shipDestroyed(Ship ship) {

    }

    private void checkName(String borderTopName) throws WrongBoardStringException {
        if (borderTopName.length() != 10) throw new WrongBoardStringException("Lenght must be equal 10");
        if (!isUniqueChars2(borderTopName))
            throw new WrongBoardStringException("All characters in String must be unique");
    }

    private boolean isUniqueChars2(String str) {
        // Create a new boolean array of 256 characters to account for basic ascii and extended ascii characters
        boolean[] char_set = new boolean[256];

        //iterate through the string we are testing
        for (int i = 0; i < str.length(); i++) {

            // Get the numerical (ascii) value of the character in the `str` at position `i`.
            int val = str.charAt(i);

            // If char_set[val] has been set, that means that this character was already present in the string. (so in string 'hello' this would be true for the second 'l')
            if (char_set[val]) {
                return false;
            }
            // If the character hasn't been encountered yet (otherwise we would have returned true above), then mark this particular character as present in the string
            char_set[val] = true;
        }
        // If the function hasn't returned false after going through the entire string that means that each character is unique - thus returning true
        return true;
    }

}
