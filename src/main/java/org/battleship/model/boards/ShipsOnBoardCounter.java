package org.battleship.model.boards;


import org.battleship.exceptions.UnsupportedShipException;
import org.battleship.model.ships.Ship;

/**
 * used to add possibility of counting {@link Ship} on {@link Board}*/
public interface ShipsOnBoardCounter<T extends Ship> {

     void addShipToCounter(T ship) throws UnsupportedShipException;

}
